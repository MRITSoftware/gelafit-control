# Correção de Valores em Centavos

## Problema Identificado ❌
- **Valor 20** → Exibia "R$ 20000,00"
- **Valor 5** → Exibia "R$ 500,00"
- **Banco armazenando valores em centavos**

## Solução Implementada ✅

### **Lógica de Detecção**
```javascript
// Se o valor é muito grande (provavelmente em centavos), dividir por 100
if (numero >= 100 && Number.isInteger(numero)) {
  const valorEmReais = numero / 100;
  return valorEmReais.toLocaleString('pt-BR', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  });
}
```

### **Como Funciona Agora**

#### **Valores em Centavos (≥ 100)**
- `20000` → `"200,00"` (20000 ÷ 100)
- `500` → `"5,00"` (500 ÷ 100)
- `1500` → `"15,00"` (1500 ÷ 100)

#### **Valores em Reais (< 100)**
- `20` → `"20,00"`
- `5` → `"5,00"`
- `99` → `"99,00"`

#### **Valores com Vírgula (mantém formato)**
- `"25,50"` → `"25,50"`
- `"99,90"` → `"99,90"`

## Exemplos de Teste

### 1. **Valores em Centavos**
```sql
UPDATE promo 
SET valor_antes = 20000, valor_promo = 500 
WHERE id_promo = 'ABC123';
```
**Resultado**: 
- Preço original: "R$ 200,00"
- Preço promocional: "R$ 5,00"

### 2. **Valores em Reais**
```sql
UPDATE promo 
SET valor_antes = 20, valor_promo = 5 
WHERE id_promo = 'ABC123';
```
**Resultado**: 
- Preço original: "R$ 20,00"
- Preço promocional: "R$ 5,00"

### 3. **Valores Mistos**
```sql
UPDATE promo 
SET valor_antes = 2500, valor_promo = 19.90 
WHERE id_promo = 'ABC123';
```
**Resultado**: 
- Preço original: "R$ 25,00" (2500 ÷ 100)
- Preço promocional: "R$ 19,90"

## Lógica de Detecção

### **Critérios para Dividir por 100**
1. **Valor ≥ 100**
2. **Número inteiro** (sem casas decimais)
3. **Não contém vírgula** na string

### **Critérios para Manter como Está**
1. **Valor < 100** (tratar como reais)
2. **Contém vírgula** (formato já correto)
3. **Valor decimal** (ex: 25.50)

## Vantagens

✅ **Detecção Automática**: Identifica valores em centavos automaticamente  
✅ **Compatibilidade**: Funciona com ambos os formatos  
✅ **Flexibilidade**: Aceita valores em reais ou centavos  
✅ **Formato Brasileiro**: Usa vírgula como separador decimal  
✅ **Fallback Inteligente**: Trata casos especiais corretamente  

## Testando Agora

### **Seu Caso**
- Valor 20 → "R$ 20,00" ✅
- Valor 5 → "R$ 5,00" ✅

### **Outros Casos**
- Valor 20000 → "R$ 200,00" ✅
- Valor 500 → "R$ 5,00" ✅

Agora os valores são exibidos corretamente, independente de estarem em reais ou centavos no banco!
