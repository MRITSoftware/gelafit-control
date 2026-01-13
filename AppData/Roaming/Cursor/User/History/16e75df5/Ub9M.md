# Como usar o tipo "Promo"

## Implementação com Nova Janela

O tipo "Promo" foi implementado para abrir uma **nova janela/popup** com o visual da oferta relâmpago, saindo do player principal.

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
- ✅ **Nova janela**: Abre popup com design da oferta relâmpago
- ✅ **Duração configurável**: Usa campo `duration` ou 10 segundos padrão
- ✅ **Foco automático**: Nova janela ganha foco automaticamente
- ✅ **Fechar automático**: Janela fecha sozinha após o tempo definido
- ✅ **Navegação automática**: Player avança para próximo item após o tempo

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

Este exemplo criará uma promoção que abre uma nova janela por 8 segundos com o visual da oferta relâmpago.

## Nota importante

⚠️ **Popups podem ser bloqueados pelo navegador**. Para melhor experiência, permita popups no site.
