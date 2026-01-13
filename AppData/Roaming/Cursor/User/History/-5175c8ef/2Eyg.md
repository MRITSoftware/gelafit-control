# Configuração do E-mail - MRIT

## 📧 Para o formulário funcionar, você precisa:

### 1. Baixar o PHPMailer
- Acesse: https://github.com/PHPMailer/PHPMailer
- Baixe o arquivo ZIP
- Extraia a pasta `PHPMailer-master` na raiz do seu site
- A estrutura deve ficar assim:
```
/
├── index.html
├── styles.css
├── script.js
├── send_email.php
└── PHPMailer-master/
    ├── src/
    │   ├── Exception.php
    │   ├── PHPMailer.php
    │   └── SMTP.php
    └── ...
```

### 2. Verificar as configurações no send_email.php
- **SMTP Host:** smtp.titan.email
- **Porta:** 465
- **Usuário:** disparador@mrit.com.br
- **Senha:** ME2KC1B84HCB@
- **E-mail de destino:** contato@mrit.com.br

### 3. Testar o formulário
1. Preencha todos os campos obrigatórios
2. Clique em "Enviar Solicitação"
3. Você será redirecionado para uma página de sucesso
4. O e-mail será enviado para contato@mrit.com.br

### 4. Campos do formulário
- **nome** → Nome do cliente
- **email** → E-mail do cliente
- **phone** → Telefone (opcional)
- **assunto** → Tipo de serviço selecionado
- **mensagem** → Descrição do projeto

### 5. Validações
- Nome, e-mail e assunto são obrigatórios
- E-mail deve ter formato válido
- JavaScript valida antes do envio
- PHP processa e envia o e-mail

## ✅ Status
- [x] HTML atualizado com action="send_email.php"
- [x] Campos renomeados para corresponder ao PHP
- [x] JavaScript ajustado para validação
- [x] PHP criado com suas configurações
- [ ] PHPMailer baixado e instalado
- [ ] Teste de envio realizado
