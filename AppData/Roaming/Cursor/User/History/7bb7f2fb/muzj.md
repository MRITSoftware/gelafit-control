# Sistema de Gerenciamento Empresarial - Barrella Eventos

## Visão Geral
Sistema moderno de gerenciamento empresarial desenvolvido para substituir o sistema legado da Barrella Locação e Instalação de Equipamentos Ltda. O sistema oferece uma interface moderna, intuitiva e responsiva para gerenciar todos os aspectos do negócio.

## Funcionalidades Principais

### 1. Gestão de Orçamentos
- Criação e edição de orçamentos
- Aprovação/negação de orçamentos
- Envio de orçamentos por email
- Cópia de orçamentos existentes
- Pesquisa avançada de orçamentos
- Gestão de itens do orçamento
- Descrição de pagamentos

### 2. Gestão de Clientes
- Cadastro completo de clientes
- Informações de contato (email, telefone, celular)
- Endereços múltiplos
- Dados fiscais (CNPJ/CPF, inscrição estadual)
- Histórico de orçamentos e pedidos
- Pesquisa avançada de clientes

### 3. Gestão de Produtos e Serviços
- Cadastro de produtos
- Categorização por tipo (Fixo, Locação, Serviço)
- Controle de estoque
- Tabelas de preços
- Gestão de fornecedores

### 4. Gestão de Pedidos
- Criação de pedidos baseados em orçamentos
- Controle de status
- Gestão de expedição
- Integração com estoque

### 5. Gestão Financeira
- Contas a receber
- Contas a pagar
- Relatórios financeiros
- Controle de pagamentos

### 6. Relatórios
- Relatórios de orçamentos
- Relatórios de pedidos
- Relatórios de clientes
- Relatórios de produtos
- Relatórios financeiros
- Relatórios administrativos

### 7. Utilitários
- Gestão de usuários e senhas
- Manutenção do sistema
- Backup e restauração
- Configurações da empresa

## Tecnologias Utilizadas

### Frontend
- **React 18** - Biblioteca para interface de usuário
- **TypeScript** - Tipagem estática
- **Tailwind CSS** - Framework de estilização
- **React Router** - Roteamento
- **React Hook Form** - Gerenciamento de formulários
- **React Query** - Gerenciamento de estado do servidor
- **Lucide React** - Ícones modernos

### Backend
- **Node.js** - Runtime JavaScript
- **Express.js** - Framework web
- **TypeScript** - Tipagem estática
- **Prisma** - ORM para banco de dados
- **PostgreSQL** - Banco de dados principal
- **JWT** - Autenticação
- **Bcrypt** - Criptografia de senhas

### Banco de Dados
- **PostgreSQL** - Banco de dados relacional
- **Prisma** - ORM e migrações

## Estrutura do Projeto

```
sge-barrella/
├── frontend/                 # Aplicação React
│   ├── src/
│   │   ├── components/      # Componentes reutilizáveis
│   │   ├── pages/          # Páginas da aplicação
│   │   ├── hooks/          # Hooks customizados
│   │   ├── services/       # Serviços de API
│   │   ├── types/          # Definições de tipos
│   │   └── utils/          # Utilitários
│   ├── public/             # Arquivos estáticos
│   └── package.json
├── backend/                 # API Node.js
│   ├── src/
│   │   ├── controllers/    # Controladores
│   │   ├── models/         # Modelos de dados
│   │   ├── routes/         # Rotas da API
│   │   ├── middleware/     # Middlewares
│   │   └── utils/          # Utilitários
│   └── package.json
├── database/               # Scripts de banco de dados
│   ├── migrations/         # Migrações do Prisma
│   └── seed/              # Dados iniciais
└── docs/                  # Documentação
```

## Instalação e Configuração

### Pré-requisitos
- Node.js 18+ 
- PostgreSQL 14+
- npm ou yarn

### Instalação

1. Clone o repositório:
```bash
git clone <repository-url>
cd sge-barrella
```

2. Instale as dependências do backend:
```bash
cd backend
npm install
```

3. Instale as dependências do frontend:
```bash
cd ../frontend
npm install
```

4. Configure as variáveis de ambiente:
```bash
# Backend
cp backend/.env.example backend/.env
# Edite o arquivo .env com suas configurações

# Frontend
cp frontend/.env.example frontend/.env
# Edite o arquivo .env com suas configurações
```

5. Configure o banco de dados:
```bash
cd backend
npx prisma migrate dev
npx prisma db seed
```

6. Inicie o servidor de desenvolvimento:
```bash
# Terminal 1 - Backend
cd backend
npm run dev

# Terminal 2 - Frontend
cd frontend
npm start
```

## Funcionalidades Detalhadas

### Interface Moderna
- Design responsivo que funciona em desktop, tablet e mobile
- Interface intuitiva com navegação por abas
- Formulários com validação em tempo real
- Modais e notificações modernas
- Tema claro/escuro

### Gestão de Orçamentos
- Formulário completo com todos os campos necessários
- Validação de dados em tempo real
- Salvamento automático de rascunhos
- Aprovação/negação com comentários
- Envio automático por email
- Histórico de alterações

### Gestão de Clientes
- Cadastro completo com validação de CPF/CNPJ
- Múltiplos endereços por cliente
- Histórico de interações
- Pesquisa avançada com filtros
- Importação/exportação de dados

### Relatórios Avançados
- Relatórios em PDF e Excel
- Gráficos e dashboards interativos
- Filtros personalizáveis
- Agendamento de relatórios
- Exportação de dados

## Contribuição

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## Suporte

Para suporte, entre em contato através de:
- Email: suporte@barrella.com.br
- Telefone: (11) 99999-9999
