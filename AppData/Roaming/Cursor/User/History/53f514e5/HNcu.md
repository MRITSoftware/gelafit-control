# 🚀 Guia de Deploy - SignaMRIT na Hostinger

## Pré-requisitos

- ✅ Node.js 18+ instalado na Hostinger
- ✅ Acesso SSH ao servidor
- ✅ Domínio configurado (ex: signamrit.com.br)
- ✅ Credenciais do Mercado Pago
- ✅ Credenciais de email SMTP

---

## 📋 Passo a Passo

### 1. Preparar o Projeto Localmente

```powershell
# No Windows, execute:
npm run build
```

Isso criará a pasta `.next` com o build de produção.

### 2. Conectar via SSH

```bash
ssh seu_usuario@seu_ip_hostinger
# ou
ssh root@seu_ip_hostinger
```

### 3. Criar Diretório no Servidor

```bash
mkdir -p /home/usuario/apps/signamrit
cd /home/usuario/apps/signamrit
```

**Nota:** Substitua `/home/usuario` pelo seu diretório home na Hostinger.

### 4. Enviar Arquivos para o Servidor

#### Opção A: Via SCP (Windows PowerShell)

```powershell
# Enviar pasta app
scp -r app usuario@hostinger:/home/usuario/apps/signamrit/

# Enviar outras pastas
scp -r components usuario@hostinger:/home/usuario/apps/signamrit/
scp -r lib usuario@hostinger:/home/usuario/apps/signamrit/
scp -r utils usuario@hostinger:/home/usuario/apps/signamrit/
scp -r types usuario@hostinger:/home/usuario/apps/signamrit/
scp -r public usuario@hostinger:/home/usuario/apps/signamrit/
scp -r .next usuario@hostinger:/home/usuario/apps/signamrit/

# Enviar arquivos de configuração
scp package.json usuario@hostinger:/home/usuario/apps/signamrit/
scp package-lock.json usuario@hostinger:/home/usuario/apps/signamrit/
scp next.config.js usuario@hostinger:/home/usuario/apps/signamrit/
scp tsconfig.json usuario@hostinger:/home/usuario/apps/signamrit/
scp tailwind.config.js usuario@hostinger:/home/usuario/apps/signamrit/
scp postcss.config.js usuario@hostinger:/home/usuario/apps/signamrit/
scp middleware.ts usuario@hostinger:/home/usuario/apps/signamrit/
```

#### Opção B: Via FTP (FileZilla/WinSCP)

Conecte-se via FTP e envie as seguintes pastas/arquivos:
- `app/`
- `components/`
- `lib/`
- `utils/`
- `types/`
- `public/`
- `.next/` (pasta do build)
- `package.json`
- `package-lock.json`
- `next.config.js`
- `tsconfig.json`
- `tailwind.config.js`
- `postcss.config.js`
- `middleware.ts`
- `next-env.d.ts`

**⚠️ NÃO ENVIE:**
- `node_modules/` (instale no servidor)
- `.env` ou `.env.local` (crie no servidor)
- `database.sqlite` (será criado automaticamente)
- `.git/`

### 5. No Servidor - Instalar Dependências

```bash
cd /home/usuario/apps/signamrit
npm install --production
```

### 6. Criar Arquivo .env

```bash
nano .env
```

Cole o seguinte conteúdo (ajuste os valores):

```env
NODE_ENV=production
NEXT_PUBLIC_BASE_URL=https://signamrit.com.br

# Mercado Pago
MERCADOPAGO_ACCESS_TOKEN=seu_token_mercadopago_aqui

# Email SMTP (Hostinger usa Titan Email)
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=seu_email@mrit.com.br
SMTP_PASS=sua_senha_email

# JWT Secret (gere uma string aleatória forte)
JWT_SECRET=SUA_CHAVE_SECRETA_MUITO_FORTE_AQUI_GERE_UMA_STRING_ALEATORIA
```

**Gerar JWT_SECRET:**
```bash
openssl rand -hex 32
```

Salve o arquivo: `Ctrl+X`, depois `Y`, depois `Enter`

### 7. Instalar PM2 (Gerenciador de Processos)

```bash
npm install -g pm2
```

### 8. Iniciar a Aplicação com PM2

```bash
cd /home/usuario/apps/signamrit
pm2 start npm --name "signamrit" -- start
```

Ou se preferir usar a porta padrão 3002:
```bash
pm2 start npm --name "signamrit" -- run start
```

### 9. Salvar Configuração PM2

```bash
pm2 save
pm2 startup
```

Siga as instruções que aparecerem para configurar o PM2 para iniciar automaticamente.

### 10. Verificar Status

```bash
pm2 status
pm2 logs signamrit
```

### 11. Configurar Nginx (Proxy Reverso)

Se você tem acesso root e quer usar Nginx:

```bash
sudo nano /etc/nginx/sites-available/signamrit
```

Cole:

```nginx
server {
    listen 80;
    server_name signamrit.com.br www.signamrit.com.br;

    location / {
        proxy_pass http://localhost:3002;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_cache_bypass $http_upgrade;
    }
}
```

Ativar site:
```bash
sudo ln -s /etc/nginx/sites-available/signamrit /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### 12. Configurar SSL (Let's Encrypt)

```bash
sudo apt install certbot python3-certbot-nginx
sudo certbot --nginx -d signamrit.com.br -d www.signamrit.com.br
```

---

## 🔄 Atualizar Aplicação (Deploy)

Quando fizer alterações:

1. **Localmente:**
```powershell
npm run build
```

2. **Enviar apenas as pastas alteradas:**
```powershell
scp -r .next usuario@hostinger:/home/usuario/apps/signamrit/
scp -r app usuario@hostinger:/home/usuario/apps/signamrit/
# ... outras pastas alteradas
```

3. **No servidor:**
```bash
cd /home/usuario/apps/signamrit
pm2 restart signamrit
```

---

## 📊 Comandos PM2 Úteis

```bash
pm2 status              # Ver status
pm2 logs signamrit      # Ver logs
pm2 restart signamrit   # Reiniciar
pm2 stop signamrit      # Parar
pm2 delete signamrit    # Remover
pm2 monit               # Monitor em tempo real
```

---

## 🐛 Troubleshooting

### Erro: "Cannot find module"
```bash
cd /home/usuario/apps/signamrit
rm -rf node_modules
npm install --production
pm2 restart signamrit
```

### Erro: "Port already in use"
```bash
# Verificar qual processo está usando a porta
lsof -i :3002
# Matar o processo ou mudar a porta no package.json
```

### Erro: "Database locked"
```bash
# Verificar permissões do banco
chmod 644 database.sqlite
chmod 755 $(dirname database.sqlite)
```

### Ver logs detalhados
```bash
pm2 logs signamrit --lines 100
```

---

## ✅ Checklist Final

- [ ] Build criado localmente (`npm run build`)
- [ ] Arquivos enviados para o servidor
- [ ] Dependências instaladas (`npm install --production`)
- [ ] Arquivo `.env` criado com todas as variáveis
- [ ] PM2 instalado e configurado
- [ ] Aplicação rodando (`pm2 status`)
- [ ] Nginx configurado (se aplicável)
- [ ] SSL configurado (se aplicável)
- [ ] Testar acesso ao site
- [ ] Testar login
- [ ] Testar upload de PDF
- [ ] Testar assinatura
- [ ] Testar pagamento (sandbox)

---

## 📞 Suporte

Se encontrar problemas:
1. Verifique os logs: `pm2 logs signamrit`
2. Verifique o arquivo `.env`
3. Verifique as permissões dos arquivos
4. Verifique se a porta está acessível

