# ✅ Checklist de Setup - Sistema de Gestão de Arquivos

Use este checklist para garantir que tudo está configurado corretamente!

## 📋 Fase 1: Preparação (5 min)

- [ ] **Node.js 18+ instalado**
  - Verificar: `node --version`
  - Se não, baixar em: https://nodejs.org

- [ ] **Conta no Supabase criada**
  - Criar em: https://supabase.com
  - Projeto criado

- [ ] **Credenciais do Supabase anotadas**
  - URL do projeto
  - Anon Key
  - Localização: Dashboard > Settings > API

## 📦 Fase 2: Instalação (5 min)

- [ ] **Dependências instaladas**
  ```bash
  npm install
  ```
  - Aguardar conclusão sem erros

- [ ] **Arquivo .env.local criado**
  - Criar na raiz do projeto
  - Adicionar as credenciais:
  ```env
  NEXT_PUBLIC_SUPABASE_URL=sua-url
  NEXT_PUBLIC_SUPABASE_ANON_KEY=sua-key
  ```

## 🗄️ Fase 3: Banco de Dados (5 min)

### Storage Bucket
- [ ] **Bucket 'documents' criado**
  - Dashboard > Storage > New bucket
  - Nome: `documents`
  - Tipo: Private ✅

- [ ] **Políticas de Storage configuradas**
  - Bucket documents > Policies
  - 4 políticas adicionadas:
    - [ ] INSERT (upload)
    - [ ] SELECT (download)
    - [ ] UPDATE (atualizar)
    - [ ] DELETE (deletar)

### Tabelas e RLS
- [ ] **Script SQL executado**
  - Dashboard > SQL Editor > New query
  - Copiar `supabase-setup.sql`
  - Colar e executar ▶️
  - Verificar sucesso ✅

- [ ] **Tabelas criadas**
  - Dashboard > Table Editor
  - Verificar se existem:
    - [ ] profiles
    - [ ] documents
    - [ ] comments
    - [ ] document_history

## 👤 Fase 4: Primeiro Usuário (3 min)

- [ ] **Usuário admin criado**
  - Dashboard > Authentication > Users
  - Add user > Create new user
  - Email e senha preenchidos
  - ✅ Auto Confirm User marcado

- [ ] **Role admin atribuído**
  - SQL Editor
  - Executar:
  ```sql
  UPDATE public.profiles 
  SET role = 'admin', full_name = 'Administrador'
  WHERE email = 'seu-email@exemplo.com';
  ```
  - Verificar sucesso ✅

- [ ] **Perfil criado na tabela profiles**
  - Table Editor > profiles
  - Verificar se o usuário aparece

## 🚀 Fase 5: Primeiro Start (2 min)

- [ ] **Servidor iniciado**
  ```bash
  npm run dev
  ```
  - Aguardar "Ready in..."
  - Porta 3000 livre

- [ ] **Página abre no navegador**
  - Abrir: http://localhost:3000
  - Redireciona para /login

- [ ] **Login realizado com sucesso**
  - Email e senha do admin
  - Redireciona para /admin/dashboard

## ✨ Fase 6: Teste Completo (10 min)

### Como Admin
- [ ] **Dashboard carrega**
  - Estatísticas aparecem
  - Sem erros no console (F12)

- [ ] **Criar usuário de teste (Equipe)**
  - Menu: Usuários
  - Novo Usuário
  - Role: Equipe
  - Email: equipe@teste.com
  - Senha: teste123

- [ ] **Criar usuário de teste (Cliente)**
  - Novo Usuário
  - Role: Cliente
  - Email: cliente@teste.com
  - Senha: teste123

### Como Cliente
- [ ] **Logout e login como cliente**
  - Email: cliente@teste.com
  - Redireciona para /client/dashboard

- [ ] **Upload de documento**
  - Menu: Enviar Arquivo
  - Preencher título e descrição
  - Selecionar PDF de teste
  - Enviar com sucesso ✅

- [ ] **Ver documento enviado**
  - Menu: Meus Documentos
  - Documento aparece na lista
  - Status: Pendente (amarelo)

### Como Equipe
- [ ] **Logout e login como equipe**
  - Email: equipe@teste.com
  - Redireciona para /team/dashboard

- [ ] **Ver documento do cliente**
  - Menu: Documentos
  - Documento do cliente aparece

- [ ] **Baixar documento**
  - Botão Baixar funciona
  - PDF abre corretamente

- [ ] **Mudar status**
  - Mudar para "Em revisão"
  - Status atualiza (azul)

- [ ] **Adicionar comentário**
  - Clicar em Comentários
  - Adicionar: "Documento recebido"
  - Comentário aparece

- [ ] **Atualizar documento**
  - Botão Atualizar Arquivo
  - Selecionar novo PDF
  - Descrição da mudança
  - Atualizar com sucesso
  - Versão incrementa para v2

### Verificação Final
- [ ] **Login como cliente novamente**
  - Ver comentário da equipe
  - Ver versão atualizada (v2)
  - Status atualizado

- [ ] **Login como admin**
  - Ver todos os documentos
  - Ver todos os usuários
  - Ver estatísticas atualizadas

## 📱 Fase 7: Teste Mobile (5 min)

- [ ] **Abrir no celular**
  - Mesma rede WiFi
  - IP do computador:3000
  - Exemplo: 192.168.1.100:3000

- [ ] **Login funciona no mobile**
  - Interface responsiva
  - Botões clicáveis

- [ ] **Upload funciona no mobile**
  - Seleção de arquivo
  - Upload completa

- [ ] **Menu lateral funciona**
  - Navegação suave
  - Todas as páginas carregam

## 🎯 Fase 8: Personalização (Opcional)

- [ ] **Logo da empresa**
  - Substituir em Sidebar.tsx
  - Substituir em Login page

- [ ] **Cores personalizadas**
  - Editar tailwind.config.ts
  - Ajustar primary colors

- [ ] **Textos personalizados**
  - Ajustar títulos e descrições
  - Personalizar mensagens

## 🚀 Fase 9: Deploy (Quando estiver pronto)

- [ ] **Código no GitHub**
  - Repositório criado
  - Código commitado

- [ ] **Deploy na Vercel**
  - Projeto importado
  - Variáveis de ambiente configuradas
  - Build com sucesso

- [ ] **URLs de produção no Supabase**
  - Authentication > URL Configuration
  - Site URL adicionada
  - Redirect URLs configuradas

- [ ] **Teste em produção**
  - Login funciona
  - Upload funciona
  - Todas as funcionalidades OK

## ✅ Verificação Final

### Status Geral
```
[ ] Fase 1: Preparação       ___/3
[ ] Fase 2: Instalação       ___/2
[ ] Fase 3: Banco de Dados   ___/8
[ ] Fase 4: Primeiro Usuário ___/3
[ ] Fase 5: Primeiro Start   ___/3
[ ] Fase 6: Teste Completo   ___/15
[ ] Fase 7: Teste Mobile     ___/4
[ ] Fase 8: Personalização   ___/3 (Opcional)
[ ] Fase 9: Deploy           ___/4 (Quando pronto)

TOTAL: ___/41 itens completados
```

## 🆘 Problemas Comuns

### ❌ npm install falha
- Verificar versão do Node.js (precisa 18+)
- Deletar node_modules e tentar novamente
- Verificar conexão com internet

### ❌ Erro ao executar SQL
- Copiar TODO o conteúdo do arquivo
- Verificar se não ficou nenhum caractere especial
- Executar novamente

### ❌ Login não funciona
- Verificar se "Auto Confirm User" foi marcado
- Verificar se o perfil foi criado na tabela profiles
- Verificar se o role foi atribuído

### ❌ Upload não funciona
- Verificar se o bucket foi criado
- Verificar se as 4 políticas foram adicionadas
- Verificar se o arquivo é PDF

### ❌ Erro RLS policy violation
- Executar o script SQL novamente
- Verificar se todas as tabelas têm RLS ativado
- Verificar se o usuário tem role correto

## 📞 Precisa de Ajuda?

1. ✅ Revise este checklist item por item
2. ✅ Consulte INSTALLATION.md para detalhes
3. ✅ Verifique QUICK-REFERENCE.md para comandos
4. ✅ Leia README.md para documentação completa

---

## 🎉 Parabéns!

Se todos os itens estão marcados, seu sistema está pronto para uso! 🚀

**Próximos passos:**
1. Criar mais usuários
2. Testar todos os fluxos
3. Personalizar conforme necessário
4. Deploy em produção

**Bom trabalho! ✨**

