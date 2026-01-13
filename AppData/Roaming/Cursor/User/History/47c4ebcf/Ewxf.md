# 📁 Sistema de Gestão de Arquivos

Sistema web moderno e responsivo para gestão de documentos entre clientes e equipes, com controle de versões, comentários e diferentes níveis de acesso.

## ✨ Funcionalidades

### 👑 Painel Administrativo
- Criar e gerenciar usuários com diferentes níveis de acesso (Admin, Equipe, Cliente)
- Visualizar todos os clientes cadastrados
- Dashboard com estatísticas em tempo real
- Controle completo sobre documentos e usuários

### 👥 Área da Equipe
- Visualizar todos os documentos enviados pelos clientes
- Baixar documentos para análise
- Atualizar documentos com novas versões
- Mudar status dos documentos (Pendente, Em Revisão, Aprovado, Rejeitado)
- Adicionar comentários nos documentos
- Histórico de versões

### 📤 Área do Cliente
- Enviar documentos PDF com título e descrição
- Visualizar todos os documentos enviados
- Acompanhar status dos documentos
- Baixar seus próprios documentos
- Comentar nos documentos
- Ver histórico de atualizações

## 🛠️ Tecnologias Utilizadas

- **Next.js 14** - Framework React com App Router
- **TypeScript** - Tipagem estática
- **Tailwind CSS** - Estilização moderna e responsiva
- **Supabase** - Backend (Autenticação, Banco de Dados, Storage)
- **Lucide React** - Ícones modernos

## 📋 Pré-requisitos

- Node.js 18+ instalado
- Conta no Supabase (gratuita)
- npm ou yarn

## 🚀 Instalação

### 1. Clone o projeto (ou use os arquivos já criados)

```bash
cd "d:\projeto sem nome"
```

### 2. Instale as dependências

```bash
npm install
```

### 3. Configure o Supabase

#### 3.1. Criar o Bucket de Storage

1. Acesse o [Dashboard do Supabase](https://supabase.com/dashboard)
2. Selecione seu projeto
3. Vá em **Storage** no menu lateral
4. Clique em **New bucket**
5. Nome: `documents`
6. Deixe como **Private** (não público)
7. Clique em **Create bucket**

#### 3.2. Configurar Políticas do Storage

No bucket `documents` que você acabou de criar:

1. Clique nos três pontinhos e selecione **Policies**
2. Adicione as seguintes políticas:

**Política de INSERT (Upload):**
```sql
CREATE POLICY "Allow authenticated uploads"
ON storage.objects FOR INSERT
TO authenticated
WITH CHECK (bucket_id = 'documents');
```

**Política de SELECT (Download):**
```sql
CREATE POLICY "Allow authenticated downloads"
ON storage.objects FOR SELECT
TO authenticated
USING (bucket_id = 'documents');
```

**Política de UPDATE:**
```sql
CREATE POLICY "Allow team and admin updates"
ON storage.objects FOR UPDATE
TO authenticated
USING (
  bucket_id = 'documents' AND
  EXISTS (
    SELECT 1 FROM public.profiles
    WHERE id = auth.uid() AND role IN ('admin', 'team')
  )
);
```

**Política de DELETE:**
```sql
CREATE POLICY "Allow admin deletes"
ON storage.objects FOR DELETE
TO authenticated
USING (
  bucket_id = 'documents' AND
  EXISTS (
    SELECT 1 FROM public.profiles
    WHERE id = auth.uid() AND role = 'admin'
  )
);
```

#### 3.3. Executar o Script SQL

1. No Dashboard do Supabase, vá em **SQL Editor**
2. Clique em **New query**
3. Copie todo o conteúdo do arquivo `supabase-setup.sql`
4. Cole no editor e clique em **Run**

### 4. Configure as Variáveis de Ambiente

Crie um arquivo `.env.local` na raiz do projeto (já foi criado, mas verifique):

```env
NEXT_PUBLIC_SUPABASE_URL=https://base3.muraltv.com.br
NEXT_PUBLIC_SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlzcyI6InN1cGFiYXNlMiIsImlhdCI6MTc0OTc4NjAwMCwiZXhwIjoxOTE3NTUyNDAwfQ.MYmpgQo5ODwqR4Ihv8Fbwn4t2Ev7LR3fud7GpWWrXbU
```

### 5. Criar o Primeiro Usuário Admin

1. No Dashboard do Supabase, vá em **Authentication** > **Users**
2. Clique em **Add user** > **Create new user**
3. Preencha:
   - Email: seu-email@exemplo.com
   - Password: sua-senha-segura
   - Marque "Auto Confirm User"
4. Clique em **Create user**

5. Vá em **SQL Editor** e execute:

```sql
UPDATE public.profiles 
SET role = 'admin', full_name = 'Administrador'
WHERE email = 'seu-email@exemplo.com';
```

### 6. Inicie o servidor de desenvolvimento

```bash
npm run dev
```

Acesse: [http://localhost:3000](http://localhost:3000)

## 📱 Uso do Sistema

### Primeiro Acesso (Admin)

1. Acesse `http://localhost:3000`
2. Faça login com o email e senha do admin criado
3. Você será redirecionado para o dashboard administrativo

### Criar Usuários

1. No menu lateral, clique em **Usuários**
2. Clique no botão **Novo Usuário**
3. Preencha os dados:
   - Email
   - Senha
   - Nome Completo
   - Função (Admin, Equipe ou Cliente)
4. Clique em **Criar**

### Fluxo de Trabalho Típico

#### Como Cliente:
1. Login no sistema
2. Clique em **Enviar Arquivo** no menu
3. Preencha título e descrição
4. Selecione o arquivo PDF
5. Clique em **Enviar Documento**
6. Acompanhe o status em **Meus Documentos**

#### Como Equipe:
1. Login no sistema
2. Vá em **Documentos**
3. Veja os documentos pendentes
4. Clique em **Baixar** para analisar
5. Adicione comentários se necessário
6. Atualize o status (Em Revisão, Aprovado, Rejeitado)
7. Se necessário, clique em **Atualizar Arquivo** para enviar uma versão revisada

#### Como Admin:
1. Acesso completo a todas as áreas
2. Gerencie usuários e clientes
3. Visualize estatísticas no dashboard
4. Controle total sobre documentos

## 🎨 Interface

O sistema possui:
- ✅ Design moderno e profissional
- ✅ Totalmente responsivo (funciona perfeitamente no celular)
- ✅ Navegação intuitiva
- ✅ Feedback visual para ações
- ✅ Cards informativos
- ✅ Tabelas organizadas
- ✅ Modais para ações importantes

## 🔒 Segurança

- Autenticação via Supabase Auth
- Row Level Security (RLS) em todas as tabelas
- Políticas de acesso baseadas em roles
- Storage protegido
- Validação de tipos de arquivo (apenas PDF)
- Limite de tamanho de arquivo

## 📊 Estrutura do Banco de Dados

### Tabelas:
- **profiles** - Perfis dos usuários com roles
- **documents** - Documentos enviados
- **comments** - Comentários nos documentos
- **document_history** - Histórico de versões

### Roles (Níveis de Acesso):
- **admin** - Acesso total ao sistema
- **team** - Pode visualizar e gerenciar documentos
- **client** - Pode enviar e visualizar seus próprios documentos

## 🐛 Troubleshooting

### Erro ao fazer upload de arquivo

1. Verifique se o bucket `documents` foi criado
2. Confirme que as políticas de storage foram aplicadas
3. Verifique se o arquivo é PDF e tem menos de 10MB

### Erro "RLS policy violation"

1. Verifique se executou o script SQL completo
2. Confirme que o usuário tem o role correto no banco
3. Verifique as políticas RLS no Supabase Dashboard

### Não consigo fazer login

1. Confirme que o usuário foi criado no Supabase Auth
2. Verifique se o perfil foi criado na tabela `profiles`
3. Confirme que o role foi atribuído corretamente

## 🚀 Deploy em Produção

### Vercel (Recomendado)

1. Faça push do código para o GitHub
2. Importe o projeto na Vercel
3. Configure as variáveis de ambiente
4. Deploy automático!

### Outras opções:
- Railway
- Netlify
- AWS Amplify

## 📝 Licença

Este projeto foi desenvolvido para uso interno. Sinta-se livre para modificar conforme necessário.

## 🤝 Suporte

Para dúvidas ou problemas:
1. Verifique a documentação do Supabase
2. Revise os logs no console do navegador
3. Consulte a documentação do Next.js

---

**Desenvolvido com ❤️ usando Next.js e Supabase**



