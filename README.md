# Boot Receiver - Aplicativo Android para Digital Signage

## 📱 Descrição

Aplicativo Android desenvolvido em Kotlin para **Android Sticks/TV Box** usado em **Digital Signage**. O app inicia automaticamente um aplicativo específico após o boot do dispositivo, verificando primeiro se há conexão com internet.

## 🎯 Funcionalidades

- ✅ **Inicialização automática no boot**: Escuta o evento `BOOT_COMPLETED` e inicia o processo
- ✅ **Seleção de app na primeira vez**: Tela para escolher qual app será aberto automaticamente
- ✅ **Verificação de internet**: Aguarda conexão antes de abrir o app
- ✅ **Retry automático**: Tenta novamente se não houver internet (até 60 tentativas)
- ✅ **Sem interface visual**: Roda em segundo plano após configuração
- ✅ **Logs detalhados**: Facilita debug via Logcat
- ✅ **Compatível com Android TV/Stick**: Otimizado para dispositivos sem interação do usuário

## 🏗️ Estrutura do Projeto

```
app/
├── src/main/
│   ├── java/com/bootreceiver/app/
│   │   ├── BootReceiverApplication.kt      # Application class
│   │   ├── receiver/
│   │   │   └── BootReceiver.kt             # BroadcastReceiver para BOOT_COMPLETED
│   │   ├── service/
│   │   │   └── BootService.kt               # Serviço que verifica internet e abre app
│   │   ├── ui/
│   │   │   └── AppSelectionActivity.kt     # Tela de seleção de app (primeira vez)
│   │   └── utils/
│   │       ├── PreferenceManager.kt         # Gerenciamento de SharedPreferences
│   │       └── AppLauncher.kt              # Utilitário para abrir apps
│   ├── res/
│   │   ├── layout/
│   │   │   └── activity_app_selection.xml   # Layout da tela de seleção
│   │   └── values/
│   │       ├── strings.xml
│   │       └── themes.xml
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## 🚀 Build Automático (CI/CD)

Este projeto usa **GitHub Actions** para gerar APKs automaticamente a cada push.

### Como Funciona

1. **Push para o repositório** → GitHub Actions detecta automaticamente
2. **Build automático** → Compila o projeto e gera APKs (debug e release)
3. **Download dos APKs** → Disponível na aba **Actions** do GitHub

### 📥 Baixar APKs Gerados

1. Acesse: https://github.com/MRITSoftware/boot-receiver/actions
2. Clique no workflow mais recente (com ✓ verde)
3. Role até a seção **Artifacts**
4. Baixe:
   - `app-debug-apk` - Para testes
   - `app-release-apk` - Para produção

### 🔄 Executar Build Manualmente

1. Vá em **Actions** → **Build APK**
2. Clique em **Run workflow**
3. Escolha o tipo de build (debug/release/both)
4. Clique em **Run workflow**

📖 **Guia completo de deploy**: Veja [DEPLOY.md](DEPLOY.md)

## 🔧 Configuração

### 1. Permissões

O app requer as seguintes permissões (já configuradas no `AndroidManifest.xml`):

- `RECEIVE_BOOT_COMPLETED`: Para escutar o evento de boot
- `INTERNET`: Para verificar conexão
- `ACCESS_NETWORK_STATE`: Para verificar estado da rede
- `QUERY_ALL_PACKAGES`: Para listar apps instalados (primeira vez)

### 2. Build

```bash
# Build do projeto
./gradlew build

# Instalar no dispositivo
./gradlew installDebug
```

### 3. Primeira Execução

1. Instale o app no Android Stick
2. Abra o app manualmente (aparecerá a tela de seleção)
3. Escolha o aplicativo que deve ser aberto automaticamente
4. O app será salvo e usado nos próximos boots

## 📋 Como Funciona

### Fluxo de Execução

1. **Boot do dispositivo** → `BootReceiver` recebe `BOOT_COMPLETED`
2. **Verificação de configuração** → Verifica se há app configurado
3. **Se não configurado** → Abre `AppSelectionActivity`
4. **Se configurado** → Inicia `BootService`
5. **BootService**:
   - Aguarda 5 segundos (delay após boot)
   - Verifica conexão com internet
   - Se houver internet → Abre o app configurado
   - Se não houver → Aguarda 10 segundos e tenta novamente (até 60 tentativas)

### Componentes Principais

#### BootReceiver
- Escuta o evento `BOOT_COMPLETED`
- Verifica se há app configurado
- Inicia o serviço ou abre a tela de seleção

#### BootService
- Verifica internet usando `ConnectivityManager`
- Implementa retry com Coroutines
- Abre o app usando `AppLauncher`

#### AppSelectionActivity
- Lista todos os apps instalados
- Permite seleção do app alvo
- Salva a escolha em `SharedPreferences`

#### PreferenceManager
- Gerencia persistência de dados
- Salva/carrega o package name do app alvo

#### AppLauncher
- Verifica se o app está instalado
- Abre o app com as flags corretas (`FLAG_ACTIVITY_NEW_TASK`)

## 🔍 Debug

### Logs

O app gera logs detalhados no Logcat. Filtre por:

- `BootReceiver`: Logs do BroadcastReceiver
- `BootService`: Logs do serviço
- `AppSelectionActivity`: Logs da tela de seleção
- `AppLauncher`: Logs ao abrir apps
- `PreferenceManager`: Logs de configuração

### Comandos Úteis

```bash
# Ver logs em tempo real
adb logcat | grep -E "BootReceiver|BootService|AppSelection"

# Limpar configuração (força nova seleção)
adb shell pm clear com.bootreceiver.app

# Verificar se o receiver está registrado
adb shell dumpsys package com.bootreceiver.app | grep -A 5 "receiver"
```

## ⚙️ Configurações Avançadas

### Alterar Delay Após Boot

No arquivo `BootService.kt`, modifique:

```kotlin
private const val DELAY_AFTER_BOOT_MS = 5000L // 5 segundos
```

### Alterar Intervalo de Retry

No arquivo `BootService.kt`, modifique:

```kotlin
private const val RETRY_DELAY_MS = 10000L // 10 segundos
private const val MAX_RETRY_ATTEMPTS = 60 // Máximo de tentativas
```

### Configurar Package Name Manualmente

Para configurar o package name via código (útil para testes), adicione no `AppSelectionActivity`:

```kotlin
// Exemplo: Configurar Chrome
preferenceManager.saveTargetPackageName("com.android.chrome")
```

## 🚀 Melhorias para Digital Signage

### 1. Usar como Launcher

Para usar como launcher padrão, adicione no `AndroidManifest.xml`:

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.HOME" />
    <category android:name="android.intent.category.DEFAULT" />
</intent-filter>
```

### 2. Prevenir Sleep

Adicione no `BootService`:

```kotlin
window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
```

### 3. Monitoramento de Conexão

Implemente um `NetworkCallback` para monitorar mudanças de rede em tempo real.

### 4. Auto-restart do App Alvo

Adicione um `WatchdogService` que monitora se o app alvo está rodando e o reinicia se necessário.

## 🛡️ Boas Práticas para Android Sticks

1. **Desabilitar atualizações automáticas**: Evita reinicializações inesperadas
2. **Configurar modo Kiosk**: Use apps de gerenciamento de dispositivos (MDM)
3. **Desabilitar sleep**: Mantém o dispositivo sempre ligado
4. **Configurar WiFi para não dormir**: Evita perda de conexão
5. **Usar fonte de energia estável**: Evita quedas de energia
6. **Monitorar logs remotamente**: Use ferramentas como Firebase Crashlytics

## 📝 Notas Importantes

- **Android TV/Stick**: Testado e otimizado para dispositivos sem tela touch
- **24/7**: Projetado para rodar continuamente sem intervenção
- **Recuperação automática**: Se o app alvo fechar, o próximo boot o abrirá novamente
- **Sem interface**: Após configuração, o app roda completamente em background

## 🔒 Bloqueio de Tela

### O app funciona com tela bloqueada?

**Sim!** O `BOOT_COMPLETED` funciona mesmo com tela bloqueada. No entanto:

1. **Primeira instalação**: O app precisa ser aberto **manualmente pelo menos uma vez** após a instalação para o Android registrar o BroadcastReceiver. Após isso, funcionará automaticamente.

2. **Recomendação para Digital Signage**: 
   - **Desabilite o bloqueio de tela** para melhor experiência
   - Vá em: Configurações > Segurança > Bloqueio de tela > Nenhum
   - Ou use: `adb shell settings put secure lock_screen_lock_after_timeout 0`

3. **Tablets/Dispositivos com bloqueio**:
   - O app funcionará, mas o app alvo pode não abrir se a tela estiver bloqueada
   - **Solução**: Desabilite o bloqueio de tela ou configure para não bloquear automaticamente

### Como garantir que funcione

```bash
# Desabilitar bloqueio de tela (requer root ou ADB)
adb shell settings put secure lock_screen_lock_after_timeout 0

# Desabilitar sleep da tela
adb shell settings put system screen_off_timeout 2147483647

# Manter tela sempre ligada quando conectado
adb shell settings put global stay_on_while_plugged_in 7
```

## 🐛 Troubleshooting

### App não abre após boot

1. **Verifique se o app foi aberto manualmente após instalação** (necessário para registrar o receiver)
2. Verifique logs: `adb logcat | grep BootReceiver`
3. Verifique se o receiver está registrado: `adb shell dumpsys package com.bootreceiver.app | grep receiver`
4. Teste manualmente: `adb shell am broadcast -a android.intent.action.BOOT_COMPLETED`
5. Verifique se há app configurado: `adb shell run-as com.bootreceiver.app cat /data/data/com.bootreceiver.app/shared_prefs/BootReceiverPrefs.xml`

### Internet não detectada

1. Verifique permissões de rede
2. Teste conectividade: `adb shell ping 8.8.8.8`
3. Verifique se o WiFi está configurado para não dormir

### App alvo não encontrado

1. Verifique se o app está instalado: `adb shell pm list packages | grep <package>`
2. Verifique o package name salvo nas preferências

## 📄 Licença

Este projeto é fornecido como está, para uso em projetos de Digital Signage.

## 👨‍💻 Autor

Desenvolvido para uso em Android Sticks em ambientes de Digital Signage.

---

**Versão**: 1.0  
**Min SDK**: 21 (Android 5.0)  
**Target SDK**: 34 (Android 14)
