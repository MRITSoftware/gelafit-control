# 📝 eSign - Sistema de Assinatura de PDF

Sistema completo para assinatura de documentos PDF com interface responsiva para desktop e mobile.

## ✨ Funcionalidades

- ✅ Upload de PDF (arrastar e soltar ou clicar para selecionar)
- ✅ Visualização de PDF com navegação entre páginas
- ✅ Assinatura desenhando no canvas (suporte a mouse e touch)
- ✅ Download do PDF assinado
- ✅ Interface responsiva para celular e desktop
- ✅ Processamento 100% no cliente (não envia dados ao servidor)

## 🚀 Tecnologias

- **Frontend**: React + Vite
- **Visualização PDF**: react-pdf
- **Processamento PDF**: pdf-lib
- **Backend**: Node.js + Express (opcional, para deploy)

## 📦 Instalação

### Pré-requisitos

- Node.js (versão 16 ou superior)
- npm ou yarn

### Passo a passo

1. **Instalar dependências do frontend:**
```bash
cd frontend
npm install
```

2. **Instalar dependências do backend (opcional):**
```bash
cd backend
npm install
```

## 🛠️ Desenvolvimento

### Frontend

```bash
cd frontend
npm run dev
```

O frontend estará rodando em `http://localhost:5173`

### Backend (opcional)

```bash
cd backend
npm run dev
```

O backend estará rodando em `http://localhost:3000`

## 📱 Como Usar

1. **Abrir o sistema** no navegador
2. **Carregar PDF**: Arraste o arquivo PDF ou clique para selecionar
3. **Navegar**: Use os botões "Anterior" e "Próxima" para navegar entre páginas
4. **Assinar**: Clique em "Assinar Documento" e desenhe sua assinatura
5. **Salvar**: Clique em "Salvar Assinatura" após desenhar
6. **Baixar**: Clique em "Baixar PDF Assinado" para fazer o download

## 🌐 Deploy na HostGator

Consulte o arquivo `DEPLOY.md` para instruções detalhadas de deploy na HostGator.

### Resumo rápido:

1. Build do frontend: `cd frontend && npm run build`
2. Upload dos arquivos via FTP/SFTP
3. Configurar `.htaccess` conforme instruções no `DEPLOY.md`

## 📂 Estrutura do Projeto

```
esign/
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── UploadPDF.jsx      # Tela de upload
│   │   │   ├── SignPDF.jsx        # Visualizador e assinatura
│   │   │   └── SignatureCanvas.jsx # Canvas para desenhar
│   │   ├── App.jsx
│   │   └── main.jsx
│   ├── index.html
│   └── package.json
├── backend/                        # Opcional para API
├── .htaccess                       # Configuração HostGator
└── DEPLOY.md                       # Guia de deploy
```

## 🎨 Características

- **Responsivo**: Funciona perfeitamente em celulares, tablets e desktops
- **Touch-friendly**: Suporta assinatura com o dedo em dispositivos móveis
- **Privacidade**: Todo processamento é feito no navegador, sem enviar dados ao servidor
- **Rápido**: Interface otimizada e carregamento rápido

## 🐛 Solução de Problemas

### PDF não carrega
- Verifique se o arquivo é um PDF válido
- Tente com um PDF menor (máximo 10MB recomendado)

### Assinatura não aparece
- Certifique-se de desenhar algo no canvas
- Clique em "Salvar Assinatura" após desenhar

### Erro ao baixar
- Verifique se salvou a assinatura antes de tentar baixar
- Tente com um navegador diferente

## 📄 Licença

ISC

## 👨‍💻 Desenvolvido com

- React
- Vite
- pdf-lib
- react-pdf
