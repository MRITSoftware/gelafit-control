# 🔧 Problema com Microsoft/Hotmail - Solução

## ❌ **Problema Identificado:**
O Microsoft/Hotmail tem políticas muito restritivas de envio de email, especialmente com:
- Porta 465 (SMTPS) - pode ser bloqueada
- Certificados SSL não reconhecidos
- Timeout muito baixo

## ✅ **Soluções Aplicadas:**

### 1. **Mudança de Porta e Protocolo:**
- ❌ Antes: Porta 465 + SMTPS
- ✅ Agora: Porta 587 + STARTTLS

### 2. **Configurações SSL Flexíveis:**
```php
$mail->SMTPOptions = array(
    'ssl' => array(
        'verify_peer' => false,
        'verify_peer_name' => false,
        'allow_self_signed' => true
    )
);
```

### 3. **Timeout Aumentado:**
- De 30s para 60s (provedores restritivos são lentos)

### 4. **Debug Habilitado:**
- Logs detalhados em `email_debug.log`
- Erro técnico na resposta JSON

## 🧪 **Como Testar:**

1. **Suba os arquivos** para o servidor HostGator
2. **Teste o formulário** no site
3. **Verifique os logs:**
   - `email_debug.log` - debug SMTP completo
   - `email_smtp_debug.log` - erros de exceção
4. **Veja a resposta JSON** - campo `error` com detalhes

## 📧 **Possíveis Problemas Adicionais:**

### **Se ainda não funcionar:**

1. **Verificar DNS do domínio:**
   - SPF: `v=spf1 include:spf.titan.email ~all`
   - DKIM: configurado no painel Titan
   - DMARC: `v=DMARC1; p=quarantine; rua=mailto:dmarc@barrellaeventos.com.br`

2. **Verificar credenciais Titan:**
   - Usuário: `disparador@barrellaeventos.com.br`
   - Senha: `xP9KD7M62RTQ@`
   - Testar login no painel webmail

3. **Verificar logs do servidor:**
   - cPanel > Error Logs
   - Procurar por erros PHP/SMTP

## 🔄 **Alternativas se persistir:**

### **Opção 1: Usar SMTP do HostGator**
```php
$mail->Host = 'mail.barrellaeventos.com.br';
$mail->Username = 'disparador@barrellaeventos.com.br';
$mail->Password = 'sua_senha_hostgator';
```

### **Opção 2: Usar SendGrid/Mailgun**
- Serviços especializados em deliverability
- Melhor compatibilidade com Microsoft

### **Opção 3: Usar função mail() nativa**
- Mais simples, mas menos controle
- Pode ter limitações de spam

## 📋 **Checklist de Verificação:**

- [ ] Arquivos subidos para HostGator
- [ ] PHP 7.4+ ativo
- [ ] Extensão openssl habilitada
- [ ] DNS SPF/DKIM configurado
- [ ] Credenciais Titan válidas
- [ ] Logs sendo gerados
- [ ] Teste realizado

## 🚨 **Se precisar de ajuda:**

1. **Me envie o conteúdo de `email_debug.log`**
2. **Me envie a resposta JSON com erro**
3. **Me confirme se o DNS está configurado**

---

**🎯 A mudança para porta 587 + STARTTLS deve resolver o problema com Microsoft/Hotmail!**
