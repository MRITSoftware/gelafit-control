# Como Resolver o Erro de CORS

## Problema
O erro `Access to internal resource at 'file:///...' from origin 'null' has been blocked by CORS policy` ocorre porque o arquivo está sendo aberto diretamente no navegador (protocolo `file://`) em vez de ser servido por um servidor HTTP.

## Soluções

### Opção 1: Usar um Servidor Local (Recomendado)

#### Python (se instalado):
```bash
# Na pasta do projeto
python -m http.server 8000
# ou
python3 -m http.server 8000
```
Acesse: `http://localhost:8000`

#### Node.js (se instalado):
```bash
# Instalar servidor globalmente
npm install -g http-server

# Na pasta do projeto
http-server -p 8000
```
Acesse: `http://localhost:8000`

#### PHP (se instalado):
```bash
# Na pasta do projeto
php -S localhost:8000
```
Acesse: `http://localhost:8000`

### Opção 2: Usar Live Server (VS Code)
1. Instale a extensão "Live Server" no VS Code
2. Clique com botão direito no arquivo `index.html`
3. Selecione "Open with Live Server"

### Opção 3: Usar um Servidor Web
- Apache
- Nginx
- IIS
- Qualquer servidor web que sirva arquivos estáticos

## Verificação
Após usar qualquer uma das opções acima, acesse o arquivo através do protocolo HTTP (ex: `http://localhost:8000/index.html`) e o erro de CORS será resolvido.

## Teste da Promoção
1. Resolva o problema de CORS usando uma das opções acima
2. Configure uma promoção no banco de dados:
   ```sql
   -- Inserir promoção
   INSERT INTO promo (id_promo, _promo, texto_promo, valor_antes, valor_promo, contador) 
   VALUES ('O6A28X', 'https://exemplo.com/imagem.jpg', 'Oferta especial!', '299,90', '149,90', 15);
   
   -- Ativar na tela
   UPDATE displays 
   SET promo = true, id_promo = 'O6A28X' 
   WHERE codigo_unico = 'SEU_CODIGO_AQUI';
   ```
3. Abra o console do navegador (F12) para ver os logs de debug
4. O popup deve aparecer automaticamente
