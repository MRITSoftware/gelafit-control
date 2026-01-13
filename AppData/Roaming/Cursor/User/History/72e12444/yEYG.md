# 📱 Servidorzinho - APK Android

APK que instala automaticamente todas as dependências e configura o servidor para rodar em segundo plano.

## 🎯 Funcionalidades

- ✅ Instala Termux automaticamente (se necessário)
- ✅ Instala Python e dependências
- ✅ Configura auto-inicialização
- ✅ Roda em background via serviço Android
- ✅ Monitora e reinicia automaticamente

## 🔨 Como Compilar o APK

### Pré-requisitos

1. **Android Studio** instalado
2. **JDK 11+** instalado
3. **Android SDK** configurado

### Passos

1. **Abra o projeto no Android Studio:**
   ```bash
   cd android
   # Abra no Android Studio: File → Open → Selecione a pasta android
   ```

2. **Copie os arquivos Python para assets:**
   ```bash
   # Crie a pasta assets se não existir
   mkdir -p app/src/main/assets
   
   # Copie os arquivos necessários
   cp ../servidor_auto.py app/src/main/assets/
   cp ../iniciar_auto.sh app/src/main/assets/
   cp ../parar.sh app/src/main/assets/
   cp ../INSTALAR_AUTO.sh app/src/main/assets/
   cp ../requirements.txt app/src/main/assets/
   cp ../setup_boot.sh app/src/main/assets/
   ```

3. **Compile o APK:**
   - No Android Studio: `Build → Build Bundle(s) / APK(s) → Build APK(s)`
   - Ou via linha de comando:
     ```bash
     ./gradlew assembleDebug
     ```

4. **O APK estará em:**
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

## 📦 Estrutura do Projeto

```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/servidorzinho/installer/
│   │   │   ├── MainActivity.java      # Tela principal
│   │   │   └── ServerService.java    # Serviço em background
│   │   ├── res/
│   │   │   └── layout/
│   │   │       └── activity_main.xml # Layout da tela
│   │   ├── assets/                   # Arquivos Python/scripts
│   │   └── AndroidManifest.xml
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## 🚀 Como Funciona

1. **Usuário instala o APK**
2. **Abre o app** → Verifica se Termux está instalado
3. **Clica em "Instalar"** → O app:
   - Instala Termux (se necessário)
   - Copia arquivos para `~/servidorzinho`
   - Executa `INSTALAR_AUTO.sh` via Termux API
   - Configura auto-inicialização
   - Inicia serviço em background

4. **Serviço em background:**
   - Monitora se o servidor está rodando
   - Reinicia automaticamente se parar
   - Mostra notificação permanente

## ⚙️ Configuração para Kiosque

Para usar em modo kiosque com o app `com.mrit.gelafitgo`:

### Opção 1: Launcher Kiosque

Configure o tablet para iniciar diretamente no app Gelafit Go, mas o serviço do servidorzinho continuará rodando em background.

### Opção 2: Modificar AndroidManifest

Adicione ao `AndroidManifest.xml` para iniciar automaticamente:

```xml
<receiver android:name=".BootReceiver"
    android:enabled="true"
    android:exported="true">
    <intent-filter>
        <action android:name="android.intent.action.BOOT_COMPLETED" />
    </intent-filter>
</receiver>
```

## 🔧 Personalização

### Mudar Package Name

Edite `app/build.gradle`:
```gradle
applicationId "com.seuapp.installer"
```

### Mudar Nome do App

Edite `app/src/main/res/values/strings.xml`:
```xml
<string name="app_name">Seu Nome</string>
```

## 📝 Notas Importantes

1. **Termux API**: O app usa Termux API para executar comandos. Certifique-se de que o Termux está instalado e tem permissões.

2. **Permissões**: O app precisa de permissões de armazenamento para copiar arquivos.

3. **Background**: O serviço roda em foreground (com notificação) para não ser morto pelo Android.

4. **Compatibilidade**: Testado em Android 7.0+ (API 24+).

## 🐛 Troubleshooting

### APK não instala
- Verifique se "Fontes desconhecidas" está habilitado
- Verifique se há espaço suficiente

### Termux não executa comandos
- Abra o Termux manualmente uma vez
- Verifique permissões do Termux

### Servidor não inicia
- Verifique logs: `tail -f ~/servidorzinho/servidor.log`
- Execute manualmente: `cd ~/servidorzinho && bash iniciar_auto.sh`

## 📞 Suporte

Para problemas, verifique:
1. Logs do Android: `adb logcat | grep Servidorzinho`
2. Logs do servidor: `tail -f ~/servidorzinho/servidor.log`
3. Status do Termux: Abra o Termux e verifique se está funcionando

