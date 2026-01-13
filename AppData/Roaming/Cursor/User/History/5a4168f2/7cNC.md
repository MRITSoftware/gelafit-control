# Sistema de Assinaturas Digitais - MRIT

Sistema completo de assinaturas digitais com integração ao Mercado Pago para pagamentos via PIX.

## 📋 Requisitos

- PHP 7.4 ou superior
- MySQL 5.7 ou superior
- Extensões PHP: PDO, cURL, JSON
- Servidor web (Apache/Nginx)

## 🚀 Instalação

### 1. Banco de Dados

Execute o arquivo `database.sql` no seu banco de dados MySQL:

```bash
mysql -u seu_usuario -p < database.sql
```

Ou importe pelo phpMyAdmin.

### 2. Configuração

Edite o arquivo `config.php` e configure:

- **Banco de dados**: DB_HOST, DB_USER, DB_PASS, DB_NAME
- **Mercado Pago**: MP_ACCESS_TOKEN (já configurado)
- **Diretórios**: Verifique os caminhos dos diretórios de upload

### 3. Permissões

Certifique-se de que os diretórios têm permissões de escrita:

```bash
chmod 777 uploads/
chmod 777 signed_documents/
```

Ou crie os diretórios manualmente:

```bash
mkdir uploads
mkdir signed_documents
```

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
│   └── pdf_processor.php      # Processamento de PDF
├── assinatura.html           # Página de assinatura
├── assinatura.js             # Script da página de assinatura
├── dashboard.html            # Dashboard administrativo
├── dashboard.js              # Script do dashboard
├── config.php                # Configurações
├── database.sql              # Estrutura do banco
└── uploads/                  # Arquivos enviados
└── signed_documents/         # Documentos assinados
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

Para acessar o dashboard administrativo, use o CPF: `449.669.918-46`

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

## 📊 Banco de Dados

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
   - Use HTTPS
   - Sanitize todas as entradas

2. **Upload de Arquivos**:
   - Configure limite de tamanho no PHP (`upload_max_filesize`)
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

## 🔧 Melhorias Futuras

- [ ] Sistema de sessão completo
- [ ] Webhooks do Mercado Pago
- [ ] Biblioteca avançada de PDF
- [ ] Conversão DOC/DOCX para PDF
- [ ] Assinatura digital real com certificado
- [ ] Histórico completo de documentos
- [ ] Notificações por email
- [ ] Dashboard com gráficos

## 📞 Suporte

Para dúvidas ou problemas, entre em contato:
- Email: contato@mrit.com.br
- WhatsApp: +55 19 9 7134-9642

