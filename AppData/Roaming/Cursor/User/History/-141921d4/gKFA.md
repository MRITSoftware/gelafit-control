# 🚀 Guia Rápido de Deploy na Hostinger

## Passo 1: Preparar o Build Localmente

```bash
# No seu computador, execute:
npm run build
```

Isso criará a pasta `.next` otimizada para produção.

## Passo 2: Arquivos para Enviar

**✅ ENVIE estas pastas/arquivos:**
- `.next/` (build gerado)
- `app/`
- `components/`
- `lib/`
- `utils/`
- `middleware.ts`
- `next.config.js`
- `package.json`
- `package-lock.json`
- `tsconfig.json`
- `tailwind.config.js`
- `postcss.config.js`
- `next-env.d.ts`

**❌ NÃO envie:**
- `node_modules/` (instale no servidor)
- `.env.local` (crie no servidor)
- `.git/`

## Passo 3: Conectar na Hostinger

### Via SSH (Recomendado):

```bash
ssh usuario@seudominio.com.br
```

### Via FTP (FileZilla):
- Use as credenciais FTP do painel da Hostinger
- Navegue até `public_html` ou `domains/esign.mrit.com.br/public_html`

## Passo 4: Configurar no Servidor

### 4.1. Enviar arquivos
Envie todos os arquivos listados acima para o servidor.

### 4.2. Criar arquivo `.env` no servidor:

```env
NODE_ENV=production
NEXT_PUBLIC_BASE_URL=https://www.esign.mrit.com.br
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=disparador@mrit.com.br
SMTP_PASS=sua_senha_aqui
MERCADOPAGO_ACCESS_TOKEN=seu_token
MERCADOPAGO_PUBLIC_KEY=sua_public_key
```

### 4.3. Instalar dependências:

```bash
cd public_html  # ou caminho do seu projeto
npm install --production
```

### 4.4. Criar build (se não enviou a pasta .next):

```bash
npm run build
```

### 4.5. Instalar PM2 (manter servidor rodando):

```bash
npm install -g pm2
pm2 start npm --name "esign" -- run production
pm2 save
pm2 startup
```

## Passo 5: Configurar Nginx

A Hostinger geralmente já tem Nginx. Você precisa configurar via painel ou criar arquivo:

**No painel da Hostinger:**
- Vá em "Gerenciador de Sites"
- Adicione configuração para `www.esign.mrit.com.br`
- Configure proxy reverso para `localhost:3002`

**Ou via arquivo** (se tiver acesso SSH root):
Criar `/etc/nginx/sites-available/esign.mrit.com.br`:

```nginx
server {
    listen 80;
    server_name www.esign.mrit.com.br;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name www.esign.mrit.com.br;

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

    client_max_body_size 50M;
}
```

## Passo 6: Configurar SSL/HTTPS

No painel da Hostinger:
1. Vá em "SSL"
2. Ative SSL para `www.esign.mrit.com.br`
3. Ou use Let's Encrypt (gratuito)

## Passo 7: Verificar

1. Acesse: `https://www.esign.mrit.com.br`
2. Verifique logs: `pm2 logs esign`
3. Teste login, assinatura, etc.

## Comandos Úteis

```bash
# Ver servidor rodando
pm2 list

# Ver logs
pm2 logs esign

# Reiniciar
pm2 restart esign

# Parar
pm2 stop esign

# Status
pm2 status
```

## Troubleshooting

**Servidor não inicia:**
- Verifique porta: `netstat -tulpn | grep 3002`
- Verifique logs: `pm2 logs esign`
- Verifique `.env`: `cat .env`

**Erro 502:**
- Servidor não está rodando na porta 3002
- Reinicie: `pm2 restart esign`

**Banco de dados:**
- Verifique permissões: `chmod 664 database.sqlite`
- O banco será criado automaticamente se não existir

---

📖 **Guia completo:** Veja `DEPLOY_HOSTINGER.md` para mais detalhes

