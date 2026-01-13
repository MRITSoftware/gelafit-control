# Guia de Configuração - MR Food

## 📋 Pré-requisitos

- Node.js 18+ instalado
- Conta no Supabase (gratuita)
- Conta no Google Cloud (opcional - para Google Maps)

## 🚀 Passo a Passo

### 1. Instalar Dependências

```bash
npm install
```

### 2. Configurar Supabase

1. Acesse [supabase.com](https://supabase.com) e crie um novo projeto
2. Vá em **SQL Editor** e execute o conteúdo do arquivo `supabase-setup.sql`
3. Vá em **Settings > API** e copie:
   - **Project URL**
   - **anon/public key**

### 3. Configurar Variáveis de Ambiente

Crie um arquivo `.env.local` na raiz do projeto:

```env
NEXT_PUBLIC_SUPABASE_URL=https://seu-projeto.supabase.co
NEXT_PUBLIC_SUPABASE_ANON_KEY=sua-chave-anon
```

### 4. Configurar Autenticação no Supabase

1. No Supabase, vá em **Authentication > Settings**
2. Configure as URLs permitidas:
   - Site URL: `http://localhost:3000`
   - Redirect URLs: `http://localhost:3000/**`

### 5. Executar o Projeto

```bash
npm run dev
```

Acesse [http://localhost:3000](http://localhost:3000)

## 🔧 Configurações Opcionais

### Google Maps (Roteirização)

1. Crie um projeto no [Google Cloud Console](https://console.cloud.google.com)
2. Ative a API **Maps JavaScript API** e **Distance Matrix API**
3. Crie uma chave de API
4. Adicione ao `.env.local`:

```env
NEXT_PUBLIC_GOOGLE_MAPS_API_KEY=sua-chave-google-maps
```

### WhatsApp (Notificações)

Para enviar notificações via WhatsApp, você pode usar:
- Twilio API
- WhatsApp Business API
- Outras APIs compatíveis

Adicione ao `.env.local`:

```env
WHATSAPP_API_KEY=sua-chave-whatsapp
WHATSAPP_PHONE_NUMBER=seu-numero
```

### iFood Webhook

Para receber pedidos do iFood:

1. Configure a URL do webhook no painel do iFood: `https://seu-dominio.com/api/webhooks/ifood`
2. Configure a validação de assinatura conforme a documentação do iFood

## 📱 Primeiros Passos

1. **Criar Conta**: Acesse `/register` e crie sua conta de restaurante
2. **Configurar Restaurante**: Vá em Configurações e complete os dados
3. **Adicionar Cardápio**: Vá em Cardápio e adicione seus produtos
4. **Criar Pedido**: Vá em Pedidos > Novo Pedido para testar

## 🎯 Funcionalidades Principais

- ✅ Gestão de Pedidos
- ✅ Controle de Entregas
- ✅ Gestão de Clientes
- ✅ Programa de Fidelidade
- ✅ Relatórios e Analytics
- ✅ Integração com iFood (webhook)
- ✅ Notificações Automáticas

## 🐛 Troubleshooting

### Erro de Autenticação

- Verifique se as variáveis de ambiente estão corretas
- Confirme que as URLs estão configuradas no Supabase

### Erro ao Criar Pedido

- Verifique se o restaurante foi criado corretamente
- Confirme que há itens no cardápio

### Webhook não funciona

- Verifique se a URL está acessível publicamente
- Confirme a validação de assinatura

## 📚 Próximos Passos

- [ ] Configurar domínio personalizado
- [ ] Configurar SSL/HTTPS
- [ ] Integrar com mais plataformas
- [ ] Adicionar mais relatórios
- [ ] Implementar app mobile para entregadores

