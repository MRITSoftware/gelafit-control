# 🚀 Guia de Instalação - MRIT Orion

## Passo a Passo para Executar o Sistema

### 1. Pré-requisitos
Certifique-se de ter instalado:
- **Node.js 18+** ([Download aqui](https://nodejs.org/))
- **npm** (vem com o Node.js)
- **Git** ([Download aqui](https://git-scm.com/))

### 2. Configuração do Projeto

```bash
# 1. Clone o repositório (se ainda não fez)
git clone <url-do-repositorio>
cd mrit-orion

# 2. Instale as dependências
npm install

# 3. Execute o projeto
npm run dev
```

### 3. Configuração do Banco de Dados

O sistema já está configurado para usar o Supabase fornecido. Para configurar o banco:

1. **Acesse o Supabase Dashboard**
   - URL: https://base.muraltv.com.br
   - Use as credenciais fornecidas

2. **Execute o Schema SQL**
   - Vá para "SQL Editor" no dashboard
   - Copie todo o conteúdo do arquivo `database/schema.sql`
   - Cole e execute o script
   - Isso criará todas as tabelas e políticas necessárias

### 4. Primeiro Acesso

1. **Acesse a aplicação**
   - Abra [http://localhost:3000](http://localhost:3000)

2. **Crie sua conta**
   - Clique em "Não tem uma conta? Cadastre-se"
   - Preencha email, senha e nome
   - Confirme o email (se necessário)

3. **Adicione uma empresa**
   - No dashboard, adicione os dados da empresa
   - CNPJ, razão social, etc.

### 5. Testando o Sistema

1. **Upload de Extrato**
   - Vá para "Upload de Extratos"
   - Selecione a empresa
   - Faça upload de um arquivo OFX, CSV ou Excel
   - O sistema processará automaticamente

2. **Verificar Classificações**
   - As transações serão classificadas automaticamente
   - Revise as classificações no dashboard

## 🔧 Solução de Problemas

### Erro de Conexão com Supabase
- Verifique se a URL e chave estão corretas
- Confirme se o banco está acessível

### Erro de Upload de Arquivo
- Verifique se o arquivo está em formato suportado
- Tamanho máximo recomendado: 10MB

### Erro de Classificação
- Verifique se as regras estão configuradas
- Transações podem ficar como "pendentes" para revisão manual

## 📊 Estrutura de Arquivos

```
mrit-orion/
├── app/                    # Páginas Next.js
├── components/            # Componentes React
├── lib/                  # Utilitários e configurações
├── database/             # Scripts de banco
├── package.json          # Dependências
├── tailwind.config.js    # Configuração Tailwind
└── README.md            # Documentação principal
```

## 🚀 Comandos Disponíveis

```bash
# Desenvolvimento
npm run dev          # Executa em modo desenvolvimento

# Produção
npm run build        # Gera build de produção
npm run start        # Executa build de produção

# Linting
npm run lint         # Verifica código
```

## 📱 Acesso

- **URL Local**: http://localhost:3000
- **Supabase**: https://base.muraltv.com.br
- **Dashboard**: http://localhost:3000/dashboard
- **Upload**: http://localhost:3000/upload

## 🔐 Credenciais de Teste

Para testar rapidamente, você pode usar:
- **Email**: teste@exemplo.com
- **Senha**: 123456

*Nota: Crie sua própria conta para uso real*

## 📞 Suporte

Se encontrar problemas:
1. Verifique os logs no terminal
2. Consulte a documentação
3. Abra uma issue no GitHub

---

**Sistema pronto para uso!** 🎉

