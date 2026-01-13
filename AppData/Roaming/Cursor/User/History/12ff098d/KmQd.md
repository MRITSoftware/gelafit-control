# Frontend - Sistema de Assinatura Eletrônica

## ⚠️ Erro Comum: CORS Policy

### O que está acontecendo?

Se você está vendo este erro:
```
Access to script at 'file:///D:/src/main.tsx' from origin 'null' has been blocked by CORS policy
```

**Isso significa que você está tentando abrir o `index.html` diretamente no navegador**, usando o protocolo `file://`.

### Por que isso acontece?

O projeto usa **Vite** como bundler, que precisa processar os arquivos TypeScript/React antes de enviá-los ao navegador. Quando você abre o HTML diretamente, o navegador tenta carregar os módulos TypeScript como se fossem arquivos locais, o que não funciona devido às políticas de segurança do navegador.

### ✅ Solução Correta

**NUNCA abra o `index.html` diretamente!** Sempre use o servidor de desenvolvimento:

```bash
# 1. Certifique-se de estar na pasta frontend
cd frontend

# 2. Instale as dependências (se ainda não fez)
npm install

# 3. Execute o servidor de desenvolvimento
npm run dev
```

Isso vai:
- Iniciar um servidor HTTP na porta 5173
- Processar os arquivos TypeScript/React
- Habilitar Hot Module Replacement (HMR) para desenvolvimento
- Proxificar requisições para a API do backend

### Acessar a aplicação

Após executar `npm run dev`, você verá algo como:

```
  VITE v4.x.x  ready in xxx ms

  ➜  Local:   http://localhost:5173/
  ➜  Network: use --host to expose
```

Abra `http://localhost:5173` no seu navegador.

## Scripts Disponíveis

- `npm run dev` - Inicia servidor de desenvolvimento
- `npm run build` - Cria build de produção
- `npm run preview` - Visualiza o build de produção

## Estrutura

```
frontend/
├── src/
│   ├── components/     # Componentes reutilizáveis
│   ├── contexts/       # Context API (Autenticação, etc)
│   ├── pages/          # Páginas da aplicação
│   ├── services/       # Serviços (API calls)
│   ├── App.tsx         # Componente principal
│   └── main.tsx        # Ponto de entrada
├── index.html          # HTML base (NÃO abrir diretamente!)
└── vite.config.js      # Configuração do Vite
```

