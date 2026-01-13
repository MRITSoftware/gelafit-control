# 🏋️ Sistema de Totem Interativo MotionFit - Guia de Instalação

## 📋 Pré-requisitos

- **Node.js** (versão 16 ou superior)
- **npm** (vem com o Node.js)
- **Git** (opcional, para clonar o repositório)

## 🚀 Instalação Rápida

### Windows
```bash
# Execute o arquivo start.bat
start.bat
```

### Linux/Mac
```bash
# Execute o script start.sh
./start.sh
```

### Instalação Manual

1. **Instalar dependências do projeto principal:**
```bash
npm install
```

2. **Instalar dependências do backend:**
```bash
cd server
npm install
```

3. **Instalar dependências do frontend:**
```bash
cd client
npm install
```

4. **Executar o sistema:**
```bash
# Terminal 1 - Backend
cd server
npm run dev

# Terminal 2 - Frontend
cd client
npm start
```

## 🌐 Acessos

- **Frontend (Totem):** http://localhost:3000
- **Backend (API):** http://localhost:3001
- **Health Check:** http://localhost:3001/health

## ⚙️ Configuração

### Backend (.env)
Copie o arquivo `server/env.example` para `server/.env` e configure:

```env
# Database
DATABASE_URL=postgresql://user:password@localhost:5432/totem_db

# Redis
REDIS_URL=redis://localhost:6379

# Server
PORT=3001
NODE_ENV=development

# JWT
JWT_SECRET=your_jwt_secret_key_here
JWT_EXPIRES_IN=24h

# APIs Externas
GYMPASS_API_KEY=your_gympass_api_key
TOTALPASS_API_KEY=your_totalpass_api_key

# Pagamento
STRIPE_SECRET_KEY=sk_test_your_stripe_secret_key
MERCADO_PAGO_ACCESS_TOKEN=your_mercadopago_access_token
```

### Frontend (.env)
Copie o arquivo `client/env.example` para `client/.env`:

```env
REACT_APP_API_URL=http://localhost:3001/api
REACT_APP_ENV=development
```

## 🎯 Funcionalidades Implementadas

### ✅ Sistema Completo
- **Interface Touch-Friendly** - Otimizada para telas de toque
- **Sistema de Login** - Autenticação por CPF
- **FAQ Interativo** - Perguntas frequentes com busca
- **Venda de Produtos** - Carrinho de compras completo
- **Check-in Multiplataforma** - Gympass, TotalPass e QR Code
- **Impressão de Treinos** - Fichas de treino personalizadas
- **Sistema de Pagamento** - PIX, cartão e QR Code
- **Design Responsivo** - Funciona em diferentes tamanhos de tela

### 🏗️ Arquitetura
- **Frontend:** React 18 + TypeScript + Tailwind CSS
- **Backend:** Node.js + Express + TypeScript
- **Estado:** React Context + React Query
- **UI/UX:** Interface otimizada para totem

## 📱 Como Usar

1. **Acesse o totem** em http://localhost:3000
2. **Navegue pelas opções** usando a interface touch
3. **Faça login** com CPF para acessar funcionalidades completas
4. **Use as funcionalidades:**
   - Compre produtos e serviços
   - Faça check-in com agregadores
   - Imprima suas fichas de treino
   - Consulte o FAQ

## 🔧 Desenvolvimento

### Estrutura do Projeto
```
totem-interativo/
├── client/          # Frontend React
│   ├── src/
│   │   ├── components/  # Componentes reutilizáveis
│   │   ├── pages/       # Páginas do sistema
│   │   ├── contexts/    # Contextos React
│   │   ├── services/    # Serviços de API
│   │   └── types/       # Tipos TypeScript
├── server/          # Backend Node.js
│   ├── src/
│   │   ├── controllers/ # Controladores
│   │   ├── routes/      # Rotas da API
│   │   ├── middleware/  # Middlewares
│   │   └── services/    # Serviços de negócio
└── docs/            # Documentação
```

### Scripts Disponíveis
```bash
# Desenvolvimento
npm run dev          # Executa frontend e backend
npm run server       # Apenas backend
npm run client       # Apenas frontend

# Produção
npm run build        # Build do frontend
npm start            # Inicia em produção
```

## 🐛 Solução de Problemas

### Erro de Porta em Uso
```bash
# Windows
netstat -ano | findstr :3000
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:3000 | xargs kill -9
```

### Erro de Dependências
```bash
# Limpar cache e reinstalar
rm -rf node_modules package-lock.json
npm install
```

### Erro de CORS
Verifique se o backend está rodando na porta 3001 e o frontend na 3000.

## 📞 Suporte

- **Email:** suporte@academia.com
- **Telefone:** (11) 9999-9999
- **Documentação:** [Link para docs completas]

## 📄 Licença

MIT License - Veja o arquivo LICENSE para detalhes.

---

**Desenvolvido com ❤️ para academias modernas**
