# ⚡ Instalação Rápida - Sistema PHP para HostGator

## Passo a Passo Simplificado

### 1. Preparar Banco de Dados
1. Acesse o painel da HostGator
2. Crie um banco de dados MySQL
3. Anote: host, nome, usuário e senha

### 2. Configurar Credenciais

Edite `config/database.php`:
```php
'host' => 'localhost',
'database' => 'seu_banco',
'username' => 'seu_usuario',
'password' => 'sua_senha',
```

Edite `config/config.php` e defina:
- `JWT_SECRET` (uma string aleatória longa)
- `MP_ACCESS_TOKEN` (seu token do Mercado Pago)

### 3. Enviar Arquivos

1. Envie todos os arquivos da pasta `php-version/` para `public_html/` via FTP
2. Certifique-se de que a pasta `uploads/` existe e tem permissão de escrita

### 4. Instalar Dependências

**Opção A - Via SSH (recomendado):**
```bash
cd public_html
composer install --no-dev --optimize-autoloader
```

**Opção B - Localmente:**
```bash
# No seu computador
composer install --no-dev --optimize-autoloader
# Depois envie a pasta vendor/ via FTP
```

### 5. Testar

1. Acesse seu domínio: `https://seudominio.com.br`
2. O sistema criará as tabelas automaticamente na primeira conexão
3. Teste o login/cadastro

## ✅ Pronto!

O sistema está funcionando. As tabelas serão criadas automaticamente.

## ⚠️ Importante

- Altere o `JWT_SECRET` em produção (use uma string aleatória longa)
- Configure o token do Mercado Pago corretamente
- Verifique as permissões da pasta `uploads/`

## 📝 Nota sobre PDF

A classe `PDFSigner` precisa de implementação completa usando FPDI para assinar PDFs. A estrutura está pronta, mas você precisará implementar a lógica de adicionar imagens e texto aos PDFs usando FPDI.

