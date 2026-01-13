# Controle Dinâmico do Contador de Promoção

## Problemas Corrigidos ✅

1. **Popup não pisca mais** - Removida a verificação periódica que causava o piscar
2. **Contador não desce automaticamente** - Removido o contador automático
3. **Popup fica fixo na tela** - Aparece uma vez e permanece até ser fechado manualmente

## Como Controlar o Contador Dinamicamente

### 1. Via Console do Navegador (F12)

```javascript
// Atualizar contador para um valor específico
mritDebug.atualizarContador(10);

// Fechar popup de promoção
mritDebug.fecharPromocao();

// Verificar promoção novamente
mritDebug.verificarPromocao();
```

### 2. Via API Externa

Você pode criar um endpoint que chame a função `atualizarContadorPromocao()`:

```javascript
// Exemplo de endpoint que atualiza o contador
app.post('/atualizar-contador', (req, res) => {
  const { valor } = req.body;
  
  // Enviar comando para todas as telas ativas
  io.emit('atualizarContador', valor);
  
  res.json({ success: true });
});
```

### 3. Via WebSocket/Realtime

```javascript
// No lado do cliente (player)
socket.on('atualizarContador', (valor) => {
  atualizarContadorPromocao(valor);
});
```

### 4. Via Banco de Dados

Você pode atualizar o contador no banco e o player detectará a mudança:

```sql
-- Atualizar contador na tabela promo
UPDATE promo 
SET contador = 5 
WHERE id_promo = 'O6A28X';
```

## Funcionalidades Disponíveis

### `atualizarContadorPromocao(novoValor)`
- Atualiza o contador para o valor especificado
- Se o valor for 0 ou menor, desativa a promoção automaticamente
- Atualiza a exibição na tela em tempo real

### `fecharPopupPromocao()`
- Fecha o popup de promoção
- Limpa todas as variáveis relacionadas
- Não desativa a promoção no banco (apenas fecha o popup)

### `verificarPromocao()`
- Verifica se há uma promoção ativa
- Abre o popup se encontrar uma promoção válida
- Não faz nada se o popup já estiver aberto

## Exemplo de Uso Completo

```javascript
// 1. Abrir popup de promoção
mritDebug.verificarPromocao();

// 2. Atualizar contador para 15
mritDebug.atualizarContador(15);

// 3. Decrementar contador manualmente
mritDebug.atualizarContador(14);
mritDebug.atualizarContador(13);
mritDebug.atualizarContador(12);

// 4. Zerar contador (desativa promoção automaticamente)
mritDebug.atualizarContador(0);

// 5. Fechar popup manualmente
mritDebug.fecharPromocao();
```

## Integração com Sistema de Vendas

```javascript
// Exemplo: Quando uma venda é realizada
function processarVenda() {
  // Decrementar contador
  const contadorAtual = promoCounter || 0;
  const novoContador = Math.max(0, contadorAtual - 1);
  
  // Atualizar na tela
  atualizarContadorPromocao(novoContador);
  
  // Atualizar no banco de dados
  atualizarContadorNoBanco(novoContador);
}
```

## Logs de Debug

O sistema agora mostra logs detalhados no console:
- 🔍 Verificação de promoção
- 📊 Dados do display
- 🎯 Dados da promoção
- ⏰ Valor do contador
- ✅ Popup exibido
- 🔄 Popup já aberto (não pisca)
- ❌ Erros e problemas

Abra o console do navegador (F12) para acompanhar o funcionamento.
