# 🔧 Solução para App Não Abrir Automaticamente no Boot

## ⚠️ Problema Comum

O app não abre automaticamente após reiniciar o dispositivo, mesmo tendo configurado um app alvo.

## ✅ Solução

### 1. Abrir o App Manualmente Após Instalação

**IMPORTANTE**: O Android requer que você abra o app **manualmente pelo menos uma vez** após a instalação para registrar o BroadcastReceiver.

**Passos:**
1. Instale o app
2. **Abra o app manualmente** (pelo launcher)
3. Configure o app alvo (se ainda não configurou)
4. Agora o app funcionará automaticamente nos próximos boots

### 2. Verificar se o Receiver Está Registrado

```bash
# Verificar se o receiver está registrado
adb shell dumpsys package com.bootreceiver.app | grep -A 10 "receiver"

# Deve mostrar algo como:
# Receiver #0: com.bootreceiver.app/.receiver.BootReceiver
```

### 3. Testar Manualmente

```bash
# Simular boot sem reiniciar
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

# Verificar logs
adb logcat | grep -E "BootReceiver|BootService"
```

### 4. Verificar Configuração

```bash
# Ver se há app configurado
adb shell run-as com.bootreceiver.app cat /data/data/com.bootreceiver.app/shared_prefs/BootReceiverPrefs.xml
```

## 🔒 Sobre Bloqueio de Tela

### O app funciona com tela bloqueada?

**Sim**, o `BOOT_COMPLETED` funciona mesmo com tela bloqueada. No entanto:

- O **BootReceiver** recebe o evento mesmo com tela bloqueada
- O **BootService** inicia mesmo com tela bloqueada
- Mas o **app alvo pode não abrir** se a tela estiver bloqueada (depende do app)

### Recomendação para Digital Signage

**Desabilite o bloqueio de tela** para garantir que tudo funcione perfeitamente:

```bash
# Desabilitar bloqueio de tela
adb shell settings put secure lock_screen_lock_after_timeout 0

# Desabilitar sleep da tela
adb shell settings put system screen_off_timeout 2147483647

# Manter tela sempre ligada quando conectado
adb shell settings put global stay_on_while_plugged_in 7
```

### Para Tablets/Dispositivos com Bloqueio

1. **Opção 1 (Recomendada)**: Desabilite o bloqueio de tela completamente
2. **Opção 2**: Configure para não bloquear automaticamente
3. **Opção 3**: Use um app de gerenciamento de dispositivos (MDM) para configurar modo Kiosk

## 🐛 Troubleshooting Detalhado

### Problema: App não abre após boot

**Causas possíveis:**

1. **App não foi aberto manualmente após instalação**
   - ✅ **Solução**: Abra o app manualmente uma vez

2. **Receiver não está registrado**
   - ✅ **Solução**: Verifique com `dumpsys package`
   - ✅ **Solução**: Reinstale o app e abra manualmente

3. **App alvo não está configurado**
   - ✅ **Solução**: Abra o app e configure um app alvo

4. **Permissões não concedidas**
   - ✅ **Solução**: Verifique se todas as permissões foram concedidas

5. **Tela bloqueada impedindo abertura do app alvo**
   - ✅ **Solução**: Desabilite o bloqueio de tela

### Verificar Logs

```bash
# Ver todos os logs do app
adb logcat | grep -E "BootReceiver|BootService|AppSelection"

# Ver apenas erros
adb logcat | grep -E "BootReceiver|BootService" | grep -i error

# Salvar logs em arquivo
adb logcat -f bootreceiver.log | grep -E "BootReceiver|BootService"
```

### Testar Passo a Passo

1. **Limpar dados do app** (força nova configuração):
   ```bash
   adb shell pm clear com.bootreceiver.app
   ```

2. **Abrir o app manualmente**:
   ```bash
   adb shell am start -n com.bootreceiver.app/.ui.AppSelectionActivity
   ```

3. **Configurar um app alvo** (via interface ou código)

4. **Simular boot**:
   ```bash
   adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
   ```

5. **Verificar se funcionou**:
   ```bash
   adb logcat | grep BootReceiver
   ```

## 📝 Checklist de Configuração

- [ ] App instalado
- [ ] App aberto manualmente pelo menos uma vez
- [ ] App alvo configurado
- [ ] Receiver registrado (verificar com `dumpsys`)
- [ ] Bloqueio de tela desabilitado (recomendado)
- [ ] Sleep da tela desabilitado (recomendado)
- [ ] WiFi configurado para não dormir (recomendado)
- [ ] Testado com `adb shell am broadcast`

## 💡 Dicas Importantes

1. **Sempre abra o app manualmente após instalação** - isso é obrigatório no Android
2. **Desabilite o bloqueio de tela** para Digital Signage - garante funcionamento perfeito
3. **Monitore os logs** durante os primeiros testes
4. **Teste com `adb broadcast`** antes de reiniciar o dispositivo real

---

**Lembre-se**: O Android requer que o app seja aberto manualmente uma vez após a instalação para registrar o BroadcastReceiver. Isso é uma limitação de segurança do sistema Android.
