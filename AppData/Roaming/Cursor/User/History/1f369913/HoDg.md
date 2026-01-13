# ERP SGE - Sistema de Gestão de Eventos e Locações (Desktop)

## 🖥️ Aplicação Desktop Windows

Este é um **sistema desktop** que roda diretamente no Windows, sem necessidade de navegador web!

## Estrutura do Projeto

```
erp-sge/
├── backend/                 # API Python + FastAPI
│   ├── main.py             # Servidor principal
│   └── requirements.txt    # Dependências Python
├── frontend/               # Interface React + Electron
│   ├── src/
│   │   ├── components/     # Componentes React
│   │   ├── contexts/       # Context API
│   │   └── App.js         # App principal
│   ├── public/
│   │   └── electron.js     # Configuração Electron
│   ├── package.json       # Dependências Node.js + Electron
│   └── tailwind.config.js # Configuração Tailwind
├── build-desktop.ps1      # Script para criar executável
└── README.md              # Este arquivo
```

## 🚀 Como Executar (Modo Desenvolvimento)

### Opção 1: Executar Aplicação Desktop

```powershell
# Navegar para o diretório frontend
cd frontend

# Instalar dependências
npm install

# Executar aplicação desktop (inicia backend + frontend automaticamente)
npm run electron-dev
```

### Opção 2: Executar Separadamente

```powershell
# Terminal 1 - Backend
cd backend
pip install -r requirements.txt
python main.py

# Terminal 2 - Frontend Desktop
cd frontend
npm install
npm run electron-dev
```

## 📦 Como Criar Executável Windows

```powershell
# Executar o script de build
.\build-desktop.ps1
```

Ou manualmente:

```powershell
cd frontend
npm install
npm run build
npm run electron-pack
```

O executável será criado na pasta `frontend/dist/`

## Funcionalidades Implementadas

### ✅ Autenticação
- Tela de login moderna e responsiva
- Sistema de autenticação com JWT (simulado)
- Proteção de rotas
- Context API para gerenciamento de estado

### ✅ Dashboard Principal
- Cards com indicadores principais
- Atividades recentes
- Layout responsivo com sidebar
- Design moderno com Tailwind CSS

### ✅ API Backend
- Endpoints de autenticação
- Dados mockados para demonstração
- CORS configurado
- Documentação automática (FastAPI)

## Usuários de Teste

- **Admin**: `admin` / `admin123`
- **Usuário**: `user` / `user123`

## Próximos Passos

1. Implementar módulo de Orçamentos
2. Adicionar banco de dados MySQL
3. Implementar módulo de Pedidos
4. Adicionar sistema de relatórios
5. Implementar integrações externas

## Tecnologias Utilizadas

- **Backend**: Python 3.8+, FastAPI, Uvicorn
- **Frontend**: React 18, Tailwind CSS, Axios
- **Autenticação**: JWT (simulado)
- **Roteamento**: React Router DOM
- **Estado**: Context API
