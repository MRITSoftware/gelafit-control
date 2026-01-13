# Migração: Separação de Ciclo e Horário Diário

## 📋 O que foi alterado?

### Problema resolvido:
Antes, havia confusão entre:
- **Data do ciclo** (quando começa e termina a repetição - ex: 30 dias)
- **Horário diário** (que horas exibir - ex: 11:00 às 14:00)

### Solução:
Adicionados campos separados na tabela `agendamentos_playlist`:
- `ciclo_inicio` (DATE) - Data de início do ciclo
- `ciclo_fim` (DATE) - Data de fim do ciclo  
- `hora_inicio_diaria` (TIME) - Horário diário de início
- `hora_fim_diaria` (TIME) - Horário diário de fim

## 🚀 Como aplicar a migração:

### 1. Execute a migração no Supabase:
```sql
-- Execute o arquivo: database/migracao_agendamentos_recorrentes.sql
```

### 2. Atualize a função pg_cron:
```sql
-- Execute o arquivo: database/agendamentos_playlist_cron.sql
```

### 3. O frontend já está atualizado!
O código React já foi modificado para usar os novos campos.

## ✅ Validações implementadas:

1. **Horário de fim > Horário de início**
   - ❌ Erro: Início 03:20, Fim 03:15
   - ✅ Correto: Início 03:20, Fim 15:30

2. **Data fim >= Data início**
   - ❌ Erro: Início 08/02, Fim 08/01
   - ✅ Correto: Início 08/01, Fim 08/02

## 📝 Exemplo de uso:

**Cenário:** Exibir por 10 minutos, 3 dias da semana, por 30 dias

**Configuração:**
- Data início ciclo: `08/01/2026`
- Data fim ciclo: `07/02/2026` (30 dias depois)
- Horário início diário: `11:00`
- Horário fim diário: `11:10` (10 minutos depois)
- Dias da semana: `Quinta (4), Sexta (5), Segunda (1)`

**Resultado:**
- ✅ Exibe apenas nas Quintas, Sextas e Segundas
- ✅ Exibe apenas das 11:00 às 11:10 (10 minutos)
- ✅ Repete por 30 dias (até 07/02/2026)
- ✅ Após 07/02/2026, o ciclo se encerra automaticamente

## 🔄 Compatibilidade:

- ✅ **Dados antigos**: Continuam funcionando (fallback automático)
- ✅ **Novos dados**: Usam campos separados (mais claro)
- ✅ **Migração automática**: Dados existentes são migrados automaticamente

## 📊 Estrutura da tabela:

```sql
agendamentos_playlist (
  id,
  playlist_id,
  codigo_unico,
  inicio,              -- Mantido para compatibilidade
  fim,                 -- Mantido para compatibilidade
  ciclo_inicio,        -- NOVO: Data início do ciclo (DATE)
  ciclo_fim,           -- NOVO: Data fim do ciclo (DATE)
  hora_inicio_diaria,  -- NOVO: Horário diário início (TIME)
  hora_fim_diaria,     -- NOVO: Horário diário fim (TIME)
  recorrente,
  dias_semana,
  ...
)
```

