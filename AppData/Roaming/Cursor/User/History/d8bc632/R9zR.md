# 📱 Configuração de Dispositivos - MRIT Control

Este documento explica como configurar e gerenciar dispositivos usando a nova tabela `devices` no Supabase.

## 🎯 Visão Geral

O sistema agora possui um registro automático de dispositivos que:
- **Registra automaticamente** cada dispositivo na primeira conexão
- **Permite configurar um nome** para cada unidade (ex: "Sala 01", "Recepção")
- **Atualiza automaticamente** o `last_seen` quando o dispositivo se conecta
- **Facilita o gerenciamento** de múltiplos dispositivos

## 🗄️ Estrutura do Banco de Dados

### Tabela `devices`

Armazena informações de cada dispositivo:

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | UUID | ID único do registro |
| `device_id` | TEXT | Android ID único do dispositivo (único) |
| `unit_name` | TEXT | Nome personalizado da unidade (opcional) |
| `registered_at` | TIMESTAMP | Data/hora do primeiro registro |
| `last_seen` | TIMESTAMP | Última vez que o dispositivo se conectou |
| `is_active` | BOOLEAN | Se o dispositivo está ativo |
| `created_at` | TIMESTAMP | Data de criação do registro |
| `updated_at` | TIMESTAMP | Última atualização |

### Tabela `device_commands`

Armazena comandos enviados para os dispositivos:

| Coluna | Tipo | Descrição |
|--------|------|-----------|
| `id` | UUID | ID único do comando |
| `device_id` | TEXT | Referência ao `device_id` da tabela `devices` |
| `command` | TEXT | Tipo de comando (ex: "reboot") |
| `executed` | BOOLEAN | Se o comando foi executado |
| `executed_at` | TIMESTAMP | Quando o comando foi executado |
| `created_at` | TIMESTAMP | Quando o comando foi criado |

## 🚀 Setup Inicial

### 1. Executar Script SQL

Execute o script `SUPABASE_SETUP.sql` no SQL Editor do Supabase:

1. Acesse o dashboard do Supabase: https://kihyhoqbrkwbfudttevo.supabase.co
2. Vá em **SQL Editor**
3. Cole o conteúdo do arquivo `SUPABASE_SETUP.sql`
4. Clique em **Run**

Isso criará:
- Tabela `devices`
- Tabela `device_commands`
- Índices para performance
- Triggers para atualização automática

### 2. Configuração no App

As credenciais do Supabase já estão configuradas no código:
- **URL**: `https://kihyhoqbrkwbfudttevo.supabase.co`
- **Anon Key**: Configurada no `SupabaseManager.kt`

Não é necessário configurar nada manualmente!

## 📋 Como Funciona

### Registro Automático

1. **Primeira vez que o app é aberto:**
   - O app detecta que o dispositivo não está registrado
   - Registra automaticamente na tabela `devices` com o Android ID
   - Mostra um diálogo para configurar o nome da unidade (opcional)

2. **Próximas conexões:**
   - O app atualiza automaticamente o `last_seen`
   - Mantém o registro sincronizado

### Configurar Nome da Unidade

O nome da unidade pode ser configurado de duas formas:

#### Opção 1: Via App (Primeira Vez)
- Quando o app é aberto pela primeira vez, um diálogo aparece
- Digite o nome da unidade (ex: "Sala 01", "Recepção")
- Clique em "Salvar"

#### Opção 2: Via Supabase Dashboard
```sql
UPDATE devices 
SET unit_name = 'Sala de Reuniões 01' 
WHERE device_id = 'abc123def456';
```

#### Opção 3: Via API REST
```bash
curl -X PATCH 'https://kihyhoqbrkwbfudttevo.supabase.co/rest/v1/devices?device_id=eq.abc123def456' \
  -H "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{"unit_name": "Sala 01"}'
```

## 🔍 Consultas Úteis

### Ver Todos os Dispositivos Registrados

```sql
SELECT 
    device_id,
    unit_name,
    registered_at,
    last_seen,
    is_active
FROM devices
ORDER BY last_seen DESC;
```

### Ver Dispositivos Ativos

```sql
SELECT 
    device_id,
    unit_name,
    last_seen
FROM devices
WHERE is_active = true
ORDER BY last_seen DESC;
```

### Ver Comandos Pendentes com Nome da Unidade

```sql
SELECT 
    dc.id,
    dc.device_id,
    d.unit_name,
    dc.command,
    dc.executed,
    dc.created_at
FROM device_commands dc
JOIN devices d ON dc.device_id = d.device_id
WHERE dc.executed = false
ORDER BY dc.created_at DESC;
```

### Encontrar Device ID de um Dispositivo

```sql
-- Por nome da unidade
SELECT device_id, unit_name, last_seen
FROM devices
WHERE unit_name ILIKE '%Sala 01%';

-- Por último visto (mais recente)
SELECT device_id, unit_name, last_seen
FROM devices
ORDER BY last_seen DESC
LIMIT 1;
```

## 📤 Enviar Comandos

### Via SQL

```sql
-- Reiniciar dispositivo específico por device_id
INSERT INTO device_commands (device_id, command)
VALUES ('abc123def456', 'reboot');

-- Reiniciar dispositivo por nome da unidade
INSERT INTO device_commands (device_id, command)
SELECT device_id, 'reboot'
FROM devices
WHERE unit_name = 'Sala 01';
```

### Via API REST

```bash
curl -X POST 'https://kihyhoqbrkwbfudttevo.supabase.co/rest/v1/device_commands' \
  -H "apikey: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "device_id": "abc123def456",
    "command": "reboot"
  }'
```

### Via Dashboard

1. Acesse **Table Editor** → **device_commands**
2. Clique em **Insert row**
3. Preencha:
   - `device_id`: ID do dispositivo (obtenha da tabela `devices`)
   - `command`: `reboot`
4. Salve

## 🔧 Gerenciamento de Dispositivos

### Desativar um Dispositivo

```sql
UPDATE devices
SET is_active = false
WHERE device_id = 'abc123def456';
```

### Reativar um Dispositivo

```sql
UPDATE devices
SET is_active = true
WHERE device_id = 'abc123def456';
```

### Remover um Dispositivo

```sql
-- Remove dispositivo e todos os seus comandos (cascade)
DELETE FROM devices
WHERE device_id = 'abc123def456';
```

### Ver Histórico de Comandos

```sql
SELECT 
    dc.*,
    d.unit_name
FROM device_commands dc
JOIN devices d ON dc.device_id = d.device_id
WHERE dc.device_id = 'abc123def456'
ORDER BY dc.created_at DESC;
```

## 📊 Monitoramento

### Dispositivos que Não se Conectaram Recentemente

```sql
SELECT 
    device_id,
    unit_name,
    last_seen,
    NOW() - last_seen AS tempo_desconectado
FROM devices
WHERE is_active = true
  AND last_seen < NOW() - INTERVAL '1 hour'
ORDER BY last_seen ASC;
```

### Estatísticas de Comandos

```sql
SELECT 
    d.unit_name,
    COUNT(dc.id) AS total_comandos,
    COUNT(CASE WHEN dc.executed = true THEN 1 END) AS executados,
    COUNT(CASE WHEN dc.executed = false THEN 1 END) AS pendentes
FROM devices d
LEFT JOIN device_commands dc ON d.device_id = dc.device_id
GROUP BY d.device_id, d.unit_name
ORDER BY total_comandos DESC;
```

## ⚠️ Troubleshooting

### Dispositivo Não Está Aparecendo na Tabela

1. Verifique se o app foi aberto pelo menos uma vez
2. Verifique os logs: `adb logcat | grep DeviceRegistry`
3. Verifique se há conexão com internet
4. Verifique se as credenciais do Supabase estão corretas

### Nome da Unidade Não Está Sendo Atualizado

1. Verifique se o dispositivo está registrado: `SELECT * FROM devices WHERE device_id = '...'`
2. Verifique os logs: `adb logcat | grep DeviceRegistry`
3. Tente atualizar manualmente via SQL

### Comandos Não Estão Sendo Executados

1. Verifique se o `device_id` está correto
2. Verifique se o comando não foi marcado como `executed = true`
3. Verifique os logs: `adb logcat | grep SupabaseCommandService`
4. Verifique se o serviço está rodando

## 🔐 Segurança

⚠️ **IMPORTANTE**: As credenciais estão hardcoded no app para facilitar o uso, mas em produção você pode querer:

1. **Habilitar Row Level Security (RLS)** no Supabase
2. **Usar autenticação** para proteger os dados
3. **Validar device_id** antes de executar comandos
4. **Limitar permissões** de escrita na tabela

Para habilitar RLS, descomente as linhas no script SQL.

## 📚 Arquivos Relacionados

- `SUPABASE_SETUP.sql` - Script de criação das tabelas
- `DeviceRegistry.kt` - Gerencia registro de dispositivos
- `SupabaseManager.kt` - Gerencia conexão com Supabase
- `SupabaseCommandService.kt` - Escuta e processa comandos
- `AppSelectionActivity.kt` - Interface para configurar nome da unidade
