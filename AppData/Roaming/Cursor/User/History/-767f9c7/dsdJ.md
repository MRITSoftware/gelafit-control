# 🚀 Guia de Instalação - PHP Version

## Passo a Passo para Hostgator

### 1. Preparação Local (Opcional mas Recomendado)

```bash
# Clonar/navegar para a pasta php-version
cd php-version

# Instalar dependências
composer install

# Ou, se não tiver composer, baixar vendor/ manualmente
```

### 2. Upload para Hostgator

#### Via FileZilla/WinSCP:

1. **Conectar ao servidor**
   - Host: ftp.seudominio.com.br
   - Usuário: seu_usuario
   - Senha: sua_senha
   - Porta: 21

2. **Navegar para public_html/**
   - Ou htdocs/ dependendo da configuração

3. **Upload de arquivos**
   - Faça upload de TODA a pasta `php-version/`
   - Mantenha a estrutura de pastas
   - Certifique-se de que `.htaccess` foi enviado

### 3. Configuração no Servidor

#### 3.1. Criar arquivo `.env`

No servidor, crie o arquivo `.env` na raiz (mesmo nível do index.php):

```env
APP_ENV=production
APP_URL=https://seudominio.com.br
DB_HOST=localhost
DB_NAME=seu_banco
DB_USER=seu_usuario_mysql
DB_PASS=sua_senha_mysql
JWT_SECRET=uma-chave-secreta-muito-longa-e-aleatoria-aqui
MP_ACCESS_TOKEN=seu-token-mercadopago
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=seu-email@gmail.com
SMTP_PASS=sua-senha-app
```

**⚠️ IMPORTANTE:**
- O arquivo `.env` deve ter permissão 644
- Não compartilhe o `.env` publicamente
- Use uma chave JWT forte e aleatória

#### 3.2. Configurar Permissões

Via SSH (se tiver acesso) ou FileZilla:

```bash
chmod 755 uploads/
chmod 644 .htaccess
chmod 644 .env
```

#### 3.3. Criar Banco de Dados

1. Acesse cPanel → MySQL Databases
2. Crie um novo banco de dados
3. Crie um usuário e senha
4. Adicione o usuário ao banco com todos os privilégios
5. As tabelas serão criadas automaticamente na primeira execução

### 4. Instalar Dependências PHP

#### Opção 1: Via SSH (Recomendado)

```bash
cd public_html/php-version
composer install --no-dev --optimize-autoloader
```

#### Opção 2: Upload Manual

1. No seu computador local:
   ```bash
   composer install
   ```
2. Faça upload da pasta `vendor/` completa para o servidor

### 5. Testar Instalação

1. Acesse: `https://seudominio.com.br/php-version/login.php`
2. Se aparecer erro, verifique:
   - Logs de erro do PHP (cPanel → Error Log)
   - Permissões dos arquivos
   - Se o `.env` está configurado corretamente
   - Se o banco de dados está acessível

### 6. Configurações Adicionais

#### 6.1. PHP Settings (via .htaccess ou php.ini)

O arquivo `.htaccess` já inclui:
```apache
php_value upload_max_filesize 10M
php_value post_max_size 10M
php_value max_execution_time 300
php_value memory_limit 256M
```

#### 6.2. SSL (Recomendado)

Certifique-se de que seu site tem SSL ativo (HTTPS)

## ✅ Checklist de Verificação

- [ ] Arquivos enviados para o servidor
- [ ] `.env` configurado com dados corretos
- [ ] Banco de dados criado e acessível
- [ ] Permissões configuradas (uploads/ = 755)
- [ ] Dependências instaladas (vendor/ presente)
- [ ] `.htaccess` no lugar correto
- [ ] SSL ativo (HTTPS)
- [ ] Site acessível sem erros

## 🐛 Troubleshooting

### Erro 500 Internal Server Error
- Verifique logs de erro no cPanel
- Verifique se todas as dependências estão instaladas
- Confirme que o PHP está na versão 7.4+

### Erro de Conexão com Banco
- Verifique credenciais no `.env`
- Teste conexão via phpMyAdmin
- Confirme que o usuário tem permissões

### Uploads não funcionam
- Verifique permissões da pasta `uploads/` (755)
- Verifique `upload_max_filesize` no PHP
- Verifique espaço em disco

### Página em branco
- Verifique logs de erro
- Verifique se todas as classes estão sendo carregadas
- Confirme que o autoload.php está funcionando

## 📞 Próximos Passos

Após a instalação:
1. Teste o login
2. Faça upload de um PDF
3. Teste a assinatura
4. Verifique integração com MercadoPago (se configurado)

## 🔒 Segurança

- Mantenha o `.env` privado
- Use senhas fortes
- Mantenha o PHP atualizado
- Faça backups regulares
- Use HTTPS sempre
