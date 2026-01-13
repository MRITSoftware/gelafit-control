# Instalação de Bibliotecas para Manipulação de PDF

Para que o sistema possa adicionar uma nova página ao PDF com as assinaturas, é necessário instalar as bibliotecas PHP via Composer.

## Como Instalar

### Opção 1: Via Composer (Recomendado)

Se você tem acesso SSH ao servidor (Hostinger):

1. Acesse a pasta raiz do projeto via SSH
2. Execute:
```bash
composer install
```

Isso instalará automaticamente:
- `setasign/fpdi` - Para manipular PDFs existentes
- `tecnickcom/tcpdf` - Para criar novos PDFs

### Opção 2: Via Painel da Hostinger

1. Acesse o painel da Hostinger
2. Vá em "Ferramentas" > "Terminal"
3. Navegue até a pasta do projeto
4. Execute: `composer install`

### Opção 3: Upload Manual (Se não tiver Composer)

Se você não tem acesso ao Composer, pode fazer upload manual:

1. Acesse: https://packagist.org/packages/setasign/fpdi
2. Baixe a biblioteca
3. Extraia na pasta `vendor/setasign/fpdi/`
4. Acesse: https://packagist.org/packages/tecnickcom/tcpdf
5. Baixe a biblioteca
6. Extraia na pasta `vendor/tecnickcom/tcpdf/`
7. Crie o arquivo `vendor/autoload.php` (ou copie de um projeto que tenha Composer)

## Verificação

Após instalar, o sistema tentará usar essas bibliotecas automaticamente. Se não encontrar, usará um método alternativo (HTML que pode ser convertido para PDF).

## Nota Importante

Se as bibliotecas não estiverem instaladas, o sistema funcionará mas criará um arquivo HTML em vez de modificar diretamente o PDF. O usuário poderá:
- Visualizar o HTML no navegador
- Imprimir como PDF usando "Salvar como PDF" do navegador

