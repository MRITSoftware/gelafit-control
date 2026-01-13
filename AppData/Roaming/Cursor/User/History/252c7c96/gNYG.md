# Instruções para Testar o Envio de Email

## Problemas Identificados e Corrigidos

### 1. **Campo "Assunto" não estava sendo capturado corretamente**
- **Problema**: O formulário enviava `company` mas o PHP procurava por `service`
- **Solução**: Alterado para priorizar `company` e adicionado valor padrão

### 2. **Falta de logs de debug**
- **Problema**: Difícil identificar onde estava o erro
- **Solução**: Adicionado logs detalhados em `email_debug.log` e `email_errors.log`

### 3. **Validação muito restritiva**
- **Problema**: Campo assunto era obrigatório mesmo tendo valor padrão
- **Solução**: Removido validação obrigatória do assunto

## Como Testar

### Passo 1: Testar se PHP está funcionando
1. Acesse: `http://localhost/test_php_simple.php`
2. Deve retornar JSON com `"success": true`

### Passo 2: Testar envio simples
1. Acesse: `http://localhost/test_email_simple.php`
2. Use o formulário do site para enviar dados
3. Deve retornar sucesso mesmo sem servidor SMTP

### Passo 3: Testar envio real
1. Use o formulário do site normalmente
2. Verifique os logs em:
   - `email_debug.log` - Logs detalhados do SMTP
   - `email_errors.log` - Erros específicos

## Configuração SMTP

### Credenciais atuais (no código):
- **Host**: smtp.office365.com
- **Porta**: 587
- **Usuário**: disparador@barrellaeventos.com.br
- **Senha**: K7g@yzqZaW!JTdz

### Possíveis problemas:
1. **Autenticação**: Senha pode estar incorreta ou expirada
2. **Firewall**: Porta 587 pode estar bloqueada
3. **2FA**: Pode precisar de senha de aplicativo
4. **Permissões**: Conta pode não ter permissão para enviar emails

## Soluções Alternativas

### Se o SMTP não funcionar:
1. **Usar serviço de email externo** (SendGrid, Mailgun, etc.)
2. **Configurar servidor local** (XAMPP, WAMP)
3. **Usar função mail() do PHP** (se disponível)

### Para configurar outro SMTP:
1. Edite as variáveis no `send_email.php`:
   ```php
   $smtpUser = 'seu-email@exemplo.com';
   $smtpPass = 'sua-senha';
   ```

## Arquivos Criados/Modificados

### Novos arquivos:
- `test_php_simple.php` - Teste básico do PHP
- `test_email_simple.php` - Teste de envio sem SMTP
- `INSTRUCOES_TESTE_EMAIL.md` - Este arquivo

### Arquivos modificados:
- `send_email.php` - Corrigido mapeamento de campos e adicionado logs
- `script.js` - Melhorado tratamento de erros

## Próximos Passos

1. **Teste os arquivos de teste** para verificar se PHP funciona
2. **Verifique os logs** para identificar erros específicos
3. **Configure SMTP correto** se necessário
4. **Teste o formulário** no site

## Logs de Debug

Os logs são salvos em:
- `email_debug.log` - Logs detalhados do PHPMailer
- `email_errors.log` - Erros específicos com dados do formulário

Verifique estes arquivos para identificar problemas específicos.
