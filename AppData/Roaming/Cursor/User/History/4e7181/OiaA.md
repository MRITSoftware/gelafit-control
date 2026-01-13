# ⚡ Instalação Rápida - Teste Local

## 🎯 Opção Mais Rápida: XAMPP

### Passo 1: Instalar XAMPP

**Método 1: Via winget (recomendado)**
```powershell
# Execute no PowerShell como Administrador
winget install ApacheFriends.Xampp.8.2 --accept-package-agreements --accept-source-agreements
```

**Método 2: Download Manual**
1. Acesse: https://www.apachefriends.org/download.html
2. Baixe **XAMPP para Windows** (versão 8.2 ou superior)
3. Execute o instalador
4. Escolha a pasta padrão: `C:\xampp`
5. Marque: Apache, MySQL, PHP
6. Complete a instalação

### Passo 2: Iniciar Serviços

1. Abra o **XAMPP Control Panel** (procure no menu iniciar)
2. Clique em **Start** para:
   - ✅ Apache
   - ✅ MySQL

### Passo 3: Criar Banco de Dados

1. Acesse: http://localhost/phpmyadmin
2. Clique em **Novo** (New) no menu lateral
3. Nome do banco: `esign_test`
4. Clique em **Criar**

### Passo 4: Configurar o Projeto

1. **Copie a pasta do projeto** para o XAMPP:
```powershell
# Copiar php-version para htdocs
Copy-Item "D:\ass digital\php-version" -Destination "C:\xampp\htdocs\esign" -Recurse
```

2. **Configure o banco de dados**:
   - Edite: `C:\xampp\htdocs\esign\config\database.php`
   - Ou copie o arquivo de exemplo:
```powershell
Copy-Item "C:\xampp\htdocs\esign\config\database.php.example" -Destination "C:\xampp\htdocs\esign\config\database.php"
```

3. **Edite `config/database.php`**:
```php
return [
    'host' => 'localhost',
    'database' => 'esign_test',
    'username' => 'root',
    'password' => '', // XAMPP geralmente não tem senha
    'charset' => 'utf8mb4',
    // ...
];
```

4. **Configure `config/config.php`**:
```php
define('ENVIRONMENT', 'development');
define('BASE_URL', 'http://localhost/esign');
// ... resto das configurações
```

### Passo 5: Instalar Dependências (Composer)

**Opção A: Se já tem Composer instalado**
```powershell
cd C:\xampp\htdocs\esign
composer install --no-dev
```

**Opção B: Instalar Composer primeiro**
1. Baixe: https://getcomposer.org/download/
2. Execute: `composer-setup.exe`
3. Depois execute: `composer install`

**Opção C: Sem Composer (temporário)**
- Você pode testar sem as dependências, mas algumas funcionalidades não funcionarão
- Para produção, instale o Composer

### Passo 6: Testar!

1. Acesse: **http://localhost/esign/test.php**
2. Deve ver todas as verificações passando ✅

---

## 🚀 Alternativa: PHP Standalone + Servidor Built-in

Se preferir apenas PHP (sem XAMPP):

### 1. Instalar PHP
```powershell
winget install PHP.PHP.8.3 --accept-package-agreements --accept-source-agreements
```

### 2. Adicionar ao PATH
- Abra "Variáveis de Ambiente" do Windows
- Adicione: `C:\Program Files\PHP\` ao PATH
- Reinicie o PowerShell

### 3. Instalar MySQL separadamente
- Ou use Docker
- Ou use XAMPP apenas para MySQL

### 4. Rodar servidor
```powershell
cd "D:\ass digital\php-version"
php -S localhost:8000
```

---

## ✅ Checklist

Após instalação, verifique:

- [ ] XAMPP instalado e rodando (Apache + MySQL)
- [ ] Banco `esign_test` criado no phpMyAdmin
- [ ] Projeto copiado para `C:\xampp\htdocs\esign`
- [ ] `config/database.php` configurado
- [ ] `config/config.php` configurado
- [ ] Composer instalado e dependências instaladas
- [ ] Teste em http://localhost/esign/test.php funciona

---

## 🐛 Problemas?

### "PHP não encontrado"
- Adicione `C:\xampp\php` ao PATH
- Ou use: `C:\xampp\php\php.exe` diretamente

### "Composer não encontrado"
- Baixe e instale: https://getcomposer.org/download/
- Ou use: `php composer.phar install`

### "Erro de conexão com banco"
- Verifique se MySQL está rodando no XAMPP
- Verifique credenciais em `config/database.php`
- Crie o banco `esign_test` no phpMyAdmin

### "Porta 80 em uso"
- Feche outros servidores web
- Ou mude a porta do Apache no XAMPP

---

**Pronto!** Agora você pode testar localmente! 🎉

