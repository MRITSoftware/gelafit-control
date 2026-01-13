# 🚀 Passo a Passo - Deploy na Hostgator

## ✅ PASSO 1: Verificar Permissões (FileZilla)

1. No FileZilla, lado direito → `public_html/esign`
2. Clique direito na pasta `esign` → **"File Permissions"** ou **"Permissões de Arquivo"**
3. Marque: **"Recurse into subdirectories"** (Recursivo)
4. Defina:
   - **Pastas:** `755`
   - **Arquivos:** `644`
5. Clique em **OK**
6. Aguarde aplicar (pode demorar alguns minutos)

---

## ✅ PASSO 2: Criar Arquivo .env (FileZilla)

1. No FileZilla, dentro de `public_html/esign`
2. Clique direito → **"Create new file"** ou **"Criar novo arquivo"**
3. Nome do arquivo: **`.env`** (com o ponto na frente!)
4. Abra para editar (duplo clique)

### Conteúdo do .env:

```env
NODE_ENV=production
NEXT_PUBLIC_BASE_URL=https://esign.mrit.com.br

# Configurações de Email (Titan Email)
SMTP_HOST=smtp.titan.email
SMTP_PORT=465
SMTP_USER=disparador@mrit.com.br
SMTP_PASS=INSIRA_SUA_SENHA_AQUI

# Mercado Pago
MERCADOPAGO_ACCESS_TOKEN=INSIRA_SEU_TOKEN_AQUI
MERCADOPAGO_PUBLIC_KEY=INSIRA_SUA_PUBLIC_KEY_AQUI

# JWT Secret (gere uma string aleatória forte)
JWT_SECRET=INSIRA_UMA_STRING_ALEATORIA_FORTE_AQUI

# Database (MySQL na Hostgator)
DB_HOST=localhost
DB_PORT=3306
DB_USER=INSIRA_USUARIO_MYSQL
DB_PASS=INSIRA_SENHA_MYSQL
DB_NAME=INSIRA_NOME_DO_BANCO
```

**Substitua os campos "INSIRA_..." com suas credenciais reais!**

5. **Salve o arquivo**

---

## ⚠️ IMPORTANTE: Próximos Passos Requerem SSH

Os próximos passos precisam ser feitos via **SSH/Terminal**, não pelo FileZilla:

### PASSO 3: Acessar via SSH

**Opção A - Terminal no cPanel:**
1. Acesse: https://br838.hostgator.com.br:2083/
2. Login: `math4513` / `ONN7RW9MA6FU`
3. Procure por **"Terminal"** ou **"SSH Access"**
4. Abra o terminal

**Opção B - PuTTY:**
- Download: https://www.putty.org/
- Host: `br838.hostgator.com.br`
- Porta: `22`
- Protocolo: SSH
- Usuário: `math4513`
- Senha: `ONN7RW9MA6FU`

---

### PASSO 4: Instalar Dependências (SSH)

Depois de conectar via SSH:

```bash
# Navegar até a pasta
cd public_html/esign

# Verificar Node.js
node --version
npm --version

# Instalar dependências
npm install --production
```

---

### PASSO 5: Iniciar Servidor (SSH)

```bash
# Instalar PM2
npm install -g pm2

# Iniciar aplicação
pm2 start npm --name "esign" -- run production

# Salvar configuração
pm2 save
```

---

## 📋 Resumo do que fazer AGORA:

1. ✅ **Passo 1:** Corrigir permissões no FileZilla
2. ✅ **Passo 2:** Criar arquivo `.env` no FileZilla
3. ⏭️ **Próximo:** Acessar SSH para instalar dependências e iniciar servidor

---

**Faça os Passos 1 e 2 no FileZilla primeiro, depois me avise quando terminar para continuarmos!** 🚀

