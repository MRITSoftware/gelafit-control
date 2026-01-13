# Migração: Suporte a Múltiplos Agendamentos do Mesmo Conteúdo

## Problema
O sistema não estava inserindo vídeos na playlist se eles já existiam, mesmo quando eram de agendamentos diferentes.

## Solução
Criamos um campo `agendamento_id` na tabela `playlist_itens` que identifica qual agendamento adicionou cada item. Isso permite que:
- Múltiplos agendamentos adicionem o mesmo conteúdo à mesma playlist
- Cada agendamento tenha seu próprio registro em `playlist_itens`
- Remoções sejam específicas para cada agendamento

## Estrutura

### Tabela `agendamentos_playlist`
- Já possui campo `id` (chave primária) que identifica cada agendamento

### Tabela `playlist_itens`
- Campo `agendamento_id` (INTEGER, pode ser NULL)
  - Se preenchido: identifica qual agendamento adicionou este item
  - Se NULL: item foi adicionado manualmente
- Campo `data_adicao` (TIMESTAMP WITH TIME ZONE)
  - Data/hora em que o item foi adicionado à playlist
  - Usado para calcular quando remover baseado na duração de exibição

## Como Funciona

1. **Verificação de Existência**: A função `processar_agendamentos_playlist()` verifica se já existe um item com o mesmo `agendamento_id` específico
2. **Inserção**: Se não existir (`NOT FOUND`), adiciona o item com o `agendamento_id` do agendamento atual
3. **Múltiplos Agendamentos**: Se o mesmo conteúdo for agendado por múltiplos agendamentos, cada um terá seu próprio registro em `playlist_itens`
4. **Remoção**: Quando um agendamento expira ou é removido, apenas o item específico daquele agendamento é removido (usando `agendamento_id`)

## Migração

Execute o script `migracao_agendamento_id.sql` no Supabase SQL Editor para garantir que os campos existem:

```sql
-- O script garante que:
-- 1. Campo agendamento_id existe em playlist_itens
-- 2. Campo data_adicao existe em playlist_itens
-- 3. Índices são criados para melhor performance
-- 4. Dados existentes são atualizados
```

## Exemplo

**Cenário**: Dois agendamentos diferentes adicionam o mesmo vídeo à mesma playlist

- **Agendamento 1** (id=1): Vídeo A, Playlist X, Horário 10:00-12:00
- **Agendamento 2** (id=2): Vídeo A, Playlist X, Horário 14:00-16:00

**Resultado em `playlist_itens`**:
- Registro 1: `codigo_anuncio='A'`, `playlist_id='X'`, `agendamento_id=1` (adicionado às 10:00)
- Registro 2: `codigo_anuncio='A'`, `playlist_id='X'`, `agendamento_id=2` (adicionado às 14:00)

Ambos os registros existem simultaneamente na playlist, cada um controlado por seu agendamento específico.

## Verificação

Para verificar se está funcionando:

```sql
-- Ver itens na playlist com seus agendamentos
SELECT 
  pi.*,
  ap.id as agendamento_id,
  ap.titulo as agendamento_titulo
FROM playlist_itens pi
LEFT JOIN agendamentos_playlist ap ON pi.agendamento_id = ap.id
WHERE pi.playlist_id = 'SUA_PLAYLIST_ID'
ORDER BY pi.ordem;
```
