# Configuração do Supabase para Reiniciar Dispositivo

Este documento explica como configurar as tabelas no Supabase para permitir comandos de reiniciar dispositivo remotamente.

## ⚠️ IMPORTANTE: Estrutura Correta

O código do app usa a tabela `device_commands` (não `reboot_commands`). Certifique-se de usar a estrutura correta abaixo.

## Estrutura das Tabelas

Você precisa criar duas tabelas no seu banco de dados Supabase:

### 1. Tabela `devices` (Registro de Dispositivos)

```sql
CREATE TABLE devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id TEXT NOT NULL UNIQUE,
    unit_name TEXT,
    registered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_seen TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Índices para melhorar performance
CREATE INDEX idx_devices_device_id ON devices(device_id);
CREATE INDEX idx_devices_is_active ON devices(is_active);
```

### 2. Tabela `device_commands` (Comandos para Dispositivos)

```sql
CREATE TABLE device_commands (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id TEXT NOT NULL,
    command TEXT NOT NULL,
    executed BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    executed_at TIMESTAMP WITH TIME ZONE
);

-- Índices para melhorar performance nas consultas
CREATE INDEX idx_device_commands_device_id ON device_commands(device_id);
CREATE INDEX idx_device_commands_pending ON device_commands(device_id, command, executed) 
WHERE executed = false;
```

**📝 Nota:** O código procura por `command = 'reboot'` e `executed = false`.

## Como usar

### 1. Obter o Device ID

O app usa o Android ID como identificador único do dispositivo. Para obter o Device ID de um dispositivo:

**Via ADB:**
```bash
adb shell settings get secure android_id
```

**Via Logs do App:**
1. Abra o app no dispositivo
2. Verifique os logs do Android (usando `adb logcat` ou Android Studio)
3. Procure por logs com a tag `DeviceIdManager` ou `RebootMonitorService`
4. O Device ID será exibido nos logs

### 2. Criar um comando de reiniciar

Para reiniciar um dispositivo, insira um registro na tabela `device_commands`:

```sql
INSERT INTO device_commands (device_id, command, executed)
VALUES ('SEU_DEVICE_ID_AQUI', 'reboot', false);
```

**⚠️ IMPORTANTE:** O campo `command` deve ser exatamente `'reboot'` (em minúsculas).

### 3. Verificar status

Para verificar se um comando foi executado:

```sql
SELECT * FROM device_commands 
WHERE device_id = 'SEU_DEVICE_ID_AQUI' 
ORDER BY created_at DESC;
```

## Permissões RLS (Row Level Security)

Se você estiver usando Row Level Security no Supabase, você precisará configurar políticas para permitir que o app leia e atualize os registros:

```sql
-- Habilitar RLS
ALTER TABLE device_commands ENABLE ROW LEVEL SECURITY;
ALTER TABLE devices ENABLE ROW LEVEL SECURITY;

-- Política para permitir leitura de comandos
CREATE POLICY "Permitir leitura de comandos"
ON device_commands
FOR SELECT
USING (true);

-- Política para permitir atualização de comandos
CREATE POLICY "Permitir atualização de comandos"
ON device_commands
FOR UPDATE
USING (true);

-- Política para permitir leitura de dispositivos
CREATE POLICY "Permitir leitura de dispositivos"
ON devices
FOR SELECT
USING (true);

-- Política para permitir inserção/atualização de dispositivos
CREATE POLICY "Permitir escrita de dispositivos"
ON devices
FOR ALL
USING (true);
```

**Nota:** Essas políticas são muito permissivas. Para produção, considere adicionar autenticação ou restrições mais específicas.

## Configuração do Device Admin

**IMPORTANTE:** Para que o reinício funcione, o app precisa ser configurado como Device Admin no dispositivo Android:

1. Quando o app iniciar pela primeira vez, ele solicitará permissão de Device Admin
2. O usuário precisa aceitar essa permissão
3. Sem essa permissão, o app não conseguirá reiniciar o dispositivo
4. **Após instalar/atualizar o app, é necessário reativar o Device Admin** para que a política `<reboot />` seja aplicada

## Fluxo de Funcionamento

1. O app inicia o `RebootMonitorService` automaticamente
2. O serviço verifica o Supabase a cada 30 segundos
3. Se encontrar um comando com `command = 'reboot'` e `executed = false` para o Device ID do dispositivo:
   - Marca o comando como executado (`executed = true`, `executed_at = NOW()`)
   - Reinicia o dispositivo
4. Quando o dispositivo reinicia, o `BootReceiver` detecta o boot e executa o processo normal (abre o app configurado)

## Troubleshooting

### O dispositivo não reinicia

- Verifique se o Device Admin está ativo (o app solicitará quando necessário)
- Verifique os logs para erros
- Certifique-se de que o Device ID está correto no banco de dados

### O comando não é detectado

- Verifique se o `device_id` no banco corresponde ao Android ID do dispositivo
- Verifique se há conexão com internet
- Verifique os logs do `RebootMonitorService`

### Erro de conexão com Supabase

- Verifique se a URL e a Key do Supabase estão corretas no código
- Verifique se há conexão com internet
- Verifique as políticas RLS se estiver usando
