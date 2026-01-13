# Guia de Deploy na Hostinger

## 📋 Pré-requisitos

1. Conta na Hostinger com acesso SSH
2. Domínio configurado: `esign.mrit.com.br`
3. Node.js instalado no servidor (versão 18 ou superior)
4. Acesso ao painel de controle da Hostinger

## 🚀 Passo a Passo

### 1. Preparar o Projeto Localmente

#### 1.1. Criar build de produção

```bash
# No seu computador, no diretório do projeto
npm run build
```

Isso criará a pasta `.next` com os arquivos otimizados para produção.

#### 1.2. Arquivos que DEVEM ser enviados:

- ✅ `.next/` (pasta com build)
- ✅ `app/` (pasta com as rotas e páginas)
- ✅ `components/` (componentes React)
- ✅ `lib/` (bibliotecas e utilitários)
- ✅ `utils/` (utilitários)
- ✅ `middleware.ts` (middleware do Next.js)
- ✅ `next.config.js` (configuração do Next.js)
- ✅ `package.json` (dependências)
- ✅ `package-lock.json` (versões exatas)
- ✅ `tsconfig.json` (configuração TypeScript)
- ✅ `tailwind.config.js` (configuração Tailwind)
- ✅ `postcss.config.js` (configuração PostCSS)
- ✅ `database.sqlite` (vazio ou com dados iniciais - será criado automaticamente se não existir)

#### 1.3. Arquivos que NÃO devem ser enviados:

- ❌ `node_modules/` (será instalado no servidor)
- ❌ `.next/cache/` (pode ser recriado)
- ❌ `.env.local` (você criará no servidor)
- ❌ `.git/` (se houver)
- ❌ Arquivos de desenvolvimento

### 2. Criar Arquivo .env na Hostinger

Crie um arquivo `.env` no diretório raiz do projeto no servidor com:

```env
# Node.js
NODE_ENV=production

# Base URL do site
NEXT_PUBLIC_BASE_URL=https://esign.mrit.com.br

# Configurações de Email (Titan Email)
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=disparador@mrit.com.br
SMTP_PASS=sua_senha_aqui

# Mercado Pago (se necessário)
MERCADOPAGO_ACCESS_TOKEN=seu_token_aqui
MERCADOPAGO_PUBLIC_KEY=sua_public_key_aqui
```

### 3. Conectar via SSH/FTP na Hostinger

#### Opção A: Via FTP (FileZilla, WinSCP, etc.)

1. Acesse o painel da Hostinger
2. Obtenha as credenciais FTP
3. Conecte ao servidor
4. Navegue até a pasta `public_html` ou `domains/esign.mrit.com.br/public_html`

#### Opção B: Via SSH (Recomendado)

1. Acesse o painel da Hostinger
2. Ative o acesso SSH
3. Conecte usando:
```bash
ssh usuario@seudominio.com.br
```

### 4. Estrutura de Pastas na Hostinger

```
public_html/
├── .env                    # Variáveis de ambiente
├── package.json
├── package-lock.json
├── next.config.js
├── tsconfig.json
├── tailwind.config.js
├── postcss.config.js
├── middleware.ts
├── .next/                  # Build do Next.js
├── app/                    # Rotas do Next.js
├── components/            # Componentes React
├── lib/                   # Bibliotecas
├── utils/                 # Utilitários
└── database.sqlite        # Banco de dados SQLite
```

### 5. Instalar Dependências e Iniciar

No servidor via SSH:

```bash
# Navegar até o diretório do projeto
cd public_html  # ou domains/esign.mrit.com.br/public_html

# Instalar dependências
npm install --production

# Criar build (se não foi feito localmente)
npm run build

# Iniciar o servidor na porta 3002
npm run production
```

### 6. Configurar PM2 (Gerenciador de Processos)

PM2 mantém o servidor rodando mesmo após desconectar do SSH:

```bash
# Instalar PM2 globalmente
npm install -g pm2

# Iniciar aplicação com PM2
pm2 start npm --name "esign" -- run production

# Salvar configuração para reiniciar automaticamente
pm2 save
pm2 startup

# Comandos úteis do PM2
pm2 list              # Ver processos rodando
pm2 logs esign        # Ver logs
pm2 restart esign     # Reiniciar
pm2 stop esign        # Parar
pm2 delete esign      # Remover
```

### 7. Configurar Nginx (Proxy Reverso)

A Hostinger geralmente já tem Nginx configurado. Você precisa criar um arquivo de configuração:

Crie/edite: `/etc/nginx/sites-available/esign.mrit.com.br` (ou via painel da Hostinger)

```nginx
server {
    listen 80;
    server_name esign.mrit.com.br;

    # Redirecionar HTTP para HTTPS
    return 301 https://esign.mrit.com.br$request_uri;
}

server {
    listen 443 ssl http2;
    server_name esign.mrit.com.br;

    # Certificado SSL (Hostinger geralmente configura automaticamente)
    ssl_certificate /etc/ssl/certs/esign.mrit.com.br.crt;
    ssl_certificate_key /etc/ssl/private/esign.mrit.com.br.key;

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
    }

    # Otimizações
    client_max_body_size 50M;
}
```

Após criar, ative:
```bash
sudo ln -s /etc/nginx/sites-available/esign.mrit.com.br /etc/nginx/sites-enabled/
sudo nginx -t  # Testar configuração
sudo systemctl reload nginx  # Recarregar
```

**Nota:** Se você não tiver acesso root, pode configurar via painel da Hostinger usando o "Gerenciador de Sites".

### 8. Atualizar package.json para Produção

Já está configurado, mas verifique:

```json
{
  "scripts": {
    "dev": "next dev -p 3002",
    "build": "next build",
    "start": "next start -p 3002",
    "production": "NODE_ENV=production next start -p 3002"
  }
}
```

### 9. Permissões de Arquivo

```bash
# Dar permissões corretas para o banco de dados
chmod 664 database.sqlite
chmod 775 .  # Diretório atual

# Se precisar criar o banco de dados
touch database.sqlite
chmod 664 database.sqlite
```

### 10. Verificar se Está Funcionando

1. Acesse: `https://www.esign.mrit.com.br`
2. Verifique os logs: `pm2 logs esign`
3. Teste login, assinatura, etc.

### 11. Configurar Backup Automático

```bash
# Criar script de backup simples
nano ~/backup-esign.sh
```

Conteúdo:
```bash
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
cp /caminho/para/public_html/database.sqlite ~/backups/database_$DATE.sqlite
# Manter apenas últimos 7 dias
find ~/backups -name "database_*.sqlite" -mtime +7 -delete
```

Tornar executável:
```bash
chmod +x ~/backup-esign.sh
```

Adicionar ao crontab (backup diário às 3h):
```bash
crontab -e
# Adicionar linha:
0 3 * * * /caminho/para/backup-esign.sh
```

## 🔧 Troubleshooting

### Servidor não inicia
- Verifique logs: `pm2 logs esign`
- Verifique se porta 3002 está livre: `netstat -tulpn | grep 3002`
- Verifique variáveis de ambiente: `cat .env`

### Erro 502 Bad Gateway
- Next.js não está rodando na porta 3002
- Verifique PM2: `pm2 list`
- Reinicie: `pm2 restart esign`

### Banco de dados não funciona
- Verifique permissões: `ls -la database.sqlite`
- Verifique se existe: `ls database.sqlite`

### SSL não funciona
- Configure via painel da Hostinger
- Ou use Let's Encrypt: `sudo certbot --nginx -d www.esign.mrit.com.br`

## 📝 Checklist Final

- [ ] Build criado (`npm run build`)
- [ ] Arquivos enviados via FTP/SSH
- [ ] `.env` configurado no servidor
- [ ] `npm install --production` executado
- [ ] PM2 instalado e configurado
- [ ] Nginx configurado como proxy reverso
- [ ] SSL/HTTPS configurado
- [ ] Porta 3002 acessível
- [ ] Banco de dados com permissões corretas
- [ ] Site acessível via https://esign.mrit.com.br
- [ ] Backup configurado

## 🆘 Suporte

Se tiver problemas:
1. Verifique logs: `pm2 logs esign`
2. Verifique console do navegador (F12)
3. Verifique logs do Nginx: `/var/log/nginx/error.log`
4. Contate suporte da Hostinger se necessário
