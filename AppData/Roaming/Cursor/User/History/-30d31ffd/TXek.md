# Como usar o tipo "Promo" no MRIT Player

## Visão Geral
O MRIT Player agora suporta um novo tipo de conteúdo chamado "Promo" que exibe uma janela modal com oferta relâmpago, similar ao design mostrado na imagem.

## Configuração no Banco de Dados

### 1. Para Conteúdo Único (tabela `conteudos`)

```sql
INSERT INTO conteudos (
  codigoAnuncio,
  tipo,
  url,
  duration,
  promoData
) VALUES (
  'PROMO001',
  'Promo',
  'https://exemplo.com/promo.jpg',
  15000, -- 15 segundos
  '{
    "titulo": "OFERTA RELÂMPAGO",
    "precoOriginal": "R$ 200,00",
    "precoDesconto": "R$ 99,90",
    "unidades": "9",
    "descricao": "Promoção especial",
    "imagem": "https://exemplo.com/produto.jpg"
  }'::jsonb
);
```

### 2. Para Itens de Playlist (tabela `playlist_itens`)

```sql
INSERT INTO playlist_itens (
  playlist_id,
  url,
  tipo,
  ordem,
  duration,
  promoData
) VALUES (
  'PLAYLIST001',
  'https://exemplo.com/promo.jpg',
  'Promo',
  1,
  10000, -- 10 segundos
  '{
    "titulo": "OFERTA RELÂMPAGO",
    "precoOriginal": "R$ 150,00",
    "precoDesconto": "R$ 79,90",
    "unidades": "5",
    "descricao": "Oferta por tempo limitado",
    "imagem": "https://exemplo.com/produto2.jpg"
  }'::jsonb
);
```

## Estrutura do Campo `promoData`

O campo `promoData` é um JSON que pode conter os seguintes campos opcionais:

```json
{
  "titulo": "OFERTA RELÂMPAGO",           // Título do cabeçalho
  "precoOriginal": "R$ 200,00",          // Preço original (riscado)
  "precoDesconto": "R$ 99,90",           // Preço com desconto
  "unidades": "9",                       // Número de unidades restantes
  "descricao": "Promoção especial",      // Descrição da promoção
  "imagem": "https://exemplo.com/img.jpg" // URL da imagem do produto
}
```

## Comportamento

1. **Detecção**: O player detecta automaticamente quando `tipo = 'Promo'`
2. **Exibição**: Mostra o modal de oferta relâmpago com o design personalizado
3. **Duração**: Usa o campo `duration` ou 10 segundos como padrão
4. **Navegação**: Avança automaticamente para o próximo item após o tempo definido
5. **Interação**: Clique fora do modal para fechar (opcional)

## Campos Obrigatórios

- `tipo`: Deve ser exatamente "Promo" (case-insensitive)
- `url`: URL do conteúdo (pode ser uma imagem ou qualquer URL)

## Campos Opcionais

- `duration`: Duração em milissegundos (padrão: 10000ms)
- `promoData`: Dados personalizados da promoção em formato JSON

## Exemplo de Uso Simples

```sql
-- Exemplo básico sem dados personalizados
INSERT INTO conteudos (codigoAnuncio, tipo, url, duration) 
VALUES ('PROMO_SIMPLES', 'Promo', 'https://exemplo.com/promo.jpg', 8000);
```

Este exemplo criará uma promoção que exibe por 8 segundos com os valores padrão do modal.
