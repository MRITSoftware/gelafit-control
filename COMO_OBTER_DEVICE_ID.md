# Como Obter o Device ID (ID do Dispositivo)

O **Device ID** é um identificador único do dispositivo Android usado para enviar comandos de reiniciar via Supabase.

## Método 1: Via Interface do App (Mais Fácil)

1. Abra o app **MRIT Control** no dispositivo
2. Na tela de seleção de app, você verá o Device ID no **rodapé** da tela
3. **Toque no Device ID** para ver um diálogo com mais informações
4. Clique em **"Copiar"** para copiar o ID para a área de transferência
5. Cole o ID no Supabase quando for criar um comando de reiniciar

## Método 2: Via Logs do Android

Se você tem acesso ao Android Studio ou `adb`:

1. Conecte o dispositivo via USB (ou use WiFi debugging)
2. Abra o terminal e execute:
   ```bash
   adb logcat | grep -i "DeviceIdManager\|RebootMonitorService"
   ```
3. Procure por uma linha que mostre: `Device ID obtido: [SEU_DEVICE_ID]`
4. O Device ID será exibido nos logs

## Método 3: Via Código Android

O Device ID é obtido usando o **Android ID** do sistema:

```kotlin
val androidId = Settings.Secure.getString(
    contentResolver,
    Settings.Secure.ANDROID_ID
)
```

Este é um ID único por dispositivo que:
- É o mesmo mesmo após reinstalar o app
- É diferente em cada dispositivo
- Pode ser `null` em dispositivos muito antigos (raro)

## Como Usar o Device ID

Depois de obter o Device ID, você pode usá-lo no Supabase para criar comandos de reiniciar:

```sql
INSERT INTO reboot_commands (device_id, should_reboot, executed)
VALUES ('SEU_DEVICE_ID_AQUI', true, false);
```

## Observações Importantes

- O Device ID é **único por dispositivo**
- Cada dispositivo Android tem um Android ID diferente
- O mesmo dispositivo sempre terá o mesmo Android ID (mesmo após reinstalar apps)
- Se você tiver múltiplos dispositivos, cada um terá um Device ID diferente

## Exemplo de Device ID

Um Device ID típico se parece com:
```
a1b2c3d4e5f6g7h8
```

Ou seja, uma string hexadecimal de 16 caracteres.
