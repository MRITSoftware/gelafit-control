# Sistema de Email - Barrella Eventos

## 📧 Configuração do Sistema de Email

### Arquivos Criados:
- `send_email.php` - Script principal de envio
- `config.php` - Configurações do sistema
- `.htaccess` - Configurações do servidor
- `email_log.txt` - Log de emails enviados (criado automaticamente)
- `security_log.txt` - Log de segurança (criado automaticamente)

### Configuração do Servidor:

1. **Upload dos arquivos** para o servidor web
2. **Configurar permissões**:
   ```bash
   chmod 644 send_email.php
   chmod 644 config.php
   chmod 644 .htaccess
   chmod 666 email_log.txt
   chmod 666 security_log.txt
   ```

3. **Configurar PHP** para envio de emails:
   - Verificar se a função `mail()` está habilitada
   - Configurar SMTP se necessário

### Configurações Personalizáveis:

No arquivo `config.php`:
```php
define('EMAIL_TO', 'contato.mrit@gmail.com');        // Email de destino
define('EMAIL_FROM', 'noreply@barrellaeventos.com.br'); // Email de origem
define('SITE_NAME', 'Barrella Eventos');             // Nome do site
```

### Funcionalidades:

✅ **Validação completa** de dados
✅ **Sanitização** de entrada
✅ **Logs automáticos** de envios
✅ **Logs de segurança**
✅ **Limpeza automática** de logs antigos
✅ **Headers de segurança**
✅ **CORS configurado**
✅ **Fallback para mailto** se PHP falhar

### Teste:

1. Acesse a landing page
2. Preencha o formulário
3. Clique em "Enviar"
4. Verifique se o email chegou em `contato.mrit@gmail.com`
5. Verifique os logs em `email_log.txt`

### Troubleshooting:

- **Email não enviado**: Verificar configuração do PHP mail()
- **Erro 500**: Verificar permissões dos arquivos
- **CORS error**: Verificar configuração do .htaccess
- **Logs**: Verificar `email_log.txt` e `security_log.txt`

### Segurança:

- ✅ Validação de entrada
- ✅ Sanitização de dados
- ✅ Headers de segurança
- ✅ Logs de tentativas suspeitas
- ✅ Limitação de tamanho de campos
- ✅ Rate limiting (pode ser implementado)

## 🚀 Pronto para Produção!

O sistema está configurado e pronto para uso em produção.
