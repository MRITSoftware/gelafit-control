# Sistema de Assinaturas Digitais - MRIT

Sistema completo de assinaturas digitais com integração ao Mercado Pago para pagamentos via PIX.

## 📋 Requisitos

- PHP 7.4 ou superior (com SQLite habilitado)
- Servidor web (Apache/Nginx) - **Compatível com Hostinger**
- Extensões PHP: PDO, SQLite, cURL, JSON

## 🚀 Instalação na Hostinger

### 1. Upload dos Arquivos

1. Faça upload de todos os arquivos para a raiz do seu site na Hostinger
2. Certifique-se de que os arquivos PHP têm permissão de leitura/escrita

### 2. Configuração Automática

O sistema usa **SQLite** (banco de dados em arquivo), então:
- ✅ **NÃO precisa configurar MySQL**
- ✅ **NÃO precisa importar SQL**
- ✅ O banco é criado automaticamente na primeira execução
- ✅ Tudo funciona automaticamente!

### 3. Permissões

Certifique-se de que o servidor pode criar/ler os seguintes arquivos e pastas:
- `database.db` (será criado automaticamente)
- `uploads/` (será criado automaticamente)
- `signed_documents/` (será criado automaticamente)

Na Hostinger, geralmente as permissões já estão corretas por padrão.

### 4. Configuração Manual (Opcional)

Se precisar ajustar algo, edite o arquivo `config.php`:
- Token do Mercado Pago (já configurado)
- Caminhos dos diretórios
- Valores de assinaturas gratuitas

## 📁 Estrutura de Arquivos

```
/
├── api/
│   ├── auth.php              # Autenticação/Registro
│   ├── upload.php            # Upload de documentos
│   ├── check_payment.php     # Verificação de pagamento
│   ├── dashboard.php         # API do dashboard
│   ├── download.php          # Download de documentos
│   ├── mercado_pago.php      # Integração Mercado Pago
│   └── pdf_processor.php     # Processamento de PDF
├── assinatura.html           # Página de assinatura
├── assinatura.js             # Script da página de assinatura
├── dashboard.html            # Dashboard administrativo
├── dashboard.js              # Script do dashboard
├── config.php                # Configurações (SQLite)
├── database.db               # Banco SQLite (criado automaticamente)
├── .htaccess                 # Configurações Apache
├── uploads/                  # Arquivos enviados (criado automaticamente)
└── signed_documents/         # Documentos assinados (criado automaticamente)
```

## 🎯 Funcionalidades

### Para Clientes:

1. **Login/Registro por CPF/CNPJ**
   - Validação automática de CPF/CNPJ
   - Registro básico (CPF/CNPJ + Email)

2. **Upload de Documento**
   - Suporte para PDF, DOC, DOCX
   - Drag and drop ou seleção de arquivo

3. **Configuração de Assinaturas**
   - Adicionar múltiplas assinaturas
   - Campos: Nome, Data, Local

4. **Sistema de Pagamento**
   - Primeira assinatura gratuita
   - Assinaturas adicionais: R$ 5,00 cada
   - Pagamento via PIX (Mercado Pago)
   - QR Code gerado automaticamente

5. **Download do Documento**
   - Documento original + página de assinaturas
   - Download após pagamento aprovado

### Para Administrador:

**Acesso**: CPF `449.669.918-46`

1. **Dashboard Completo**
   - Estatísticas gerais
   - Lista de clientes
   - Total de assinaturas
   - Total de pagamentos

2. **Gerenciamento de Clientes**
   - Criar novos clientes
   - Editar informações
   - Excluir clientes
   - Liberar assinaturas gratuitas

## 🔐 Autenticação Admin

Para acessar o dashboard administrativo:

1. Acesse: `assinatura.html`
2. Digite o CPF: `449.669.918-46`
3. Você será redirecionado automaticamente para o dashboard

Ou acesse diretamente: `dashboard.html?cpf=44966991846`

## 💳 Mercado Pago

O sistema está configurado com o Access Token fornecido:

```
APP_USR-3472086984963081-053123-9b1a4a027169af10a5ebd9940a75d7ee-208507556
```

### Como funciona:

1. Cliente faz upload do documento
2. Configura assinaturas
3. Sistema verifica assinaturas gratuitas disponíveis
4. Para assinaturas pagas, cria pagamento no Mercado Pago
5. Gera QR Code PIX
6. Cliente paga via PIX
7. Sistema verifica status do pagamento automaticamente
8. Ao ser aprovado, processa o documento e disponibiliza download

## 📊 Banco de Dados (SQLite)

O sistema usa **SQLite**, um banco de dados em arquivo:
- ✅ Criado automaticamente
- ✅ Não precisa de servidor MySQL separado
- ✅ Funciona em qualquer hospedagem com PHP
- ✅ Arquivo: `database.db` na raiz

### Tabelas:

- **clientes**: Dados dos clientes
- **documentos**: Documentos enviados
- **assinaturas**: Dados das assinaturas
- **pagamentos**: Registros de pagamento

## 🎨 Interface

O sistema usa o mesmo design da página principal MRIT, mantendo consistência visual e profissional.

## ⚠️ Observações Importantes

1. **Segurança**: 
   - Em produção, implemente sistema de sessão
   - Valide tokens CSRF
   - Use HTTPS (Hostinger geralmente já fornece)
   - Sanitize todas as entradas

2. **Upload de Arquivos**:
   - Limite de tamanho configurado no `.htaccess`
   - Valide tipos de arquivo
   - Considere usar biblioteca de PDF mais avançada

3. **Mercado Pago**:
   - O token fornecido é para ambiente de produção
   - Configure webhooks para notificações de pagamento
   - Implemente retry logic para verificação de pagamento

4. **Processamento de PDF**:
   - Atualmente usa método básico
   - Para produção, considere usar biblioteca como TCPDF ou FPDF
   - Implemente conversão DOC/DOCX para PDF

5. **Backup do Banco**:
   - Faça backup regular do arquivo `database.db`
   - É importante ter cópias de segurança

## 🔧 Melhorias Futuras

- [ ] Sistema de sessão completo
- [ ] Webhooks do Mercado Pago
- [ ] Biblioteca avançada de PDF
- [ ] Conversão DOC/DOCX para PDF
- [ ] Assinatura digital real com certificado
- [ ] Histórico completo de documentos
- [ ] Notificações por email
- [ ] Dashboard com gráficos
- [ ] Sistema de backup automático

## 🆘 Solução de Problemas

### Banco de dados não é criado:
- Verifique permissões da pasta raiz (deve ser 755 ou 777)
- Verifique se PHP tem SQLite habilitado

### Upload não funciona:
- Verifique permissões da pasta `uploads/`
- Verifique limite de upload no `.htaccess`

### Erro 500:
- Verifique logs de erro do PHP
- Verifique se todas as extensões PHP estão habilitadas

## 📞 Suporte

Para dúvidas ou problemas, entre em contato:
- Email: contato@mrit.com.br
- WhatsApp: +55 19 9 7134-9642
