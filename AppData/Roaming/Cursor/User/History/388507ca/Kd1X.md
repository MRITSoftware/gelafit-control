# 💻 Exemplos de Integração Supabase

## 📚 Guia Prático de Como Usar o Supabase no Sistema

Este arquivo contém exemplos práticos de código para integrar o Supabase com seu sistema de gestão de documentos.

---

## 🔧 1. Configuração Inicial

### lib/supabase.ts

```typescript
import { createClient } from '@supabase/supabase-js'
import type { Database } from '@/types/supabase' // Opcional: tipos gerados

const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL!
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!

// Cliente público (use no frontend e backend)
export const supabase = createClient<Database>(supabaseUrl, supabaseAnonKey)

// Cliente admin (use APENAS no servidor)
export const supabaseAdmin = createClient<Database>(
  supabaseUrl,
  process.env.SUPABASE_SERVICE_ROLE_KEY!,
  {
    auth: {
      autoRefreshToken: false,
      persistSession: false
    }
  }
)
```

---

## 🔐 2. Autenticação

### Login

```typescript
// app/api/auth/login/route.ts
import { supabase } from '@/lib/supabase'
import { NextResponse } from 'next/server'

export async function POST(request: Request) {
  const { email, password } = await request.json()

  // Login no Supabase
  const { data: authData, error: authError } = await supabase.auth.signInWithPassword({
    email,
    password
  })

  if (authError) {
    return NextResponse.json({ error: authError.message }, { status: 401 })
  }

  // Buscar dados completos do usuário
  const { data: user, error: userError } = await supabase
    .from('users')
    .select('*, client:clients(id, name)')
    .eq('id', authData.user.id)
    .single()

  if (userError) {
    return NextResponse.json({ error: 'Usuário não encontrado' }, { status: 404 })
  }

  // Log de auditoria
  await supabase.from('audit_logs').insert({
    user_id: user.id,
    action: 'user.login',
    resource_type: 'auth',
    resource_id: user.id,
    client_id: user.client_id,
    metadata: { ip: request.headers.get('x-forwarded-for') }
  })

  return NextResponse.json({
    user,
    session: authData.session
  })
}
```

### Cadastro

```typescript
// app/api/auth/register/route.ts
import { supabaseAdmin } from '@/lib/supabase'
import { NextResponse } from 'next/server'
import bcrypt from 'bcryptjs'

export async function POST(request: Request) {
  const { email, password, name, role, clientId } = await request.json()

  // Criar usuário no Auth do Supabase
  const { data: authData, error: authError } = await supabaseAdmin.auth.admin.createUser({
    email,
    password,
    email_confirm: true
  })

  if (authError) {
    return NextResponse.json({ error: authError.message }, { status: 400 })
  }

  // Hash da senha para nossa tabela
  const passwordHash = await bcrypt.hash(password, 10)

  // Criar registro na tabela users
  const { data: user, error: userError } = await supabaseAdmin
    .from('users')
    .insert({
      id: authData.user.id, // Mesmo ID do auth
      email,
      name,
      password_hash: passwordHash,
      role,
      client_id: clientId,
      is_active: true
    })
    .select()
    .single()

  if (userError) {
    // Rollback: deletar do auth se falhar
    await supabaseAdmin.auth.admin.deleteUser(authData.user.id)
    return NextResponse.json({ error: userError.message }, { status: 400 })
  }

  return NextResponse.json({ user })
}
```

### Verificar Sessão

```typescript
// middleware.ts ou componente
import { supabase } from '@/lib/supabase'

export async function getCurrentUser() {
  const { data: { user } } = await supabase.auth.getUser()
  
  if (!user) return null

  // Buscar dados completos
  const { data: userData } = await supabase
    .from('users')
    .select('*, client:clients(*)')
    .eq('id', user.id)
    .single()

  return userData
}
```

---

## 📄 3. Documentos

### Listar Documentos

```typescript
// app/api/documents/route.ts
import { supabase } from '@/lib/supabase'
import { NextResponse } from 'next/server'

export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const clientId = searchParams.get('clientId')
  const folderId = searchParams.get('folderId')
  const status = searchParams.get('status')
  const page = parseInt(searchParams.get('page') || '1')
  const limit = parseInt(searchParams.get('limit') || '20')

  let query = supabase
    .from('documents')
    .select(`
      *,
      uploaded_by:users!documents_uploaded_by_fkey(id, name, email),
      folder:folders(id, name),
      tags:document_tags(tag:tags(*))
    `, { count: 'exact' })
    .eq('client_id', clientId)
    .order('created_at', { ascending: false })

  // Filtros opcionais
  if (folderId) query = query.eq('folder_id', folderId)
  if (status) query = query.eq('status', status)

  // Paginação
  const from = (page - 1) * limit
  const to = from + limit - 1
  query = query.range(from, to)

  const { data: documents, error, count } = await query

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({
    documents,
    pagination: {
      page,
      limit,
      total: count,
      totalPages: Math.ceil((count || 0) / limit)
    }
  })
}
```

### Criar Documento

```typescript
// app/api/documents/route.ts
export async function POST(request: Request) {
  const formData = await request.formData()
  const file = formData.get('file') as File
  const title = formData.get('title') as string
  const clientId = formData.get('clientId') as string
  const userId = formData.get('userId') as string
  const folderId = formData.get('folderId') as string | null

  // 1. Upload do arquivo para Supabase Storage
  const fileName = `${Date.now()}_${file.name}`
  const filePath = `${clientId}/${fileName}`

  const { data: uploadData, error: uploadError } = await supabase.storage
    .from('documents')
    .upload(filePath, file)

  if (uploadError) {
    return NextResponse.json({ error: uploadError.message }, { status: 400 })
  }

  // 2. Obter URL pública
  const { data: urlData } = supabase.storage
    .from('documents')
    .getPublicUrl(filePath)

  // 3. Criar registro no banco
  const { data: document, error: dbError } = await supabase
    .from('documents')
    .insert({
      title,
      description: formData.get('description') as string,
      file_name: file.name,
      file_size: file.size,
      file_type: file.type,
      file_path: filePath,
      client_id: clientId,
      uploaded_by: userId,
      folder_id: folderId,
      status: 'pending'
    })
    .select()
    .single()

  if (dbError) {
    // Rollback: deletar arquivo se falhar
    await supabase.storage.from('documents').remove([filePath])
    return NextResponse.json({ error: dbError.message }, { status: 400 })
  }

  // 4. Log de auditoria
  await supabase.from('audit_logs').insert({
    user_id: userId,
    action: 'document.created',
    resource_type: 'document',
    resource_id: document.id,
    client_id: clientId,
    metadata: { title, file_name: file.name }
  })

  // 5. Notificar admins
  const { data: admins } = await supabase
    .from('users')
    .select('id')
    .eq('client_id', clientId)
    .eq('role', 'client_admin')

  if (admins) {
    const notifications = admins.map(admin => ({
      user_id: admin.id,
      type: 'document_uploaded',
      title: 'Novo Documento Enviado',
      message: `${title} foi enviado por usuário`,
      priority: 'medium',
      link: `/dashboard/documents/${document.id}`
    }))

    await supabase.from('notifications').insert(notifications)
  }

  return NextResponse.json({ document })
}
```

### Atualizar Status do Documento

```typescript
// app/api/documents/[id]/status/route.ts
export async function PATCH(
  request: Request,
  { params }: { params: { id: string } }
) {
  const { status, comment, userId } = await request.json()
  const documentId = params.id

  // Buscar documento atual
  const { data: document } = await supabase
    .from('documents')
    .select('*, uploaded_by:users(id, name, email)')
    .eq('id', documentId)
    .single()

  if (!document) {
    return NextResponse.json({ error: 'Documento não encontrado' }, { status: 404 })
  }

  const oldStatus = document.status

  // Atualizar status
  const { error } = await supabase
    .from('documents')
    .update({ status })
    .eq('id', documentId)

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  // Log de auditoria
  await supabase.from('audit_logs').insert({
    user_id: userId,
    action: 'document.status_changed',
    resource_type: 'document',
    resource_id: documentId,
    client_id: document.client_id,
    metadata: { from: oldStatus, to: status, comment }
  })

  // Notificar uploader
  await supabase.from('notifications').insert({
    user_id: document.uploaded_by,
    type: 'status_changed',
    title: 'Status do Documento Alterado',
    message: `"${document.title}" está agora: ${status}`,
    priority: status === 'approved' ? 'high' : 'medium',
    link: `/dashboard/documents/${documentId}`
  })

  // Se houver comentário, criar
  if (comment) {
    await supabase.from('comments').insert({
      document_id: documentId,
      user_id: userId,
      content: comment,
      is_internal: true
    })
  }

  return NextResponse.json({ success: true })
}
```

### Busca Full-Text

```typescript
// app/api/documents/search/route.ts
export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const query = searchParams.get('q')
  const clientId = searchParams.get('clientId')

  if (!query || !clientId) {
    return NextResponse.json({ error: 'Query e clientId são obrigatórios' }, { status: 400 })
  }

  // Usar função personalizada de busca
  const { data: documents, error } = await supabase
    .rpc('search_documents', {
      search_query: query,
      client_id_param: clientId
    })

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({ documents })
}
```

---

## 💬 4. Comentários

### Adicionar Comentário

```typescript
// app/api/comments/route.ts
export async function POST(request: Request) {
  const { documentId, userId, content, mentions } = await request.json()

  // Criar comentário
  const { data: comment, error } = await supabase
    .from('comments')
    .insert({
      document_id: documentId,
      user_id: userId,
      content,
      is_internal: false
    })
    .select()
    .single()

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  // Adicionar menções
  if (mentions && mentions.length > 0) {
    const mentionRecords = mentions.map((mentionedUserId: string) => ({
      comment_id: comment.id,
      user_id: mentionedUserId
    }))

    await supabase.from('comment_mentions').insert(mentionRecords)

    // Notificar mencionados
    const notifications = mentions.map((mentionedUserId: string) => ({
      user_id: mentionedUserId,
      type: 'mention',
      title: 'Você foi Mencionado',
      message: `Você foi mencionado em um comentário`,
      priority: 'medium',
      link: `/dashboard/documents/${documentId}`
    }))

    await supabase.from('notifications').insert(notifications)
  }

  // Log de auditoria
  await supabase.from('audit_logs').insert({
    user_id: userId,
    action: 'comment.created',
    resource_type: 'comment',
    resource_id: comment.id,
    metadata: { document_id: documentId }
  })

  return NextResponse.json({ comment })
}
```

### Listar Comentários

```typescript
// app/api/comments/route.ts
export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const documentId = searchParams.get('documentId')

  const { data: comments, error } = await supabase
    .from('comments')
    .select(`
      *,
      user:users(id, name, email, avatar),
      mentions:comment_mentions(user:users(id, name)),
      replies:comments(*)
    `)
    .eq('document_id', documentId)
    .is('reply_to', null) // Apenas comentários raiz
    .order('created_at', { ascending: true })

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({ comments })
}
```

---

## 📂 5. Pastas

### Criar Pasta

```typescript
// app/api/folders/route.ts
export async function POST(request: Request) {
  const { name, description, clientId, parentId, userId } = await request.json()

  const { data: folder, error } = await supabase
    .from('folders')
    .insert({
      name,
      description,
      client_id: clientId,
      parent_id: parentId
    })
    .select()
    .single()

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  // Log
  await supabase.from('audit_logs').insert({
    user_id: userId,
    action: 'folder.created',
    resource_type: 'folder',
    resource_id: folder.id,
    client_id: clientId,
    metadata: { name, parent_id: parentId }
  })

  return NextResponse.json({ folder })
}
```

### Listar Árvore de Pastas

```typescript
// app/api/folders/tree/route.ts
export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const clientId = searchParams.get('clientId')

  const { data: folders, error } = await supabase
    .from('folders')
    .select('*')
    .eq('client_id', clientId)
    .order('name')

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  // Construir árvore hierárquica
  const buildTree = (parentId: string | null = null): any[] => {
    return folders
      .filter(f => f.parent_id === parentId)
      .map(folder => ({
        ...folder,
        children: buildTree(folder.id)
      }))
  }

  const tree = buildTree()

  return NextResponse.json({ tree })
}
```

---

## 🔔 6. Notificações

### Listar Notificações

```typescript
// app/api/notifications/route.ts
export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const userId = searchParams.get('userId')
  const unreadOnly = searchParams.get('unreadOnly') === 'true'

  let query = supabase
    .from('notifications')
    .select('*')
    .eq('user_id', userId)
    .order('created_at', { ascending: false })
    .limit(50)

  if (unreadOnly) {
    query = query.eq('is_read', false)
  }

  const { data: notifications, error } = await query

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({ notifications })
}
```

### Marcar como Lida

```typescript
// app/api/notifications/[id]/read/route.ts
export async function PATCH(
  request: Request,
  { params }: { params: { id: string } }
) {
  const { error } = await supabase
    .from('notifications')
    .update({ is_read: true })
    .eq('id', params.id)

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({ success: true })
}
```

---

## 📊 7. Estatísticas e Analytics

### Estatísticas do Cliente

```typescript
// app/api/stats/client/route.ts
export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const clientId = searchParams.get('clientId')

  // Usar função personalizada
  const { data: stats, error } = await supabase
    .rpc('get_client_stats', { client_id_param: clientId })

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({ stats })
}
```

### Atividades Recentes

```typescript
// app/api/activities/route.ts
export async function GET(request: Request) {
  const { searchParams } = new URL(request.url)
  const clientId = searchParams.get('clientId')
  const limit = parseInt(searchParams.get('limit') || '20')

  const { data: activities, error } = await supabase
    .from('activities')
    .select(`
      *,
      user:users(id, name, email)
    `)
    .eq('client_id', clientId)
    .order('timestamp', { ascending: false })
    .limit(limit)

  if (error) {
    return NextResponse.json({ error: error.message }, { status: 400 })
  }

  return NextResponse.json({ activities })
}
```

---

## 🔄 8. Real-time (Opcional)

### Escutar Novas Notificações

```typescript
// components/NotificationListener.tsx
'use client'

import { useEffect } from 'react'
import { supabase } from '@/lib/supabase'

export function NotificationListener({ userId }: { userId: string }) {
  useEffect(() => {
    // Subscrever a mudanças em notificações
    const channel = supabase
      .channel('notifications')
      .on(
        'postgres_changes',
        {
          event: 'INSERT',
          schema: 'public',
          table: 'notifications',
          filter: `user_id=eq.${userId}`
        },
        (payload) => {
          console.log('Nova notificação:', payload.new)
          // Atualizar UI, mostrar toast, etc
        }
      )
      .subscribe()

    return () => {
      supabase.removeChannel(channel)
    }
  }, [userId])

  return null
}
```

### Escutar Novos Documentos

```typescript
// components/DocumentListener.tsx
'use client'

import { useEffect } from 'react'
import { supabase } from '@/lib/supabase'

export function DocumentListener({ clientId, onNewDocument }: any) {
  useEffect(() => {
    const channel = supabase
      .channel('documents')
      .on(
        'postgres_changes',
        {
          event: 'INSERT',
          schema: 'public',
          table: 'documents',
          filter: `client_id=eq.${clientId}`
        },
        (payload) => {
          onNewDocument(payload.new)
        }
      )
      .subscribe()

    return () => {
      supabase.removeChannel(channel)
    }
  }, [clientId, onNewDocument])

  return null
}
```

---

## 🎯 9. Hooks Personalizados

### useDocuments

```typescript
// hooks/useDocuments.ts
import { useState, useEffect } from 'react'
import { supabase } from '@/lib/supabase'

export function useDocuments(clientId: string, filters?: any) {
  const [documents, setDocuments] = useState<any[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    async function fetchDocuments() {
      try {
        let query = supabase
          .from('documents')
          .select(`
            *,
            uploaded_by:users(name),
            folder:folders(name)
          `)
          .eq('client_id', clientId)

        if (filters?.status) {
          query = query.eq('status', filters.status)
        }

        const { data, error } = await query

        if (error) throw error

        setDocuments(data || [])
      } catch (err: any) {
        setError(err.message)
      } finally {
        setLoading(false)
      }
    }

    fetchDocuments()
  }, [clientId, filters])

  return { documents, loading, error }
}
```

### useNotifications

```typescript
// hooks/useNotifications.ts
import { useState, useEffect } from 'react'
import { supabase } from '@/lib/supabase'

export function useNotifications(userId: string) {
  const [notifications, setNotifications] = useState<any[]>([])
  const [unreadCount, setUnreadCount] = useState(0)

  useEffect(() => {
    // Buscar notificações iniciais
    async function fetchNotifications() {
      const { data } = await supabase
        .from('notifications')
        .select('*')
        .eq('user_id', userId)
        .order('created_at', { ascending: false })
        .limit(20)

      if (data) {
        setNotifications(data)
        setUnreadCount(data.filter(n => !n.is_read).length)
      }
    }

    fetchNotifications()

    // Subscrever a novas notificações
    const channel = supabase
      .channel('user-notifications')
      .on(
        'postgres_changes',
        {
          event: 'INSERT',
          schema: 'public',
          table: 'notifications',
          filter: `user_id=eq.${userId}`
        },
        (payload) => {
          setNotifications(prev => [payload.new as any, ...prev])
          setUnreadCount(prev => prev + 1)
        }
      )
      .subscribe()

    return () => {
      supabase.removeChannel(channel)
    }
  }, [userId])

  const markAsRead = async (notificationId: string) => {
    await supabase
      .from('notifications')
      .update({ is_read: true })
      .eq('id', notificationId)

    setNotifications(prev =>
      prev.map(n => n.id === notificationId ? { ...n, is_read: true } : n)
    )
    setUnreadCount(prev => Math.max(0, prev - 1))
  }

  return { notifications, unreadCount, markAsRead }
}
```

---

## 🎉 Conclusão

Com esses exemplos, você pode:

✅ Integrar completamente o Supabase  
✅ Implementar autenticação segura  
✅ Gerenciar documentos e pastas  
✅ Criar sistema de comentários  
✅ Implementar notificações  
✅ Adicionar analytics  
✅ Usar real-time updates  
✅ Criar hooks personalizados  

**Próximos passos:**
1. Adapte os exemplos ao seu código
2. Teste cada funcionalidade
3. Adicione tratamento de erros
4. Implemente cache quando necessário
5. Configure monitoring

---

**Desenvolvido com 💜 para facilitar sua integração!**

