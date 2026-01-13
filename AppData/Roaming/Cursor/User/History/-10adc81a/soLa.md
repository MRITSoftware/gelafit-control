# 🚀 Guia Rápido - Configuração Supabase em 10 Minutos

## ⚡ Passo a Passo Super Rápido

### 1️⃣ Criar Projeto (2 min)
```
1. Acesse supabase.com
2. New Project
3. Escolha nome e senha
4. Aguarde criação
```

### 2️⃣ Executar Schema (3 min)
```
1. Dashboard → SQL Editor
2. New Query
3. Cole o conteúdo de: supabase-schema.sql
4. Clique "Run"
5. Aguarde ✅
```

### 3️⃣ Adicionar Dados de Exemplo (1 min)
```
1. SQL Editor → New Query
2. Cole o conteúdo de: supabase-seed.sql
3. Clique "Run"
4. Pronto! Dados inseridos ✅
```

### 4️⃣ Configurar .env (2 min)
```
1. Dashboard → Settings → API
2. Copie:
   - Project URL
   - anon public key
3. Crie .env.local na raiz:
```

```env
NEXT_PUBLIC_SUPABASE_URL=https://seu-projeto.supabase.co
NEXT_PUBLIC_SUPABASE_ANON_KEY=sua-chave-aqui
SUPABASE_SERVICE_ROLE_KEY=chave-service-role
JWT_SECRET=qualquer-string-secreta
```

### 5️⃣ Instalar Dependências (2 min)
```bash
npm install @supabase/supabase-js @supabase/auth-helpers-nextjs
```

---

## 📊 Estrutura do Banco

### Tabelas Principais
```
📁 clients          → Clientes do sistema
👤 users            → Usuários (super_admin, client_admin, collaborator)
📄 documents        → Documentos enviados
📂 folders          → Pastas para organização
🏷️  tags            → Tags de categorização
💬 comments         → Comentários em documentos
🔔 notifications    → Notificações in-app
📋 audit_logs       → Log de auditoria
```

### Tabelas de Funcionalidades
```
⭐ favorites        → Documentos favoritos
🔗 shares          → Links de compartilhamento
✍️  signatures      → Assinaturas digitais
🔄 workflows       → Workflows de aprovação
💬 chat_messages   → Mensagens de chat
📝 annotations     → Anotações em documentos
📋 templates       → Templates de documentos
📊 activities      → Analytics e atividades
⚙️  client_settings → Configurações por cliente
👤 user_preferences → Preferências do usuário
```

---

## 🔐 Usuários de Teste (senha: 123456)

### Super Admin
```
Email: admin@sistema.com
Acesso: Total ao sistema
```

### Admin do Cliente
```
Email: admin@empresaexemplo.com
Acesso: Gerenciar seu cliente
```

### Colaborador
```
Email: colaborador@empresaexemplo.com
Acesso: Enviar/ver documentos
```

---

## 💻 Código Básico

### Criar Cliente Supabase (`lib/supabase.ts`)

```typescript
import { createClient } from '@supabase/supabase-js'

const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL!
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY!

export const supabase = createClient(supabaseUrl, supabaseAnonKey)
```

### Buscar Documentos

```typescript
const { data, error } = await supabase
  .from('documents')
  .select('*, uploaded_by:users(name), folder:folders(name)')
  .eq('client_id', clientId)
  .order('created_at', { ascending: false })
```

### Criar Documento

```typescript
const { data, error } = await supabase
  .from('documents')
  .insert({
    title: 'Meu Documento',
    file_name: 'doc.pdf',
    file_size: 1024,
    file_type: 'application/pdf',
    file_path: '/uploads/doc.pdf',
    client_id: clientId,
    uploaded_by: userId,
    status: 'pending'
  })
  .select()
  .single()
```

### Adicionar Comentário

```typescript
const { data, error } = await supabase
  .from('comments')
  .insert({
    document_id: docId,
    user_id: userId,
    content: 'Meu comentário'
  })
```

### Buscar Notificações Não Lidas

```typescript
const { data, error } = await supabase
  .from('notifications')
  .select('*')
  .eq('user_id', userId)
  .eq('is_read', false)
  .order('created_at', { ascending: false })
```

---

## 🔍 Queries Úteis

### Busca por Texto
```typescript
const { data } = await supabase.rpc('search_documents', {
  search_query: 'contrato',
  client_id_param: clientId
})
```

### Estatísticas do Cliente
```typescript
const { data } = await supabase.rpc('get_client_stats', {
  client_id_param: clientId
})
```

### Documentos com Detalhes
```typescript
const { data } = await supabase
  .from('documents_with_details')
  .select('*')
  .eq('client_id', clientId)
```

---

## 🎯 Checklist Rápido

- [ ] ✅ Projeto criado no Supabase
- [ ] ✅ Schema executado (supabase-schema.sql)
- [ ] ✅ Dados inseridos (supabase-seed.sql)
- [ ] ✅ Tabelas visíveis no Table Editor
- [ ] ✅ .env.local criado e preenchido
- [ ] ✅ Dependências instaladas
- [ ] ✅ lib/supabase.ts criado
- [ ] ✅ Teste de conexão funcionando

---

## 🐛 Problemas Comuns

### Erro: "relation does not exist"
**Solução:** Execute o schema novamente

### Erro: "permission denied"
**Solução:** Verifique se as políticas RLS estão ativas

### Erro: "Invalid JWT"
**Solução:** Verifique se as credenciais estão corretas no .env.local

---

## 📚 Próximos Passos

1. ✅ Integrar autenticação do Supabase
2. ✅ Migrar upload de arquivos para Supabase Storage
3. ✅ Implementar real-time updates
4. ✅ Configurar backup automático
5. ✅ Adicionar monitoramento

---

## 🆘 Ajuda

**Documentação Completa:** Veja `supabase-setup.md`

**Dúvidas?**
- [Docs Supabase](https://supabase.com/docs)
- [Discord Supabase](https://discord.supabase.com)

---

## 📁 Arquivos Importantes

```
📦 Seu Projeto
├── 📄 supabase-schema.sql      ← Schema completo (EXECUTE PRIMEIRO)
├── 📄 supabase-seed.sql        ← Dados de exemplo (EXECUTE DEPOIS)
├── 📄 supabase-setup.md        ← Guia completo e detalhado
├── 📄 GUIA-RAPIDO-SUPABASE.md  ← Este arquivo (guia rápido)
├── 📄 .env.local               ← Credenciais (CRIAR)
└── 📂 lib
    └── 📄 supabase.ts          ← Cliente Supabase (CRIAR)
```

---

## 🎉 Pronto!

Após seguir esses passos, você terá:

✅ Banco de dados completo no Supabase  
✅ 4 clientes de exemplo  
✅ 12 usuários de teste  
✅ 9 documentos de exemplo  
✅ Sistema multi-tenant funcionando  
✅ Row Level Security configurado  
✅ Políticas de acesso ativas  
✅ Índices para performance  
✅ Funções auxiliares criadas  

**Tempo total:** ~10 minutos ⏱️

---

**Desenvolvido com 💜 para um sistema enterprise de alto nível!**

