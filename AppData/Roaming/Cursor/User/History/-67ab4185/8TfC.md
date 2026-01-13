# 🚀 Instalação Rápida - Sistema de Assinaturas MRIT

## Para Hostinger (ou qualquer hospedagem PHP)

### Passo 1: Upload dos Arquivos
1. Faça upload de **TODOS** os arquivos para a raiz do seu site
2. Não precisa configurar MySQL ou importar SQL

### Passo 2: Acesse o Sistema
1. Acesse: `seudominio.com.br/assinatura.html`
2. O banco de dados será criado automaticamente na primeira execução
3. As pastas necessárias serão criadas automaticamente

### Passo 3: Testar
1. **Como cliente**: Digite um CPF válido para testar o cadastro
2. **Como admin**: Digite CPF `449.669.918-46` para acessar o dashboard

## ✅ Pronto!

O sistema usa **SQLite** (banco em arquivo), então:
- ✅ Não precisa MySQL
- ✅ Não precisa importar SQL
- ✅ Tudo funciona automaticamente

## 🔧 Configurações (Opcional)

Se precisar ajustar algo, edite `config.php`:
- Token do Mercado Pago (já configurado)
- Caminhos dos diretórios
- Valores de assinaturas gratuitas

## 📝 Arquivos Importantes

- `config.php` - Configurações
- `database.db` - Banco de dados (criado automaticamente)
- `uploads/` - Arquivos enviados (criado automaticamente)
- `signed_documents/` - Documentos assinados (criado automaticamente)

## 🆘 Problemas?

1. Verifique se PHP tem SQLite habilitado (geralmente já vem)
2. Verifique permissões das pastas (755 ou 777)
3. Verifique logs de erro do PHP no painel da Hostinger

Pronto para usar! 🎉

