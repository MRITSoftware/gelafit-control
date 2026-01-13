# Sistema de Gestão de Documentos

Sistema web de gestão de documentos com autenticação via Supabase, controle de acesso por papéis, fluxo de revisão, comentários e histórico de versões.

## 🚀 Tecnologias

- **Next.js 14** - Framework React
- **TypeScript** - Tipagem estática
- **Supabase** - Autenticação e banco de dados
- **Tailwind CSS** - Estilização

## 📦 Instalação

1. Instale as dependências:
```bash
npm install
```

2. Configure as variáveis de ambiente:
   - Copie `.env.local.example` para `.env.local`
   - Adicione suas credenciais do Supabase:
     - `NEXT_PUBLIC_SUPABASE_URL`
     - `NEXT_PUBLIC_SUPABASE_ANON_KEY`

3. Execute o servidor de desenvolvimento:
```bash
npm run dev
```

## 🔐 Papéis do Sistema

- **admin**: Acesso total, gerencia usuários e visualiza tudo
- **equipe**: Gerencia documentos, altera status, comenta, atualiza versões
- **usuario (cliente)**: Envia, visualiza e baixa seus próprios documentos, comenta

## 📁 Estrutura

```
/
├── app/
│   ├── login/          # Tela de login
│   ├── admin/          # Área administrativa (a criar)
│   ├── team/           # Área da equipe (a criar)
│   └── client/         # Área do cliente (a criar)
├── lib/
│   └── supabase/       # Clientes Supabase
├── types/
│   └── database.ts     # Tipos do banco de dados
└── middleware.ts        # Proteção de rotas
```

## 🎯 Funcionalidades Implementadas

- ✅ Tela de login com autenticação Supabase
- ✅ Middleware de proteção de rotas
- ✅ Redirecionamento automático por papel após login
- ✅ Estrutura base do projeto

## 📝 Próximos Passos

- Dashboards por papel (admin, equipe, cliente)
- Upload e gerenciamento de documentos
- Sistema de comentários
- Controle de versões
- Filtros e listagens

