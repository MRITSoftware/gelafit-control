# Instruções de Instalação e Execução

## 🚀 Como Executar o Sistema

### 1. Instalar Dependências
```bash
npm install
```

### 2. Executar o Projeto
```bash
npm run dev
```

### 3. Acessar o Sistema
Abra seu navegador e acesse: `http://localhost:3000`

## 👥 Usuários de Teste

### Super Administrador
- **Email**: admin@sistema.com
- **Senha**: 123456
- **Funcionalidades**: 
  - Gerencia todos os clientes
  - Cria novos clientes
  - Visualiza estatísticas gerais
  - Acessa logs de auditoria global

### Admin do Cliente
- **Email**: admin@empresaexemplo.com
- **Senha**: 123456
- **Funcionalidades**:
  - Gerencia usuários do cliente
  - Organiza documentos em pastas
  - Aprova/rejeita documentos
  - Visualiza logs de auditoria do cliente

### Colaborador
- **Email**: colaborador@empresaexemplo.com
- **Senha**: 123456
- **Funcionalidades**:
  - Faz upload de documentos
  - Visualiza documentos da empresa
  - Adiciona comentários
  - Baixa documentos aprovados

## 🎯 Fluxo de Uso

### 1. Login como Super Admin
1. Acesse `/login`
2. Use: `admin@sistema.com` / `123456`
3. Será redirecionado para `/admin`
4. Crie novos clientes se necessário

### 2. Login como Admin do Cliente
1. Acesse `/login`
2. Use: `admin@empresaexemplo.com` / `123456`
3. Será redirecionado para `/dashboard`
4. Gerencie usuários, pastas e documentos

### 3. Login como Colaborador
1. Acesse `/login`
2. Use: `colaborador@empresaexemplo.com` / `123456`
3. Será redirecionado para `/dashboard`
4. Faça upload de documentos e comente

## 📋 Funcionalidades Implementadas

### ✅ Sistema Multi-Tenant
- Separação completa de dados por cliente
- Controle de acesso baseado em cliente
- Portal específico para cada cliente

### ✅ Gestão de Usuários
- 3 perfis diferentes (Super Admin, Admin Cliente, Colaborador)
- Convites de usuários
- Ativação/desativação de usuários

### ✅ Gestão de Documentos
- Upload de documentos (PDF, DOC, DOCX, imagens)
- Versionamento automático
- Organização em pastas
- Download de documentos

### ✅ Workflow de Aprovação
- Status: Pendente → Em Revisão → Aprovado/Rejeitado
- Mudança de status pelos admins
- Histórico de versões

### ✅ Sistema de Comentários
- Comentários públicos e internos
- Timeline de comentários
- Visualização por documento

### ✅ Auditoria Completa
- Log de todas as ações
- Rastreamento de usuários
- Metadados detalhados
- Filtros por cliente

### ✅ Interface Moderna
- Design responsivo
- Componentes reutilizáveis
- Notificações toast
- Modais interativos

## 🔧 Estrutura do Projeto

```
├── app/                    # App Router do Next.js
│   ├── admin/             # Painel de administração
│   ├── dashboard/         # Dashboard do cliente
│   ├── login/             # Página de login
│   └── layout.tsx         # Layout principal
├── components/            # Componentes React
├── lib/                   # Utilitários e serviços
├── types/                 # Definições TypeScript
└── README.md             # Documentação completa
```

## ⚠️ Importante

- **Dados em Memória**: Os dados são armazenados em memória e serão perdidos ao reiniciar o servidor
- **Senhas Simples**: Para demonstração, as senhas são armazenadas em texto simples
- **Upload Simulado**: Os uploads são simulados (não salvos fisicamente)
- **Produção**: Este é um sistema de demonstração, não use em produção sem as devidas adaptações

## 🚀 Próximos Passos

Para usar em produção, considere:
1. Integrar com banco de dados real (PostgreSQL/MongoDB)
2. Implementar autenticação robusta com JWT
3. Configurar storage real para arquivos (AWS S3, etc.)
4. Adicionar testes automatizados
5. Implementar notificações por email reais
6. Configurar HTTPS e segurança adequada
