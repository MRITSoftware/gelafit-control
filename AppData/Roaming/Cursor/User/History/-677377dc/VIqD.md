# Teste da Imagem da Promoção

## Campo Corrigido ✅

- ❌ **Antes**: Usava campo `_promo`
- ✅ **Agora**: Usa campo `imagem_promo`

## Como Testar

### 1. **Configurar Promoção com Imagem**
```sql
-- Inserir promoção com imagem
INSERT INTO promo (id_promo, imagem_promo, texto_promo, valor_antes, valor_promo, contador) 
VALUES ('ABC123', 'https://exemplo.com/sua-imagem.jpg', 'Oferta especial!', '299,90', '149,90', 15);

-- Ativar promoção na tela
UPDATE displays 
SET promo = true, id_promo = 'ABC123' 
WHERE codigo_unico = 'SEU_CODIGO';
```

### 2. **Testar Sem Imagem**
```sql
-- Inserir promoção sem imagem
INSERT INTO promo (id_promo, texto_promo, valor_antes, valor_promo, contador) 
VALUES ('ABC124', 'Oferta especial!', '299,90', '149,90', 15);

-- Ativar promoção na tela
UPDATE displays 
SET promo = true, id_promo = 'ABC124' 
WHERE codigo_unico = 'SEU_CODIGO';
```

## Resultados Esperados

### **Com Imagem**
- ✅ Imagem aparece no popup
- ✅ Imagem é responsiva (max-width: 200px)
- ✅ Imagem tem bordas arredondadas
- ✅ Imagem se ajusta ao container

### **Sem Imagem**
- ✅ Mostra texto "Nenhuma imagem configurada"
- ✅ Mostra placeholder cinza com bordas tracejadas
- ✅ Layout permanece consistente

## Formatos de Imagem Suportados

- ✅ JPG/JPEG
- ✅ PNG
- ✅ WebP
- ✅ GIF
- ✅ SVG

## Tamanho Recomendado

- **Largura**: 200px a 400px
- **Altura**: Proporcional (ex: 200x120px)
- **Formato**: JPG ou PNG para melhor compatibilidade

## Exemplo de URL Válida

```
https://exemplo.com/produto-promocao.jpg
https://cdn.exemplo.com/images/promo.png
https://storage.googleapis.com/bucket/imagem.webp
```

## Debug

Se a imagem não aparecer, verifique:

1. **URL está correta** no campo `imagem_promo`
2. **URL é acessível** (teste no navegador)
3. **Console não mostra erros** de CORS ou 404
4. **Campo não está vazio** ou NULL

## Logs de Debug

O console mostra:
- 🎯 Dados da promoção: {imagem_promo: "https://...", ...}
- Se houver erro de carregamento da imagem, aparecerá no console

Agora a imagem da promoção deve aparecer corretamente no popup!
