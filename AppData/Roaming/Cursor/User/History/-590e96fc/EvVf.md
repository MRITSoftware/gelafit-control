# Sistema de Assinatura Eletrônica (eSign)

Sistema completo para gerenciamento de documentos e assinaturas eletrônicas.

## Estrutura do Projeto

```
esign/
├── backend/          # API e lógica de negócio
├── frontend/         # Interface do usuário
├── shared/           # Código compartilhado entre frontend e backend
└── docs/             # Documentação
```

## Tecnologias

- **Backend**: Node.js + Express (ou NestJS)
- **Frontend**: React + TypeScript
- **Database**: PostgreSQL ou MongoDB
- **Autenticação**: JWT

## Como começar

### Pré-requisitos
- Node.js (versão 16 ou superior)
- MongoDB instalado e rodando (ou use MongoDB Atlas)

### Backend

```bash
# Navegar para a pasta do backend
cd backend

# Instalar dependências
npm install

# Criar arquivo .env (copie do .env.example e ajuste)
cp .env.example .env

# Executar servidor de desenvolvimento
npm run dev
```

O backend estará rodando em `http://localhost:3000`

### Frontend

**⚠️ IMPORTANTE: Não abra o index.html diretamente no navegador!**

O frontend usa Vite e precisa ser executado via servidor de desenvolvimento:

```bash
# Em um novo terminal, navegar para a pasta do frontend
cd frontend

# Instalar dependências
npm install

# Executar servidor de desenvolvimento
npm run dev
```

O frontend estará rodando em `http://localhost:5173`

### Erro comum: CORS policy

Se você ver o erro:
```
Access to script at 'file:///...' has been blocked by CORS policy
```

**Solução**: Você está abrindo o `index.html` diretamente no navegador. Use o comando `npm run dev` no diretório `frontend` para iniciar o servidor de desenvolvimento.

## Funcionalidades

- [ ] Autenticação de usuários
- [ ] Upload de documentos
- [ ] Criação de campos de assinatura
- [ ] Assinatura eletrônica de documentos
- [ ] Histórico e auditoria
- [ ] Notificações por email

