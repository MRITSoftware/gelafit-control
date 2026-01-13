# 🚀 Guia Completo de Deploy na Hostinger

## ✅ O que fazer ANTES de enviar os arquivos:

### 1. Build já está feito! ✅
A pasta `.next/` já foi criada com o build de produção. Não é necessário fazer build novamente no servidor.

---

## 📦 O QUE ENVIAR para a Hostinger:

### **Pastas (via FTP/SSH):**
```
.next/          ← Build do Next.js (CRÍTICO - já está criado!)
app/            ← Páginas e rotas
components/     ← Componentes React
lib/            ← Bibliotecas
utils/          ← Utilitários
types/          ← Tipos TypeScript (se existir)
```

### **Arquivos:**
```
package.json
package-lock.json
next.config.js
tsconfig.json
tailwind.config.js
postcss.config.js
middleware.ts
next-env.d.ts
```

### **NÃO ENVIAR:**
- ❌ `node_modules/` (instale no servidor)
- ❌ `.env` ou `.env.local` (crie no servidor)
- ❌ `database.sqlite` (será criado automaticamente)
- ❌ `.git/` (se existir)
- ❌ `.next/cache/` (pode ser recriado)

---

## 🔧 PASSOS NO SERVIDOR HOSTINGER:

### 1. **Conectar via FTP/SSH**
- Use FileZilla, WinSCP ou terminal SSH
- Acesse a pasta `public_html` ou `domains/esign.mrit.com.br/public_html`

### 2. **Enviar os arquivos**
- Envie todas as pastas e arquivos listados acima
- Mantenha a estrutura de pastas igual

### 3. **Criar arquivo .env no servidor**
Copie o conteúdo de `.env.example` e crie um arquivo `.env` no servidor com suas credenciais reais:

```env
NODE_ENV=production
NEXT_PUBLIC_BASE_URL=https://esign.mrit.com.br
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=disparador@mrit.com.br
SMTP_PASS=sua_senha_real
MERCADOPAGO_ACCESS_TOKEN=seu_token_real
MERCADOPAGO_PUBLIC_KEY=sua_public_key_real
JWT_SECRET=gerar_uma_string_aleatoria_forte
DB_HOST=localhost
DB_PORT=3306
DB_USER=seu_usuario_mysql
DB_PASS=sua_senha_mysql
DB_NAME=nome_do_banco
```

### 4. **Instalar dependências no servidor**
Conecte via SSH e execute:

```bash
cd public_html  # ou domains/esign.mrit.com.br/public_html

# Instalar dependências de produção
npm install --production
```

**IMPORTANTE:** Não execute `npm run build` novamente! O build já está feito na pasta `.next/` que você enviou.

### 5. **Iniciar o servidor**

#### Opção A: PM2 (Recomendado)
```bash
# Instalar PM2 globalmente (se ainda não tiver)
npm install -g pm2

# Iniciar aplicação
pm2 start npm --name "esign" -- run production

# Salvar configuração
pm2 save
pm2 startup
```

#### Opção B: Direto (não recomendado - para testar)
```bash
npm run production
```

### 6. **Configurar Nginx/Proxy Reverso**
A Hostinger geralmente já tem Nginx configurado. Você precisa criar uma regra de proxy reverso:

Via painel da Hostinger ou editando `/etc/nginx/sites-available/esign.mrit.com.br`:

```nginx
server {
    listen 80;
    server_name esign.mrit.com.br;
    return 301 https://esign.mrit.com.br$request_uri;
}

server {
    listen 443 ssl http2;
    server_name esign.mrit.com.br;

    ssl_certificate /etc/ssl/certs/esign.mrit.com.br.crt;
    ssl_certificate_key /etc/ssl/private/esign.mrit.com.br.key;

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

Se não tiver acesso root, configure via painel da Hostinger no "Gerenciador de Sites".

---

## ✅ CHECKLIST FINAL:

- [ ] Build criado localmente (`.next/` existe)
- [ ] Arquivos enviados via FTP/SSH
- [ ] Arquivo `.env` criado no servidor com credenciais reais
- [ ] `npm install --production` executado no servidor
- [ ] PM2 instalado e configurado
- [ ] Aplicação rodando na porta 3002
- [ ] Nginx configurado como proxy reverso
- [ ] SSL/HTTPS configurado
- [ ] Site acessível via https://esign.mrit.com.br

---

## 🐛 Troubleshooting

### Aplicação não inicia:
```bash
# Ver logs do PM2
pm2 logs esign

# Verificar se porta 3002 está livre
netstat -tulpn | grep 3002

# Verificar variáveis de ambiente
cat .env
```

### Erro 502 Bad Gateway:
- Next.js não está rodando na porta 3002
- Verifique: `pm2 list`
- Reinicie: `pm2 restart esign`

### Banco de dados não funciona:
- Verifique credenciais MySQL no `.env`
- Certifique-se que o banco foi criado
- Verifique permissões do usuário MySQL

---

## 📚 Documentação Adicional:

- Veja também: `DEPLOY_HOSTINGER.md` (guia mais detalhado)
- Veja também: `BUILD_GUIA_RAPIDO.md` (guia de build)

