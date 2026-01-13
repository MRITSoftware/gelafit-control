# MR Food - Plataforma de Gestão de Delivery

Sistema completo de gestão de pedidos e entregas para pequenos restaurantes.

## 🚀 Funcionalidades

### Essenciais
- ✅ Cadastro e Gestão de Pedidos
- ✅ Controle de Entregas com Roteirização
- ✅ Gestão de Clientes e Fidelidade
- ✅ Integração com iFood/WhatsApp

### Avançadas
- ✅ Gestão de Motoboys/Entregadores
- ✅ Relatórios e Analytics
- ✅ Sistema de Notificações
- ✅ Comunicação Automática

## 🛠️ Tecnologias

- **Next.js 14** - Framework React
- **TypeScript** - Tipagem estática
- **SQLite** - Banco de dados local
- **Tailwind CSS** - Estilização
- **React Hook Form** - Formulários
- **Zod** - Validação

## 📦 Instalação

```bash
npm install
```

## 🔧 Configuração

1. Inicialize o banco de dados:

```bash
npm run db:init
```

Ou acesse: `http://localhost:3000/api/init-db` após iniciar o servidor

2. Inicie o servidor de desenvolvimento:

```bash
npm run dev
```

Acesse [http://localhost:3000](http://localhost:3000)

## 📝 Estrutura do Projeto

```
mr-food/
├── app/                    # Next.js App Router
│   ├── (auth)/            # Rotas de autenticação
│   ├── (dashboard)/       # Dashboard principal
│   └── api/               # API Routes
├── components/            # Componentes React
├── lib/                   # Utilitários e configurações
│   ├── db.ts             # Configuração do banco SQLite
│   ├── db-client.ts      # Cliente do banco (servidor)
│   ├── db-client-browser.ts # Cliente do banco (browser)
│   └── auth.ts           # Sistema de autenticação
├── types/                 # Tipos TypeScript
└── data/                  # Banco de dados SQLite (gerado automaticamente)
```

## 🎯 Primeiros Passos

1. **Inicializar Banco**: Execute `npm run db:init` ou acesse `/api/init-db`
2. **Criar Conta**: Acesse `/register` e crie sua conta de restaurante
3. **Configurar Restaurante**: Vá em Configurações e complete os dados
4. **Adicionar Cardápio**: Vá em Cardápio e adicione seus produtos
5. **Criar Pedido**: Vá em Pedidos > Novo Pedido para testar

## 📊 Banco de Dados

O banco de dados SQLite é criado automaticamente na pasta `data/` quando você inicializa o sistema. Todos os dados são armazenados localmente.

### Migração Futura para Supabase

O código foi estruturado para facilitar migração futura para Supabase. Basta substituir os clientes em `lib/supabase/` pelos clientes reais do Supabase.

## 🔒 Autenticação

O sistema usa autenticação baseada em sessões com JWT. As senhas são criptografadas com bcrypt.

## 🐛 Troubleshooting

### Erro ao inicializar banco

- Verifique se a pasta `data/` tem permissões de escrita
- Execute `npm run db:init` manualmente

### Erro de autenticação

- Limpe os cookies do navegador
- Verifique se o banco foi inicializado corretamente

## 📚 Próximos Passos

- [ ] Adicionar mais relatórios
- [ ] Implementar app mobile para entregadores
- [ ] Migrar para Supabase (opcional)
- [ ] Adicionar testes automatizados
