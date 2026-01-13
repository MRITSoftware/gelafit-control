# 🔗 Como Conectar FileZilla à Hostgator

## 📋 Credenciais Fornecidas:

- **Painel:** https://br838.hostgator.com.br:2083/
- **Usuário:** math4513
- **Senha:** ONN7RW9MA6FU

---

## 🚀 OPÇÃO 1: Usar Mesmas Credenciais (MAIS FÁCIL)

As credenciais do cPanel geralmente funcionam também para FTP/SFTP:

### Configuração no FileZilla:

1. **Host (Servidor):**
   - `br838.hostgator.com.br` 
   - OU: `ftp.br838.hostgator.com.br`
   - OU: `ftp.esign.mrit.com.br` (se o domínio estiver configurado)

2. **Usuário:**
   - `math4513`

3. **Senha:**
   - `ONN7RW9MA6FU`

4. **Porta:**
   - **21** para FTP
   - **22** para SFTP (recomendado)

5. **Protocolo:**
   - Selecione **SFTP** (mais seguro) OU **FTP**

6. **Clicar em "Conectar" (ou Quickconnect)**

---

## 🔍 OPÇÃO 2: Buscar Credenciais FTP no cPanel

Se a Opção 1 não funcionar, busque as credenciais FTP específicas:

### No cPanel da Hostgator:

1. **Acesse:** https://br838.hostgator.com.br:2083/
2. **Login:** math4513 / ONN7RW9MA6FU
3. **Procure por:** "FTP Accounts" ou "Contas FTP"
4. **Ou procure por:** "SSH Access" ou "Acesso SSH"

**Onde encontrar:**
- Seção **"Files"** → **"FTP Accounts"**
- Ou **"Advanced"** → **"SSH Access"**

**Anote:**
- Host FTP/SFTP
- Usuário FTP
- Senha FTP
- Porta (geralmente 21 para FTP ou 22 para SFTP)

---

## 📁 Navegar até a Pasta

Depois de conectar:

### No FileZilla:

**Lado Esquerdo (Local):**
- Navegue até: `D:\ass digital`
- Ou clique em "Selecionar pasta local" e escolha a pasta do projeto

**Lado Direito (Servidor - Hostgator):**
- Navegue até: `public_html` → `esign`
- Ou: `domains/esign.mrit.com.br/public_html/esign`
- Depende da estrutura de pastas da Hostgator

---

## ✅ Teste Rápido:

### 1. Tente primeiro com:
- **Host:** `br838.hostgator.com.br`
- **Usuário:** `math4513`
- **Senha:** `ONN7RW9MA6FU`
- **Porta:** **22** (SFTP)
- **Protocolo:** **SFTP**

### 2. Se não conectar:
- Tente porta **21** (FTP)
- Tente protocolo **FTP**

### 3. Se ainda não conectar:
- Acesse o cPanel e encontre "FTP Accounts"
- Use as credenciais específicas de FTP

---

## 🐛 Problemas Comuns:

### Erro "Cannot connect to server"
- Verifique se o SFTP/FTP está habilitado no cPanel
- Tente FTP ao invés de SFTP
- Verifique firewall/antivírus

### "Authentication failed"
- Credenciais podem ser diferentes para FTP
- Busque credenciais FTP específicas no cPanel
- Verifique se usuário e senha estão corretos

### Não encontra a pasta `public_html`
- Procure por `domains/` ou `htdocs/`
- Ou navegue pela raiz e procure a pasta `public_html`

---

## 📤 Depois de Conectar:

Quando conseguir conectar:

1. **Navegue até:** `public_html/esign` (ou `domains/esign.mrit.com.br/public_html/esign`)

2. **Faça upload das pastas e arquivos:**
   - `.next/`
   - `app/`
   - `components/`
   - `lib/`
   - `utils/`
   - `types/`
   - `package.json`
   - `package-lock.json`
   - `next.config.js`
   - `tsconfig.json`
   - `tailwind.config.js`
   - `postcss.config.js`
   - `middleware.ts`
   - `next-env.d.ts`

3. **Aguarde o upload concluir**

---

## 🔐 Segurança:

⚠️ **IMPORTANTE:** As senhas que você compartilhou agora estão no histórico desta conversa. Após o deploy, considere:

1. Alterar senhas se necessário
2. Não compartilhar senhas em conversas públicas
3. Usar autenticação de dois fatores se disponível

---

**Tente primeiro com as credenciais do cPanel. Se não funcionar, me avise e te ajudo a encontrar as credenciais FTP específicas!** 🚀

