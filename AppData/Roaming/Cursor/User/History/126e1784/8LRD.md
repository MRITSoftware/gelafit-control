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
- **Supabase** - Backend (Banco de dados, Auth, Real-time)
- **Tailwind CSS** - Estilização
- **React Hook Form** - Formulários
- **Zod** - Validação

## 📦 Instalação

```bash
npm install
```

## 🔧 Configuração

1. Crie um arquivo `.env.local` com suas credenciais do Supabase:

```env
NEXT_PUBLIC_SUPABASE_URL=your_supabase_url
NEXT_PUBLIC_SUPABASE_ANON_KEY=your_supabase_anon_key
```

2. Execute o script SQL em `supabase-setup.sql` no seu projeto Supabase

3. Inicie o servidor de desenvolvimento:

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
│   └── supabase/         # Cliente Supabase
├── types/                 # Tipos TypeScript
└── supabase-setup.sql    # Schema do banco de dados
```

## 🎯 Próximos Passos

- [ ] Configurar autenticação
- [ ] Implementar dashboard
- [ ] Criar módulo de pedidos
- [ ] Integrar Google Maps
- [ ] Configurar webhooks

