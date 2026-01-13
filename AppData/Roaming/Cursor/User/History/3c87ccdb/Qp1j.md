# 🚀 Instalar PHP no Windows para Testes Locais

## Opção 1: XAMPP (Mais Fácil - Recomendado)

### Passo 1: Baixar XAMPP
1. Acesse: https://www.apachefriends.org/download.html
2. Baixe a versão com PHP 8.0+ para Windows
3. Execute o instalador

### Passo 2: Instalar
1. Escolha um local (ex: `C:\xampp`)
2. Marque: Apache, MySQL, PHP
3. Complete a instalação

### Passo 3: Configurar
1. Abra o **XAMPP Control Panel**
2. Inicie **Apache** e **MySQL**
3. Abra o **phpMyAdmin** (http://localhost/phpmyadmin)
4. Crie um banco de dados chamado `esign_test`

### Passo 4: Configurar o Projeto
1. Copie a pasta `php-version` para `C:\xampp\htdocs\esign`
2. Edite `config/database.php`:
```php
return [
    'host' => 'localhost',
    'database' => 'esign_test',
    'username' => 'root',
    'password' => '', // XAMPP geralmente não tem senha
    // ...
];
```

### Passo 5: Instalar Dependências
1. Abra o PowerShell como Administrador
2. Navegue até o projeto:
```powershell
cd C:\xampp\htdocs\esign
```

3. Instale o Composer (se não tiver):
   - Baixe: https://getcomposer.org/download/
   - Execute: `php composer-setup.php`

4. Instale dependências:
```powershell
php composer.phar install
```

### Passo 6: Testar
1. Acesse: http://localhost/esign/test.php
2. Deve ver todas as verificações passando ✅

---

## Opção 2: PHP Standalone (Sem XAMPP)

### Passo 1: Baixar PHP
1. Acesse: https://windows.php.net/download/
2. Baixe **PHP 8.0+ Thread Safe** (ZIP)
3. Extraia para `C:\php`

### Passo 2: Configurar PHP
1. Renomeie `php.ini-development` para `php.ini`
2. Edite `php.ini` e descomente:
```ini
extension=pdo_mysql
extension=mbstring
extension=gd
extension=openssl
```

3. Adicione PHP ao PATH:
   - Abra "Variáveis de Ambiente"
   - Adicione `C:\php` ao PATH

### Passo 3: Instalar MySQL
1. Baixe MySQL: https://dev.mysql.com/downloads/installer/
2. Ou use XAMPP apenas para MySQL

### Passo 4: Testar
```powershell
php --version
```

---

## Opção 3: Usar PHP Built-in Server (Mais Simples)

Se você já tem PHP instalado (mesmo que não esteja no PATH):

### Passo 1: Localizar PHP
Pode estar em:
- `C:\xampp\php\php.exe`
- `C:\php\php.exe`
- Ou outro local

### Passo 2: Rodar Servidor
```powershell
cd "D:\ass digital\php-version"
C:\xampp\php\php.exe -S localhost:8000
```

### Passo 3: Acessar
- http://localhost:8000/test.php

---

## Opção 4: Docker (Avançado)

Se você tem Docker instalado, posso criar um `docker-compose.yml` para você.

---

## 🎯 Recomendação: XAMPP

Para testes rápidos, recomendo **XAMPP** porque:
- ✅ Instalação fácil
- ✅ Já vem com Apache, MySQL e PHP
- ✅ phpMyAdmin incluído
- ✅ Funciona "out of the box"

---

## ⚡ Teste Rápido após Instalação

Após instalar qualquer opção:

1. **Configure o banco** em `config/database.php`
2. **Instale dependências**: `composer install`
3. **Acesse**: http://localhost/php-version/test.php

Se tudo funcionar, você verá:
```
=== TESTE DO SISTEMA DE ASSINATURA DIGITAL ===

1. Testando carregamento do bootstrap...
   ✅ Bootstrap carregado com sucesso

2. Verificando extensões PHP...
   ✅ pdo está instalada
   ✅ pdo_mysql está instalada
   ...
```

---

## 🐛 Problemas Comuns

### "PHP não é reconhecido"
- Adicione PHP ao PATH do Windows
- Ou use o caminho completo: `C:\xampp\php\php.exe`

### "Composer não encontrado"
- Baixe Composer: https://getcomposer.org/download/
- Ou use: `php composer.phar install`

### "Erro de conexão com banco"
- Verifique se MySQL está rodando
- Verifique credenciais em `config/database.php`
- Crie o banco de dados primeiro

---

**Pronto!** Agora você pode testar localmente antes de subir para HostGator.

