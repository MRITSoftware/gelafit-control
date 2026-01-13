# 📚 Guia de Referência Rápida

## 🎯 Comandos Principais

```bash
# Instalar dependências
npm install

# Iniciar servidor de desenvolvimento
npm run dev

# Build para produção
npm run build

# Iniciar em produção
npm start

# Verificar erros de código
npm run lint
```

## 🔐 Níveis de Acesso (Roles)

### Admin
- ✅ Gerenciar usuários (criar, editar, deletar)
- ✅ Ver todos os clientes
- ✅ Ver todos os documentos
- ✅ Mudar status de documentos
- ✅ Acessar dashboard administrativo
- ✅ Comentar em documentos

### Equipe (Team)
- ✅ Ver todos os documentos
- ✅ Baixar documentos
- ✅ Atualizar documentos (novas versões)
- ✅ Mudar status de documentos
- ✅ Comentar em documentos
- ❌ Gerenciar usuários

### Cliente (Client)
- ✅ Enviar documentos PDF
- ✅ Ver seus próprios documentos
- ✅ Baixar seus documentos
- ✅ Comentar em seus documentos
- ❌ Ver documentos de outros clientes
- ❌ Atualizar documentos (apenas equipe pode)

## 📊 Status de Documentos

| Status | Cor | Descrição |
|--------|-----|-----------|
| `pending` | 🟡 Amarelo | Aguardando revisão |
| `in_review` | 🔵 Azul | Em análise pela equipe |
| `approved` | 🟢 Verde | Aprovado |
| `rejected` | 🔴 Vermelho | Rejeitado |

## 🗂️ Estrutura de Pastas

```
projeto/
├── app/                      # Páginas do Next.js (App Router)
│   ├── admin/               # Área administrativa
│   │   ├── dashboard/       # Dashboard do admin
│   │   ├── users/           # Gerenciar usuários
│   │   ├── clients/         # Ver clientes
│   │   └── documents/       # Ver todos documentos
│   ├── team/                # Área da equipe
│   │   ├── dashboard/       # Dashboard da equipe
│   │   └── documents/       # Gerenciar documentos
│   ├── client/              # Área do cliente
│   │   ├── dashboard/       # Dashboard do cliente
│   │   ├── upload/          # Enviar documentos
│   │   └── documents/       # Ver documentos
│   ├── login/               # Página de login
│   ├── layout.tsx           # Layout raiz
│   ├── page.tsx             # Página inicial (redireciona)
│   └── globals.css          # Estilos globais
├── components/              # Componentes reutilizáveis
│   ├── Header.tsx           # Cabeçalho das páginas
│   └── Sidebar.tsx          # Menu lateral
├── lib/                     # Utilitários e configurações
│   ├── supabase/            # Cliente Supabase
│   │   ├── client.ts        # Cliente para componentes
│   │   └── server.ts        # Cliente para servidor
│   └── utils.ts             # Funções utilitárias
├── types/                   # Tipos TypeScript
│   ├── database.ts          # Tipos do banco
│   └── index.ts             # Tipos principais
├── middleware.ts            # Middleware de autenticação
├── supabase-setup.sql       # Script SQL do banco
├── package.json             # Dependências
├── tailwind.config.ts       # Configuração Tailwind
├── tsconfig.json            # Configuração TypeScript
└── next.config.js           # Configuração Next.js
```

## 🔗 Rotas Principais

### Públicas
- `/login` - Página de login

### Admin
- `/admin/dashboard` - Dashboard administrativo
- `/admin/users` - Gerenciar usuários
- `/admin/clients` - Ver clientes
- `/admin/documents` - Ver todos documentos

### Equipe
- `/team/dashboard` - Dashboard da equipe
- `/team/documents` - Gerenciar documentos

### Cliente
- `/client/dashboard` - Dashboard do cliente
- `/client/upload` - Enviar documento
- `/client/documents` - Ver meus documentos

## 💾 Tabelas do Banco

### profiles
```sql
id           UUID      (PK, FK para auth.users)
email        TEXT      (único)
full_name    TEXT
role         TEXT      (admin/team/client)
avatar_url   TEXT
created_at   TIMESTAMP
updated_at   TIMESTAMP
```

### documents
```sql
id           UUID      (PK)
client_id    UUID      (FK para profiles)
title        TEXT
description  TEXT
file_path    TEXT
file_name    TEXT
file_size    BIGINT
version      INTEGER
status       TEXT      (pending/in_review/approved/rejected)
created_at   TIMESTAMP
updated_at   TIMESTAMP
created_by   UUID      (FK para profiles)
```

### comments
```sql
id           UUID      (PK)
document_id  UUID      (FK para documents)
user_id      UUID      (FK para profiles)
content      TEXT
created_at   TIMESTAMP
```

### document_history
```sql
id                  UUID      (PK)
document_id         UUID      (FK para documents)
version             INTEGER
file_path           TEXT
file_name           TEXT
updated_by          UUID      (FK para profiles)
updated_at          TIMESTAMP
change_description  TEXT
```

## 🛠️ Funções Úteis

### Criar novo usuário via SQL
```sql
-- 1. Criar usuário no Auth (pelo dashboard)
-- 2. Atualizar o perfil:
UPDATE public.profiles 
SET role = 'admin', full_name = 'Nome do Usuário'
WHERE email = 'email@exemplo.com';
```

### Ver todos os documentos de um cliente
```sql
SELECT * FROM documents 
WHERE client_id = 'uuid-do-cliente'
ORDER BY created_at DESC;
```

### Ver comentários de um documento
```sql
SELECT c.*, p.full_name, p.email 
FROM comments c
JOIN profiles p ON c.user_id = p.id
WHERE c.document_id = 'uuid-do-documento'
ORDER BY c.created_at ASC;
```

## 🎨 Classes CSS Úteis

### Botões
```css
btn-primary    /* Botão primário azul */
btn-secondary  /* Botão secundário cinza */
btn-danger     /* Botão vermelho */
```

### Inputs
```css
input          /* Input padrão */
```

### Cards
```css
card           /* Card branco com sombra */
```

## 🔧 Configurações do Supabase

### Storage - Bucket documents
- **Nome**: documents
- **Tipo**: Private
- **Tamanho máximo**: 50MB (configurável)

### Authentication
- **Providers**: Email/Password
- **Auto-confirm**: Sim (para desenvolvimento)

## 📱 Responsividade

O sistema é totalmente responsivo usando Tailwind CSS:

### Breakpoints
- `sm`: 640px (mobile landscape)
- `md`: 768px (tablet)
- `lg`: 1024px (desktop)
- `xl`: 1280px (desktop large)

### Exemplo de uso:
```jsx
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
  {/* Mobile: 1 coluna, Tablet: 2 colunas, Desktop: 4 colunas */}
</div>
```

## 🐛 Debug

### Ver logs do Next.js
```bash
npm run dev
# Logs aparecem no terminal
```

### Ver logs do Supabase
- Dashboard → Logs → Selecione o tipo (Auth/Database/Storage)

### Ver erros do navegador
- F12 → Console
- Verifique Network tab para requisições

## 🔒 Segurança

### Checklist de Segurança
- ✅ RLS ativado em todas as tabelas
- ✅ Storage com políticas configuradas
- ✅ Autenticação via Supabase Auth
- ✅ Validação de tipos de arquivo (apenas PDF)
- ✅ Limite de tamanho de arquivo
- ✅ HTTPS em produção
- ✅ Variáveis de ambiente protegidas

## 📞 Links Úteis

- **Supabase Dashboard**: https://supabase.com/dashboard
- **Documentação Supabase**: https://supabase.com/docs
- **Documentação Next.js**: https://nextjs.org/docs
- **Documentação Tailwind**: https://tailwindcss.com/docs
- **Documentação TypeScript**: https://typescriptlang.org/docs

## 💡 Dicas

### Performance
- Imagens são otimizadas automaticamente pelo Next.js
- Use `loading="lazy"` para imagens grandes
- O cache é gerenciado automaticamente

### Desenvolvimento
- Use `console.log()` para debug
- Instale React DevTools no navegador
- Use o SQL Editor do Supabase para queries

### Produção
- Sempre teste antes de fazer deploy
- Faça backup do banco periodicamente
- Monitore os logs regularmente

---

**Use este guia como referência rápida durante o desenvolvimento!** 📖

