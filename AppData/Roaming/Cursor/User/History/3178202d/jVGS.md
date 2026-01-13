# Instalação - Processamento de PDFs

Este guia explica como instalar e configurar as dependências necessárias para o processamento de PDFs com OCR.

## 📋 Dependências Adicionais

As seguintes dependências foram adicionadas para suporte a PDFs:

```json
{
  "pdf-parse": "^1.1.1",
  "pdf2pic": "^2.11.4", 
  "tesseract.js": "^4.1.1",
  "pdfjs-dist": "^3.11.174",
  "canvas": "^2.11.2",
  "jspdf": "^2.5.1"
}
```

## 🚀 Instalação

### 1. Instalar Dependências

```bash
npm install
```

### 2. Configuração do Canvas (Windows)

Para Windows, você pode precisar instalar dependências adicionais:

```bash
npm install --global windows-build-tools
```

Ou usando yarn:

```bash
yarn add canvas
```

### 3. Configuração do Tesseract.js

O Tesseract.js baixa automaticamente os modelos de linguagem necessários na primeira execução. Para português, ele baixará:

- `por.traineddata` - Modelo de reconhecimento em português
- `eng.traineddata` - Modelo de reconhecimento em inglês (fallback)

## 🔧 Configurações do Next.js

O arquivo `next.config.js` foi atualizado com configurações webpack para suportar as bibliotecas de PDF:

```javascript
webpack: (config, { isServer }) => {
  if (!isServer) {
    config.resolve.fallback = {
      ...config.resolve.fallback,
      fs: false,
      path: false,
      crypto: false,
    }
  }
  return config
}
```

## 🧪 Testando a Instalação

### 1. Executar o Projeto

```bash
npm run dev
```

### 2. Testar Upload de PDF

1. Acesse `http://localhost:3000`
2. Faça login no sistema
3. Vá para "Processar PDFs"
4. Faça upload de um PDF de extrato bancário
5. Verifique se o processamento funciona corretamente

## 🐛 Solução de Problemas

### Erro: "Canvas is not defined"

**Solução**: Instale as dependências de build do Windows:

```bash
npm install --global windows-build-tools
```

### Erro: "Module not found: pdfjs-dist"

**Solução**: Reinstale as dependências:

```bash
rm -rf node_modules package-lock.json
npm install
```

### Erro: "Tesseract.js worker not found"

**Solução**: O Tesseract.js baixa os workers automaticamente. Se houver problemas, limpe o cache:

```bash
# No navegador, abra o DevTools e execute:
localStorage.clear()
```

### Performance Lenta

**Soluções**:
1. Use PDFs com qualidade de texto boa
2. Evite PDFs muito grandes (mais de 50 páginas)
3. Processe PDFs em lotes menores (2-3 por vez)

## 📊 Limitações Conhecidas

### Tesseract.js
- **Precisão**: Depende da qualidade do PDF original
- **Performance**: Processamento pode ser lento em PDFs grandes
- **Idioma**: Otimizado para português brasileiro

### PDF.js
- **Compatibilidade**: Funciona melhor com PDFs gerados digitalmente
- **Imagens**: PDFs escaneados podem ter precisão reduzida

### Canvas
- **Windows**: Pode precisar de configurações adicionais
- **Linux**: Funciona nativamente
- **macOS**: Funciona nativamente

## 🔄 Atualizações

Para atualizar as dependências de PDF:

```bash
npm update pdf-parse tesseract.js pdfjs-dist canvas
```

## 📞 Suporte

Se encontrar problemas:

1. Verifique se todas as dependências foram instaladas
2. Confirme que o Node.js está na versão 18+
3. Teste com um PDF simples primeiro
4. Verifique os logs do console do navegador

## 🎯 Próximos Passos

Após a instalação bem-sucedida:

1. Teste com diferentes tipos de PDFs
2. Configure regras de classificação personalizadas
3. Ajuste os padrões de reconhecimento se necessário
4. Monitore a performance do processamento

---

**Nota**: O processamento de PDFs com OCR é computacionalmente intensivo. Para melhor performance, considere usar um servidor com recursos adequados em produção.
