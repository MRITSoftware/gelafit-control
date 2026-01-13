# 🏋️ Sistema de Totem Interativo MotionFit

Sistema profissional de autoatendimento desenvolvido para academias, permitindo que clientes e alunos realizem diversas ações de forma prática e sem necessidade de auxílio da recepção.

## 🚀 Funcionalidades

- ✅ **Venda de Produtos e Serviços** - Compra de planos, suplementos, bebidas
- ✅ **Check-in em Agregadores** - Integração com Gympass e TotalPass
- ✅ **Consulta de Dúvidas** - FAQ com informações úteis
- ✅ **Impressão de Treino** - Acesso e impressão de fichas de treino
- ✅ **Pagamento** - Pix, cartão de crédito/débito e QR Code

## 🛠️ Tecnologias

### Frontend
- React 18
- TypeScript
- Tailwind CSS
- React Router
- React Query

### Backend
- Node.js
- Express
- TypeScript
- PostgreSQL
- Redis

## 📦 Instalação

```bash
# Instalar todas as dependências
npm run install-all

# Executar em modo desenvolvimento
npm run dev
```

## 🏗️ Estrutura do Projeto

```
totem-interativo/
├── client/          # Frontend React
├── server/          # Backend Node.js
├── shared/          # Tipos compartilhados
└── docs/           # Documentação
```

## 🚀 Executando o Projeto

1. **Desenvolvimento:**
   ```bash
   npm run dev
   ```

2. **Produção:**
   ```bash
   npm run build
   ```

## 📱 Interface do Totem

Interface otimizada para telas touch com:
- Design responsivo
- Navegação intuitiva
- Acessibilidade
- Performance otimizada

## 🔧 Configuração

Configure as variáveis de ambiente no arquivo `.env`:

```env
# Database
DATABASE_URL=postgresql://user:password@localhost:5432/totem_db

# Redis
REDIS_URL=redis://localhost:6379

# APIs
GYMPASS_API_KEY=your_key
TOTALPASS_API_KEY=your_key

# Pagamento
STRIPE_SECRET_KEY=your_key
MERCADO_PAGO_ACCESS_TOKEN=your_token
```

## 📄 Licença

MIT License

---

**Desenvolvido com ❤️ por MRIT para academias modernas**
