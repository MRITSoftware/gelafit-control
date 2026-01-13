# 🚀 Guia Completo - Deploy na Hostinger via WinSCP/SSH

## 📋 Pré-requisitos

- WinSCP instalado e aberto
- Acesso SSH ao servidor Hostinger (credenciais no painel)
- Node.js instalado no servidor (ou será instalado)
- Build local criado (vamos fazer isso agora)

---

## PASSO 1: Preparar Build Localmente (no seu PC)

### 1.1. No terminal do Windows (PowerShell), na pasta do projeto:

```powershell
# Verificar se está na pasta correta
cd "D:\ass digital"

# Limpar build anterior (opcional)
.\limpar-build.bat

# Criar build de produção
npm run build
```

**Aguarde até aparecer "Compiled successfully"** ✅

### 1.2. Verificar se a pasta `.next` foi criada:
- Deve aparecer na raiz do projeto
- Se não aparecer, verifique erros no terminal

---

## PASSO 2: Conectar no WinSCP

### 2.1. No WinSCP:

1. **Clique em "Nova Sessão"** (ou `Ctrl+N`)

2. **Preencha as credenciais SSH:**
   - **Protocolo:** `SFTP`
   - **Nome do host:** Seu servidor Hostinger (ex: `u123456789.hosted-by-vdsina.com` ou IP)
   - **Porta:** `22` (padrão SSH)
   - **Nome do usuário:** Seu usuário SSH (geralmente `root` ou `u123456789`)
   - **Senha:** Sua senha SSH

   💡 **Dica:** As credenciais SSH estão no painel da Hostinger:
   - Acesse: Painel → SSH Access
   - Ou: Painel → Servidor → Informações de Acesso

3. **Clique em "Login"**

4. **Se aparecer aviso de chave do servidor, clique em "Sim"**

---

## PASSO 3: Escolher Localização no Servidor

### Opções recomendadas:

**Opção A: Diretório dedicado (Recomendado)**
```
/root/apps/assinatura-digital
```

**Opção B: Dentro do domínio (se tiver configurado)**
```
/root/domains/esign.mrit.com.br/public_html
```

**Opção C: Pasta personalizada**
```
/home/usuario/assinatura-digital
```

⚠️ **Anote o caminho escolhido! Você vai precisar depois!**

---

## PASSO 4: Criar Pasta no Servidor

### 4.1. No WinSCP:

1. **No painel direito (servidor remoto):**
   - Navegue até o diretório desejado (ex: `/root/apps/`)
   
2. **Clique com botão direito → "Novo → Diretório"**
   - Nome: `assinatura-digital`
   - Pressione Enter

3. **Entre na pasta criada** (duplo clique)

---

## PASSO 5: Enviar Arquivos

### 5.1. No painel esquerdo (seu PC):
- Navegue até: `D:\ass digital`

### 5.2. Selecione e envie estas pastas/arquivos:

**✅ ENVIE:**
- `.next/` (pasta inteira)
- `app/` (pasta inteira)
- `components/` (pasta inteira)
- `lib/` (pasta inteira)
- `utils/` (pasta inteira)
- `types/` (se existir)
- `middleware.ts`
- `next.config.js`
- `package.json`
- `package-lock.json`
- `tsconfig.json`
- `tailwind.config.js`
- `postcss.config.js`
- `next-env.d.ts`

**❌ NÃO ENVIE:**
- `node_modules/` (instale no servidor)
- `.env` ou `.env.local` (vamos criar no servidor)
- `database.sqlite` (será criado automaticamente)
- `.git/`
- Arquivos de documentação (`.md`)

### 5.3. Como enviar:

1. **Selecione os arquivos/pastas** no painel esquerdo (Ctrl+Clique para múltiplos)
2. **Arraste para o painel direito** (servidor)
3. **Aguarde o upload terminar** (barra de progresso no rodapé)

⚠️ **Atenção:** O upload pode demorar alguns minutos dependendo da conexão!

---

## PASSO 6: Conectar via SSH Terminal

### 6.1. No WinSCP:

1. **Menu superior:** `Comandos → Abrir Terminal...` (ou `Ctrl+P`)
2. **Ou:** Botão "Terminal" na barra de ferramentas

### 6.2. No terminal SSH que abrir:

```bash
# Navegar até a pasta do projeto
cd /root/apps/assinatura-digital

# Verificar se os arquivos estão lá
ls -la
```

---

## PASSO 7: Verificar/Instalar Node.js

### 7.1. Verificar se Node.js está instalado:

```bash
node --version
npm --version
```

### 7.2. Se NÃO estiver instalado ou versão < 18:

```bash
# Instalar Node.js 18.x
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Verificar novamente
node --version
```

**Versão mínima:** Node.js 18.x ou superior ✅

---

## PASSO 8: Instalar Dependências

```bash
# Certifique-se de estar na pasta do projeto
cd /root/apps/assinatura-digital

# Instalar apenas dependências de produção (mais rápido)
npm install --production

# Aguarde terminar...
```

⏱️ Isso pode levar 2-5 minutos

---

## PASSO 9: Criar Arquivo .env

### 9.1. No WinSCP:

1. **No painel direito (servidor), navegue até a pasta do projeto**
2. **Clique com botão direito → Novo → Arquivo**
3. **Nome:** `.env`
4. **Clique em "OK"**

### 9.2. Editar o arquivo .env:

1. **Clique duas vezes no arquivo `.env`** (abre no editor)
2. **Cole este conteúdo** (ajuste com suas credenciais):

```env
# Ambiente
NODE_ENV=production

# URL do seu site (ajuste para seu domínio)
NEXT_PUBLIC_BASE_URL=https://www.esign.mrit.com.br

# Email SMTP (Hostinger geralmente usa Titan Email)
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=seu_email@dominio.com.br
SMTP_PASS=sua_senha_email

# Mercado Pago (pegue no painel do Mercado Pago)
MERCADOPAGO_ACCESS_TOKEN=seu_access_token_aqui
MERCADOPAGO_PUBLIC_KEY=sua_public_key_aqui

# JWT Secret (gere uma chave aleatória forte)
JWT_SECRET=GERE_UMA_CHAVE_ALEATORIA_MUITO_FORTE_AQUI_MINIMO_32_CARACTERES
```

### 9.3. Gerar JWT_SECRET:

No terminal SSH:

```bash
openssl rand -hex 32
```

**Copie o resultado** e cole no `.env` no lugar de `GERE_UMA_CHAVE_ALEATORIA...`

### 9.4. Salvar o arquivo .env:
- `Ctrl+S` ou `Alt+S` (salvar e fechar)

---

## PASSO 10: Testar se o Build Funciona

```bash
# Testar se o servidor inicia (por enquanto, vamos parar depois)
npm run production
```

Se aparecer "Ready on http://localhost:3002", está funcionando! ✅

**Pare o servidor:** `Ctrl+C`

---

## PASSO 11: Instalar e Configurar PM2

PM2 mantém o servidor rodando automaticamente:

```bash
# Instalar PM2 globalmente
npm install -g pm2

# Parar processo anterior (se existir)
pm2 delete assinatura 2>/dev/null || true

# Iniciar aplicação com PM2
pm2 start npm --name "assinatura" -- run production

# Salvar configuração para iniciar após reinicialização
pm2 save

# Configurar PM2 para iniciar no boot (opcional, mas recomendado)
pm2 startup
```

**Copie o comando que aparecer** e execute (pode pedir senha sudo)

---

## PASSO 12: Verificar se Está Rodando

```bash
# Ver status dos processos
pm2 list

# Ver logs em tempo real
pm2 logs assinatura

# Parar logs: Ctrl+C
```

✅ **Deve mostrar:** `online | npm | assinatura`

---

## PASSO 13: Configurar Nginx (Proxy Reverso)

O Nginx vai redirecionar as requisições HTTP/HTTPS para o Node.js na porta 3002.

### 13.1. No painel da Hostinger:

1. **Acesse:** Painel → Gerenciador de Sites
2. **Selecione ou crie:** Configuração para `www.esign.mrit.com.br`
3. **Configure Proxy Reverso:**
   - Proxy: `http://localhost:3002`
   - Ou use as opções avançadas do painel

### 13.2. Ou via SSH (se tiver acesso):

```bash
# Criar arquivo de configuração
sudo nano /etc/nginx/sites-available/esign.mrit.com.br
```

**Cole este conteúdo:**

```nginx
# Redirecionar HTTP para HTTPS
server {
    listen 80;
    server_name www.esign.mrit.com.br esign.mrit.com.br;
    return 301 https://www.esign.mrit.com.br$request_uri;
}

# Configuração HTTPS
server {
    listen 443 ssl http2;
    server_name www.esign.mrit.com.br esign.mrit.com.br;

    # Certificados SSL (geralmente gerenciados pela Hostinger)
    ssl_certificate /etc/ssl/certs/seu_certificado.crt;
    ssl_certificate_key /etc/ssl/private/sua_chave.key;

    # Limite de tamanho de upload (PDFs podem ser grandes)
    client_max_body_size 50M;

    # Proxy reverso para Node.js
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
        proxy_read_timeout 300s;
        proxy_connect_timeout 300s;
    }
}
```

```bash
# Ativar site
sudo ln -s /etc/nginx/sites-available/esign.mrit.com.br /etc/nginx/sites-enabled/

# Testar configuração
sudo nginx -t

# Reiniciar Nginx
sudo systemctl restart nginx
```

---

## PASSO 14: Configurar SSL/HTTPS

### No painel da Hostinger:

1. **Acesse:** Painel → SSL
2. **Selecione o domínio:** `www.esign.mrit.com.br`
3. **Ative SSL** (Let's Encrypt é gratuito)
4. **Aguarde ativação** (pode levar alguns minutos)

---

## PASSO 15: Testar o Site

1. **Abra o navegador**
2. **Acesse:** `https://www.esign.mrit.com.br`
3. **Teste:**
   - Página de login deve aparecer ✅
   - Tente criar uma conta ✅
   - Tente fazer login ✅

---

## 🔧 Comandos Úteis para Manutenção

```bash
# Ver status do servidor
pm2 list

# Ver logs
pm2 logs assinatura

# Reiniciar aplicação
pm2 restart assinatura

# Parar aplicação
pm2 stop assinatura

# Ver uso de recursos
pm2 monit

# Parar monitoramento: Ctrl+C
```

---

## 🐛 Troubleshooting (Solução de Problemas)

### Servidor não inicia (502 Bad Gateway):

```bash
# Verificar se está rodando
pm2 list

# Verificar porta
netstat -tulpn | grep 3002

# Reiniciar
pm2 restart assinatura

# Ver logs de erro
pm2 logs assinatura --err
```

### Erro "Cannot find module":

```bash
# Reinstalar dependências
cd /root/apps/assinatura-digital
rm -rf node_modules
npm install --production
pm2 restart assinatura
```

### Erro de permissão no banco de dados:

```bash
# Dar permissões à pasta do projeto
cd /root/apps/assinatura-digital
chmod 664 database.sqlite
chown root:root database.sqlite
```

### Rebuild necessário após mudanças:

```bash
# No seu PC (Windows):
npm run build

# No WinSCP:
# Envie apenas a pasta .next/ atualizada

# No servidor (SSH):
pm2 restart assinatura
```

---

## 📝 Checklist Final

- [ ] Build criado localmente (pasta `.next` existe)
- [ ] Arquivos enviados para servidor via WinSCP
- [ ] Node.js instalado (versão 18+)
- [ ] Dependências instaladas (`npm install --production`)
- [ ] Arquivo `.env` criado com todas as credenciais
- [ ] PM2 instalado e configurado
- [ ] Servidor rodando (`pm2 list` mostra online)
- [ ] Nginx configurado como proxy reverso
- [ ] SSL/HTTPS ativado
- [ ] Site acessível em `https://www.esign.mrit.com.br`
- [ ] Login e cadastro funcionando

---

## 🎉 Pronto!

Seu sistema está hospedado na Hostinger! 🚀

**Para atualizações futuras:**
1. Faça mudanças no código
2. Execute `npm run build` localmente
3. Envie a pasta `.next/` atualizada via WinSCP
4. No servidor: `pm2 restart assinatura`

---

**Dúvidas?** Verifique os logs: `pm2 logs assinatura`

