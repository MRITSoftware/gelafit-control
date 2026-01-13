# 🚀 Guia de Deploy em Produção

## Vercel (Recomendado) - Deploy Gratuito

### Pré-requisitos
- Conta no GitHub
- Conta na Vercel (gratuita)
- Projeto Supabase configurado

### Passo 1: Preparar o Código

1. Crie um repositório no GitHub
2. Faça commit de todos os arquivos:

```bash
git init
git add .
git commit -m "Initial commit - Sistema de Gestão de Arquivos"
git branch -M main
git remote add origin https://github.com/seu-usuario/seu-repositorio.git
git push -u origin main
```

### Passo 2: Deploy na Vercel

1. Acesse: https://vercel.com
2. Clique em **Add New** → **Project**
3. Selecione **Import Git Repository**
4. Escolha o repositório do GitHub
5. Configure o projeto:

#### Framework Preset
- Framework: **Next.js**
- Root Directory: **./** (raiz)

#### Environment Variables

Adicione as variáveis de ambiente:

```env
NEXT_PUBLIC_SUPABASE_URL=https://base3.muraltv.com.br
NEXT_PUBLIC_SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlzcyI6InN1cGFiYXNlMiIsImlhdCI6MTc0OTc4NjAwMCwiZXhwIjoxOTE3NTUyNDAwfQ.MYmpgQo5ODwqR4Ihv8Fbwn4t2Ev7LR3fud7GpWWrXbU
```

6. Clique em **Deploy**
7. Aguarde o build (2-3 minutos)
8. 🎉 Pronto! Seu site está no ar

### Passo 3: Configurar Domínio (Opcional)

1. No dashboard da Vercel, vá em **Settings** → **Domains**
2. Adicione seu domínio personalizado
3. Configure o DNS conforme instruções

## Railway

### Deploy no Railway

1. Acesse: https://railway.app
2. Clique em **New Project**
3. Selecione **Deploy from GitHub repo**
4. Escolha seu repositório
5. Adicione as variáveis de ambiente
6. Deploy automático!

## Netlify

### Deploy no Netlify

1. Acesse: https://netlify.com
2. Clique em **Add new site** → **Import an existing project**
3. Conecte ao GitHub
4. Configure:
   - Build command: `npm run build`
   - Publish directory: `.next`
5. Adicione as variáveis de ambiente
6. Deploy!

## AWS Amplify

### Deploy no AWS Amplify

1. Acesse AWS Console → Amplify
2. Clique em **New app** → **Host web app**
3. Conecte ao GitHub
4. Configure o build
5. Adicione variáveis de ambiente
6. Deploy!

## ⚙️ Configurações Importantes

### Supabase - Adicionar URLs de Produção

1. Dashboard do Supabase → **Authentication** → **URL Configuration**
2. Adicione as URLs de produção em:
   - **Site URL**: https://seu-dominio.com
   - **Redirect URLs**: 
     - https://seu-dominio.com
     - https://seu-dominio.com/login
     - https://seu-dominio.com/admin/dashboard
     - https://seu-dominio.com/team/dashboard
     - https://seu-dominio.com/client/dashboard

### Next.js - Configurações de Produção

Verifique o arquivo `next.config.js`:

```javascript
/** @type {import('next').NextConfig} */
const nextConfig = {
  experimental: {
    serverActions: true,
  },
  // Adicione se estiver usando domínio personalizado
  // images: {
  //   domains: ['seu-dominio.com'],
  // },
}

module.exports = nextConfig
```

## 🔒 Segurança em Produção

### 1. Variáveis de Ambiente

✅ **NUNCA** commit o arquivo `.env.local`
✅ Use as configurações de ambiente da plataforma de deploy
✅ Mantenha as chaves seguras

### 2. Supabase RLS

✅ Verifique se todas as políticas RLS estão ativas
✅ Teste o acesso com diferentes roles
✅ Revise as permissões do storage

### 3. CORS

O Supabase já gerencia CORS, mas verifique em:
- Dashboard → **API** → **API Settings**

### 4. Rate Limiting

Configure limites de requisição no Supabase:
- Dashboard → **Settings** → **API**

## 📊 Monitoramento

### Vercel Analytics

1. No dashboard da Vercel, ative **Analytics**
2. Visualize métricas de performance
3. Monitore erros em tempo real

### Supabase Logs

1. Dashboard → **Logs**
2. Monitore:
   - Auth logs
   - Database logs
   - Storage logs
   - API logs

## 🔄 Atualizações

### Deploy Automático (Vercel)

Após a configuração inicial:
1. Faça alterações no código
2. Commit e push para o GitHub
3. A Vercel faz deploy automático! ✨

```bash
git add .
git commit -m "Descrição das mudanças"
git push
```

### Deploy Manual

Se preferir controle manual:
1. Vercel Dashboard → Settings → Git
2. Desative "Auto Deploy"
3. Use `vercel --prod` via CLI

## 🧪 Testar em Produção

### Checklist Pós-Deploy

- [ ] Login funcionando
- [ ] Criar usuário (admin)
- [ ] Upload de documento (cliente)
- [ ] Download de documento (equipe)
- [ ] Comentários funcionando
- [ ] Atualização de versão (equipe)
- [ ] Mudança de status (admin/equipe)
- [ ] Responsividade no mobile
- [ ] Performance (PageSpeed Insights)

### URLs para Testar

```
https://seu-dominio.com/login
https://seu-dominio.com/admin/dashboard
https://seu-dominio.com/team/dashboard
https://seu-dominio.com/client/dashboard
```

## 🚨 Rollback

Se algo der errado:

### Vercel
1. Dashboard → Deployments
2. Selecione uma versão anterior
3. Clique nos três pontos → **Promote to Production**

### Pelo Git
```bash
git revert HEAD
git push
```

## 📈 Otimizações

### 1. Configurar CDN

A Vercel já usa CDN global automaticamente!

### 2. Compressão de Imagens

Adicione no `next.config.js`:

```javascript
module.exports = {
  images: {
    formats: ['image/avif', 'image/webp'],
  },
}
```

### 3. Cache Headers

Next.js já otimiza automaticamente, mas você pode customizar.

### 4. Bundle Size

Analise o tamanho do bundle:

```bash
npm run build
```

## 💰 Custos

### Plano Gratuito (Recomendado para começar)

- **Vercel**: Grátis até 100GB bandwidth/mês
- **Supabase**: Grátis até 500MB database + 1GB storage
- **GitHub**: Repositórios públicos e privados grátis

### Quando escalar?

- Mais de 1000 usuários ativos
- Mais de 10GB de arquivos
- Mais de 100.000 requisições/mês

## 📞 Suporte

### Recursos Oficiais

- Vercel: https://vercel.com/docs
- Supabase: https://supabase.com/docs
- Next.js: https://nextjs.org/docs

### Comunidades

- Discord do Supabase
- Reddit r/nextjs
- Stack Overflow

---

**Seu sistema está pronto para produção! 🚀**

