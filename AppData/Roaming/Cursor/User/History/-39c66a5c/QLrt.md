# 🔄 Reinicialização Remota via Supabase

Este documento explica como configurar e usar a funcionalidade de reinicialização remota do dispositivo via comandos do Supabase.

## 📋 Visão Geral

O sistema agora suporta receber comandos do Supabase para reiniciar o dispositivo remotamente. Quando um comando de reinicialização é enviado para o Supabase, o app detecta e executa o reboot.

## ⚙️ Configuração

### 1. Configurar Credenciais do Supabase

Antes de usar, você precisa configurar as credenciais do Supabase no app. Existem duas formas:

#### Opção A: Configurar via Código (Temporário)

Edite o arquivo `SupabaseManager.kt` e substitua:

```kotlin
private const val SUPABASE_URL = "SUA_URL_DO_SUPABASE"
private const val SUPABASE_ANON_KEY = "SUA_CHAVE_ANON_DO_SUPABASE"
```

#### Opção B: Configurar via SharedPreferences (Recomendado)

Use o `PreferenceManager` para salvar as credenciais:

```kotlin
val prefs = PreferenceManager(context)
prefs.saveSupabaseUrl("https://seu-projeto.supabase.co")
prefs.saveSupabaseKey("sua-chave-anon-aqui")
```

### 2. Criar Tabela no Supabase

Crie uma tabela chamada `device_commands` no seu projeto Supabase com a seguinte estrutura:

```sql
CREATE TABLE device_commands (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id TEXT NOT NULL,
    command TEXT NOT NULL,
    executed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Índice para melhor performance
CREATE INDEX idx_device_commands_device_id ON device_commands(device_id);
CREATE INDEX idx_device_commands_executed ON device_commands(executed);
```

### 3. Habilitar Row Level Security (RLS) - Opcional

Se você quiser segurança adicional, configure RLS:

```sql
-- Permitir leitura para dispositivos autenticados (ajuste conforme necessário)
ALTER TABLE device_commands ENABLE ROW LEVEL SECURITY;

-- Política de exemplo (ajuste conforme sua necessidade)
CREATE POLICY "Devices can read their own commands"
ON device_commands FOR SELECT
USING (true); -- Ajuste para sua lógica de autenticação
```

## 🚀 Como Usar

### Enviar Comando de Reinicialização

Para reiniciar um dispositivo remotamente, insira um registro na tabela `device_commands`:

```sql
INSERT INTO device_commands (device_id, command)
VALUES ('device-id-aqui', 'reboot');
```

O `device_id` é o ID único do dispositivo Android (obtido via `Settings.Secure.ANDROID_ID`).

### Via Dashboard do Supabase

1. Acesse o dashboard do Supabase
2. Vá em "Table Editor" → "device_commands"
3. Clique em "Insert row"
4. Preencha:
   - `device_id`: ID do dispositivo (obtido nos logs do app)
   - `command`: `reboot`
5. Salve

### Via API REST

```bash
curl -X POST 'https://seu-projeto.supabase.co/rest/v1/device_commands' \
  -H "apikey: SUA_CHAVE_ANON" \
  -H "Content-Type: application/json" \
  -d '{
    "device_id": "device-id-aqui",
    "command": "reboot"
  }'
```

## 🔍 Como Funciona

1. **BootReceiver** inicia o `SupabaseCommandService` quando o dispositivo é ligado
2. **SupabaseCommandService** conecta ao Supabase e verifica comandos a cada 5 segundos
3. Quando encontra um comando `reboot` não executado:
   - Executa o reboot usando `DeviceRebootManager`
   - Marca o comando como executado no Supabase
4. O dispositivo reinicia

## ⚠️ Limitações e Requisitos

### Permissões Necessárias

Para reiniciar o dispositivo, o app precisa de uma das seguintes opções:

1. **App de Sistema**: O app deve ser assinado como app de sistema (requer firmware customizado)
2. **Root**: O dispositivo deve ter root e o app deve ter acesso ao `su`
3. **Permissão REBOOT**: Em alguns dispositivos, a permissão `android.permission.REBOOT` pode funcionar

### Verificar se Funciona

O app tenta reiniciar usando dois métodos:

1. **PowerManager.reboot()** - Requer app de sistema
2. **Runtime.exec("su -c reboot")** - Requer root

Se nenhum método funcionar, o app registrará um erro nos logs, mas não causará crash.

### Verificar Logs

Para verificar se o serviço está funcionando:

```bash
adb logcat | grep -E "SupabaseCommandService|DeviceRebootManager"
```

## 📝 Exemplo de Uso Completo

### 1. Obter Device ID

Primeiro, você precisa descobrir o `device_id` do dispositivo:

```bash
# Via ADB
adb shell settings get secure android_id

# Ou verifique os logs do app após iniciar
adb logcat | grep "device_id"
```

### 2. Enviar Comando

```sql
-- No Supabase SQL Editor
INSERT INTO device_commands (device_id, command)
VALUES ('abc123def456', 'reboot');
```

### 3. Verificar Execução

O comando será processado em até 5 segundos (intervalo de polling). Verifique os logs:

```bash
adb logcat | grep "Comando de reinicialização"
```

## 🔧 Troubleshooting

### O serviço não está conectando ao Supabase

- Verifique se as credenciais estão corretas
- Verifique se há conexão com internet
- Verifique os logs: `adb logcat | grep SupabaseCommandService`

### O comando não está sendo executado

- Verifique se o `device_id` está correto
- Verifique se o comando não foi marcado como `executed=true`
- Verifique os logs para erros

### O dispositivo não reinicia

- Verifique se o app tem permissões necessárias (root ou app de sistema)
- Verifique os logs: `adb logcat | grep DeviceRebootManager`
- O método de reinicialização pode não estar disponível no seu dispositivo

## 🔐 Segurança

⚠️ **IMPORTANTE**: Esta funcionalidade permite controle remoto do dispositivo. Certifique-se de:

1. Usar autenticação adequada no Supabase (RLS)
2. Validar o `device_id` antes de enviar comandos
3. Considerar adicionar autenticação adicional (tokens, assinaturas)
4. Limitar quem pode inserir comandos na tabela

## 📚 Arquivos Relacionados

- `SupabaseManager.kt` - Gerencia conexão com Supabase
- `SupabaseCommandService.kt` - Serviço que escuta comandos
- `DeviceRebootManager.kt` - Gerencia reinicialização do dispositivo
- `PreferenceManager.kt` - Armazena credenciais do Supabase
