# 🔍 Diagnóstico - Dispositivo Não Reinicia

## ⚡ Verificação Rápida (5 minutos)

### 1. Verificar se Device Admin Está Ativo

```bash
adb shell dumpsys device_policy | grep -A 20 "Admin"
```

**Deve mostrar algo como:**
```
Admin #0: ComponentInfo{com.bootreceiver.app/com.bootreceiver.app.receiver.DeviceAdminReceiver}
```

**Se não mostrar**: O Device Admin não está ativo. Veja solução abaixo.

### 2. Verificar Logs do RebootMonitorService

```bash
# Limpar logs antigos
adb logcat -c

# Ver logs em tempo real (aguarde 30 segundos para ver um ciclo completo)
adb logcat | grep -E "RebootMonitorService|RebootManager|DeviceAdmin"
```

**O que procurar:**
- `RebootMonitorService: RebootMonitorService iniciado` - Serviço está rodando
- `RebootMonitorService: Device Admin ativo: true` - Device Admin está ativo
- `RebootMonitorService: COMANDO DE REINICIAR ENCONTRADO!` - Comando foi detectado
- `RebootManager: Comando de reiniciar enviado` - Comando foi enviado
- `RebootManager: Device Admin não está ativo` - **PROBLEMA**: Device Admin não está ativo

### 3. Verificar Device ID

```bash
adb shell settings get secure android_id
```

**Anote este ID** e verifique se corresponde ao `device_id` no comando do Supabase.

### 4. Verificar Comando no Supabase

Execute no Supabase SQL Editor:

```sql
SELECT * FROM device_commands 
WHERE device_id = 'SEU_DEVICE_ID_AQUI' 
  AND command = 'reboot' 
  AND executed = false
ORDER BY created_at DESC;
```

**Deve mostrar pelo menos um registro** com `executed = false`.

### 5. Verificar se Serviço Está Rodando

```bash
adb shell dumpsys activity services | grep -A 10 "RebootMonitorService"
```

**Deve mostrar** que o serviço está ativo.

## 🐛 Problemas Comuns e Soluções

### Problema 1: Device Admin Não Está Ativo ⚠️ **MAIS COMUM**

**Sintoma**: Logs mostram `Device Admin ativo: false`

**Solução**:
1. Abra o app no dispositivo
2. O app deve solicitar permissão de Device Admin automaticamente
3. Se não solicitar, vá em: **Configurações → Segurança → Administradores do dispositivo → MRIT Control** → Ativar
4. **IMPORTANTE**: Após ativar, o app precisa ser **reinstalado** ou o dispositivo precisa ser **reiniciado** para que a política `<reboot />` seja aplicada

**Verificar se está ativo:**
```bash
adb shell dumpsys device_policy | grep -A 5 "com.bootreceiver.app"
```

### Problema 2: Política de Reboot Não Configurada ⚠️ **CORRIGIDO**

**Sintoma**: Device Admin está ativo, mas `DevicePolicyManager.reboot()` não funciona

**Solução**: 
✅ **JÁ CORRIGIDO** - O arquivo `device_admin.xml` agora inclui `<reboot />`

**Mas se você já tinha o app instalado antes da correção:**
1. Desative o Device Admin: **Configurações → Segurança → Administradores do dispositivo → MRIT Control** → Desativar
2. Reinstale o app (ou faça rebuild)
3. Ative o Device Admin novamente

### Problema 3: Device ID Não Corresponde

**Sintoma**: Comandos no Supabase não são encontrados

**Solução**:
1. Obtenha o Device ID correto: `adb shell settings get secure android_id`
2. Verifique se o comando no Supabase usa o mesmo ID
3. O Device ID é o Android ID do dispositivo (não muda, a menos que o dispositivo seja resetado)

### Problema 4: Serviço Não Está Rodando

**Sintoma**: Logs não mostram `RebootMonitorService iniciado`

**Solução**:
1. Abra o app manualmente (isso inicia o serviço)
2. Ou reinicie o dispositivo (o BootReceiver inicia o serviço)
3. Verifique se há erros nos logs: `adb logcat | grep -E "RebootMonitorService|ERROR"`

### Problema 5: Fabricante Bloqueou Reboot

**Sintoma**: Device Admin está ativo, mas `DevicePolicyManager.reboot()` retorna erro ou não faz nada

**Soluções**:
1. Alguns fabricantes (Xiaomi, Huawei, Samsung) bloqueiam reboot remoto
2. Verifique os logs: `adb logcat | grep RebootManager`
3. Se mostrar `UnsupportedOperationException`, o dispositivo não suporta reboot via DevicePolicyManager
4. **Alternativa**: Alguns dispositivos requerem root para reboot remoto

### Problema 6: Comando Já Foi Executado

**Sintoma**: Comando no Supabase tem `executed = true`

**Solução**:
1. Crie um novo comando com `executed = false`
2. O serviço verifica a cada 30 segundos

## ✅ Checklist Completo para Reboot

Execute estes comandos em ordem:

```bash
# 1. Verificar Device Admin
adb shell dumpsys device_policy | grep -A 5 "com.bootreceiver.app"

# 2. Obter Device ID
adb shell settings get secure android_id

# 3. Verificar se serviço está rodando
adb shell dumpsys activity services | grep RebootMonitorService

# 4. Limpar logs
adb logcat -c

# 5. Monitorar logs (aguarde 30-60 segundos)
adb logcat | grep -E "RebootMonitorService|RebootManager|DeviceAdmin"

# 6. Verificar último comando no Supabase (via SQL Editor)
# SELECT * FROM device_commands WHERE device_id = 'SEU_DEVICE_ID' ORDER BY created_at DESC LIMIT 1;
```

## 🔧 Solução Passo a Passo

Se o dispositivo não está reiniciando, siga estes passos:

### Passo 1: Verificar Device Admin

```bash
# Verificar status
adb shell dumpsys device_policy | grep -A 10 "Admin"

# Se não estiver ativo, ative manualmente:
# 1. Abra o app no dispositivo
# 2. Vá em Configurações → Segurança → Administradores do dispositivo
# 3. Ative "MRIT Control"
```

### Passo 2: Reinstalar App (Importante!)

**Se você já tinha o app instalado antes da correção do `device_admin.xml`:**

```bash
# 1. Desinstalar
adb uninstall com.bootreceiver.app

# 2. Reinstalar (com a versão corrigida)
adb install app-debug.apk

# 3. Abrir app e ativar Device Admin
adb shell am start -n com.bootreceiver.app/.ui.AppSelectionActivity
```

### Passo 3: Verificar Device ID

```bash
# Obter Device ID
DEVICE_ID=$(adb shell settings get secure android_id)
echo "Device ID: $DEVICE_ID"

# Verificar no Supabase se há comando para este ID
# (Execute no SQL Editor do Supabase)
```

### Passo 4: Criar Comando de Teste

No Supabase SQL Editor:

```sql
-- Substitua 'SEU_DEVICE_ID' pelo ID obtido no passo 3
INSERT INTO device_commands (device_id, command, executed)
VALUES ('SEU_DEVICE_ID', 'reboot', false);
```

### Passo 5: Monitorar Logs

```bash
# Limpar logs
adb logcat -c

# Monitorar (aguarde até 60 segundos)
adb logcat | grep -E "RebootMonitorService|RebootManager"
```

**O que deve aparecer:**
1. `RebootMonitorService: RebootMonitorService iniciado`
2. `RebootMonitorService: Device Admin ativo: true`
3. `RebootMonitorService: COMANDO DE REINICIAR ENCONTRADO!`
4. `RebootManager: Comando de reiniciar enviado via DevicePolicyManager.reboot()`
5. Dispositivo deve reiniciar em alguns segundos

## 🚨 Se Ainda Não Funcionar

### Verificar Limitações do Fabricante

Alguns dispositivos Android TV/Stick não suportam `DevicePolicyManager.reboot()`, mesmo com Device Admin ativo.

**Teste manual:**
```bash
# Tentar reboot via ADB (requer root ou modo de desenvolvedor)
adb shell reboot
```

Se `adb shell reboot` funcionar mas o app não, é limitação do Android/DevicePolicyManager.

### Alternativas

1. **Usar root**: Se o dispositivo tiver root, o app tentará usar `su -c reboot`
2. **Reiniciar manualmente**: Em alguns casos, pode ser necessário reiniciar manualmente
3. **Usar app de terceiros**: Alguns apps de gerenciamento de dispositivos podem ter mais permissões

## 📝 Notas Importantes

1. **Device Admin precisa ser ativado manualmente** na primeira vez
2. **Após corrigir `device_admin.xml`, o app precisa ser reinstalado** para aplicar a política `<reboot />`
3. **O serviço verifica comandos a cada 30 segundos** - aguarde até 60 segundos após criar o comando
4. **Alguns fabricantes bloqueiam reboot remoto** - pode não funcionar em todos os dispositivos
5. **Device ID não muda** a menos que o dispositivo seja resetado para fabrica

---

**Execute os comandos acima e me envie os resultados para diagnóstico preciso!**
