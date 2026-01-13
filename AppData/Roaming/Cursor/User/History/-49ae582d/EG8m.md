# 📦 Como Instalar o Composer

## Opção 1: Instalar Composer no Windows (Recomendado)

### Método 1: Via Instalador Oficial

1. **Baixe o instalador:**
   - Acesse: https://getcomposer.org/download/
   - Baixe o arquivo `Composer-Setup.exe`

2. **Execute o instalador:**
   - Execute o arquivo baixado
   - Siga as instruções do instalador
   - O instalador vai configurar o PATH automaticamente

3. **Verifique a instalação:**
   ```powershell
   composer --version
   ```

### Método 2: Via PowerShell (Mais Rápido)

```powershell
# Executar no PowerShell como Administrador
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
Invoke-WebRequest https://getcomposer.org/installer -OutFile composer-setup.php
php composer-setup.php
php -r "unlink('composer-setup.php');"
```

Depois, mova o `composer.phar` para um diretório no PATH ou crie um alias.

## Opção 2: Usar Composer.phar Localmente

Se não quiser instalar globalmente, você pode baixar o `composer.phar`:

```powershell
cd php-version
Invoke-WebRequest https://getcomposer.org/download/latest-stable/composer.phar -OutFile composer.phar
php composer.phar install
```

## Opção 3: Instalar Manualmente as Dependências

Se não conseguir instalar o Composer, você pode baixar as dependências manualmente:

1. Acesse: https://packagist.org/
2. Busque e baixe cada pacote:
   - `firebase/php-jwt`
   - `setasign/fpdi`
   - `tecnickcom/tcpdf`
   - `mercadopago/dx-php`
   - `phpmailer/phpmailer`
   - `endroid/qr-code`

3. Coloque na pasta `vendor/` seguindo a estrutura PSR-4

## ✅ Após Instalar o Composer

Depois de instalar o Composer, execute:

```powershell
cd php-version
composer install
```

Isso vai instalar todas as dependências necessárias na pasta `vendor/`.

---

**Nota:** O Composer é necessário para instalar as bibliotecas PHP. Se estiver com dificuldades, posso ajudar a baixar o Composer ou criar uma alternativa.
