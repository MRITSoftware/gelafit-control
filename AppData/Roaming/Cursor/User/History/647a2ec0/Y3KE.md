# 🚀 Como Executar o MRIT Orion

## ⚡ Execução Rápida

```bash
# 1. Instalar dependências
npm install

# 2. Executar o projeto
npm run dev

# 3. Acessar no navegador
# http://localhost:3000
```

## 📋 Passo a Passo Detalhado

### 1. Verificar Pré-requisitos
```bash
# Verificar versão do Node.js (deve ser 18+)
node --version

# Verificar versão do npm
npm --version
```

### 2. Configurar o Banco de Dados
1. Acesse: https://base.muraltv.com.br
2. Vá para "SQL Editor"
3. Execute o script em `database/schema.sql`
4. Aguarde a confirmação de sucesso

### 3. Executar a Aplicação
```bash
# Instalar dependências
npm install

# Executar em modo desenvolvimento
npm run dev

# Ou executar o script de setup
npm run setup
```

### 4. Acessar o Sistema
- **URL**: http://localhost:3000
- **Dashboard**: http://localhost:3000/dashboard
- **Upload**: http://localhost:3000/upload

## 🔧 Comandos Disponíveis

```bash
# Desenvolvimento
npm run dev          # Executa em modo desenvolvimento (recomendado)

# Produção
npm run build        # Gera build otimizado
npm run start        # Executa build de produção

# Utilitários
npm run lint         # Verifica código
npm run setup        # Verifica configuração
```

## 🧪 Testando o Sistema

### 1. Criar Conta
1. Acesse http://localhost:3000
2. Clique em "Não tem uma conta? Cadastre-se"
3. Preencha: email, senha, nome
4. Faça login

### 2. Adicionar Empresa
1. No dashboard, adicione uma empresa
2. Preencha: CNPJ, razão social
3. Salve

### 3. Upload de Extrato
1. Vá para "Upload de Extratos"
2. Selecione a empresa
3. Faça upload de um arquivo de exemplo:
   - `exemplos/extrato_exemplo.csv`
   - `exemplos/extrato_exemplo.ofx`
   - `exemplos/extrato_exemplo.xlsx`

### 4. Verificar Resultados
1. As transações serão processadas automaticamente
2. Verifique as classificações no dashboard
3. Ajuste conforme necessário

## 📁 Estrutura de Arquivos

```
mrit-orion/
├── app/                    # Páginas Next.js
│   ├── dashboard/         # Dashboard principal
│   ├── upload/           # Upload de extratos
│   └── page.tsx          # Página de login
├── components/           # Componentes React
├── lib/                 # Utilitários
├── database/            # Scripts SQL
├── exemplos/            # Arquivos de exemplo
├── scripts/             # Scripts de setup
└── README.md           # Documentação
```

## 🔍 Solução de Problemas

### Erro: "Cannot find module"
```bash
# Reinstalar dependências
rm -rf node_modules package-lock.json
npm install
```

### Erro: "Supabase connection failed"
- Verifique se a URL está correta
- Confirme se o banco está acessível
- Execute o schema SQL novamente

### Erro: "File upload failed"
- Verifique se o arquivo está em formato suportado
- Tamanho máximo: 10MB
- Formatos: OFX, CSV, Excel

### Erro: "Classification failed"
- Verifique se as regras estão configuradas
- Transações podem ficar como "pendentes"
- Revise manualmente se necessário

## 📊 Monitoramento

### Logs do Sistema
```bash
# Ver logs em tempo real
npm run dev

# Logs aparecem no terminal
# Procure por erros ou avisos
```

### Verificar Banco de Dados
1. Acesse o Supabase Dashboard
2. Vá para "Table Editor"
3. Verifique as tabelas:
   - `empresas`
   - `extratos`
   - `transacoes`

## 🚀 Deploy em Produção

### Build de Produção
```bash
# Gerar build otimizado
npm run build

# Executar em produção
npm run start
```

### Variáveis de Ambiente
Crie um arquivo `.env.local`:
```env
NEXT_PUBLIC_SUPABASE_URL=https://base.muraltv.com.br
NEXT_PUBLIC_SUPABASE_ANON_KEY=sua_chave_aqui
```

## 📞 Suporte

Se encontrar problemas:
1. Verifique os logs no terminal
2. Consulte a documentação
3. Verifique se todas as dependências estão instaladas
4. Confirme se o banco está configurado

---

**Sistema executando com sucesso!** 🎉

