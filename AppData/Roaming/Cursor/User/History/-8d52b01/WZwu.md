# 🎯 Instruções para Testar a Funcionalidade de Promoção

## 🚀 Como Executar o Projeto

### Opção 1: Usando o Servidor Python (Recomendado)
1. **Instale o Python** (se não tiver): https://python.org
2. **Execute o servidor**:
   - **Windows**: Clique duplo em `start_server.bat`
   - **Outros sistemas**: Execute `python server.py` no terminal
3. **Acesse**: http://localhost:8000

### Opção 2: Usando Live Server (VS Code)
1. Instale a extensão "Live Server" no VS Code
2. Clique com botão direito em `index.html`
3. Selecione "Open with Live Server"

## 🔧 Configuração da Promoção

### 1. No Banco de Dados (Supabase)
Certifique-se de que existe um registro na tabela `promos`:

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
  'PROMO001',           -- Este deve ser igual ao codigo_conteudoAtual
  'PROMO001',
  'Promoção especial de verão!',
  9990,                 -- R$ 99,90 (em centavos)
  20000,                -- R$ 200,00 (em centavos)
  'https://exemplo.com/imagem.jpg',
  5,
  'promocao'
);
```

### 2. Na Tabela de Conteúdos
Certifique-se de que existe um registro na tabela `conteudos`:

```sql
INSERT INTO conteudos (
  codigoAnuncio,
  tipo,
  url
) VALUES (
  'PROMO001',           -- Deve ser igual ao id_promo
  'Promo',              -- Tipo deve ser exatamente "Promo"
  'https://exemplo.com'
);
```

### 3. Na Tabela de Displays
Configure o `codigo_conteudoAtual` para apontar para a promoção:

```sql
UPDATE displays 
SET codigo_conteudoAtual = 'PROMO001' 
WHERE codigo_unico = 'SEU_CODIGO_TELA';
```

## 🐛 Debug e Verificação

### 1. Abra o Console do Navegador (F12)
Procure por estas mensagens:
- ✅ `"Conteúdo encontrado:"` - Confirma que o conteúdo foi carregado
- ✅ `"Tipo do conteúdo: Promo"` - Confirma que o tipo está correto
- ✅ `"Promoção encontrada:"` - Confirma que a promoção foi encontrada no banco

### 2. Possíveis Problemas
- ❌ **"Promoção não encontrada"**: Verifique se existe registro na tabela `promos`
- ❌ **"Tipo não é Promo"**: Verifique se o tipo está exatamente como "Promo"
- ❌ **Erro de CORS**: Use o servidor local, não abra o arquivo diretamente

## 📱 Testando a Responsividade

A tela de promoção se adapta automaticamente:
- **Mobile** (< 768px): Card menor, mais compacto
- **Tablet** (768-1024px): Card médio
- **Desktop** (> 1024px): Card grande

## ⏰ Funcionalidades da Promoção

- **Auto-fechamento**: Fecha automaticamente após 30 segundos
- **Clique para fechar**: Clique fora do card para fechar
- **Valores formatados**: Converte centavos para reais automaticamente
- **Imagem responsiva**: Adapta-se ao tamanho do card
- **Contador**: Exibe o número de unidades restantes

## 🔄 Fluxo Completo

1. Usuário insere código da tela
2. Sistema busca na tabela `displays`
3. Pega o `codigo_conteudoAtual`
4. Busca na tabela `conteudos` pelo `codigoAnuncio`
5. Se `tipo = "Promo"`, busca na tabela `promos` pelo `id_promo`
6. Exibe a tela de promoção com todos os dados

## 📞 Suporte

Se ainda não funcionar, verifique:
1. ✅ Está usando servidor local (não file://)
2. ✅ Existe registro na tabela `promos`
3. ✅ Existe registro na tabela `conteudos` com tipo "Promo"
4. ✅ O `codigo_conteudoAtual` aponta para o código correto
5. ✅ Console do navegador mostra as mensagens de debug
