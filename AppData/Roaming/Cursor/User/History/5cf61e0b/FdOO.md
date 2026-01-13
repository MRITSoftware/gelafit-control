# 🎯 Configuração da Promoção - Passo a Passo

## ✅ O que já está funcionando:
- ✅ Código de verificação de tipo "Promo" implementado
- ✅ Função de exibir promoção criada
- ✅ Servidor local funcionando
- ✅ Tela de promoção responsiva

## 🔧 Como configurar no Supabase:

### 1. **Criar a Promoção na tabela `promos`**
```sql
INSERT INTO promos (
  id_promo,
  codigo_promo,
  texto_promo,
  valor_promo,
  valor_antes,
  imagem_promo,
  contador,
  tipo
) VALUES (
  'PROMO001',                    -- Este será o ID da promoção
  'PROMO001',                    -- Código da promoção
  'Promoção especial de verão!', -- Texto da promoção
  9990,                          -- R$ 99,90 (em centavos)
  20000,                         -- R$ 200,00 (em centavos)
  'https://via.placeholder.com/400x200/673de6/ffffff?text=Promoção+Especial', -- URL da imagem
  5,                             -- Contador de unidades
  'promocao'                     -- Tipo da promoção
);
```

### 2. **Criar o Conteúdo na tabela `conteudos`**
```sql
INSERT INTO conteudos (
  codigoAnuncio,
  tipo,
  url
) VALUES (
  'PROMO001',    -- Deve ser IGUAL ao id_promo da tabela promos
  'Promo',       -- Tipo deve ser exatamente "Promo" (com P maiúsculo)
  'https://exemplo.com'  -- URL qualquer (não será usada)
);
```

### 3. **Configurar a Tela na tabela `displays`**
```sql
-- Primeiro, veja qual é o código da sua tela
SELECT codigo_unico FROM displays;

-- Depois configure o conteúdo atual para apontar para a promoção
UPDATE displays 
SET codigo_conteudoAtual = 'PROMO001' 
WHERE codigo_unico = 'SEU_CODIGO_TELA_AQUI';
```

## 🚀 Como testar:

### 1. **Inicie o servidor:**
- Clique duplo em `iniciar_servidor.bat`
- Ou execute: `python server.py`

### 2. **Acesse:** http://localhost:8000

### 3. **Digite o código da sua tela** (o mesmo que você configurou no UPDATE)

### 4. **Verifique o console (F12):**
- Deve aparecer: `"Conteúdo encontrado:"`
- Deve aparecer: `"Tipo do conteúdo: Promo"`
- Deve aparecer: `"Promoção encontrada:"`

## 🔍 Fluxo completo:

1. **Usuário insere código da tela** → `codigoTela`
2. **Sistema busca na tabela `displays`** → `codigo_conteudoAtual`
3. **Sistema busca na tabela `conteudos`** → `tipo = "Promo"`
4. **Sistema busca na tabela `promos`** → `id_promo = codigo_conteudoAtual`
5. **Exibe a tela de promoção** com todos os dados

## 🐛 Se não funcionar, verifique:

1. ✅ **Servidor está rodando** (http://localhost:8000)
2. ✅ **Existe registro na tabela `promos`** com `id_promo = 'PROMO001'`
3. ✅ **Existe registro na tabela `conteudos`** com `tipo = 'Promo'`
4. ✅ **O `codigo_conteudoAtual` da tela** aponta para `'PROMO001'`
5. ✅ **Console do navegador** mostra as mensagens de debug

## 📱 A tela de promoção exibe:

- ⚡ **Header**: "OFERTA RELÂMPAGO" com ícone de raio
- 🖼️ **Imagem**: `imagem_promo` da tabela
- 📝 **Texto**: `texto_promo` da tabela
- 💰 **Valor original**: `valor_antes` (riscado)
- 💰 **Valor promocional**: `valor_promo` (destaque)
- 🔢 **Contador**: `contador` de unidades restantes
- ⏰ **Auto-fechamento**: Após 30 segundos
- 🖱️ **Clique para fechar**: Clique fora do card

## 🎨 Responsividade:

- **Mobile** (< 768px): Card compacto
- **Tablet** (768-1024px): Card médio
- **Desktop** (> 1024px): Card grande
- **Adaptação automática** ao tamanho da tela
