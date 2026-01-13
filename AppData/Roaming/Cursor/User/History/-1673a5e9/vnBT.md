# Guia de Deploy na Hostinger - eSign MRIT

## Domínio
**www.esign.mrit.com.br**

## Pré-requisitos

1. Conta na Hostinger
2. Node.js 18+ instalado no servidor
3. Domínio configurado apontando para o servidor

## Passos para Deploy

### 1. Configurar Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto com as seguintes variáveis:

```env
NEXT_PUBLIC_BASE_URL=https://www.esign.mrit.com.br

# SMTP para envio de emails (opcional)
SMTP_HOST=smtp.hostinger.com
SMTP_PORT=465
SMTP_USER=seu_email@dominio.com
SMTP_PASS=sua_senha
SMTP_SECURE=true

# JWT Secret (gerar uma chave forte)
JWT_SECRET=sua_chave_secreta_muito_longa_e_aleatoria_aqui
```

**Importante**: 
- Use uma chave JWT secreta longa e aleatória em produção
- Não commite o arquivo `.env` no repositório
- O banco de dados SQLite será criado automaticamente

### 2. Build do Projeto

```bash
npm install
npm run build
```

### 3. Configurar o Servidor

#### Opção A: Usando PM2 (Recomendado)

```bash
# Instalar PM2 globalmente
npm install -g pm2

# Iniciar aplicação
pm2 start npm --name "esign-mrit" -- start

# Salvar configuração
pm2 save

# Configurar para iniciar automaticamente
pm2 startup
```

#### Opção B: Usando systemd

Crie um arquivo `/etc/systemd/system/esign-mrit.service`:

```ini
[Unit]
Description=eSign MRIT Next.js App
After=network.target

[Service]
Type=simple
User=seu_usuario
WorkingDirectory=/caminho/para/seu/projeto
Environment="NODE_ENV=production"
ExecStart=/usr/bin/npm start
Restart=always

[Install]
WantedBy=multi-user.target
```

Depois:
```bash
sudo systemctl enable esign-mrit
sudo systemctl start esign-mrit
```

### 4. Configurar Nginx (Proxy Reverso)

Crie/edite o arquivo de configuração do Nginx para o domínio:

```nginx
server {
    listen 80;
    server_name www.esign.mrit.com.br esign.mrit.com.br;

    # Redirecionar HTTP para HTTPS
    return 301 https://www.esign.mrit.com.br$request_uri;
}

server {
    listen 443 ssl http2;
    server_name www.esign.mrit.com.br esign.mrit.com.br;

    # Certificado SSL (use Let's Encrypt)
    ssl_certificate /caminho/para/certificado.crt;
    ssl_certificate_key /caminho/para/chave.key;

    # Configurações SSL
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

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

    # Timeout aumentado para uploads de PDF
    proxy_read_timeout 300s;
    proxy_connect_timeout 300s;
    proxy_send_timeout 300s;
    client_max_body_size 50M;
}
```

### 5. Instalar Certificado SSL

Use Let's Encrypt (gratuito):

```bash
sudo apt-get update
sudo apt-get install certbot python3-certbot-nginx

# Instalar certificado
sudo certbot --nginx -d www.esign.mrit.com.br -d esign.mrit.com.br
```

### 6. Configurações Importantes

#### Banco de Dados
- O SQLite será criado automaticamente na raiz do projeto
- Garanta permissões de escrita na pasta do projeto
- Faça backup regularmente do arquivo `.sqlite`

#### Mercado Pago
- Configure as URLs de callback no painel do Mercado Pago:
  - Success: `https://www.esign.mrit.com.br/comprar-creditos?status=success`
  - Failure: `https://www.esign.mrit.com.br/comprar-creditos?status=failure`
  - Pending: `https://www.esign.mrit.com.br/comprar-creditos?status=pending`
  - Webhook: `https://www.esign.mrit.com.br/api/pagamentos/webhook`

#### Portas
- A aplicação Next.js roda na porta 3002 por padrão
- Certifique-se de que a porta está acessível apenas via Nginx
- Feche a porta 3002 no firewall para acesso externo direto

### 7. Monitoramento e Logs

```bash
# Ver logs da aplicação
pm2 logs esign-mrit

# Ver status
pm2 status

# Reiniciar aplicação
pm2 restart esign-mrit
```

### 8. Atualizações Futuras

```bash
# Fazer pull das atualizações
git pull

# Reinstalar dependências (se necessário)
npm install

# Rebuild
npm run build

# Reiniciar aplicação
pm2 restart esign-mrit
```

## Troubleshooting

### Erro de permissão no banco de dados
```bash
chmod 664 /caminho/para/projeto/*.sqlite
chmod 775 /caminho/para/projeto
```

### Erro de memória
Aumente a memória do Node.js:
```bash
export NODE_OPTIONS="--max-old-space-size=2048"
```

### Verificar se está rodando
```bash
curl http://localhost:3002
# ou
pm2 status
```

## Contato e Suporte
Em caso de problemas, verifique os logs e certifique-se de que todas as variáveis de ambiente estão configuradas corretamente.

