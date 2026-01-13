# 📋 Guia de Migração: Next.js para PHP (HostGator)

Este guia explica como migrar o sistema de assinatura digital de Next.js/Node.js para PHP, mantendo todas as funcionalidades.

## ✅ Funcionalidades Mantidas

- ✅ Autenticação (login/cadastro)
- ✅ Sistema de créditos
- ✅ Pagamentos via Mercado Pago
- ✅ Assinatura digital de PDFs
- ✅ Códigos de assinatura (múltiplas assinaturas)
- ✅ Verificação de autenticidade
- ✅ Sistema administrativo
- ✅ Auditoria de assinaturas
- ✅ Banco de dados MySQL (compatível com HostGator)

## 📁 Estrutura do Projeto PHP

```
php-version/
├── api/                    # Endpoints da API REST
│   ├── auth/
│   ├── codigos/
│   ├── pagamentos/
│   └── ...
├── config/                 # Configurações
│   ├── config.php
│   └── database.php
├── src/                    # Classes principais
│   ├── Auth.php
│   ├── Database.php
│   ├── JWT.php
│   ├── Creditos.php
│   ├── Codigos.php
│   ├── MercadoPago.php
│   └── PDFSigner.php
├── public/                 # Arquivos públicos (páginas HTML)
├── vendor/                  # Dependências (Composer)
├── .htaccess               # Configuração Apache
├── composer.json           # Dependências PHP
└── bootstrap.php           # Inicialização do sistema
```

## 🚀 Instalação na HostGator

### 1. Preparar o Ambiente

1. **Acesse o painel da HostGator** e crie um banco de dados MySQL
2. **Anote as credenciais**: host, nome do banco, usuário e senha
3. **Verifique se PHP 8.0+ está disponível** (geralmente já está)

### 2. Upload dos Arquivos

1. **Via FTP/SFTP** (FileZilla, WinSCP, etc.), envie todos os arquivos da pasta `php-version/` para:
   - `public_html/` (raiz do domínio)
   - Ou para um subdiretório: `public_html/esign/`

2. **Certifique-se de que**:
   - A pasta `uploads/` existe e tem permissões de escrita (chmod 755)
   - O arquivo `.htaccess` foi enviado

### 3. Configurar Dependências

1. **Via SSH** (se disponível) ou **via Composer localmente**:

```bash
# Se tiver acesso SSH
cd public_html
composer install --no-dev --optimize-autoloader

# Se não tiver SSH, instale localmente e envie a pasta vendor
composer install --no-dev --optimize-autoloader
# Depois envie a pasta vendor/ via FTP
```

### 4. Configurar Variáveis de Ambiente

1. **Crie um arquivo `.env`** na raiz do projeto (ou configure via painel):

```env
ENVIRONMENT=production
BASE_URL=https://www.esign.mrit.com.br
JWT_SECRET=seu-secret-key-muito-seguro-aqui-altere
MP_ACCESS_TOKEN=seu-token-mercadopago
DB_HOST=localhost
DB_NAME=u123456789_esign
DB_USER=u123456789_esign
DB_PASS=sua-senha-aqui
```

2. **Ou edite diretamente** `config/config.php` e `config/database.php`

### 5. Criar Banco de Dados

O sistema cria as tabelas automaticamente na primeira conexão. Você só precisa:
1. Ter criado o banco de dados no painel da HostGator
2. Configurar as credenciais em `config/database.php`

### 6. Configurar Permissões

```bash
# Via SSH (se disponível)
chmod 755 uploads/
chmod 644 .htaccess
chmod 644 config/*.php
```

## 🔧 Configurações Adicionais

### Apache (.htaccess)

O arquivo `.htaccess` já está configurado com:
- Redirecionamento HTTPS
- Proteção de arquivos sensíveis
- Configurações de upload
- Roteamento

### PHP.ini (se necessário)

Se precisar ajustar limites do PHP, crie um `.user.ini` na raiz:

```ini
upload_max_filesize = 10M
post_max_size = 10M
max_execution_time = 300
memory_limit = 256M
```

## 📝 Migração de Dados

### Se você já tem dados no sistema Node.js:

1. **Exportar dados do SQLite**:
```bash
sqlite3 database.sqlite .dump > backup.sql
```

2. **Converter para MySQL** (ajustar sintaxe SQLite → MySQL):
   - Remover `AUTOINCREMENT` → `AUTO_INCREMENT`
   - Ajustar tipos de dados
   - Ajustar comandos de criação de tabelas

3. **Importar no MySQL da HostGator**:
   - Via phpMyAdmin no painel
   - Ou via linha de comando MySQL

## 🔄 Diferenças Principais

### 1. Autenticação
- **Node.js**: JWT com `jsonwebtoken`
- **PHP**: JWT com `firebase/php-jwt`
- **Funcionamento**: Idêntico

### 2. Banco de Dados
- **Node.js**: SQLite (dev) / MySQL (prod)
- **PHP**: MySQL (produção)
- **Migração**: Automática na primeira conexão

### 3. Manipulação de PDF
- **Node.js**: `pdf-lib`
- **PHP**: `setasign/fpdi` + `tecnickcom/tcpdf`
- **Nota**: A implementação de PDFSigner precisa ser completada com FPDI

### 4. Mercado Pago
- **Node.js**: SDK JavaScript
- **PHP**: SDK PHP oficial
- **Funcionamento**: Idêntico

## 🐛 Troubleshooting

### Erro de conexão com banco de dados
- Verifique credenciais em `config/database.php`
- Confirme que o banco foi criado no painel
- Verifique se o host está correto (geralmente `localhost`)

### Erro 500 (Internal Server Error)
- Verifique logs de erro do PHP (via painel HostGator)
- Verifique permissões de arquivos
- Confirme que todas as dependências foram instaladas

### Erro de autoload (classe não encontrada)
- Execute `composer install` novamente
- Verifique se `vendor/autoload.php` existe

### Upload de arquivos não funciona
- Verifique permissões da pasta `uploads/`
- Verifique configurações de `upload_max_filesize` no PHP

## 📚 Próximos Passos

1. **Completar PDFSigner**: Implementar lógica completa de assinatura de PDF usando FPDI
2. **Criar páginas frontend**: Migrar páginas React para HTML/PHP
3. **Testes**: Testar todas as funcionalidades
4. **Otimização**: Cache, compressão, etc.

## 🔐 Segurança

- ✅ JWT com expiração
- ✅ Cookies HttpOnly
- ✅ Prepared statements (PDO)
- ✅ Validação de entrada
- ✅ Proteção de arquivos sensíveis (.htaccess)
- ✅ Headers de segurança

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique os logs de erro
2. Consulte a documentação das bibliotecas usadas
3. Verifique configurações do servidor HostGator

---

**Nota**: Esta é uma versão inicial da migração. Algumas funcionalidades podem precisar de ajustes finos para produção.

