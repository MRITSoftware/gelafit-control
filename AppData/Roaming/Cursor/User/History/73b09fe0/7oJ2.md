# 📱 Como Criar o APK - Guia Completo

## 🎯 Objetivo

Criar um APK que instala automaticamente tudo e roda o servidor em segundo plano, sem precisar abrir o Termux manualmente.

## 📋 Pré-requisitos

1. **Android Studio** (última versão)
2. **JDK 11 ou superior**
3. **Android SDK** (via Android Studio)

## 🔨 Passo a Passo

### 1. Preparar Arquivos

```bash
# No diretório do projeto
cd android/app/src/main

# Criar pasta assets
mkdir -p assets

# Copiar arquivos necessários
cp ../../../../servidor_auto.py assets/
cp ../../../../iniciar_auto.sh assets/
cp ../../../../parar.sh assets/
cp ../../../../INSTALAR_AUTO.sh assets/
cp ../../../../setup_boot.sh assets/
cp ../../../../requirements.txt assets/
```

### 2. Abrir no Android Studio

1. Abra o Android Studio
2. **File → Open** → Selecione a pasta `android`
3. Aguarde o Gradle sincronizar

### 3. Compilar APK

**Opção A: Interface Gráfica**
- **Build → Build Bundle(s) / APK(s) → Build APK(s)**
- Aguarde a compilação
- O APK estará em: `app/build/outputs/apk/debug/app-debug.apk`

**Opção B: Linha de Comando**
```bash
cd android
./gradlew assembleDebug
```

### 4. Instalar no Tablet

```bash
# Via ADB
adb install app/build/outputs/apk/debug/app-debug.apk

# Ou transfira o APK e instale manualmente
```

## 🚀 Como Funciona

### Fluxo de Instalação

1. **Usuário instala o APK**
2. **Abre o app** → Tela simples com botão "Instalar"
3. **Clica em "Instalar"** → O app:
   - ✅ Verifica se Termux está instalado
   - ✅ Se não, abre Play Store para instalar
   - ✅ Copia todos os arquivos para `~/servidorzinho`
   - ✅ Executa `INSTALAR_AUTO.sh` via Termux API
   - ✅ Configura auto-inicialização
   - ✅ Inicia serviço em background

4. **Serviço em Background:**
   - Monitora o servidor a cada 30 segundos
   - Reinicia automaticamente se parar
   - Mostra notificação permanente

### Depois da Instalação

- **Não precisa abrir Termux**: O serviço inicia automaticamente
- **Reinicia sozinho**: Se o tablet reiniciar, o serviço inicia novamente
- **Monitora conexão**: Reinicia se a rede cair

## ⚙️ Configuração para Kiosque

### Integração com Gelafit Go

O servidor roda em background, então não interfere com o app principal (`com.mrit.gelafitgo`).

**Para garantir que inicie junto com o tablet:**

1. **Adicione Boot Receiver** (já incluído no código)
2. **Configure Launcher Kiosque** no tablet para abrir o Gelafit Go
3. **O servidorzinho inicia automaticamente** em background

### Modo Kiosque Completo

Se quiser que o servidorzinho também inicie automaticamente no boot:

1. Adicione ao `AndroidManifest.xml`:
```xml
<receiver android:name=".BootReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
```

2. Crie `BootReceiver.java`:
```java
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, ServerService.class);
        context.startForegroundService(serviceIntent);
    }
}
```

## 🔧 Personalização

### Mudar Nome do App

Edite `app/src/main/res/values/strings.xml`:
```xml
<resources>
    <string name="app_name">Servidorzinho</string>
</resources>
```

### Mudar Ícone

Substitua os arquivos em:
- `app/src/main/res/mipmap-hdpi/ic_launcher.png`
- `app/src/main/res/mipmap-mdpi/ic_launcher.png`
- etc.

### Mudar Package Name

1. Edite `app/build.gradle`:
```gradle
applicationId "com.seudominio.installer"
```

2. Mova os arquivos Java para o novo package
3. Atualize `AndroidManifest.xml`

## 📦 Distribuição

### Assinar APK para Produção

1. **Crie keystore:**
```bash
keytool -genkey -v -keystore servidorzinho.keystore -alias servidorzinho -keyalg RSA -keysize 2048 -validity 10000
```

2. **Configure `app/build.gradle`:**
```gradle
android {
    signingConfigs {
        release {
            storeFile file('servidorzinho.keystore')
            storePassword 'sua_senha'
            keyAlias 'servidorzinho'
            keyPassword 'sua_senha'
        }
    }
    buildTypes {
        release {
            signingConfig signingConfigs.release
            minifyEnabled false
        }
    }
}
```

3. **Compile release:**
```bash
./gradlew assembleRelease
```

## 🐛 Troubleshooting

### Erro: "Termux API not found"

**Solução**: O Termux precisa estar instalado. O app abre a Play Store automaticamente.

### Servidor não inicia automaticamente

**Solução**: 
1. Abra o Termux manualmente uma vez
2. Execute: `cd ~/servidorzinho && bash iniciar_auto.sh`
3. Depois o serviço Android cuida do resto

### APK muito grande

**Solução**: Os arquivos Python estão em `assets/`. Se necessário, compacte ou remova arquivos desnecessários.

### Permissões negadas

**Solução**: 
- Vá em Configurações → Apps → Servidorzinho → Permissões
- Ative todas as permissões necessárias

## 💡 Dicas

1. **Teste primeiro em debug**: Use `app-debug.apk` para testar
2. **Monitore logs**: Use `adb logcat` para ver logs do Android
3. **Verifique Termux**: Certifique-se de que o Termux funciona antes de distribuir
4. **Backup**: Faça backup do `local_config.json` antes de reinstalar

## 📞 Próximos Passos

Após criar o APK:

1. ✅ Teste em um tablet
2. ✅ Verifique se instala tudo corretamente
3. ✅ Teste reinicialização do tablet
4. ✅ Verifique se o servidor inicia automaticamente
5. ✅ Distribua para clientes

