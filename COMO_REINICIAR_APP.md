# 🔄 Como Reiniciar o App

## 📱 Métodos Disponíveis

### Método 1: Via Tela de Status (Manual)

1. Abra o app **MRIT Control**
2. Vá na tela de **Status**
3. Clique em **"Reiniciar App Agora"**
4. Confirme a ação
5. O app configurado será fechado e reaberto

### Método 2: Via Supabase (Remoto)

O app monitora comandos no Supabase a cada 30 segundos. Para reiniciar remotamente:

#### No Supabase SQL Editor:

```sql
INSERT INTO device_commands (device_id, command, executed)
VALUES ('SEU_DEVICE_ID_AQUI', 'restart_app', false);
```

**Substitua `SEU_DEVICE_ID_AQUI`** pelo Device ID do dispositivo (obtenha na tela de Status do app).

#### Exemplo:

```sql
-- Reiniciar app do dispositivo com ID específico
INSERT INTO device_commands (device_id, command, executed)
VALUES ('a2674df4a688c7d7', 'restart_app', false);
```

### Método 3: Via ADB (Se Disponível)

```bash
# Obter package name do app configurado
adb shell run-as com.bootreceiver.app cat /data/data/com.bootreceiver.app/shared_prefs/BootReceiverPrefs.xml | grep target_package_name

# Fechar o app
adb shell am force-stop <package_name>

# Reabrir o app
adb shell monkey -p <package_name> -c android.intent.category.LAUNCHER 1
```

## 🔍 Como Funciona

### Processo de Reinício

1. **Fecha processos** do app em background
2. **Aguarda 1 segundo** para garantir fechamento
3. **Reabre o app** com flags que forçam recriação completa
4. **Marca comando como executado** (se veio do Supabase)

### Limitações

- **Apps em foreground**: Pode não fechar completamente se estiver em primeiro plano
- **Apps protegidos**: Alguns apps podem não permitir fechamento forçado
- **Depende do app**: Alguns apps podem não recriar completamente ao reabrir

## 📋 Verificar Status

### Ver se Comando Foi Executado

No Supabase:

```sql
SELECT * FROM device_commands 
WHERE device_id = 'SEU_DEVICE_ID' 
  AND command = 'restart_app'
ORDER BY created_at DESC;
```

**Deve mostrar:**
- `executed = true` se foi executado
- `executed_at` com timestamp de quando foi executado

### Ver Logs

Via ADB:

```bash
adb logcat | grep -E "AppRestartMonitor|AppLauncher"
```

**Procure por:**
- `AppRestartMonitor: COMANDO DE REINICIAR APP ENCONTRADO!`
- `AppLauncher: ✅ App reiniciado com sucesso`

## ⚠️ Notas Importantes

1. **O app monitora a cada 30 segundos** - pode levar até 30 segundos para detectar o comando
2. **Reiniciar app não reinicia o dispositivo** - apenas fecha e reabre o app configurado
3. **Cada comando é executado UMA VEZ** - após executar, o comando é marcado como executado e não será executado novamente
4. **Para reiniciar novamente, crie um NOVO comando** - o mesmo comando não será executado duas vezes
5. **Se o app não fechar completamente**, pode não reiniciar corretamente
6. **Alguns apps podem não permitir fechamento** - depende das permissões do app

## 🛑 Se o App Continuar Reiniciando

Se o app reiniciar continuamente (loop), veja: [COMO_PARAR_REINICIAR.md](COMO_PARAR_REINICIAR.md)

**Solução rápida no Supabase:**
```sql
UPDATE device_commands 
SET executed = true, executed_at = NOW() 
WHERE command = 'restart_app' AND executed = false;
```

## 💡 Dica

Se o app não reiniciar corretamente:
- **Reinicie o dispositivo** - o BootReceiver abrirá o app automaticamente
- **Ou feche manualmente** o app e aguarde o próximo boot

---

**Simples e eficaz!** Use quando precisar reiniciar o app sem reiniciar o dispositivo inteiro.
