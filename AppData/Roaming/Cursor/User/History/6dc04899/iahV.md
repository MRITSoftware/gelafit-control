# 🚀 Deploy Rápido na Hostinger - esign.mrit.com.br

## Resumo: O que fazer?

**NÃO é só "jogar as pastas"!** Next.js precisa ser compilado e executado corretamente.

## Passo 1: Gerar o Build Localmente

No seu computador, execute:

```bash
npm run build
```

Isso cria a pasta `.next` com os arquivos compilados para produção.

## Passo 2: Enviar Arquivos para Hostinger

Envie estas pastas/arquivos via FTP ou SSH:

✅ **ENVIAR:**
- `.next/` (pasta gerada pelo build)
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

❌ **NÃO ENVIAR:**
- `node_modules/` (será instalado no servidor)
- `.env.local` (crie no servidor)
- `.git/`

## Passo 3: No Servidor da Hostinger (via SSH)

### 3.1. Conectar via SSH

```bash
ssh usuario@mrit.com.br
```

Navegue até a pasta do site:
```bash
cd public_html  # ou domains/esign.mrit.com.br/public_html
```

### 3.2. Instalar Dependências

```bash
npm install --production
```

### 3.3. Criar Arquivo .env

Crie um arquivo `.env` na raiz do projeto com:

```env
NODE_ENV=production
NEXT_PUBLIC_BASE_URL=https://esign.mrit.com.br
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=disparador@mrit.com.br
SMTP_PASS=sua_senha_aqui
MERCADOPAGO_ACCESS_TOKEN=seu_token_aqui
MERCADOPAGO_PUBLIC_KEY=sua_public_key_aqui
```

### 3.4. Instalar e Configurar PM2

PM2 mantém o servidor rodando mesmo após você desconectar:

```bash
# Instalar PM2 globalmente
npm install -g pm2

# Iniciar aplicação
pm2 start npm --name "esign" -- run production

# Salvar para iniciar automaticamente
pm2 save
pm2 startup
```

O servidor estará rodando na porta **3002**.

## Passo 4: Configurar Nginx (Proxy Reverso)

No painel da Hostinger, configure o domínio `esign.mrit.com.br` para apontar para `http://localhost:3002`.

Se tiver acesso SSH com sudo, crie o arquivo de configuração do Nginx:

```bash
sudo nano /etc/nginx/sites-available/esign.mrit.com.br
```

Conteúdo:
```nginx
server {
    listen 80;
    server_name esign.mrit.com.br;
    return 301 https://esign.mrit.com.br$request_uri;
}

server {
    listen 443 ssl http2;
    server_name esign.mrit.com.br;

    location / {
        proxy_pass http://localhost:3002;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    client_max_body_size 50M;
}
```

Ativar:
```bash
sudo ln -s /etc/nginx/sites-available/esign.mrit.com.br /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

**Ou configure pelo painel da Hostinger** na opção "Gerenciador de Sites".

## Passo 5: Configurar SSL/HTTPS

No painel da Hostinger, ative o SSL gratuito para o domínio `esign.mrit.com.br`.

## ✅ Pronto!

Acesse: `https://esign.mrit.com.br`

## 📋 Comandos Úteis PM2

```bash
pm2 list              # Ver processos rodando
pm2 logs esign        # Ver logs
pm2 restart esign     # Reiniciar
pm2 stop esign        # Parar
```

## 🔧 Se algo der errado

1. Verifique logs: `pm2 logs esign`
2. Verifique se porta 3002 está livre: `netstat -tulpn | grep 3002`
3. Verifique variáveis de ambiente: `cat .env`
4. Verifique se o PM2 está rodando: `pm2 list`

---

📖 **Para mais detalhes, veja o guia completo: `DEPLOY_HOSTINGER.md`**

