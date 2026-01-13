# 🔧 Como Corrigir Erro 403 na Hostgator

## ❌ Problema:
Erro 403 Forbidden ao acessar `esign.mrit.com.br`

---

## 🔍 Causas Comuns:

1. **Permissões de arquivos/pastas incorretas**
2. **Next.js não está rodando** (está tentando acessar como site estático)
3. **Falta configuração do servidor** para Node.js

---

## ✅ SOLUÇÃO PASSO A PASSO:

### PASSO 1: Corrigir Permissões dos Arquivos

Via SSH na Hostgator:

```bash
# Conectar via SSH (use terminal ou PuTTY)
# Ou use o "Terminal" no cPanel da Hostgator

# Navegar até a pasta
cd public_html/esign

# Corrigir permissões das pastas
find . -type d -exec chmod 755 {} \;

# Corrigir permissões dos arquivos
find . -type f -exec chmod 644 {} \;

# Permissão especial para pasta .next (pode ser necessário)
chmod -R 755 .next

# Se houver problemas, permissão mais aberta (temporário)
chmod -R 755 .
```

---

### PASSO 2: Verificar se Node.js está Instalado

```bash
# Verificar versão do Node.js
node --version

# Verificar versão do npm
npm --version

# Se não estiver instalado, a Hostgator precisa instalar
# Entre em contato com suporte ou instale via cPanel
```

---

### PASSO 3: Instalar Dependências

```bash
cd public_html/esign

# Instalar dependências de produção
npm install --production
```

---

### PASSO 4: Criar Arquivo .env

```bash
# Criar arquivo .env
nano .env
```

Cole o conteúdo (ajuste com suas credenciais):

```env
NODE_ENV=production
NEXT_PUBLIC_BASE_URL=https://esign.mrit.com.br
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=disparador@mrit.com.br
SMTP_PASS=sua_senha_aqui
MERCADOPAGO_ACCESS_TOKEN=seu_token
MERCADOPAGO_PUBLIC_KEY=sua_public_key
JWT_SECRET=uma_string_aleatoria_forte
DB_HOST=localhost
DB_PORT=3306
DB_USER=seu_usuario_mysql
DB_PASS=sua_senha_mysql
DB_NAME=nome_do_banco
```

Salvar: `Ctrl + X`, depois `Y`, depois `Enter`

---

### PASSO 5: Iniciar o Servidor Next.js

#### Opção A: PM2 (Recomendado)

```bash
# Instalar PM2 globalmente
npm install -g pm2

# Iniciar aplicação
pm2 start npm --name "esign" -- run production

# Salvar configuração
pm2 save
pm2 startup
```

#### Opção B: Direto (para testar)

```bash
npm run production
```

**IMPORTANTE:** O Next.js precisa rodar na porta 3002 (ou outra porta configurada)

---

### PASSO 6: Configurar Proxy Reverso (.htaccess)

Crie um arquivo `.htaccess` na pasta `public_html/esign/`:

```bash
nano .htaccess
```

Cole este conteúdo:

```apache
RewriteEngine On
RewriteRule ^(.*)$ http://localhost:3002/$1 [P,L]
```

OU (se a Hostgator não suportar ProxyPass, use redirect):

```apache
# Se a Hostgator não suportar proxy, pode precisar usar Node.js diretamente
# Nesse caso, configure o domínio para apontar para a porta do Node.js
```

---

### PASSO 7: Configurar Domínio no cPanel

1. Acesse cPanel: https://br838.hostgator.com.br:2083/
2. Procure por **"Subdomínios"** ou **"Addon Domains"**
3. Configure `esign.mrit.com.br` para apontar para `public_html/esign`

**OU** se já existe, verifique:
- **"Apache Handlers"** ou **"Document Root"**
- Aponte para: `public_html/esign`

---

### PASSO 8: Configurar Apache/Nginx (se necessário)

Se a Hostgator permitir configuração de servidor:

**Para Apache (.htaccess na pasta raiz do domínio):**

```apache
<IfModule mod_proxy.c>
    ProxyPreserveHost On
    ProxyPass / http://localhost:3002/
    ProxyPassReverse / http://localhost:3002/
</IfModule>
```

**OU use mod_rewrite:**

```apache
RewriteEngine On
RewriteCond %{REQUEST_URI} !^/api/
RewriteRule ^(.*)$ http://localhost:3002/$1 [P,L]
```

---

## 🐛 Troubleshooting:

### Verificar se o servidor está rodando:

```bash
# Ver processos PM2
pm2 list

# Ver logs
pm2 logs esign

# Verificar se porta 3002 está em uso
netstat -tulpn | grep 3002
```

### Verificar permissões:

```bash
# Listar arquivos com permissões
ls -la

# Deve mostrar permissões como:
# drwxr-xr-x para pastas
# -rw-r--r-- para arquivos
```

### Verificar logs de erro:

```bash
# Ver logs do Apache/nginx (se acessível)
tail -f /var/log/apache2/error.log
# OU
tail -f /var/log/nginx/error.log
```

---

## 📋 Checklist:

- [ ] Permissões corrigidas (755 para pastas, 644 para arquivos)
- [ ] Node.js instalado e funcionando
- [ ] `npm install --production` executado
- [ ] Arquivo `.env` criado com credenciais
- [ ] Servidor Next.js iniciado (PM2 ou direto)
- [ ] Porta 3002 está acessível
- [ ] .htaccess configurado (se necessário)
- [ ] Domínio apontando para pasta correta no cPanel

---

## 🔄 Alternativa: Se Não Conseguir Rodar Node.js Diretamente

Se a Hostgator não permitir rodar Node.js diretamente:

1. **Build estático:** Pode ser necessário fazer build estático do Next.js
2. **Usar outra porta:** Verificar qual porta a Hostgator permite
3. **Contatar suporte Hostgator:** Perguntar sobre Node.js e Next.js

---

**Execute os passos na ordem e me avise em qual passo teve problema!** 🚀

