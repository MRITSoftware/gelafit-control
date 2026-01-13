# Troubleshooting - MR Food

## Erros Comuns e Soluções

### Erro: Module not found 'bcryptjs'

**Solução:**
```bash
npm install
```

Se ainda não funcionar, limpe o cache do Next.js:
```bash
# Windows PowerShell
Remove-Item -Recurse -Force .next

# Linux/Mac
rm -rf .next
```

Depois reinicie o servidor:
```bash
npm run dev
```

### Erro: Cannot find module 'better-sqlite3'

**Solução:**
```bash
npm install
```

Se estiver no Windows e tiver problemas, pode ser necessário instalar ferramentas de build:
```bash
npm install --global windows-build-tools
```

### Erro ao inicializar banco de dados

**Solução:**
1. Verifique se a pasta `data/` tem permissões de escrita
2. Execute manualmente:
```bash
npm run db:init
```

Ou acesse: `http://localhost:3000/api/init-db`

### Erro de autenticação

**Solução:**
1. Limpe os cookies do navegador
2. Verifique se o banco foi inicializado
3. Tente criar uma nova conta

### Banco de dados não encontrado

**Solução:**
O banco é criado automaticamente na primeira execução. Se não aparecer:
1. Execute `npm run db:init`
2. Verifique se a pasta `data/` foi criada
3. Verifique permissões de escrita

### Problemas com TypeScript

**Solução:**
```bash
npm run type-check
```

Isso mostrará todos os erros de tipo.

### Porta 3000 já em uso

**Solução:**
Use outra porta:
```bash
PORT=3001 npm run dev
```

Ou no Windows PowerShell:
```powershell
$env:PORT=3001; npm run dev
```

## Dicas

- Sempre execute `npm install` após clonar o repositório
- Limpe o cache do Next.js se houver erros estranhos: `rm -rf .next` ou `Remove-Item -Recurse -Force .next`
- Verifique se todas as dependências estão instaladas: `npm list`
- Para problemas com better-sqlite3 no Windows, pode ser necessário instalar Python e ferramentas de build

