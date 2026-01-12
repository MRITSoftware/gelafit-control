# 💡 Exemplos de Uso e Personalização

## Configurar Package Name Programaticamente

Se você quiser configurar o package name diretamente no código (útil para testes ou distribuição pré-configurada):

### Opção 1: Modificar AppSelectionActivity

Adicione no `onCreate()` da `AppSelectionActivity`:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    
    // CONFIGURAÇÃO AUTOMÁTICA (descomente e ajuste)
    // val preferenceManager = PreferenceManager(this)
    // preferenceManager.saveTargetPackageName("com.android.chrome") // Exemplo: Chrome
    // finish()
    // return
    
    // ... resto do código
}
```

### Opção 2: Criar Activity de Configuração Admin

Crie uma nova Activity para administradores configurarem via código:

```kotlin
package com.bootreceiver.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bootreceiver.app.utils.PreferenceManager

class AdminConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val packageName = intent.getStringExtra("PACKAGE_NAME")
        if (packageName != null) {
            PreferenceManager(this).saveTargetPackageName(packageName)
            finish()
        }
    }
}
```

## Adicionar Notificação de Status

Para monitorar o status do serviço, adicione uma notificação no `BootService`:

```kotlin
private fun createNotification(): Notification {
    val channelId = "boot_service_channel"
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            channelId,
            "Boot Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
    
    return NotificationCompat.Builder(this, channelId)
        .setContentTitle("Boot Receiver")
        .setContentText("Aguardando conexão com internet...")
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setOngoing(true)
        .build()
}

override fun onStartCommand(...): Int {
    startForeground(1, createNotification())
    // ... resto do código
}
```

## Monitorar Mudanças de Rede em Tempo Real

Para reagir imediatamente quando a internet ficar disponível:

```kotlin
private val networkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
        Log.d(TAG, "Rede disponível!")
        // Tenta abrir o app imediatamente
        serviceScope.launch {
            tryOpenAppWithInternetCheck(targetPackageName)
        }
    }
    
    override fun onLost(network: Network) {
        Log.d(TAG, "Rede perdida")
    }
}

private fun registerNetworkCallback() {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val request = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
    connectivityManager.registerNetworkCallback(request, networkCallback)
}
```

## Adicionar Watchdog para Reiniciar App Alvo

Se o app alvo fechar, este serviço o reinicia automaticamente:

```kotlin
class AppWatchdogService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false
    
    private val checkRunnable = object : Runnable {
        override fun run() {
            if (isRunning) {
                checkAndRestartApp()
                handler.postDelayed(this, 30000) // Verifica a cada 30 segundos
            }
        }
    }
    
    private fun checkAndRestartApp() {
        val preferenceManager = PreferenceManager(this)
        val targetPackage = preferenceManager.getTargetPackageName() ?: return
        
        val isRunning = isAppRunning(targetPackage)
        if (!isRunning) {
            Log.d(TAG, "App alvo não está rodando. Reiniciando...")
            val appLauncher = AppLauncher(this)
            appLauncher.launchApp(targetPackage)
        }
    }
    
    private fun isAppRunning(packageName: String): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps = activityManager.getRunningAppProcesses()
        return runningApps?.any { it.processName == packageName } == true
    }
}
```

## Configurar como Launcher Padrão

Para usar o app como launcher padrão (kiosk mode):

### 1. Modificar AndroidManifest.xml

Adicione no `intent-filter` da `AppSelectionActivity`:

```xml
<activity
    android:name=".ui.AppSelectionActivity"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
        <!-- Adicionar estas linhas -->
        <category android:name="android.intent.category.HOME" />
        <category android:name="android.intent.category.DEFAULT" />
    </intent-filter>
</activity>
```

### 2. Tratar Botão Home

Na `AppSelectionActivity`, adicione:

```kotlin
override fun onBackPressed() {
    // Não permite voltar - força uso como launcher
    moveTaskToBack(true)
}

override fun onUserLeaveHint() {
    // Quando usuário pressiona Home, abre o app configurado
    val targetPackage = preferenceManager.getTargetPackageName()
    if (targetPackage != null) {
        val appLauncher = AppLauncher(this)
        appLauncher.launchApp(targetPackage)
    }
}
```

## Adicionar Delay Configurável

Permita configurar os delays via SharedPreferences:

```kotlin
// Em PreferenceManager.kt
fun saveBootDelay(delayMs: Long) {
    prefs.edit().putLong(KEY_BOOT_DELAY, delayMs).apply()
}

fun getBootDelay(): Long {
    return prefs.getLong(KEY_BOOT_DELAY, 5000L) // Default 5 segundos
}

// Em BootService.kt
private suspend fun processBootSequence() {
    val preferenceManager = PreferenceManager(this)
    val delay = preferenceManager.getBootDelay()
    delay(delay)
    // ... resto do código
}
```

## Logs Remotos via Firebase

Para monitorar dispositivos remotos:

### 1. Adicionar Firebase

No `build.gradle.kts`:

```kotlin
dependencies {
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")
}
```

### 2. Enviar Logs Customizados

```kotlin
import com.google.firebase.crashlytics.FirebaseCrashlytics

private fun logToFirebase(message: String, level: String = "INFO") {
    FirebaseCrashlytics.getInstance().log("[$level] $message")
    Log.d(TAG, message)
}
```

## Desabilitar Sleep da Tela

Para manter a tela sempre ligada:

```kotlin
// Em BootService ou AppSelectionActivity
window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

// Ou via código do sistema
Runtime.getRuntime().exec("svc power stayon true")
```

## Verificar e Atualizar App Alvo

Se o app alvo for desinstalado, detectar e permitir nova seleção:

```kotlin
private fun verifyTargetAppInstalled(): Boolean {
    val preferenceManager = PreferenceManager(this)
    val targetPackage = preferenceManager.getTargetPackageName() ?: return false
    
    val appLauncher = AppLauncher(this)
    return try {
        context.packageManager.getPackageInfo(targetPackage, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        Log.w(TAG, "App alvo não está mais instalado: $targetPackage")
        // Limpa configuração e abre tela de seleção
        preferenceManager.clearConfiguration()
        val intent = Intent(this, AppSelectionActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        false
    }
}
```

## Configuração via ADB

Para configurar remotamente via ADB:

```bash
# Salvar package name
adb shell run-as com.bootreceiver.app sh -c "echo 'com.android.chrome' > /data/data/com.bootreceiver.app/shared_prefs/BootReceiverPrefs.xml"

# Ou usar SharedPreferences diretamente
adb shell am start -a android.intent.action.MAIN -n com.bootreceiver.app/.ui.AdminConfigActivity --es PACKAGE_NAME "com.android.chrome"
```

## Testar Sem Reiniciar Dispositivo

Script para testar ciclo completo:

```bash
#!/bin/bash
# test_boot.sh

echo "1. Limpando configuração..."
adb shell pm clear com.bootreceiver.app

echo "2. Abrindo app para configurar..."
adb shell am start -n com.bootreceiver.app/.ui.AppSelectionActivity

echo "3. Aguardando 5 segundos..."
sleep 5

echo "4. Simulando boot..."
adb shell am broadcast -a android.intent.action.BOOT_COMPLETED

echo "5. Monitorando logs..."
adb logcat | grep -E "BootReceiver|BootService"
```

---

**Dica**: Teste cada modificação isoladamente antes de combinar múltiplas funcionalidades!
