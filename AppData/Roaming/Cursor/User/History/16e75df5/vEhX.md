# Como usar o tipo "Promo"

## Implementação Simples

O tipo "Promo" foi implementado de forma simples e direta. Quando o `tipo` for igual a "Promo", o player exibe um popup com o visual da oferta relâmpago.

## Como usar no banco de dados

### Para conteúdo único (tabela `conteudos`):
```sql
INSERT INTO conteudos (codigoAnuncio, tipo, url, duration) 
VALUES ('PROMO001', 'Promo', 'https://exemplo.com/promo.jpg', 15000);
```

### Para itens de playlist (tabela `playlist_itens`):
```sql
INSERT INTO playlist_itens (playlist_id, url, tipo, ordem, duration) 
VALUES ('PLAYLIST001', 'https://exemplo.com/promo.jpg', 'Promo', 1, 10000);
```

## Comportamento

- ✅ **Detecção automática**: Quando `tipo = 'Promo'`
- ✅ **Popup visual**: Exibe modal com design da oferta relâmpago
- ✅ **Duração configurável**: Usa campo `duration` ou 10 segundos padrão
- ✅ **Fechar com clique**: Clique fora do modal para fechar
- ✅ **Navegação automática**: Avança para próximo item após o tempo

## Campos necessários

- `tipo`: Deve ser exatamente "Promo"
- `url`: URL do conteúdo (pode ser qualquer URL)
- `duration`: Duração em milissegundos (opcional, padrão: 10000ms)

## Exemplo prático

```sql
-- Exemplo básico
INSERT INTO conteudos (codigoAnuncio, tipo, url, duration) 
VALUES ('OFERTA123', 'Promo', 'https://exemplo.com/produto.jpg', 8000);
```

Este exemplo criará uma promoção que exibe por 8 segundos com o visual padrão da oferta relâmpago.
