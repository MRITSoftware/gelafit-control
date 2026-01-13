# Como Executar o MRIT Player

## ⚠️ Problema dos Erros

Os erros que você está vendo ocorrem porque o navegador não permite certas funcionalidades quando o arquivo é aberto diretamente do sistema de arquivos (`file://`). Isso é uma limitação de segurança do navegador.

## ✅ Soluções

### Opção 1: Servidor Local com Python (Recomendado)

1. Abra o terminal/prompt na pasta do projeto
2. Execute um dos comandos abaixo:

```bash
# Python 3
python -m http.server 8000

# Python 2
python -m SimpleHTTPServer 8000
```

3. Acesse: `http://localhost:8000`

### Opção 2: Servidor Local com Node.js

1. Instale o `serve` globalmente:
```bash
npm install -g serve
```

2. Execute na pasta do projeto:
```bash
serve .
```

3. Acesse a URL mostrada no terminal

### Opção 3: Live Server (VS Code)

1. Instale a extensão "Live Server" no VS Code
2. Clique com botão direito no `index.html`
3. Selecione "Open with Live Server"

### Opção 4: Servidor Local com PHP

```bash
php -S localhost:8000
```

## 🔧 Correções Implementadas

O código foi atualizado para:

1. **Detectar ambiente inseguro** e mostrar avisos apropriados
2. **Tratar erros de Service Worker** graciosamente
3. **Contornar problemas de fullscreen** em contexto inseguro
4. **Remover referência problemática** ao manifest.json
5. **Mostrar instruções** no console para desenvolvimento

## 📱 Funcionalidades que Funcionam em file://

- ✅ Player de vídeo
- ✅ Player de imagem  
- ✅ Modal de promoção
- ✅ Navegação entre itens
- ✅ Interface responsiva

## 🚫 Funcionalidades que NÃO Funcionam em file://

- ❌ Service Worker (cache offline)
- ❌ Fullscreen automático
- ❌ Manifest PWA
- ❌ Algumas APIs de mídia

## 💡 Dica

Para desenvolvimento, use sempre um servidor local. Para produção, hospede em um servidor web com HTTPS.

## 🐛 Debug

Se ainda houver problemas, verifique o console do navegador (F12) para mensagens de erro específicas.
