# 🔧 Solução de Problemas - E-mail MRIT

## 🚨 **PROBLEMA: E-mail não está chegando**

### **Passo 1: Execute o Diagnóstico**
1. Acesse: `https://seudominio.com/test_email.php`
2. Execute todos os testes
3. Verifique os resultados

### **Passo 2: Verificações Básicas**

#### ✅ **1. Estrutura de Arquivos**
```
D:\SITE MRIT\
├── index.html
├── styles.css
├── script.js
├── send_email.php
├── test_email.php
├── send_email_alternative.php
└── PHPMailer/
    └── src/
        ├── Exception.php
        ├── PHPMailer.php
        └── SMTP.php
```

#### ✅ **2. Configurações SMTP da Hostgator**
- **Host:** `smtp.titan.email` OU `mail.mrit.com.br`
- **Porta:** `465` (SMTPS) OU `587` (STARTTLS)
- **Usuário:** `disparador@mrit.com.br`
- **Senha:** `ME2KC1B84HCB@`

### **Passo 3: Testes de Configuração**

#### **Teste 1: Configuração Original**
- Use `send_email.php` (porta 465, SMTPS)

#### **Teste 2: Configuração Alternativa**
- Renomeie `send_email_alternative.php` para `send_email.php`
- Use porta 587, STARTTLS

### **Passo 4: Possíveis Soluções**

#### **🔧 Solução 1: Verificar Credenciais**
1. Acesse o painel da Hostgator
2. Vá em "E-mail Accounts"
3. Confirme se `disparador@mrit.com.br` existe
4. Teste a senha

#### **🔧 Solução 2: Configurações SMTP**
```php
// Configuração 1 (Original)
$mail->Host = 'smtp.titan.email';
$mail->Port = 465;
$mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;

// Configuração 2 (Alternativa)
$mail->Host = 'mail.mrit.com.br';
$mail->Port = 587;
$mail->SMTPSecure = PHPMailer::ENCRYPTION_STARTTLS;
```

#### **🔧 Solução 3: Verificar Firewall**
- Hostgator pode estar bloqueando conexões SMTP
- Contate o suporte da Hostgator

#### **🔧 Solução 4: Usar E-mail do Servidor**
```php
// Configuração usando e-mail do próprio servidor
$mail->Host = 'localhost';
$mail->isMail(); // Usar função mail() do PHP
```

### **Passo 5: Testes Manuais**

#### **Teste A: Via cPanel**
1. Acesse cPanel da Hostgator
2. Vá em "E-mail Accounts"
3. Clique em "Webmail" para `disparador@mrit.com.br`
4. Envie um e-mail manual para `contato@mrit.com.br`

#### **Teste B: Via PHP mail()**
```php
// Teste simples com mail() nativo
$to = 'contato@mrit.com.br';
$subject = 'Teste MRIT';
$message = 'Teste de e-mail';
$headers = 'From: disparador@mrit.com.br';

if (mail($to, $subject, $message, $headers)) {
    echo "E-mail enviado com sucesso!";
} else {
    echo "Erro no envio!";
}
```

### **Passo 6: Alternativas**

#### **Opção 1: Formspree**
- Serviço online para formulários
- Não precisa de PHP/SMTP
- Gratuito até 50 envios/mês

#### **Opção 2: EmailJS**
- JavaScript puro
- Integração com Gmail/Outlook
- Fácil implementação

#### **Opção 3: Netlify Forms**
- Se hospedar no Netlify
- Formulários automáticos
- Sem configuração

### **Passo 7: Logs de Debug**

#### **Ativar Debug no PHPMailer:**
```php
$mail->SMTPDebug = 2; // 0 = off, 1 = client, 2 = client + server
$mail->Debugoutput = 'html';
```

#### **Verificar Logs do Servidor:**
- Acesse cPanel → "Error Logs"
- Procure por erros relacionados a e-mail

### **Passo 8: Contato com Suporte**

#### **Hostgator:**
- Ticket de suporte
- Pergunta: "SMTP não funciona na porta 465/587"
- Solicite: "Configurações SMTP corretas"

#### **Informações para o Suporte:**
- Domínio: mrit.com.br
- E-mail: disparador@mrit.com.br
- Porta testada: 465 e 587
- Erro: "E-mails não chegam"

### **🎯 Checklist Final:**
- [ ] PHPMailer instalado corretamente
- [ ] Credenciais SMTP corretas
- [ ] Porta 465 ou 587 funcionando
- [ ] Firewall não bloqueando
- [ ] E-mail de teste enviado
- [ ] Logs verificados
- [ ] Suporte contatado se necessário

### **📞 Contatos de Emergência:**
- **Hostgator:** Suporte técnico
- **E-mail alternativo:** Use outro provedor temporariamente
- **WhatsApp:** +55 19 9 7134-9642 (para contato direto)

---

**💡 Dica:** Se nada funcionar, considere usar um serviço de terceiros como Formspree ou EmailJS como solução temporária.
