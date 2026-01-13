# 🚀 Deploy Rápido no Netlify

## ✅ MÉTODO MAIS RÁPIDO: Via GitHub

### 1. Criar Repositório no GitHub

1. Acesse: https://github.com
2. **"New repository"** (botão verde)
3. Nome: `sistema-assinatura-digital`
4. **NÃO marque** "Initialize with README"
5. **Create repository**

---

### 2. Enviar Código para GitHub

**Opção A - GitHub Desktop (MAIS FÁCIL):**
1. Baixe: https://desktop.github.com/
2. Instale e abra
3. **File** → **Add Local Repository**
4. Escolha a pasta: `D:\ass digital`
5. **Commit** → Escreva: "Initial commit"
6. **Publish repository**

**Opção B - Terminal (se preferir):**
```bash
cd "D:\ass digital"
git init
git add .
git commit -m "Initial commit"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/sistema-assinatura-digital.git
git push -u origin main
```

---

### 3. Conectar Netlify ao GitHub

1. Acesse: https://app.netlify.com
2. **"Add new site"** → **"Import an existing project"**
3. Clique em **"GitHub"**
4. Autorize Netlify
5. Selecione: `sistema-assinatura-digital`

---

### 4. Configurar Build

**Configurações:**

- **Base directory:** (deixe vazio)
- **Build command:** `npm run build`
- **Publish directory:** `.next`

**Importante:** O Netlify já detecta Next.js automaticamente!

---

### 5. Adicionar Variáveis de Ambiente

1. Clique em **"Show advanced"** ou **"Environment variables"**
2. Adicione cada variável uma por uma:

```
NODE_ENV = production
NEXT_PUBLIC_BASE_URL = https://seu-projeto.netlify.app
SMTP_HOST = smtp.titan.email
SMTP_PORT = 465
SMTP_USER = disparador@mrit.com.br
SMTP_PASS = ME2KC1B84HCB@
MERCADOPAGO_ACCESS_TOKEN = APP_USR-3472086984963081-053123-9b1a4a027169af10a5ebd9940a75d7ee-208507556
MERCADOPAGO_PUBLIC_KEY = APP_USR-e286fe23-410d-403a-994a-f24e2bb7560c
JWT_SECRET = 9a443e0f4733282e9281ea2ed40706c50e0d09c1dcbf22257d1c1264f511d9be83ce061ca6928637ac428575f03090f7dcafb51736e5acf30cda975272418887
DB_HOST = (URL do banco remoto - ver abaixo)
DB_PORT = 3306
DB_USER = (usuário do banco)
DB_PASS = (senha do banco)
DB_NAME = (nome do banco)
```

**⚠️ IMPORTANTE:** O Netlify é serverless e não suporta SQLite local. Você precisa usar banco remoto (MySQL da Hostgator ou Supabase).

---

### 6. Deploy

1. Clique em **"Deploy site"**
2. Aguarde o build (2-5 minutos)
3. Quando terminar, você terá uma URL: `https://seu-projeto.netlify.app`

---

## 🔧 Ajustar Variável NEXT_PUBLIC_BASE_URL

Após o deploy:

1. No Netlify, copie a URL do site (ex: `https://seu-projeto.netlify.app`)
2. Vá em **"Site settings"** → **"Environment variables"**
3. Edite `NEXT_PUBLIC_BASE_URL` e coloque a URL do Netlify
4. Clique em **"Trigger deploy"** → **"Deploy site"** (para aplicar)

---

## ⚠️ Banco de Dados - IMPORTANTE!

**Problema:** Netlify não suporta SQLite (arquivo local).

**Soluções:**

### Opção A: Usar MySQL da Hostgator
- Use as credenciais MySQL da Hostgator
- Coloque no Netlify as variáveis `DB_HOST`, `DB_PORT`, etc.
- Use o IP/host da Hostgator para conectar

### Opção B: Usar Supabase (GRÁTIS)
- Crie conta: https://supabase.com
- Crie banco de dados
- Use credenciais do Supabase no Netlify

### Opção C: PlanetScale (GRÁTIS)
- Similar ao Supabase
- URL: https://planetscale.com

---

## ✅ Pronto!

Após o deploy, seu site estará no ar em: `https://seu-projeto.netlify.app`

---

**Me avise quando fizer o passo 1 (criar repositório GitHub) ou se tiver dúvidas!** 🚀

