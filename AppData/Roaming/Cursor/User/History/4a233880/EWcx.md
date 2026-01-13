# Como Fazer o App Iniciar Automaticamente no Android

## 📱 Requisitos

Para fazer o app Android iniciar automaticamente quando o dispositivo ligar, você precisa:

1. **Permissão RECEIVE_BOOT_COMPLETED** no AndroidManifest.xml
2. **BroadcastReceiver** que escuta o evento de boot
3. **Intent** para abrir a Activity principal quando o boot for completado

## 🔧 Implementação

### 1. Adicionar Permissão no AndroidManifest.xml

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.mrit.player">

    <!-- Permissão para receber evento de boot -->
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    
    <!-- Permissão para abrir app automaticamente (Android 10+) -->
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme">
        
        <!-- Activity principal -->
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:launchMode="singleTask"
            android:screenOrientation="landscape"
            android:configChanges="orientation|screenSize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <!-- BroadcastReceiver para boot -->
        <receiver
            android:name=".BootReceiver"
            android:enabled="true"
            android:exported="true"
            android:permission="android.permission.RECEIVE_BOOT_COMPLETED">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.QUICKBOOT_POWERON" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </receiver>
        
    </application>
</manifest>
```

### 2. Criar BootReceiver (Java)

**Arquivo: `app/src/main/java/com/mrit/player/BootReceiver.java`**

```java
package com.mrit.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {
            
            Log.d(TAG, "Boot completado, iniciando app...");
            
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            
            context.startActivity(mainIntent);
        }
    }
}
```

### 3. Criar BootReceiver (Kotlin)

**Arquivo: `app/src/main/java/com/mrit/player/BootReceiver.kt`**

```kotlin
package com.mrit.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.QUICKBOOT_POWERON") {
            
            Log.d(TAG, "Boot completado, iniciando app...")
            
            val mainIntent = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            
            context.startActivity(mainIntent)
        }
    }
    
    companion object {
        private const val TAG = "BootReceiver"
    }
}
```

### 4. Modificar MainActivity para Auto-Start

**Exemplo de MainActivity (Java):**

```java
package com.mrit.player;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private static final String URL = "https://www.muraltv.com.br/";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebViewClient(new WebViewClient());
        
        // Carregar URL
        webView.loadUrl(URL);
    }
    
    @Override
    public void onBackPressed() {
        // Desabilitar botão voltar para modo kiosk
        // Não fazer nada ou fechar app
        finish();
    }
}
```

## ⚠️ Limitações do Android Moderno

### Android 10+ (API 29+)
- Apps precisam ser instalados como "Apps do sistema" ou ter permissões especiais
- Usuário pode precisar dar permissão manualmente nas configurações

### Android 8.0+ (API 26+)
- Apps em background têm limitações
- Pode ser necessário adicionar notificação persistente

### Solução Alternativa: Kiosk Mode
Para garantir que o app sempre inicie, considere usar **Kiosk Mode**:

1. **App Lock** - Tornar o app como "App de tela inicial"
2. **Device Owner** - Configurar como dispositivo gerenciado (requer reset de fábrica)
3. **Launcher Customizado** - Criar launcher que sempre abre o app

## 🔐 Permissões Adicionais Necessárias

Alguns fabricantes (Xiaomi, Huawei, Samsung) têm proteções extras:

### Xiaomi
- Adicionar app à lista de "Apps protegidos"
- Permitir "Iniciar automaticamente"

### Huawei
- Adicionar app à lista de "Apps protegidos"
- Permitir "Iniciar automaticamente"

### Samsung
- Adicionar app à lista de "Apps que não serão colocados em espera"

## 📝 Checklist de Implementação

- [ ] Adicionar permissão `RECEIVE_BOOT_COMPLETED` no AndroidManifest.xml
- [ ] Criar classe `BootReceiver`
- [ ] Registrar `BootReceiver` no AndroidManifest.xml
- [ ] Testar em dispositivo físico (emulador pode não funcionar)
- [ ] Verificar permissões em dispositivos de fabricantes específicos
- [ ] Configurar app como "App de tela inicial" (opcional, para Kiosk Mode)

## 🧪 Como Testar

1. **Instalar o app** no dispositivo Android
2. **Reiniciar o dispositivo**
3. **Verificar** se o app abre automaticamente após o boot

**Nota**: No emulador, o evento de boot pode não funcionar corretamente. Teste em dispositivo físico.

## 🔄 Alternativa: Usar Tasker ou Automate

Se não conseguir modificar o código do APK, você pode usar apps como:
- **Tasker** - Automatizar abertura do app no boot
- **Automate** - Criar fluxo para abrir app automaticamente
- **MacroDroid** - Automatizar ações no Android

Esses apps podem abrir qualquer app ou URL automaticamente após o boot.

