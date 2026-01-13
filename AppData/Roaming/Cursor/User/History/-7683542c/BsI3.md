# 🚀 Como Compilar e Instalar - Passo a Passo

## ⚠️ IMPORTANTE

Você **NÃO pode** simplesmente copiar os arquivos para o tablet. É preciso **COMPILAR o APK** primeiro no Android Studio.

## 📋 Passo a Passo

### 1️⃣ Preparar Arquivos

No seu computador (Windows/Linux/Mac), execute:

```bash
bash preparar_apk.sh
```

Isso copia os arquivos Python para dentro do projeto Android.

### 2️⃣ Instalar Android Studio

Se ainda não tem:
1. Baixe: https://developer.android.com/studio
2. Instale e abra
3. Na primeira vez, ele vai baixar o Android SDK (pode demorar)

### 3️⃣ Abrir Projeto

1. Abra o **Android Studio**
2. **File → Open**
3. Selecione a pasta **`android`** (não a pasta raiz!)
4. Aguarde o Gradle sincronizar (pode demorar na primeira vez)

### 4️⃣ Compilar APK

**Opção A: Pela Interface**
- Menu: **Build → Build Bundle(s) / APK(s) → Build APK(s)**
- Aguarde (pode levar alguns minutos)
- Quando terminar, clique em **"locate"** no aviso que aparecer

**Opção B: Linha de Comando**
```bash
cd android
./gradlew assembleDebug
```

### 5️⃣ Encontrar o APK

O APK estará em:
```
android/app/build/outputs/apk/debug/app-debug.apk
```

### 6️⃣ Instalar no Tablet

**Opção A: Via USB (ADB)**
```bash
adb install android/app/build/outputs/apk/debug/app-debug.apk
```

**Opção B: Transferir Manualmente**
1. Copie o arquivo `app-debug.apk` para o tablet (USB, email, etc)
2. No tablet, ative **"Fontes desconhecidas"** nas configurações
3. Abra o arquivo APK e instale

## ✅ Depois de Instalar

1. Abra o app **"Servidorzinho"** no tablet
2. Clique em **"Instalar"**
3. Siga as instruções na tela
4. Pronto! O servidor roda automaticamente

## 🐛 Problemas?

### "Gradle sync failed"
- Verifique sua conexão com internet
- Tente: **File → Invalidate Caches → Restart**

### "SDK not found"
- **Tools → SDK Manager** → Instale o Android SDK

### "Build failed"
- Verifique se copiou os arquivos: `bash preparar_apk.sh`
- Verifique se a pasta `android/app/src/main/assets/` tem os arquivos

## 💡 Dica

Se você não tem Android Studio instalado, pode pedir para alguém compilar o APK para você, ou usar um serviço online de compilação (menos recomendado).

