# 🚀 Configuração do Sistema de Cupons

## ⚠️ Configuração Necessária

O sistema de cupons precisa das credenciais do Supabase para funcionar completamente. Siga os passos abaixo:

### 1. Configurar Supabase

#### **Criar Projeto no Supabase:**
1. Acesse [supabase.com](https://supabase.com)
2. Crie uma nova conta ou faça login
3. Clique em "New Project"
4. Escolha sua organização
5. Digite o nome do projeto (ex: "painel-cupons")
6. Defina uma senha para o banco de dados
7. Escolha uma região próxima
8. Clique em "Create new project"

#### **Obter Credenciais:**
1. No painel do Supabase, vá em **Settings** > **API**
2. Copie a **Project URL** (ex: `https://abcdefgh.supabase.co`)
3. Copie a **anon public** key (ex: `eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...`)

### 2. Configurar Credenciais

Edite o arquivo `public/promos/config.js`:

```javascript
window.SUPABASE_CONFIG = {
    url: 'https://SEU-PROJETO.supabase.co',  // Substitua pela sua URL
    anonKey: 'SUA-CHAVE-ANONIMA'            // Substitua pela sua chave
};
```

### 3. Criar Tabela no Banco

Execute o script SQL em **SQL Editor** no Supabase:

```sql
-- Criar tabela promo
CREATE TABLE IF NOT EXISTS promo (
    id BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    uid TEXT DEFAULT '',
    id_promo TEXT DEFAULT '',
    codigo_promo TEXT DEFAULT '',
    texto_promo TEXT DEFAULT '',
    valor_promo INTEGER DEFAULT 0,
    imagem_promo TEXT DEFAULT '',
    url_qrcode TEXT DEFAULT ''
);

-- Criar índices
CREATE INDEX IF NOT EXISTS idx_promo_id_promo ON promo(id_promo);
CREATE INDEX IF NOT EXISTS idx_promo_codigo ON promo(codigo_promo);
CREATE INDEX IF NOT EXISTS idx_promo_created_at ON promo(created_at);

-- Habilitar RLS
ALTER TABLE promo ENABLE ROW LEVEL SECURITY;

-- Política para leitura pública
CREATE POLICY "Promoções são públicas para leitura" ON promo
    FOR SELECT USING (true);

-- Política para inserção (apenas usuários autenticados)
CREATE POLICY "Usuários autenticados podem criar promoções" ON promo
    FOR INSERT WITH CHECK (auth.role() = 'authenticated');
```

### 4. Configurar Storage

#### **Criar Bucket:**
1. No painel do Supabase, vá em **Storage**
2. Clique em **Create bucket**
3. Nome: `promos`
4. Marque **Public bucket**
5. Clique em **Create bucket**

#### **Configurar Políticas de Storage:**
Execute no **SQL Editor**:

```sql
-- Política para upload de QR codes
CREATE POLICY "Upload de QR codes" ON storage.objects
    FOR INSERT WITH CHECK (bucket_id = 'promos');

-- Política para leitura pública de QR codes
CREATE POLICY "QR codes são públicos" ON storage.objects
    FOR SELECT USING (bucket_id = 'promos');
```

### 5. Testar o Sistema

1. Acesse `/promos/promo_qr.html`
2. Digite um código de cupom
3. Clique em "Gerar Cupom"
4. Verifique se:
   - QR Code é gerado
   - Dados são salvos no banco
   - Imagem é enviada para o Storage
   - Link público funciona

## 🔧 Modo Offline

Se o Supabase não estiver configurado, o sistema funcionará em **modo offline**:

- ✅ **QR Code será gerado** normalmente
- ✅ **Preview funcionará** com dados locais
- ❌ **Dados não serão salvos** no banco
- ❌ **Imagens não serão enviadas** para o Storage
- ❌ **Links públicos não funcionarão**

## 🐛 Troubleshooting

### **Erro: "Supabase não configurado"**
- Verifique se as credenciais estão corretas em `config.js`
- Confirme se a URL e chave estão corretas

### **Erro: "Failed to fetch"**
- Verifique se a URL do Supabase está correta
- Confirme se o projeto está ativo no Supabase

### **Erro: "StorageUnknownError"**
- Verifique se o bucket `promos` foi criado
- Confirme se as políticas de Storage estão configuradas

### **Erro: "Row Level Security"**
- Verifique se as políticas RLS estão configuradas
- Confirme se a tabela `promo` existe

## 📱 Funcionalidades

### **Com Supabase Configurado:**
- ✅ Criação de cupons
- ✅ Geração de QR Code
- ✅ Upload de imagens
- ✅ Salvamento no banco
- ✅ Links públicos funcionais
- ✅ Download de imagens

### **Modo Offline:**
- ✅ Criação de cupons
- ✅ Geração de QR Code
- ✅ Preview local
- ❌ Persistência de dados
- ❌ Links públicos

## 🎯 Próximos Passos

1. **Configure o Supabase** seguindo os passos acima
2. **Teste o sistema** criando um cupom
3. **Verifique o banco** para confirmar que os dados foram salvos
4. **Teste o link público** para confirmar que funciona

---

**💡 Dica:** Mantenha as credenciais do Supabase seguras e nunca as compartilhe publicamente!
