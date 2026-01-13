# ⚡ Guia Rápido - Hospedagem VPS Hostinger + Domínio HostGator

## 🎯 Resumo da Solução

**Hospedar o Next.js na VPS Hostinger** e **apontar o DNS do domínio HostGator** para o IP da VPS.

---

## 📝 Passos Resumidos

### 1️⃣ Obter IP da VPS Hostinger
```bash
ssh usuario@ip_vps
curl ifconfig.me  # Anote este IP!
```

### 2️⃣ Configurar DNS na HostGator
No painel da HostGator → DNS Zone Editor:

```
@     A     [IP_DA_VPS]
www   A     [IP_DA_VPS]
```

### 3️⃣ Enviar Arquivos para VPS

**Opção A: Usando script PowerShell** (recomendado)
```powershell
.\enviar-para-vps.ps1 -UsuarioVPS "seu_usuario" -IPVPS "ip_da_vps"
```

**Opção B: Manual via SCP**
```powershell
scp -r app components lib utils types *.json *.js *.ts middleware.ts usuario@ip_vps:~/apps/assinatura-digital/
```

### 4️⃣ Configurar na VPS

```bash
# Conectar
ssh usuario@ip_vps

# Navegar e instalar
cd ~/apps/assinatura-digital
npm install --production

# Build
npm run build

# Criar .env (copie suas variáveis de ambiente)
nano .env

# Instalar PM2
npm install -g pm2

# Iniciar aplicação
pm2 start npm --name "assinatura-digital" -- run production
pm2 save
pm2 startup
```

### 5️⃣ Configurar Nginx

```bash
# Criar configuração
sudo nano /etc/nginx/sites-available/assinatura-digital
```

Cole (substitua `seu_dominio.com.br`):

```nginx
server {
    listen 80;
    server_name seu_dominio.com.br www.seu_dominio.com.br;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name seu_dominio.com.br www.seu_dominio.com.br;

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

```bash
# Ativar
sudo ln -s /etc/nginx/sites-available/assinatura-digital /etc/nginx/sites-enabled/
sudo nginx -t
sudo systemctl reload nginx
```

### 6️⃣ Configurar SSL

```bash
sudo apt install certbot python3-certbot-nginx -y
sudo certbot --nginx -d seu_dominio.com.br -d www.seu_dominio.com.br
```

---

## ✅ Checklist

- [ ] IP da VPS anotado
- [ ] DNS configurado na HostGator (@ e www → IP da VPS)
- [ ] Arquivos enviados para VPS
- [ ] `npm install --production` executado
- [ ] `npm run build` executado
- [ ] `.env` criado no servidor
- [ ] PM2 instalado e aplicação rodando
- [ ] Nginx configurado como proxy reverso
- [ ] SSL/HTTPS configurado
- [ ] Site acessível via https://seu_dominio.com.br

---

## 🐛 Problemas Comuns

### Site não carrega
```bash
pm2 logs assinatura-digital  # Ver logs
pm2 restart assinatura-digital  # Reiniciar
```

### Erro 502
- Verificar se Next.js está rodando: `pm2 list`
- Verificar porta 3002: `netstat -tulpn | grep 3002`

### DNS não resolve
- Aguardar propagação (1-48h)
- Verificar: `nslookup seu_dominio.com.br`

---

## 📚 Documentação Completa

Veja o guia detalhado: `HOSPEDAGEM_VPS_HOSTINGER_DOMINIO_HOSTGATOR.md`

---

**⚡ Dica:** Use o script `enviar-para-vps.ps1` para facilitar o envio de arquivos!

