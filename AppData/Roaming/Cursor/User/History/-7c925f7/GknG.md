# 🚀 Guia Rápido de Build para Hostinger

## Compilação Rápida

### No Windows (PowerShell)

```powershell
# 1. Preparar arquivos para deploy
.\preparar-deploy.ps1

# 2. Compilar projeto
.\build-producao.ps1
```

Ou manualmente:

```powershell
# Instalar dependências
npm install

# Criar build de produção
npm run build
```

## ✅ Checklist de Build

- [x] Dependências instaladas (`npm install`)
- [x] Build criado (`npm run build`)
- [x] Pasta `.next` foi criada
- [x] Arquivo `.env.example` criado (usar como base no servidor)

## 📦 Arquivos para Enviar ao Servidor

Após o build, envie via FTP/SSH:

**Pastas:**
- `.next/` (build do Next.js)
- `app/`
- `components/`
- `lib/`
- `utils/`

**Arquivos:**
- `middleware.ts`
- `next.config.js`
- `package.json`
- `package-lock.json`
- `tsconfig.json`
- `tailwind.config.js`
- `postcss.config.js`
- `next-env.d.ts`

**NÃO enviar:**
- `node_modules/` (instale no servidor)
- `.env` ou `.env.local` (crie no servidor baseado em `.env.example`)
- `database.sqlite` (será criado automaticamente no servidor)
- `.git/` (se houver)

## 🔧 No Servidor Hostinger

Após enviar os arquivos:

```bash
# 1. Instalar dependências de produção
npm install --production

# 2. Criar arquivo .env
cp .env.example .env
nano .env  # Editar com suas credenciais

# 3. Iniciar servidor
npm run production

# OU usar PM2 (recomendado)
npm install -g pm2
pm2 start npm --name "esign" -- run production
pm2 save
pm2 startup
```

## 📊 Verificar Build

Após `npm run build`, verifique:

1. ✅ Pasta `.next` existe
2. ✅ Pasta `.next/standalone` existe (modo standalone)
3. ✅ Não há erros no console
4. ✅ Tamanho do build é razoável (geralmente 5-50 MB)

## 🐛 Troubleshooting

### Build falha
```bash
# Limpar cache e node_modules
Remove-Item -Recurse -Force node_modules
Remove-Item -Recurse -Force .next
npm install
npm run build
```

### Erros de tipo TypeScript
```bash
# Verificar erros
npm run lint
```

### Build muito grande
- Verifique se não está incluindo `node_modules/` no build
- Use `npm install --production` no servidor
- Verifique `.gitignore` está correto

## 📚 Mais Informações

- Guia completo: `DEPLOY_HOSTINGER.md`
- Guia rápido: `DEPLOY_RAPIDO.md`

