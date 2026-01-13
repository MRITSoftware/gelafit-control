# Guia de Instalação - Sistema de Gerenciamento Empresarial Barrella Eventos

## Pré-requisitos

Antes de começar, certifique-se de ter instalado:

- **Node.js 18+** - [Download aqui](https://nodejs.org/)
- **PostgreSQL 14+** - [Download aqui](https://www.postgresql.org/download/)
- **Git** - [Download aqui](https://git-scm.com/)

## Instalação Rápida

### Windows
1. Execute o arquivo `setup.bat` como administrador
2. Aguarde a conclusão da instalação
3. Siga as instruções exibidas

### Linux/macOS
1. Execute o arquivo `setup.sh`:
```bash
chmod +x setup.sh
./setup.sh
```

## Instalação Manual

### 1. Clone o repositório
```bash
git clone <url-do-repositorio>
cd sge-barrella
```

### 2. Configure o banco de dados
1. Crie um banco de dados PostgreSQL chamado `sge_barrella`
2. Configure as credenciais no arquivo `backend/.env`

### 3. Instale as dependências do backend
```bash
cd backend
npm install
```

### 4. Configure o banco de dados
```bash
# Executar migrações
npx prisma migrate dev --name init

# Popular com dados iniciais
npx prisma db seed
```

### 5. Instale as dependências do frontend
```bash
cd ../frontend
npm install
```

### 6. Configure as variáveis de ambiente
Crie o arquivo `frontend/.env`:
```
REACT_APP_API_URL=http://localhost:3001/api
GENERATE_SOURCEMAP=false
```

## Executando o Sistema

### Desenvolvimento

1. **Inicie o backend** (Terminal 1):
```bash
cd backend
npm run dev
```

2. **Inicie o frontend** (Terminal 2):
```bash
cd frontend
npm start
```

3. **Acesse o sistema**:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:3001

### Produção com Docker

1. **Execute o Docker Compose**:
```bash
docker-compose up -d
```

2. **Acesse o sistema**:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:3001

## Credenciais de Acesso

Após a instalação, você pode acessar o sistema com:

- **Administrador**: admin@barrella.com.br / admin123
- **Gerente**: gerente@barrella.com.br / admin123
- **Usuário**: usuario@barrella.com.br / admin123

## Configuração do Banco de Dados

### Variáveis de Ambiente (backend/.env)

```env
# Configurações do Servidor
PORT=3001
NODE_ENV=development

# Configurações do Banco de Dados
DATABASE_URL="postgresql://usuario:senha@localhost:5432/sge_barrella?schema=public"

# Configurações de Autenticação
JWT_SECRET=sua-chave-secreta-aqui
JWT_EXPIRES_IN=7d

# Configurações de Email (opcional)
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=seu-email@gmail.com
SMTP_PASS=sua-senha-de-app
SMTP_FROM=Barrella Eventos <noreply@barrella.com.br>
```

## Solução de Problemas

### Erro de conexão com o banco
- Verifique se o PostgreSQL está rodando
- Confirme as credenciais no arquivo `.env`
- Teste a conexão: `psql -h localhost -U postgres -d sge_barrella`

### Erro de dependências
- Delete as pastas `node_modules` e `package-lock.json`
- Execute `npm install` novamente

### Erro de permissões (Linux/macOS)
- Execute `chmod +x setup.sh`
- Use `sudo` se necessário

### Porta já em uso
- Altere as portas nos arquivos de configuração
- Ou pare os serviços que estão usando as portas 3000/3001

## Estrutura do Projeto

```
sge-barrella/
├── backend/                 # API Node.js
│   ├── src/
│   │   ├── controllers/    # Controladores
│   │   ├── middleware/     # Middlewares
│   │   ├── routes/         # Rotas da API
│   │   └── index.ts        # Arquivo principal
│   ├── prisma/
│   │   ├── schema.prisma   # Schema do banco
│   │   └── seed.ts         # Dados iniciais
│   └── package.json
├── frontend/               # Aplicação React
│   ├── src/
│   │   ├── components/     # Componentes
│   │   ├── pages/          # Páginas
│   │   ├── services/       # Serviços de API
│   │   └── utils/          # Utilitários
│   └── package.json
├── docker-compose.yml      # Configuração Docker
├── setup.sh               # Script de instalação (Linux/macOS)
├── setup.bat              # Script de instalação (Windows)
└── README.md              # Documentação principal
```

## Comandos Úteis

### Backend
```bash
# Desenvolvimento
npm run dev

# Build para produção
npm run build

# Executar migrações
npx prisma migrate dev

# Resetar banco de dados
npx prisma migrate reset

# Visualizar banco de dados
npx prisma studio
```

### Frontend
```bash
# Desenvolvimento
npm start

# Build para produção
npm run build

# Executar testes
npm test
```

### Docker
```bash
# Iniciar todos os serviços
docker-compose up -d

# Parar todos os serviços
docker-compose down

# Ver logs
docker-compose logs -f

# Rebuild das imagens
docker-compose up --build
```

## Suporte

Se encontrar problemas durante a instalação:

1. Verifique se todos os pré-requisitos estão instalados
2. Consulte a documentação no README.md
3. Verifique os logs de erro
4. Entre em contato com o suporte técnico

## Próximos Passos

Após a instalação bem-sucedida:

1. Configure os dados da empresa em Configurações
2. Cadastre seus clientes
3. Crie produtos e serviços
4. Configure usuários e permissões
5. Explore os relatórios e dashboards

---

**Desenvolvido por**: Equipe de Desenvolvimento Barrella Eventos  
**Versão**: 1.0.0  
**Data**: 2024
