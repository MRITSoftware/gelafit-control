# 🗄️ Guia de Configuração do Supabase

## 📋 Passo a Passo para Configurar o Banco de Dados

### 1. **Criar Projeto no Supabase**

1. Acesse [https://supabase.com](https://supabase.com)
2. Faça login ou crie uma conta
3. Clique em "New Project"
4. Escolha:
   - **Nome do projeto**: meu-sistema-documentos
   - **Database Password**: Escolha uma senha forte (GUARDE BEM!)
   - **Region**: Escolha a região mais próxima (Brazil ou South America)
5. Aguarde a criação do projeto (1-2 minutos)

---

### 2. **Executar o Schema SQL**

#### Opção A: Via Dashboard do Supabase (Recomendado)

1. No dashboard do seu projeto, vá em **SQL Editor** (no menu lateral)
2. Clique em **"New query"**
3. Abra o arquivo `supabase-schema.sql` deste projeto
4. **Copie TODO o conteúdo** do arquivo
5. Cole no editor SQL do Supabase
6. Clique em **"Run"** (ou pressione Ctrl/Cmd + Enter)
7. Aguarde a execução (pode levar 10-30 segundos)
8. Se tudo correr bem, você verá "Success. No rows returned"

#### Opção B: Via CLI do Supabase

```bash
# 1. Instalar o CLI do Supabase
npm install -g supabase

# 2. Fazer login
supabase login

# 3. Linkar ao projeto
supabase link --project-ref SEU_PROJECT_REF

# 4. Executar o schema
supabase db push
```

---

### 3. **Verificar se as Tabelas Foram Criadas**

1. No dashboard, vá em **Table Editor** (menu lateral)
2. Você deve ver todas essas tabelas:
   - ✅ clients
   - ✅ users
   - ✅ folders
   - ✅ tags
   - ✅ documents
   - ✅ document_tags
   - ✅ comments
   - ✅ comment_mentions
   - ✅ notifications
   - ✅ audit_logs
   - ✅ user_invites
   - ✅ favorites
   - ✅ shares
   - ✅ signatures
   - ✅ workflows
   - ✅ workflow_steps
   - ✅ workflow_step_approvers
   - ✅ workflow_instances
   - ✅ workflow_step_instances
   - ✅ workflow_step_assignees
   - ✅ workflow_approvals
   - ✅ chat_messages
   - ✅ chat_message_mentions
   - ✅ reactions
   - ✅ annotations
   - ✅ document_templates
   - ✅ template_tags
   - ✅ activities
   - ✅ client_settings
   - ✅ user_preferences

---

### 4. **Verificar Dados de Exemplo**

O schema já inclui dados iniciais:

#### Clientes
- **Empresa Exemplo**

#### Usuários (todos com senha: `123456`)
- **admin@sistema.com** - Super Admin (acesso total)
- **admin@empresaexemplo.com** - Admin do Cliente
- **colaborador@empresaexemplo.com** - Colaborador

#### Pastas
- Documentos Fiscais
- Contratos
- RH

#### Tags
- Urgente (vermelho)
- Importante (laranja)
- Revisão (azul)
- Confidencial (roxo)

---

### 5. **Obter Credenciais do Supabase**

1. No dashboard, vá em **Settings** → **API**
2. Anote essas informações:

```env
# URL do projeto
NEXT_PUBLIC_SUPABASE_URL=https://seu-projeto.supabase.co

# Chave pública (anon key)
NEXT_PUBLIC_SUPABASE_ANON_KEY=sua-chave-publica-aqui

# Chave de serviço (service_role key) - NUNCA EXPONHA NO FRONTEND
SUPABASE_SERVICE_ROLE_KEY=sua-chave-servico-aqui
```

---

### 6. **Configurar o Projeto Next.js**

#### 6.1. Instalar Dependências

```bash
npm install @supabase/supabase-js @supabase/auth-helpers-nextjs
```

#### 6.2. Criar Arquivo `.env.local`

Na raiz do projeto, crie o arquivo `.env.local`:

```env
# Supabase
NEXT_PUBLIC_SUPABASE_URL=https://seu-projeto.supabase.co
NEXT_PUBLIC_SUPABASE_ANON_KEY=sua-chave-publica-aqui
SUPABASE_SERVICE_ROLE_KEY=sua-chave-servico-aqui

# JWT (pode gerar em: https://generate-secret.vercel.app/32)
JWT_SECRET=sua-chave-secreta-para-jwt-aqui
```

#### 6.3. Criar Cliente Supabase

Crie o arquivo `lib/supabase.ts`:

```typescript
import { createClient } from '@supabase/supabase-js'

const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL!
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!

export const supabase = createClient(supabaseUrl, supabaseAnonKey)

// Cliente com privilégios de admin (use apenas no servidor)
export const supabaseAdmin = createClient(
  supabaseUrl,
  process.env.SUPABASE_SERVICE_ROLE_KEY!
)
```

---

## 🔐 Configuração de Autenticação

### Opção 1: Auth do Supabase (Recomendado)

O Supabase tem autenticação built-in. Para usar:

1. No dashboard, vá em **Authentication** → **Providers**
2. Habilite **Email** provider
3. Configure o template de email (opcional)

#### Implementar Login

```typescript
import { supabase } from '@/lib/supabase'

// Login
async function login(email: string, password: string) {
  const { data, error } = await supabase.auth.signInWithPassword({
    email,
    password,
  })
  return { data, error }
}

// Cadastro
async function signup(email: string, password: string) {
  const { data, error } = await supabase.auth.signUp({
    email,
    password,
  })
  return { data, error }
}

// Logout
async function logout() {
  const { error } = await supabase.auth.signOut()
  return { error }
}

// Obter usuário atual
async function getCurrentUser() {
  const { data: { user } } = await supabase.auth.getUser()
  return user
}
```

### Opção 2: Manter Sistema Atual e Sincronizar

Se quiser manter seu sistema de auth atual, você pode:

1. Criar usuários no Supabase quando criar no seu sistema
2. Usar a `service_role_key` para operações administrativas
3. Implementar middleware para sincronização

---

## 📊 Exemplos de Queries

### Buscar Documentos do Cliente

```typescript
const { data: documents, error } = await supabase
  .from('documents')
  .select(`
    *,
    uploaded_by:users(id, name, email),
    folder:folders(id, name),
    tags:document_tags(tag:tags(*))
  `)
  .eq('client_id', clientId)
  .order('created_at', { ascending: false })
```

### Criar Documento

```typescript
const { data: document, error } = await supabase
  .from('documents')
  .insert({
    title: 'Meu Documento',
    description: 'Descrição do documento',
    file_name: 'documento.pdf',
    file_size: 1024000,
    file_type: 'application/pdf',
    file_path: '/uploads/documento.pdf',
    client_id: clientId,
    uploaded_by: userId,
    status: 'pending'
  })
  .select()
  .single()
```

### Buscar Notificações Não Lidas

```typescript
const { data: notifications, error } = await supabase
  .from('notifications')
  .select('*')
  .eq('user_id', userId)
  .eq('is_read', false)
  .order('created_at', { ascending: false })
```

### Adicionar Comentário

```typescript
const { data: comment, error } = await supabase
  .from('comments')
  .insert({
    document_id: documentId,
    user_id: userId,
    content: 'Meu comentário',
    is_internal: false
  })
  .select()
  .single()
```

### Busca Full-Text em Documentos

```typescript
const { data: documents, error } = await supabase
  .rpc('search_documents', {
    search_query: 'termo de busca',
    client_id_param: clientId
  })
```

### Obter Estatísticas do Cliente

```typescript
const { data: stats, error } = await supabase
  .rpc('get_client_stats', {
    client_id_param: clientId
  })

// Retorna:
// {
//   total_users: 10,
//   total_documents: 50,
//   total_folders: 5,
//   storage_used: 10485760,
//   documents_by_status: {
//     pending: 10,
//     approved: 30,
//     rejected: 5,
//     in_review: 5
//   }
// }
```

---

## 🔒 Segurança (RLS - Row Level Security)

O schema já inclui políticas de segurança (RLS) que garantem:

### ✅ O que está protegido:

1. **Super Admin** pode ver TUDO
2. **Admin do Cliente** pode:
   - Ver e gerenciar usuários do próprio cliente
   - Ver e gerenciar documentos do próprio cliente
   - Gerenciar pastas e tags do cliente
3. **Colaborador** pode:
   - Ver documentos do próprio cliente
   - Fazer upload de documentos
   - Comentar em documentos
   - Editar apenas próprios documentos/comentários

### ⚠️ Importante:

- As políticas RLS são **aplicadas automaticamente**
- Mesmo se alguém tentar acessar via API, será bloqueado
- Use `service_role_key` **apenas no servidor** para bypass do RLS

---

## 🔄 Storage de Arquivos

Para armazenar os arquivos reais (não apenas metadados):

### 1. Criar Bucket no Supabase

```typescript
// Criar bucket (rode apenas uma vez)
const { data, error } = await supabase.storage.createBucket('documents', {
  public: false,
  fileSizeLimit: 52428800, // 50MB
  allowedMimeTypes: ['application/pdf', 'image/jpeg', 'image/png', 'application/msword']
})
```

### 2. Fazer Upload de Arquivo

```typescript
const { data, error } = await supabase.storage
  .from('documents')
  .upload(`${clientId}/${Date.now()}_${fileName}`, file, {
    cacheControl: '3600',
    upsert: false
  })

// Obter URL pública
const { data: urlData } = supabase.storage
  .from('documents')
  .getPublicUrl(data.path)
```

### 3. Configurar Políticas de Storage

No dashboard do Supabase:

1. Vá em **Storage** → **Policies**
2. Crie políticas para:
   - Upload: Apenas usuários autenticados
   - Download: Apenas do próprio cliente
   - Delete: Apenas admins

---

## 📈 Monitoramento e Logs

### Ver Logs em Tempo Real

No dashboard:
1. **Database** → **Logs** - Ver queries executadas
2. **Auth** → **Logs** - Ver tentativas de login
3. **Storage** → **Logs** - Ver uploads/downloads

### Configurar Alertas

Em **Settings** → **Alerts**, você pode configurar:
- Alertas de uso de recursos
- Alertas de erros
- Alertas de segurança

---

## 🚀 Performance e Otimização

### 1. Índices Já Criados

O schema inclui índices para:
- Buscas por cliente
- Buscas por status
- Buscas full-text
- Ordenação por data

### 2. Caching

```typescript
// Usar cache do Supabase
const { data, error } = await supabase
  .from('documents')
  .select('*')
  .eq('client_id', clientId)
  .cache(300) // Cache por 5 minutos
```

### 3. Paginação

```typescript
const pageSize = 20
const page = 1

const { data, error } = await supabase
  .from('documents')
  .select('*')
  .range((page - 1) * pageSize, page * pageSize - 1)
```

---

## 🔧 Manutenção

### Backup Automático

O Supabase faz backup automático diário dos dados. Para backup manual:

1. **Database** → **Backups**
2. Clique em **"Create backup"**

### Migrations

Para mudanças futuras no schema:

```bash
# Criar migration
supabase migration new nome_da_migration

# Editar o arquivo gerado em supabase/migrations/

# Aplicar migration
supabase db push
```

---

## 🐛 Troubleshooting

### Erro: "relation does not exist"

**Solução**: Verifique se o schema foi executado corretamente. Rode novamente.

### Erro: "permission denied for table"

**Solução**: Verifique as políticas RLS. Certifique-se de estar autenticado corretamente.

### Erro: "JWT expired"

**Solução**: Token de autenticação expirou. Faça login novamente.

### Erro: "row level security policy violation"

**Solução**: Você está tentando acessar dados que não pertencem ao seu cliente/usuário.

---

## 📚 Recursos Úteis

- [Documentação Supabase](https://supabase.com/docs)
- [Supabase Auth](https://supabase.com/docs/guides/auth)
- [Row Level Security](https://supabase.com/docs/guides/auth/row-level-security)
- [Storage](https://supabase.com/docs/guides/storage)
- [Functions](https://supabase.com/docs/guides/functions)

---

## ✅ Checklist de Configuração

- [ ] Projeto criado no Supabase
- [ ] Schema SQL executado
- [ ] Tabelas verificadas no Table Editor
- [ ] Credenciais copiadas
- [ ] Arquivo `.env.local` criado
- [ ] Cliente Supabase configurado (`lib/supabase.ts`)
- [ ] Dependências instaladas
- [ ] Auth configurada
- [ ] Políticas RLS verificadas
- [ ] Storage bucket criado (se necessário)
- [ ] Teste de login realizado
- [ ] Primeiro documento criado

---

## 🎉 Próximos Passos

Depois de configurar o Supabase:

1. Integrar com o sistema atual de auth
2. Migrar upload de arquivos para Supabase Storage
3. Implementar real-time updates (Supabase Realtime)
4. Configurar notificações por email (Supabase Edge Functions)
5. Implementar webhooks para integrações

---

**Desenvolvido com 💜 para um sistema enterprise de alto nível!**

