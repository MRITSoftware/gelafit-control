# 📦 Guia de Instalação e Teste

## Pré-requisitos

- Android Studio (versão mais recente recomendada)
- Android SDK instalado
- Dispositivo Android ou emulador (mínimo Android 5.0 / API 21)
- Para testar boot: dispositivo físico ou emulador com suporte a boot

## Instalação

### 1. Abrir o Projeto

1. Abra o Android Studio
2. Selecione `File > Open`
3. Navegue até a pasta do projeto e selecione-a
4. Aguarde o Gradle sincronizar as dependências

### 2. Configurar o Projeto

O projeto já está configurado, mas verifique:

- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### 3. Build do Projeto

```bash
# Via terminal
./gradlew build

# Ou via Android Studio
Build > Make Project
```

### 4. Instalar no Dispositivo

#### Via Android Studio:
1. Conecte o dispositivo via USB ou inicie um emulador
2. Clique em `Run > Run 'app'` ou pressione `Shift+F10`

#### Via ADB:
```bash
# Instalar APK de debug
./gradlew installDebug

# Ou gerar APK e instalar manualmente
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 🧪 Testes

### Teste 1: Primeira Execução (Seleção de App)

1. Instale o app no dispositivo
2. Abra o app manualmente (pelo launcher)
3. **Resultado esperado**: Deve aparecer uma lista de apps instalados
4. Selecione um app da lista
5. **Resultado esperado**: Toast confirmando a seleção e app fecha

### Teste 2: Verificar Configuração Salva

```bash
# Verificar se o package name foi salvo
adb shell run-as com.bootreceiver.app cat /data/data/com.bootreceiver.app/shared_prefs/BootReceiverPrefs.xml
```

### Teste 3: Simular Boot (Sem Reiniciar)

```bash
# Enviar broadcast de boot manualmente
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

# Verificar logs
adb logcat | grep -E "BootReceiver|BootService"
```

**Resultado esperado**:
- `BootReceiver` deve receber o broadcast
- `BootService` deve iniciar
- Após 5 segundos, deve verificar internet
- Se houver internet, deve abrir o app configurado

### Teste 4: Teste Real de Boot

1. Configure um app na primeira execução
2. Reinicie o dispositivo completamente
3. **Resultado esperado**: 
   - Após boot completo
   - Aguarda ~5 segundos
   - Verifica internet
   - Abre o app configurado automaticamente

### Teste 5: Teste Sem Internet

1. Configure um app
2. Desative WiFi/dados móveis
3. Simule boot: `adb shell am broadcast -a android.intent.action.BOOT_COMPLETED`
4. **Resultado esperado**:
   - Deve detectar falta de internet
   - Deve aguardar 10 segundos
   - Deve tentar novamente
   - Deve repetir até 60 tentativas ou até internet estar disponível

### Teste 6: Verificar Logs em Tempo Real

```bash
# Filtrar apenas logs do app
adb logcat | grep -E "BootReceiver|BootService|AppSelection|AppLauncher"

# Ou ver todos os logs e filtrar depois
adb logcat > logcat.txt
```

## 🔍 Debug

### Verificar se o Receiver está Registrado

```bash
adb shell dumpsys package com.bootreceiver.app | grep -A 10 "receiver"
```

### Verificar Permissões

```bash
adb shell dumpsys package com.bootreceiver.app | grep permission
```

### Limpar Dados do App (Resetar Configuração)

```bash
# Limpa dados e força nova seleção
adb shell pm clear com.bootreceiver.app
```

### Verificar Apps Instalados

```bash
# Listar todos os apps
adb shell pm list packages

# Verificar se um app específico está instalado
adb shell pm list packages | grep <package_name>
```

## ⚠️ Problemas Comuns

### 1. Receiver não recebe BOOT_COMPLETED

**Causa**: App não foi aberto manualmente pelo menos uma vez após instalação.

**Solução**: 
- Abra o app manualmente uma vez
- Reinicie o dispositivo
- Verifique se o receiver está registrado

### 2. App não abre após boot

**Verificações**:
- App está configurado? (`adb shell run-as com.bootreceiver.app cat ...`)
- App alvo está instalado?
- Há logs de erro no Logcat?

### 3. Internet não detectada

**Verificações**:
- WiFi está conectado?
- Permissões de rede estão concedidas?
- Teste: `adb shell ping 8.8.8.8`

### 4. Lista de apps vazia

**Causa**: Permissão `QUERY_ALL_PACKAGES` pode não estar funcionando em Android 11+

**Solução**: 
- Verifique se a permissão está no manifest
- Em Android 11+, pode ser necessário adicionar o app na lista de exceções

## 📱 Testando em Android TV/Stick

### Configurações Recomendadas

1. **Desabilitar Sleep**:
   ```bash
   adb shell settings put global stay_on_while_plugged_in 7
   ```

2. **Manter WiFi Ativo**:
   ```bash
   adb shell settings put global wifi_sleep_policy 2
   ```

3. **Desabilitar Atualizações Automáticas**:
   - Configurações > Sistema > Atualizações automáticas > Desativar

### Teste Remoto

Para testar em um Android Stick remoto:

1. Conecte via ADB over WiFi:
   ```bash
   adb connect <IP_DO_STICK>:5555
   ```

2. Siga os mesmos passos de teste acima

## 🚀 Deploy em Produção

### Gerar APK de Release

1. Configure uma keystore (se ainda não tiver):
   ```bash
   keytool -genkey -v -keystore bootreceiver.keystore -alias bootreceiver -keyalg RSA -keysize 2048 -validity 10000
   ```

2. Configure `app/build.gradle.kts` com informações da keystore

3. Gere APK:
   ```bash
   ./gradlew assembleRelease
   ```

4. APK estará em: `app/build/outputs/apk/release/app-release.apk`

### Instalação em Múltiplos Dispositivos

```bash
# Instalar em todos os dispositivos conectados
adb devices | grep device | awk '{print $1}' | xargs -I {} adb -s {} install app-release.apk
```

## 📊 Monitoramento

### Logs Persistentes

Para salvar logs em arquivo:

```bash
adb logcat -f bootreceiver.log | grep -E "BootReceiver|BootService"
```

### Verificar Status do Serviço

```bash
adb shell dumpsys activity services com.bootreceiver.app
```

---

**Dica**: Mantenha os logs sempre visíveis durante os testes iniciais para identificar problemas rapidamente!
