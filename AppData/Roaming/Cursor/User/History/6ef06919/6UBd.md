# Dashboard Admin - Sistema de Assinaturas MRIT

## 🔐 Acesso

**CPF Administrador**: `449.669.918-46`

Para acessar o dashboard:
1. Acesse: `assinatura.html`
2. Digite o CPF: `449.669.918-46`
3. Você será redirecionado automaticamente para o dashboard

Ou acesse diretamente: `dashboard.html?cpf=44966991846`

## 🎯 Funcionalidades

### Visualização de Estatísticas

- **Total de Clientes**: Conta todos os clientes cadastrados (exceto admin)
- **Total de Assinaturas**: Total de assinaturas realizadas
- **Total de Pagamentos**: Soma de todos os pagamentos aprovados
- **Total de Documentos**: Documentos processados

### Gerenciamento de Clientes

1. **Lista de Clientes**
   - CPF/CNPJ formatado
   - Nome e e-mail
   - Assinaturas realizadas
   - Assinaturas gratuitas disponíveis
   - Total pago

2. **Criar Cliente**
   - Clique em "Novo Cliente"
   - Preencha CPF/CNPJ, e-mail e nome
   - Defina quantas assinaturas gratuitas disponibilizar
   - Salvar

3. **Editar Cliente**
   - Clique em "Editar" na linha do cliente
   - Altere os dados necessários
   - Libere mais assinaturas gratuitas se necessário
   - Salvar

4. **Excluir Cliente**
   - Clique em "Excluir" na linha do cliente
   - Confirme a exclusão
   - Cliente e dados relacionados serão removidos

## 🔧 Solução de Problemas

### Dashboard não mostra clientes

1. Verifique o console do navegador (F12)
2. Verifique se o arquivo `database.db` existe na raiz
3. Verifique se há erros no console do PHP
4. Teste criando um cliente manualmente

### Erro ao criar cliente

1. Verifique se CPF/CNPJ é válido
2. Verifique se e-mail é válido
3. Verifique se CPF/CNPJ já não está cadastrado
4. Veja o console para detalhes do erro

### Erro ao excluir cliente

1. Verifique se o cliente não é o administrador
2. Verifique se o ID é válido
3. Veja o console para detalhes do erro

## 📊 Banco de Dados

O dashboard usa SQLite (`database.db`):
- Criado automaticamente na primeira execução
- Não precisa configurar MySQL
- Funciona na Hostinger

## 🚀 Testar na Hostinger

1. Faça upload de todos os arquivos
2. Acesse o dashboard pelo domínio
3. O banco será criado automaticamente
4. Teste criando um cliente

