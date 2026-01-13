# Exemplos Completos de Código Android para Auto-Start

## 📦 Estrutura do Projeto Android

```
app/
├── src/
│   └── main/
│       ├── java/com/mrit/player/
│       │   ├── MainActivity.java (ou .kt)
│       │   └── BootReceiver.java (ou .kt)
│       ├── res/
│       │   ├── layout/
│       │   │   └── activity_main.xml
│       │   └── values/
│       │       └── strings.xml
│       └── AndroidManifest.xml
```

## 🔧 AndroidManifest.xml Completo

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.mrit.player">

    <!-- Permissões -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    
    <!-- Para Android 10+ -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/AppTheme"
        android:usesCleartextTraffic="true">
        
        <!-- Activity Principal -->
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:launchMode="singleTask"
            android:screenOrientation="landscape"
            android:configChanges="orientation|screenSize|keyboardHidden"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        
        <!-- BroadcastReceiver para Boot -->
        <receiver
            android:name=".BootReceiver"
            android:enabled="true"
            android:exported="true"
            android:permission="android.permission.RECEIVE_BOOT_COMPLETED">
            <intent-filter android:priority="1000">
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <action android:name="android.intent.action.QUICKBOOT_POWERON" />
                <action android:name="com.htc.intent.action.QUICKBOOT_POWERON" />
                <category android:name="android.intent.category.DEFAULT" />
            </intent-filter>
        </receiver>
        
    </application>
</manifest>
```

## 📱 MainActivity.java (Completo)

```java
package com.mrit.player;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private static final String URL = "https://www.muraltv.com.br/";
    
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Manter tela ligada
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        // Modo tela cheia
        hideSystemUI();
        
        setContentView(R.layout.activity_main);
        
        webView = findViewById(R.id.webview);
        
        // Configurar WebView
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        
        // Habilitar cache
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Injetar JavaScript para auto-início se necessário
                injectAutoStartScript();
            }
        });
        
        webView.setWebChromeClient(new WebChromeClient());
        
        // Carregar URL
        webView.loadUrl(URL);
    }
    
    private void injectAutoStartScript() {
        // Injetar código JavaScript para tentar auto-início
        String script = "javascript:(function() {" +
            "if (typeof tentarIniciarAutomaticamente === 'function') {" +
            "  setTimeout(tentarIniciarAutomaticamente, 2000);" +
            "}" +
            "})();";
        webView.evaluateJavascript(script, null);
    }
    
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    
    @Override
    public void onBackPressed() {
        // Desabilitar botão voltar (modo kiosk)
        // Para sair, use Alt+Tab ou feche o app pelas configurações
    }
    
    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
        hideSystemUI();
    }
}
```

## 📱 MainActivity.kt (Kotlin)

```kotlin
package com.mrit.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private val url = "https://www.muraltv.com.br/"
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Manter tela ligada
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        // Modo tela cheia
        hideSystemUI()
        
        setContentView(R.layout.activity_main)
        
        webView = findViewById(R.id.webview)
        
        // Configurar WebView
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            databaseEnabled = true
            mediaPlaybackRequiresUserGesture = false
            allowFileAccess = true
            allowContentAccess = true
            cacheMode = WebSettings.LOAD_DEFAULT
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            setAppCacheEnabled(true)
        }
        
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return true
            }
            
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                injectAutoStartScript()
            }
        }
        
        webView.webChromeClient = WebChromeClient()
        
        // Carregar URL
        webView.loadUrl(url)
    }
    
    private fun injectAutoStartScript() {
        val script = """
            javascript:(function() {
                if (typeof tentarIniciarAutomaticamente === 'function') {
                    setTimeout(tentarIniciarAutomaticamente, 2000);
                }
            })();
        """.trimIndent()
        webView.evaluateJavascript(script, null)
    }
    
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
        )
    }
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }
    
    override fun onBackPressed() {
        // Desabilitar botão voltar (modo kiosk)
    }
    
    override fun onDestroy() {
        if (::webView.isInitialized) {
            webView.destroy()
        }
        super.onDestroy()
    }
    
    override fun onPause() {
        super.onPause()
        if (::webView.isInitialized) {
            webView.onPause()
        }
    }
    
    override fun onResume() {
        super.onResume()
        if (::webView.isInitialized) {
            webView.onResume()
        }
        hideSystemUI()
    }
}
```

## 📡 BootReceiver.java

```java
package com.mrit.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.os.Handler;
import android.os.Looper;

public class BootReceiver extends BroadcastReceiver {
    private static final String TAG = "BootReceiver";
    private static final int DELAY_MS = 3000; // Aguardar 3 segundos após boot
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        
        if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
            "android.intent.action.QUICKBOOT_POWERON".equals(action) ||
            "com.htc.intent.action.QUICKBOOT_POWERON".equals(action)) {
            
            Log.d(TAG, "Boot completado detectado: " + action);
            
            // Aguardar um pouco antes de iniciar (para garantir que o sistema está pronto)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                try {
                    Intent mainIntent = new Intent(context, MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    
                    context.startActivity(mainIntent);
                    Log.d(TAG, "App iniciado com sucesso após boot");
                } catch (Exception e) {
                    Log.e(TAG, "Erro ao iniciar app após boot", e);
                }
            }, DELAY_MS);
        }
    }
}
```

## 📡 BootReceiver.kt

```kotlin
package com.mrit.player

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        
        if (action == Intent.ACTION_BOOT_COMPLETED ||
            action == "android.intent.action.QUICKBOOT_POWERON" ||
            action == "com.htc.intent.action.QUICKBOOT_POWERON") {
            
            Log.d(TAG, "Boot completado detectado: $action")
            
            // Aguardar 3 segundos antes de iniciar
            Handler(Looper.getMainLooper()).postDelayed({
                try {
                    val mainIntent = Intent(context, MainActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    }
                    
                    context.startActivity(mainIntent)
                    Log.d(TAG, "App iniciado com sucesso após boot")
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao iniciar app após boot", e)
                }
            }, DELAY_MS)
        }
    }
    
    companion object {
        private const val TAG = "BootReceiver"
        private const val DELAY_MS = 3000L
    }
}
```

## 📄 activity_main.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <WebView
        android:id="@+id/webview"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</RelativeLayout>
```

## 📄 strings.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="app_name">MRIT Player</string>
</resources>
```

## 🔧 build.gradle (app)

```gradle
android {
    compileSdkVersion 33
    
    defaultConfig {
        applicationId "com.mrit.player"
        minSdkVersion 21
        targetSdkVersion 33
        versionCode 1
        versionName "1.0"
    }
    
    buildTypes {
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}

dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
}
```

## ✅ Checklist de Teste

1. **Compilar e instalar o app** no dispositivo Android
2. **Reiniciar o dispositivo** completamente
3. **Verificar logs** usando `adb logcat | grep BootReceiver`
4. **Confirmar** que o app abre automaticamente após o boot

## 🐛 Troubleshooting

### App não inicia após boot
- Verificar se a permissão foi concedida
- Verificar logs: `adb logcat | grep BootReceiver`
- Testar em dispositivo físico (emulador pode não funcionar)

### App fecha imediatamente
- Verificar se há erros no WebView
- Verificar conexão com internet
- Verificar se a URL está acessível

### Permissões negadas
- Alguns fabricantes (Xiaomi, Huawei) têm proteções extras
- Adicionar app à lista de "Apps protegidos"
- Permitir "Iniciar automaticamente" nas configurações

