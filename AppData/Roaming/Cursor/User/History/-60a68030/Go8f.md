# 🧪 Teste do Sistema de Email

## Passos para Testar:

### 1. **Verificar se o PHP está funcionando:**
Acesse: `http://seudominio.com/test_email.php`

### 2. **Testar envio direto:**
Acesse: `http://seudominio.com/test_email.php?test=send`

### 3. **Verificar logs do servidor:**
- Procure por logs de erro do PHP
- Verifique se há mensagens de "Email enviado com sucesso"

### 4. **Testar o formulário:**
- Acesse a landing page
- Preencha o formulário
- Abra o Console do navegador (F12)
- Veja as mensagens de debug

## 🔍 Debug no Console:

Quando você enviar o formulário, deve ver no console:
```
Enviando dados para send_email.php...
Resposta recebida: 200
Resultado: {success: true, message: "..."}
```

## ❌ Possíveis Problemas:

### 1. **Função mail() não configurada:**
- O servidor precisa ter SMTP configurado
- Contate o suporte do hosting

### 2. **Arquivo não encontrado:**
- Verifique se `send_email_simple.php` está na pasta correta
- Teste acessando diretamente: `http://seudominio.com/send_email_simple.php`

### 3. **Permissões:**
- O servidor precisa ter permissão para enviar emails
- Verifique com o suporte do hosting

### 4. **CORS:**
- Se estiver em localhost, pode ter problemas de CORS
- Teste em um servidor real

## 🚀 Solução Alternativa:

Se o PHP não funcionar, podemos usar:
1. **Formspree** (gratuito)
2. **EmailJS** (gratuito)
3. **Netlify Forms** (se hospedar no Netlify)

## 📞 Próximos Passos:

1. Teste o `test_email.php`
2. Me informe o resultado
3. Ajustaremos conforme necessário
