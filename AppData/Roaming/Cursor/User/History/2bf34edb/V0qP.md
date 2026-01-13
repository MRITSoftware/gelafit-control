# Fluxo do Sistema de Assinatura Digital

## 📋 Estrutura do Sistema

### 1. **Autenticação**
- **Cadastro** (`/cadastro`): Primeira vez - nome, CPF e email
- **Login** (`/login`): Acesso com email e senha
- **Banco de dados**: SQLite local (desenvolvimento) / MySQL (produção Hostinger)

### 2. **Dashboard do Cliente** (`/dashboard`)
- Tela inicial após login
- Mostra informações do cliente
- Botão para "Assinar Documento"

### 3. **Seleção de Documento** (`/documentos`)
- Upload de PDF
- Pré-visualização do documento
- Continuar para configuração de assinatura

### 4. **Configuração de Assinatura** (`/assinatura`)
- Configurar local da assinatura (opcional)
- Escolher data da assinatura
- Opção de incluir/não incluir data
- Desenhar assinatura no canvas
- Visualizar e assinar o PDF
- Download do PDF assinado

## 🔧 Como Funciona

1. Cliente se cadastra com nome, CPF e email
2. Faz login no sistema
3. Vê o dashboard com opções
4. Clica em "Assinar Documento"
5. Faz upload do PDF
6. Configura as opções de assinatura (local, data, etc.)
7. Desenha a assinatura
8. Assina o PDF
9. Faz download do PDF assinado

## 📁 Estrutura de Arquivos

```
app/
├── page.tsx           # Redireciona para /login
├── login/page.tsx     # Página de login
├── cadastro/page.tsx  # Página de cadastro
├── dashboard/page.tsx # Dashboard do cliente
├── documentos/page.tsx # Seleção de documento
└── assinatura/page.tsx # Configuração e assinatura

lib/
├── db.ts             # Configuração do banco de dados
└── auth.ts           # Autenticação e JWT

utils/
└── PDFSigner.ts      # Lógica de assinatura do PDF

components/
├── PDFUploader.tsx
├── PDFViewer.tsx
└── SignaturePad.tsx
```

## 🗄️ Banco de Dados

**Tabelas:**
- `clientes`: Nome, CPF, email, senha (hash)
- `documentos`: PDFs enviados pelos clientes
- `assinaturas`: Registro das assinaturas realizadas

## 🚀 Produção (Hostinger)

Para usar MySQL na Hostinger:

1. Instalar `mysql2` (já instalado)
2. Configurar variáveis de ambiente
3. Criar adapter de banco de dados MySQL
4. Configurar conexão no `lib/db.ts`
