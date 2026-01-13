# Sistema de Popup de Promoção - MRIT Player

## Visão Geral

Este sistema implementa um popup de promoção que aparece automaticamente quando uma tela tem uma promoção ativa. O popup é totalmente responsivo e se adapta a qualquer tamanho de tela.

## Funcionalidades Implementadas

✅ **Verificação Automática**: O sistema verifica periodicamente se há uma promoção ativa na tabela `displays`  
✅ **Layout Responsivo**: O popup se adapta automaticamente a diferentes tamanhos de tela  
✅ **Contador Dinâmico**: Contador que decrementa automaticamente a cada segundo  
✅ **Desativação Automática**: Quando o contador chega a zero, a promoção é desativada automaticamente  
✅ **Limpeza de Dados**: Remove os dados da promoção quando expira  
✅ **Integração Completa**: Funciona perfeitamente com o fluxo existente do player  

## Estrutura do Banco de Dados

### Tabela `displays`
- `promo` (boolean): Indica se há uma promoção ativa
- `id_promo` (integer): ID da promoção na tabela `promo`

### Tabela `promo`
- `id` (integer): ID único da promoção
- `_promo` (text): URL da imagem da promoção (opcional)
- `texto_promo` (text): Texto descritivo da promoção
- `valor_antes` (text): Preço original (ex: "200,00")
- `valor_promo` (text): Preço promocional (ex: "99,90")
- `contador` (integer): Número de unidades restantes

## Como Usar

### 1. Configurar Promoção no Banco
```sql
-- Inserir promoção na tabela promo
INSERT INTO promo (id, _promo, texto_promo, valor_antes, valor_promo, contador) 
VALUES (1, 'https://exemplo.com/imagem.jpg', 'Oferta especial!', '299,90', '149,90', 15);

-- Ativar promoção na tela
UPDATE displays 
SET promo = true, id_promo = 1 
WHERE codigo_unico = 'CODIGO_DA_TELA';
```

### 2. O Popup Aparece Automaticamente
- Quando o player carrega e detecta `promo = true`
- O popup é exibido com todos os dados da promoção
- O contador inicia automaticamente

### 3. Desativação Automática
- Quando o contador chega a zero:
  - O popup é fechado
  - `promo` é definido como `false`
  - `id_promo` é definido como `null`
  - A linha da tabela `promo` é deletada

## Layout do Popup

O popup segue exatamente o layout da imagem de referência:

- **Header**: Gradiente roxo com ícone de raio e texto "OFERTA RELÂMPAGO"
- **Imagem**: Exibe `_promo` ou placeholder se não houver imagem
- **Texto**: Mostra `texto_promo`
- **Preços**: `valor_antes` (riscado) e `valor_promo` (destaque)
- **Contador**: Seção com gradiente mostrando unidades restantes

## Responsividade

O popup é totalmente responsivo:
- **Desktop**: Largura máxima de 500px
- **Mobile**: Ocupa 90% da largura da tela
- **Padding**: 20px nas laterais para telas pequenas
- **Fonte**: Tamanhos adaptativos para diferentes telas

## Integração com o Player

A funcionalidade está integrada ao fluxo principal:
- Verificação na inicialização do player
- Verificação periódica a cada 5 segundos
- Limpeza automática quando o player é parado
- Não interfere no funcionamento normal do player

## Teste

Use o arquivo `teste_promo.html` para visualizar o popup em ação:
1. Abra o arquivo no navegador
2. Clique em "Testar Popup (Simulação)"
3. Observe o contador decrementando
4. Clique em "Fechar Popup" para fechar manualmente

## Código Adicionado

O código foi adicionado ao arquivo `player.js` sem alterar nenhuma funcionalidade existente:
- Variáveis de controle da promoção
- Função `verificarPromocao()`
- Função `mostrarPopupPromocao()`
- Função `iniciarContadorPromocao()`
- Função `desativarPromocao()`
- Função `fecharPopupPromocao()`
- Integração no fluxo principal

## Compatibilidade

- ✅ Funciona em todos os navegadores modernos
- ✅ Responsivo para mobile e desktop
- ✅ Não afeta o cache existente
- ✅ Não interfere no service worker
- ✅ Mantém todas as funcionalidades originais
