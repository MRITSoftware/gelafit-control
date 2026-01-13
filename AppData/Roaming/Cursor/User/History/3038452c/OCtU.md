# Como Instalar Bibliotecas na Hostinger

Guia completo para instalar as bibliotecas FPDI e TCPDF na Hostinger.

## 📋 Opção 1: Via SSH (Recomendado - Mais Rápido)

### Passo 1: Acessar SSH
1. Acesse o **Painel da Hostinger**
2. Vá em **"Avancado"** > **"SSH"** ou procure por **"Terminal"** / **"SSH Access"**
3. Ative o acesso SSH (se necessário)
4. Anote as credenciais SSH fornecidas

### Passo 2: Conectar via SSH
**Windows:**
- Use o **PuTTY** ou **PowerShell**
- Baixe PuTTY: https://www.putty.org/

**Mac/Linux:**
- Use o Terminal nativo

**Comando:**
```bash
ssh usuario@seu-servidor.hostinger.com
# Ou use o comando fornecido pela Hostinger
```

### Passo 3: Navegar até a pasta do projeto
```bash
cd public_html
# Ou cd htdocs
# Ou o caminho onde estão seus arquivos do site
```

### Passo 4: Verificar se o Composer está instalado
```bash
composer --version
```

Se não estiver instalado, instale:
```bash
curl -sS https://getcomposer.org/installer | php
mv composer.phar /usr/local/bin/composer
```

### Passo 5: Instalar as bibliotecas
```bash
cd "D:\SITE MRIT"
# Ou o caminho completo onde está o composer.json

composer install
```

Isso instalará automaticamente:
- ✅ `setasign/fpdi` 
- ✅ `tecnickcom/tcpdf`

### Passo 6: Verificar instalação
```bash
ls vendor/setasign/fpdi
ls vendor/tecnickcom/tcpdf
```

Se essas pastas existirem, está instalado! ✅

---

## 📋 Opção 2: Via File Manager (Mais Simples - Mas Manual)

Se você não tem acesso SSH ou não quer usar SSH:

### Passo 1: Acessar File Manager
1. Acesse o **Painel da Hostinger**
2. Vá em **"Gerenciador de Arquivos"** (File Manager)
3. Navegue até a pasta raiz do seu site (geralmente `public_html` ou `htdocs`)

### Passo 2: Verificar se existe pasta `vendor`
- Se não existir, crie a pasta `vendor`

### Passo 3: Baixar as bibliotecas manualmente

#### Biblioteca 1: FPDI
1. Acesse: https://github.com/Setasign/FPDI/releases
2. Baixe a versão mais recente (zip)
3. Extraia e faça upload da pasta `FPDI` para `vendor/setasign/fpdi/`
4. A estrutura deve ficar:
   ```
   vendor/
     setasign/
       fpdi/
         src/
           Fpdi.php
           ... (outros arquivos)
   ```

#### Biblioteca 2: TCPDF
1. Acesse: https://github.com/tecnickcom/TCPDF/releases
2. Baixe a versão mais recente (zip)
3. Extraia e faça upload da pasta `tcpdf` para `vendor/tecnickcom/tcpdf/`
4. A estrutura deve ficar:
   ```
   vendor/
     tecnickcom/
       tcpdf/
         tcpdf.php
         ... (outros arquivos)
   ```

### Passo 4: Criar arquivo autoload.php
Crie o arquivo `vendor/autoload.php` com o seguinte conteúdo:

```php
<?php
// Autoload simples para FPDI e TCPDF

// FPDI
spl_autoload_register(function ($class) {
    if (strpos($class, 'setasign\\Fpdi\\') === 0) {
        $path = __DIR__ . '/setasign/fpdi/src/' . str_replace('\\', '/', substr($class, 13)) . '.php';
        if (file_exists($path)) {
            require_once $path;
        }
    }
});

// TCPDF
if (file_exists(__DIR__ . '/tecnickcom/tcpdf/tcpdf.php')) {
    require_once __DIR__ . '/tecnickcom/tcpdf/tcpdf.php';
}
```

---

## 📋 Opção 3: Usar PHP Nativo (Sem Bibliotecas Externas)

Se você não conseguir instalar as bibliotecas, o sistema ainda funcionará, mas criará um arquivo HTML em vez de modificar diretamente o PDF. O usuário poderá:
- Visualizar o HTML no navegador
- Imprimir como PDF usando "Salvar como PDF" do navegador

---

## ✅ Como Verificar se Funcionou

Após instalar, faça um teste:

1. Acesse o sistema de assinaturas
2. Faça upload de um PDF
3. Adicione uma assinatura
4. Processe a assinatura

**Se funcionar corretamente:**
- ✅ O PDF terá uma nova página no final com as assinaturas
- ✅ O download será do PDF completo (não HTML)

**Se não funcionar:**
- ⚠️ O sistema criará um HTML com o PDF embutido
- ⚠️ O download será do HTML (mas ainda funciona)

---

## 🆘 Problemas Comuns

### Erro: "Class 'setasign\Fpdi\Fpdi' not found"
**Solução:** Verifique se a pasta `vendor/setasign/fpdi` existe e tem os arquivos corretos.

### Erro: "Composer not found"
**Solução:** Instale o Composer via SSH ou use a Opção 2 (instalação manual).

### Erro: "Permission denied"
**Solução:** Verifique as permissões da pasta `vendor`. Deve ser 755 ou 777.

---

## 📞 Suporte Hostinger

Se tiver dúvidas sobre acesso SSH ou File Manager:
- Chat da Hostinger
- Centro de ajuda: https://www.hostinger.com.br/tutoriais

---

## 💡 Dica Final

A **Opção 1 (SSH + Composer)** é a mais rápida e confiável. Se você tiver acesso SSH, use essa opção!

