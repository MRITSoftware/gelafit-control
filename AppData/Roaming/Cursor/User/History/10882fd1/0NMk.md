# 🚀 Deploy no Netlify - Passo a Passo

## ✅ Pré-requisitos:
- ✅ Conta no Netlify
- ✅ Projeto já compilado (build feito)
- ✅ Código no GitHub (recomendado) OU pode fazer upload direto

---

## 📦 MÉTODO 1: Deploy via GitHub (RECOMENDADO)

### Passo 1: Preparar Projeto no GitHub

1. **Criar repositório no GitHub** (se ainda não tiver)
   - Acesse: https://github.com
   - Clique em **"New repository"**
   - Nome: `sistema-assinatura-digital` (ou qualquer nome)
   - **NÃO** marque "Initialize with README" (os arquivos já existem)
   - Clique em **"Create repository"**

2. **Enviar código para GitHub:**
   ```bash
   # No terminal, na pasta do projeto (D:\ass digital)
   git init
   git add .
   git commit -m "Initial commit"
   git branch -M main
   git remote add origin https://github.com/SEU_USUARIO/sistema-assinatura-digital.git
   git push -u origin main
   ```

   **OU** use GitHub Desktop (mais fácil)

---

### Passo 2: Conectar Netlify ao GitHub

1. **Acesse:** https://app.netlify.com
2. Clique em **"Add new site"** → **"Import an existing project"**
3. Escolha **"GitHub"**
4. Autorize Netlify a acessar GitHub
5. Selecione o repositório: `sistema-assinatura-digital`

---

### Passo 3: Configurar Build no Netlify

**Configurações importantes:**

- **Base directory:** (deixe vazio)
- **Build command:** `npm run build`
- **Publish directory:** `.next`
- **Node version:** 18.x ou 20.x

**Variáveis de ambiente (.env):**
1. Em **"Environment variables"**
2. Adicione TODAS as variáveis do arquivo `.env`:
   - `NODE_ENV=production`
   - `NEXT_PUBLIC_BASE_URL` (URL que o Netlify vai gerar)
   - `SMTP_HOST`, `SMTP_PORT`, `SMTP_USER`, `SMTP_PASS`
   - `MERCADOPAGO_ACCESS_TOKEN`, `MERCADOPAGO_PUBLIC_KEY`
   - `JWT_SECRET`
   - `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASS`, `DB_NAME`

3. Clique em **"Deploy site"**

---

## 📦 MÉTODO 2: Deploy Manual (Upload Direto)

### Passo 1: Fazer Build Local

```bash
# No seu computador (D:\ass digital)
npm run build
```

### Passo 2: Compactar Pasta .next

Crie um arquivo ZIP com a pasta `.next/` (pode ser só a pasta .next)

### Passo 3: Upload no Netlify

1. Acesse: https://app.netlify.com
2. Arraste e solte o ZIP ou clique em **"Add new site"** → **"Deploy manually"**
3. Faça upload do ZIP

**LIMITAÇÃO:** Deploy manual só funciona para sites estáticos. Para Next.js completo, use Método 1 (GitHub).

---

## ⚠️ IMPORTANTE: Ajustes para Netlify

### 1. Next.js precisa de Runtime Node.js

Netlify suporta Next.js, mas pode precisar de ajustes:

**Criar arquivo `netlify.toml` na raiz do projeto:**

```toml
[build]
  command = "npm run build"
  publish = ".next"

[[plugins]]
  package = "@netlify/plugin-nextjs"

[build.environment]
  NODE_VERSION = "18"
```

### 2. Ajustar next.config.js

Se necessário, adicione suporte a Netlify:

```js
/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  // Otimizações para produção
  // output: 'standalone', // Desabilitar para Netlify
  compress: true,
  poweredByHeader: false,
  
  // ... resto do código
}
```

---

## 🔧 Após Deploy

### 1. Configurar Domínio Personalizado (Opcional)

Se quiser usar `esign.mrit.com.br`:
1. No Netlify → **"Domain settings"**
2. **"Add custom domain"**
3. Digite: `esign.mrit.com.br`
4. Siga instruções de DNS

### 2. Atualizar Variáveis de Ambiente

Atualize `NEXT_PUBLIC_BASE_URL` no Netlify com a URL real do deploy.

---

## 🐛 Problemas Comuns

### Erro no Build
- Verifique logs no Netlify
- Certifique-se que Node.js está configurado (18.x ou 20.x)

### Rotas API não funcionam
- Netlify suporta Next.js API routes nativamente
- Verifique variáveis de ambiente

### Banco de dados
- Netlify não suporta SQLite local
- Use banco remoto (MySQL da Hostgator OU Supabase, PlanetScale, etc.)

---

## ✅ Vantagens Netlify

- ✅ Deploy automático via GitHub
- ✅ HTTPS automático
- ✅ CDN global
- ✅ Suporte nativo a Next.js
- ✅ Grátis para uso pessoal

---

**Qual método você prefere? Recomendo Método 1 (GitHub) para facilitar atualizações!** 🚀

