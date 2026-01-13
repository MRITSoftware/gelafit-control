# 🚀 Guia Completo: Hospedagem na VPS Hostinger + Domínio HostGator

## 📋 Situação Atual

- ✅ **Domínio**: Registrado na HostGator (não é VPS, é hospedagem compartilhada)
- ✅ **VPS**: Hostinger com acesso SSH
- ✅ **Projeto**: Next.js (Sistema de Assinatura Digital)

## 🎯 Solução Recomendada

**Usar a VPS da Hostinger para hospedar o Next.js** e **apontar o DNS do domínio na HostGator** para o IP da VPS.

---

## 📍 PARTE 1: Obter o IP da VPS Hostinger

### 1.1. Conectar via SSH na VPS Hostinger

```bash
ssh seu_usuario@ip_da_vps_hostinger
# ou
ssh seu_usuario@seudominio.com.br
```

### 1.2. Descobrir o IP Público da VPS

No servidor, execute:

```bash
# Opção 1: Verificar IP público
curl ifconfig.me

# Opção 2: Verificar via hostname
hostname -I

# Opção 3: Verificar no painel da Hostinger
# Acesse o painel > VPS > Ver detalhes
```

**Anote este IP!** Você precisará dele para configurar o DNS.

---

## 🌐 PARTE 2: Configurar DNS no Domínio HostGator

### 2.1. Acessar o Painel da HostGator

1. Entre no painel de controle da HostGator
2. Procure por **"DNS Zone Editor"** ou **"Gerenciador de DNS"**
3. Selecione seu domínio

### 2.2. Configurar Registros DNS

Você precisa criar/apontar os seguintes registros:

#### **Registro A (Principal)**
```
Tipo: A
Nome: @ (ou deixe em branco para o domínio raiz)
Valor: [IP_DA_VPS_HOSTINGER]
TTL: 3600 (ou padrão)
```

#### **Registro A para WWW**
```
Tipo: A
Nome: www
Valor: [IP_DA_VPS_HOSTINGER]
TTL: 3600
```

#### **Exemplo Prático:**
Se seu domínio é `mrit.com.br` e o IP da VPS é `123.456.789.10`:

```
@     A     123.456.789.10
www   A     123.456.789.10
```

### 2.3. Aguardar Propagação DNS

- Pode levar de **15 minutos a 48 horas**
- Geralmente funciona em **1-2 horas**
- Verifique com: `nslookup seu_dominio.com.br`

---

## 🖥️ PARTE 3: Configurar a VPS Hostinger

### 3.1. Conectar via SSH

```bash
ssh seu_usuario@ip_da_vps_hostinger
```

### 3.2. Verificar Node.js

```bash
node --version  # Deve ser 18 ou superior
npm --version

# Se não tiver Node.js instalado:
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

### 3.3. Instalar PM2 (Gerenciador de Processos)

```bash
npm install -g pm2
```

### 3.4. Criar Diretório do Projeto

```bash
# Criar pasta para o projeto
mkdir -p ~/apps/assinatura-digital
cd ~/apps/assinatura-digital
```

---

## 📦 PARTE 4: Enviar Arquivos para a VPS

### Opção A: Via SCP (Terminal)

No seu computador Windows (PowerShell):

```powershell
# Navegar até a pasta do projeto
cd "D:\ass digital"

# Enviar arquivos (exceto node_modules)
scp -r app components lib utils types *.json *.js *.ts middleware.ts seu_usuario@ip_vps:~/apps/assinatura-digital/
```

### Opção B: Via Git (Recomendado)

Se o projeto está no Git:

```bash
# Na VPS
cd ~/apps/assinatura-digital
git clone https://seu_repositorio.git .
# ou se já tem arquivos:
git pull
```

### Opção C: Via FTP/SFTP (FileZilla)

1. Configure conexão SFTP no FileZilla:
   - Host: `sftp://ip_da_vps` ou `sftp://seudominio.com.br`
   - Usuário: seu usuário SSH
   - Porta: 22
   - Protocolo: SFTP

2. Envie as pastas e arquivos necessários:
   - ✅ `app/`
   - ✅ `components/`
   - ✅ `lib/`
   - ✅ `utils/`
   - ✅ `types/` (se existir)
   - ✅ `*.json`
   - ✅ `*.js`
   - ✅ `*.ts`
   - ✅ `middleware.ts`
   - ❌ **NÃO envie** `node_modules/` (será instalado no servidor)

### Arquivos a Enviar (Checklist):

```
✅ app/                    ← Páginas e rotas
✅ components/             ← Componentes React
✅ lib/                    ← Bibliotecas
✅ utils/                  ← Utilitários
✅ types/                  ← Tipos TypeScript
✅ package.json
✅ package-lock.json
✅ next.config.js
✅ tsconfig.json
✅ tailwind.config.js
✅ postcss.config.js
✅ middleware.ts
✅ next-env.d.ts
❌ node_modules/           ← NÃO enviar
❌ .next/                  ← Será criado no servidor
❌ .env                    ← Criar no servidor
❌ database.sqlite         ← Será criado automaticamente
```

---

## 🔧 PARTE 5: Configurar o Projeto na VPS

### 5.1. Instalar Dependências

```bash
cd ~/apps/assinatura-digital
npm install --production
```

### 5.2. Criar Build de Produção

```bash
npm run build
```

Isso criará a pasta `.next/` com o build otimizado.

### 5.3. Criar Arquivo .env

```bash
nano .env
```

Adicione as variáveis de ambiente:

```env
# Ambiente
NODE_ENV=production

# URL Base
NEXT_PUBLIC_BASE_URL=https://seu_dominio.com.br

# Email (SMTP)
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=seu_email@dominio.com.br
SMTP_PASS=sua_senha_email

# Mercado Pago (se usar)
MERCADOPAGO_ACCESS_TOKEN=seu_token
MERCADOPAGO_PUBLIC_KEY=sua_public_key

# JWT Secret (gere uma string aleatória forte)
JWT_SECRET=uma_string_muito_segura_e_aleatoria_aqui_123456789

# Banco de Dados (SQLite será criado automaticamente)
# Não precisa configurar nada, o SQLite será criado em database.sqlite
```

Salve: `Ctrl+O`, `Enter`, `Ctrl+X`

### 5.4. Criar Permissões Corretas

```bash
# Dar permissões para o diretório
chmod 755 ~/apps/assinatura-digital

# Quando o banco de dados for criado, ajustar permissões:
# (será criado automaticamente na primeira execução)
chmod 664 database.sqlite
```

---

## 🚀 PARTE 6: Iniciar a Aplicação

### 6.1. Iniciar com PM2

```bash
cd ~/apps/assinatura-digital

# Iniciar aplicação
pm2 start npm --name "assinatura-digital" -- run production

# Ou usando o script direto:
pm2 start npm --name "assinatura-digital" -- start

# Salvar configuração (reinicia automaticamente após reboot)
pm2 save
pm2 startup  # Seguir instruções que aparecerem
```

### 6.2. Verificar se Está Rodando

```bash
# Ver processos
pm2 list

# Ver logs
pm2 logs assinatura-digital

# Ver status
pm2 status
```

A aplicação deve estar rodando na porta **3002** (conforme `package.json`).

---

## 🌐 PARTE 7: Configurar Nginx (Proxy Reverso)

A VPS da Hostinger geralmente vem com Nginx instalado. Precisamos configurar um proxy reverso.

### 7.1. Verificar se Nginx Está Instalado

```bash
nginx -v

# Se não estiver instalado:
sudo apt update
sudo apt install nginx -y
```

### 7.2. Criar Configuração do Site

```bash
sudo nano /etc/nginx/sites-available/assinatura-digital
```

Cole esta configuração:

```nginx
server {
    listen 80;
    server_name seu_dominio.com.br www.seu_dominio.com.br;

    # Redirecionar HTTP para HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name seu_dominio.com.br www.seu_dominio.com.br;

    # Certificados SSL (vamos configurar com Let's Encrypt)
    ssl_certificate /etc/letsencrypt/live/seu_dominio.com.br/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/seu_dominio.com.br/privkey.pem;

    # Configurações SSL modernas
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Proxy para aplicação Next.js
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
        
        # Timeouts
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Tamanho máximo de upload (para PDFs)
    client_max_body_size 50M;

    # Logs
    access_log /var/log/nginx/assinatura-digital-access.log;
    error_log /var/log/nginx/assinatura-digital-error.log;
}
```

**Substitua `seu_dominio.com.br` pelo seu domínio real!**

### 7.3. Ativar Site

```bash
# Criar link simbólico
sudo ln -s /etc/nginx/sites-available/assinatura-digital /etc/nginx/sites-enabled/

# Testar configuração
sudo nginx -t

# Se estiver OK, recarregar Nginx
sudo systemctl reload nginx
```

### 7.4. Configurar SSL/HTTPS com Let's Encrypt

```bash
# Instalar Certbot
sudo apt install certbot python3-certbot-nginx -y

# Obter certificado SSL (substitua pelo seu domínio)
sudo certbot --nginx -d seu_dominio.com.br -d www.seu_dominio.com.br

# Seguir as instruções:
# - Email para notificações
# - Aceitar termos
# - Escolher redirecionar HTTP para HTTPS (opção 2)

# Renovação automática já é configurada
# Testar renovação:
sudo certbot renew --dry-run
```

---

## ✅ PARTE 8: Verificação Final

### 8.1. Verificar DNS

No seu computador:

```powershell
# Windows PowerShell
nslookup seu_dominio.com.br

# Deve retornar o IP da VPS
```

### 8.2. Testar Acesso

1. Acesse: `http://seu_dominio.com.br`
2. Deve redirecionar para HTTPS
3. O site deve carregar normalmente

### 8.3. Verificar Logs

```bash
# Logs da aplicação
pm2 logs assinatura-digital

# Logs do Nginx
sudo tail -f /var/log/nginx/assinatura-digital-error.log
```

---

## 🔄 PARTE 9: Comandos Úteis (Manutenção)

### Gerenciar Aplicação

```bash
# Ver processos
pm2 list

# Ver logs
pm2 logs assinatura-digital

# Reiniciar
pm2 restart assinatura-digital

# Parar
pm2 stop assinatura-digital

# Iniciar
pm2 start assinatura-digital

# Deletar do PM2
pm2 delete assinatura-digital
```

### Atualizar Aplicação

```bash
cd ~/apps/assinatura-digital

# 1. Fazer backup do banco de dados
cp database.sqlite database.sqlite.backup

# 2. Atualizar código (se usar Git)
git pull

# 3. Ou enviar novos arquivos via SCP/FTP

# 4. Instalar novas dependências (se houver)
npm install --production

# 5. Fazer novo build
npm run build

# 6. Reiniciar aplicação
pm2 restart assinatura-digital
```

### Verificar Status do Sistema

```bash
# Ver uso de recursos
pm2 monit

# Ver processos do sistema
htop

# Ver espaço em disco
df -h

# Ver memória
free -h
```

---

## 🐛 Troubleshooting

### Site não carrega / Erro 502

1. **Verificar se Next.js está rodando:**
   ```bash
   pm2 list
   pm2 logs assinatura-digital
   ```

2. **Verificar porta 3002:**
   ```bash
   netstat -tulpn | grep 3002
   ```

3. **Reiniciar aplicação:**
   ```bash
   pm2 restart assinatura-digital
   ```

### Erro 403 Forbidden

1. **Verificar permissões:**
   ```bash
   ls -la ~/apps/assinatura-digital
   chmod 755 ~/apps/assinatura-digital
   ```

### SSL não funciona

1. **Verificar certificado:**
   ```bash
   sudo certbot certificates
   ```

2. **Renovar manualmente:**
   ```bash
   sudo certbot renew
   ```

### Banco de dados não funciona

1. **Verificar permissões:**
   ```bash
   ls -la database.sqlite
   chmod 664 database.sqlite
   ```

2. **Verificar se foi criado:**
   ```bash
   ls -la ~/apps/assinatura-digital/database.sqlite
   ```

### DNS não resolve

1. **Aguardar propagação** (pode levar até 48h)
2. **Verificar DNS:**
   ```bash
   nslookup seu_dominio.com.br
   dig seu_dominio.com.br
   ```

3. **Limpar cache DNS local:**
   ```powershell
   # Windows PowerShell (como administrador)
   ipconfig /flushdns
   ```

---

## 📋 Checklist Final

- [ ] IP da VPS Hostinger anotado
- [ ] DNS configurado na HostGator (registros A para @ e www)
- [ ] Node.js instalado na VPS (versão 18+)
- [ ] PM2 instalado globalmente
- [ ] Arquivos do projeto enviados para VPS
- [ ] Dependências instaladas (`npm install --production`)
- [ ] Build criado (`npm run build`)
- [ ] Arquivo `.env` criado com variáveis corretas
- [ ] Aplicação rodando com PM2 na porta 3002
- [ ] Nginx configurado como proxy reverso
- [ ] SSL/HTTPS configurado com Let's Encrypt
- [ ] Site acessível via https://seu_dominio.com.br
- [ ] DNS propagado e funcionando

---

## 🆘 Suporte

Se encontrar problemas:

1. **Logs da aplicação:** `pm2 logs assinatura-digital`
2. **Logs do Nginx:** `sudo tail -f /var/log/nginx/assinatura-digital-error.log`
3. **Status PM2:** `pm2 status`
4. **Testar Nginx:** `sudo nginx -t`

---

## 💡 Dicas Adicionais

### Firewall (se necessário)

Se a VPS tiver firewall ativo, liberar porta 80, 443 e 3002:

```bash
# UFW (Ubuntu)
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 3002/tcp
sudo ufw status
```

### Backup Automático

Criar script de backup do banco de dados:

```bash
nano ~/backup-assinatura.sh
```

```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
cp ~/apps/assinatura-digital/database.sqlite ~/backups/database_$DATE.sqlite
# Manter apenas últimos 7 dias
find ~/backups -name "database_*.sqlite" -mtime +7 -delete
```

```bash
chmod +x ~/backup-assinatura.sh

# Adicionar ao crontab (backup diário às 3h)
crontab -e
# Adicionar:
0 3 * * * /home/seu_usuario/backup-assinatura.sh
```

---

**🎉 Pronto! Seu sistema estará hospedado na VPS Hostinger e acessível pelo domínio da HostGator!**

