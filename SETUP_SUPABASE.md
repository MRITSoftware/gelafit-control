# Configuração do Supabase para Reiniciar Dispositivo

Este documento explica como configurar a tabela no Supabase para permitir comandos de reiniciar dispositivo remotamente.

## Estrutura da Tabela

Você precisa criar uma tabela chamada `reboot_commands` no seu banco de dados Supabase com a seguinte estrutura:

### SQL para criar a tabela:

```sql
CREATE TABLE reboot_commands (
    id BIGSERIAL PRIMARY KEY,
    device_id TEXT NOT NULL,
    should_reboot BOOLEAN DEFAULT false,
    executed BOOLEAN DEFAULT false,
    created_at BIGINT DEFAULT EXTRACT(EPOCH FROM NOW()) * 1000,
    executed_at BIGINT
);

-- Índice para melhorar performance nas consultas
CREATE INDEX idx_reboot_commands_device_id ON reboot_commands(device_id);
CREATE INDEX idx_reboot_commands_pending ON reboot_commands(device_id, should_reboot, executed) 
WHERE should_reboot = true AND executed = false;
```

## Como usar

### 1. Obter o Device ID

O app usa o Android ID como identificador único do dispositivo. Para obter o Device ID de um dispositivo:

1. Abra o app no dispositivo
2. Verifique os logs do Android (usando `adb logcat` ou Android Studio)
3. Procure por logs com a tag `DeviceIdManager` ou `RebootMonitorService`
4. O Device ID será exibido nos logs

### 2. Criar um comando de reiniciar

Para reiniciar um dispositivo, insira um registro na tabela `reboot_commands`:

```sql
INSERT INTO reboot_commands (device_id, should_reboot, executed)
VALUES ('SEU_DEVICE_ID_AQUI', true, false);
```

### 3. Verificar status

Para verificar se um comando foi executado:

```sql
SELECT * FROM reboot_commands 
WHERE device_id = 'SEU_DEVICE_ID_AQUI' 
ORDER BY created_at DESC;
```

## Permissões RLS (Row Level Security)

Se você estiver usando Row Level Security no Supabase, você precisará configurar políticas para permitir que o app leia e atualize os registros:

```sql
-- Política para permitir leitura
CREATE POLICY "Permitir leitura de comandos"
ON reboot_commands
FOR SELECT
USING (true);

-- Política para permitir atualização
CREATE POLICY "Permitir atualização de comandos"
ON reboot_commands
FOR UPDATE
USING (true);
```

**Nota:** Essas políticas são muito permissivas. Para produção, considere adicionar autenticação ou restrições mais específicas.

## Configuração do Device Admin

**IMPORTANTE:** Para que o reinício funcione, o app precisa ser configurado como Device Admin no dispositivo Android:

1. Quando o app iniciar pela primeira vez, ele solicitará permissão de Device Admin
2. O usuário precisa aceitar essa permissão
3. Sem essa permissão, o app não conseguirá reiniciar o dispositivo

## Fluxo de Funcionamento

1. O app inicia o `RebootMonitorService` automaticamente
2. O serviço verifica o Supabase a cada 30 segundos
3. Se encontrar um comando com `should_reboot = true` e `executed = false` para o Device ID do dispositivo:
   - Marca o comando como executado
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
