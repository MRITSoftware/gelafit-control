# 🚀 Solução: Auto-Start no Boot do Android

## ⚠️ Problema

O PWABuilder.com **não permite** adicionar código nativo (BootReceiver) diretamente. Mas há soluções!

## ✅ Solução 1: Modificar o APK Gerado (Recomendado)

### Passo a Passo:

1. **Gere o APK no PWABuilder.com**
   - Resolva os erros do manifest primeiro
   - Gere e baixe o projeto Android

2. **Baixe o Projeto Android Gerado**
   - O PWABuilder.com oferece download do projeto completo
   - Extraia o arquivo ZIP

3. **Adicione o BootReceiver**

   Crie o arquivo: `app/src/main/java/com/mrit/player/BootReceiver.java`
   
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
       private static final int DELAY_MS = 5000; // 5 segundos após boot
       
       @Override
       public void onReceive(Context context, Intent intent) {
           String action = intent.getAction();
           
           if (Intent.ACTION_BOOT_COMPLETED.equals(action) ||
               "android.intent.action.QUICKBOOT_POWERON".equals(action) ||
               "com.htc.intent.action.QUICKBOOT_POWERON".equals(action)) {
               
               Log.d(TAG, "Boot completado, iniciando app...");
               
               // Aguardar um pouco antes de iniciar
               new Handler(Looper.getMainLooper()).postDelayed(() -> {
                   try {
                       Intent mainIntent = new Intent(context, MainActivity.class);
                       mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                       mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       mainIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                       mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                       
                       context.startActivity(mainIntent);
                       Log.d(TAG, "App iniciado com sucesso");
                   } catch (Exception e) {
                       Log.e(TAG, "Erro ao iniciar app", e);
                   }
               }, DELAY_MS);
           }
       }
   }
   ```

4. **Modifique o AndroidManifest.xml**

   Adicione a permissão e o receiver:
   
   ```xml
   <manifest ...>
       <!-- Adicione esta permissão -->
       <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
       
       <application ...>
           <!-- Adicione este receiver -->
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
           
           <!-- Sua Activity principal já existe -->
       </application>
   </manifest>
   ```

5. **Compile o APK**
   - Abra o projeto no Android Studio
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Ou use: `./gradlew assembleRelease`

## ✅ Solução 2: Usar App de Automação (Mais Fácil)

### Opção A: Tasker (Pago, mas poderoso)

1. Instale o **Tasker** na Play Store
2. Crie um novo perfil:
   - Trigger: "Event" → "Device Boot"
   - Action: "Launch App" → Selecione "MRIT Player"
3. Pronto! Funciona sem modificar código

### Opção B: MacroDroid (Gratuito)

1. Instale o **MacroDroid** na Play Store
2. Crie uma macro:
   - Trigger: "Device Boot"
   - Action: "Launch Application" → "MRIT Player"
3. Pronto!

### Opção C: Automate (Gratuito)

1. Instale o **Automate** na Play Store
2. Crie um fluxo:
   - Bloco: "Device booted"
   - Bloco: "App start" → Selecione "MRIT Player"
3. Pronto!

## ✅ Solução 3: Script de Automação Simples

Se você tem acesso root ou pode usar ADB:

```bash
# Via ADB (requer conexão USB)
adb shell pm grant com.mrit.player android.permission.RECEIVE_BOOT_COMPLETED
```

Mas isso ainda requer o BootReceiver no código.

## 📋 Checklist para Modificar APK

- [ ] Gerar APK no PWABuilder.com
- [ ] Baixar projeto Android completo
- [ ] Criar arquivo BootReceiver.java
- [ ] Adicionar permissão no AndroidManifest.xml
- [ ] Registrar BootReceiver no AndroidManifest.xml
- [ ] Compilar APK no Android Studio
- [ ] Testar em dispositivo físico (reiniciar)

## 🎯 Recomendação

**Para começar rápido**: Use **MacroDroid** (gratuito e fácil)

**Para solução permanente**: Modifique o APK gerado pelo PWABuilder.com

## 🔍 Verificar se Funcionou

1. Instale o APK modificado
2. Reinicie o dispositivo completamente
3. Aguarde o Android iniciar completamente
4. **Resultado esperado**: App deve abrir automaticamente

## 🐛 Troubleshooting

### App não abre no boot
- Verifique logs: `adb logcat | grep BootReceiver`
- Verifique se a permissão foi concedida
- Alguns fabricantes (Xiaomi, Huawei) precisam de configuração extra

### Permissões de Fabricantes

**Xiaomi:**
- Configurações → Apps → Gerenciar apps → MRIT Player
- Permitir "Iniciar automaticamente"
- Adicionar à lista de "Apps protegidos"

**Huawei:**
- Configurações → Apps → MRIT Player
- Permitir "Iniciar automaticamente"
- Adicionar à lista de "Apps protegidos"

**Samsung:**
- Configurações → Apps → MRIT Player
- Desativar "Colocar app em espera"

## ✅ Resumo

**Opção Mais Fácil**: MacroDroid (5 minutos)
**Opção Permanente**: Modificar APK (30 minutos)

Ambas funcionam perfeitamente! 🎉

