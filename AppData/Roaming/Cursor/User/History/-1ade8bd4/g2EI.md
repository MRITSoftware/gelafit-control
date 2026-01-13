# Sistema de Assinatura Digital - Versão PHP

Versão PHP do sistema de assinatura digital, otimizada para hospedagem compartilhada (Hostgator).

## 🚀 Instalação na Hostgator

### 1. Requisitos
- PHP 7.4 ou superior
- MySQL 5.7 ou superior
- Composer (pode instalar via SSH ou usar arquivos já compilados)

### 2. Upload dos Arquivos

1. Faça upload de todos os arquivos da pasta `php-version/` para o servidor via FTP/FileZilla
2. Certifique-se de que a estrutura de pastas está preservada

### 3. Configuração

#### 3.1. Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
APP_ENV=production
APP_URL=https://seudominio.com.br
DB_HOST=localhost
DB_NAME=seu_banco
DB_USER=seu_usuario
DB_PASS=sua_senha
JWT_SECRET=sua-chave-secreta-jwt-muito-segura
MP_ACCESS_TOKEN=seu-token-mercadopago
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=seu-email@gmail.com
SMTP_PASS=sua-senha-app
```

#### 3.2. Instalar Dependências

**Opção 1: Via SSH (recomendado)**
```bash
cd public_html
composer install --no-dev --optimize-autoloader
```

**Opção 2: Upload manual**
- Baixe as dependências localmente com `composer install`
- Faça upload da pasta `vendor/` completa

#### 3.3. Permissões

```bash
chmod 755 uploads/
chmod 644 .htaccess
```

### 4. Configuração do Banco de Dados

1. Crie o banco de dados MySQL no cPanel
2. As tabelas serão criadas automaticamente na primeira execução
3. Ou execute manualmente o SQL em `database.sql` (se fornecido)

### 5. Configuração do .htaccess

O arquivo `.htaccess` já está configurado. Se não funcionar, verifique se:
- Mod_rewrite está habilitado
- O arquivo está na raiz correta

## 📁 Estrutura de Pastas

```
php-version/
├── api/              # Endpoints da API
├── config/           # Configurações
├── src/              # Classes PHP
├── uploads/          # Arquivos enviados (permissão 755)
├── vendor/           # Dependências Composer
├── assets/           # CSS, JS, imagens
├── index.php         # Ponto de entrada
├── login.php         # Página de login
├── dashboard.php     # Dashboard do cliente
├── .htaccess         # Configuração Apache
├── composer.json     # Dependências
└── .env              # Variáveis de ambiente
```

## 🔧 Funcionalidades

✅ Autenticação por CPF
✅ Upload e visualização de PDFs
✅ Assinatura digital com canvas
✅ Sistema de créditos
✅ Integração MercadoPago
✅ Assinaturas múltiplas com códigos
✅ Painel administrativo
✅ Verificação de autenticidade

## 🎨 Frontend

O frontend usa:
- **Tailwind CSS** (via CDN)
- **JavaScript vanilla** (sem frameworks)
- **HTML5 Canvas** para assinaturas
- **PDF.js** para visualização de PDFs

## 📝 Notas

- Mantém a mesma aparência visual do sistema Next.js
- Compatível com hospedagem compartilhada
- Não requer Node.js ou build process
- Funciona com PHP 7.4+ (compatível com Hostgator)

## 🆘 Troubleshooting

### Erro 500
- Verifique permissões dos arquivos
- Verifique logs de erro do PHP
- Confirme que todas as dependências estão instaladas

### Banco de dados não conecta
- Verifique credenciais no `.env`
- Confirme que o MySQL está ativo
- Teste conexão via phpMyAdmin

### Uploads não funcionam
- Verifique permissões da pasta `uploads/` (755)
- Verifique `upload_max_filesize` no PHP
- Verifique espaço em disco

## 📞 Suporte

Para dúvidas ou problemas, consulte a documentação original ou entre em contato.
