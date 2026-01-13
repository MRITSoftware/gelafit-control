# Sistema de Gestão de Documentos Multi-Tenant

Um sistema completo para gestão de documentos com suporte a múltiplos clientes (multi-tenant), diferentes perfis de usuário e workflow de aprovação.

## 🚀 Funcionalidades

### Perfis de Usuário
- **Super Admin**: Gerencia todos os clientes, usuários e configurações do sistema
- **Admin do Cliente**: Gerencia usuários do próprio cliente, pastas e permissões internas
- **Colaborador**: Envia/baixa documentos, comenta e sugere alterações

### Gestão de Documentos
- ✅ Upload de documentos (PDF, DOC, DOCX, imagens)
- ✅ Versionamento automático
- ✅ Organização em pastas
- ✅ Download de documentos
- ✅ Sistema de comentários

### Workflow de Status
- 📋 **Pendente** → 📝 **Em Revisão** → ✅ **Aprovado** / ❌ **Rejeitado**

### Sistema Multi-Tenant
- 🏢 Separação completa de dados por cliente
- 🔐 Controle de acesso baseado em cliente
- 👥 Gerenciamento de usuários por cliente

### Auditoria e Rastreabilidade
- 📊 Log completo de todas as ações
- 🔍 Rastreamento de quem fez o quê e quando
- 📈 Histórico de versões e mudanças de status

### Notificações
- 📧 Notificações por email (simuladas)
- 🔔 Notificações in-app
- ⏰ Alertas de mudanças de status

## 🛠️ Tecnologias Utilizadas

- **Next.js 14** - Framework React com App Router
- **TypeScript** - Tipagem estática
- **Tailwind CSS** - Estilização
- **React Hook Form** - Gerenciamento de formulários
- **Zod** - Validação de schemas
- **Heroicons** - Ícones
- **React Hot Toast** - Notificações
- **React Dropzone** - Upload de arquivos

## 📦 Instalação

1. **Clone o repositório**
```bash
git clone <url-do-repositorio>
cd document-management-system
```

2. **Instale as dependências**
```bash
npm install
```

3. **Execute o projeto**
```bash
npm run dev
```

4. **Acesse no navegador**
```
http://localhost:3000
```

## 👥 Usuários de Exemplo

### Super Administrador
- **Email**: admin@sistema.com
- **Senha**: 123456
- **Acesso**: Painel completo de administração

### Admin do Cliente
- **Email**: admin@empresaexemplo.com
- **Senha**: 123456
- **Acesso**: Portal do cliente "Empresa Exemplo"

### Colaborador
- **Email**: colaborador@empresaexemplo.com
- **Senha**: 123456
- **Acesso**: Portal do cliente com permissões limitadas

## 🎯 Como Usar

### 1. Login
- Acesse `/login` e use um dos usuários de exemplo
- O sistema redirecionará automaticamente baseado no seu perfil

### 2. Super Admin
- Gerencia todos os clientes do sistema
- Cria novos clientes
- Visualiza estatísticas gerais
- Acessa logs de auditoria global

### 3. Admin do Cliente
- Gerencia usuários do próprio cliente
- Organiza documentos em pastas
- Aprova/rejeita documentos
- Visualiza logs de auditoria do cliente

### 4. Colaborador
- Faz upload de documentos
- Visualiza documentos da empresa
- Adiciona comentários
- Baixa documentos aprovados

## 📁 Estrutura do Projeto

```
├── app/                    # App Router do Next.js
│   ├── admin/             # Painel de administração
│   ├── dashboard/         # Dashboard do cliente
│   ├── login/             # Página de login
│   └── layout.tsx         # Layout principal
├── components/            # Componentes React
│   ├── AuthProvider.tsx   # Provedor de autenticação
│   ├── Header.tsx         # Cabeçalho
│   ├── Sidebar.tsx        # Barra lateral
│   ├── DocumentList.tsx   # Lista de documentos
│   ├── UploadModal.tsx   # Modal de upload
│   ├── FolderManager.tsx  # Gerenciador de pastas
│   ├── UserManagement.tsx # Gerenciamento de usuários
│   └── AuditLog.tsx       # Log de auditoria
├── lib/                   # Utilitários e serviços
│   ├── auth.ts           # Sistema de autenticação
│   └── database.ts       # Simulação de banco de dados
├── types/                 # Definições de tipos TypeScript
│   └── index.ts          # Interfaces e enums
└── README.md             # Este arquivo
```

## 🔧 Configuração

### Variáveis de Ambiente
Crie um arquivo `.env.local` na raiz do projeto:

```env
JWT_SECRET=sua-chave-secreta-aqui
NEXT_PUBLIC_API_URL=http://localhost:3000
```

### Personalização
- **Cores**: Edite `tailwind.config.js` para personalizar o tema
- **Tipos de arquivo**: Modifique `UploadModal.tsx` para aceitar outros formatos
- **Workflow**: Ajuste os status em `types/index.ts`

## 🚀 Próximos Passos

### Funcionalidades Planejadas
- [ ] Integração com banco de dados real (PostgreSQL/MongoDB)
- [ ] Sistema de notificações por email real
- [ ] Upload para cloud storage (AWS S3, Google Cloud)
- [ ] Preview de documentos
- [ ] Assinatura digital
- [ ] Relatórios e dashboards avançados
- [ ] API REST completa
- [ ] Testes automatizados

### Melhorias Técnicas
- [ ] Cache com Redis
- [ ] Rate limiting
- [ ] Logs estruturados
- [ ] Monitoramento e métricas
- [ ] CI/CD pipeline
- [ ] Docker containerization

## 📝 Notas Importantes

### Segurança
- ⚠️ **ATENÇÃO**: Este é um projeto de demonstração
- 🔐 Em produção, implemente autenticação robusta
- 🛡️ Use HTTPS e validação de entrada adequada
- 🔒 Implemente rate limiting e proteção contra ataques

### Banco de Dados
- 📊 Atualmente usa armazenamento em memória
- 💾 Dados são perdidos ao reiniciar o servidor
- 🗄️ Para produção, integre com banco de dados real

### Upload de Arquivos
- 📁 Uploads são simulados (não salvos fisicamente)
- ☁️ Para produção, implemente storage real
- 📏 Configure limites de tamanho adequados

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.

## 📞 Suporte

Se você encontrar algum problema ou tiver dúvidas:

1. Verifique se seguiu todas as instruções de instalação
2. Confirme que está usando as versões corretas das dependências
3. Abra uma issue no GitHub com detalhes do problema

---

**Desenvolvido com ❤️ para demonstração de um sistema multi-tenant completo**
