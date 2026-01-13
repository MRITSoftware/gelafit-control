# 📱 Tuya Installer APK - Guia Completo

## O que este APK faz?

Este app Android automatiza a instalação do servidor Tuya no Termux do seu tablet. Ele:

1. ✅ **Copia os arquivos** Python para o Termux
2. ✅ **Fornece comandos prontos** para instalar Python e dependências
3. ✅ **Abre o Termux** automaticamente
4. ✅ **Guia passo a passo** a instalação

## 🚀 Como Gerar o APK

### Método 1: Android Studio (Mais Fácil)

1. **Baixe e instale o Android Studio:**
   - https://developer.android.com/studio

2. **Prepare os arquivos:**
   ```bash
   # Execute o script de preparação
   bash build_apk.sh
   ```
   Ou copie manualmente:
   - `tuya_server_enhanced.py` → `android_app/app/src/main/assets/`
   - `requirements.txt` → `android_app/app/src/main/assets/`
   - `start_server.sh` → `android_app/app/src/main/assets/`
   - `stop_server.sh` → `android_app/app/src/main/assets/`

3. **Abra o projeto:**
   - Android Studio → File → Open
   - Selecione a pasta `android_app`
   - Aguarde o Gradle sincronizar

4. **Gere o APK:**
   - Build → Build Bundle(s) / APK(s) → Build APK(s)
   - Aguarde a compilação
   - O APK estará em: `android_app/app/build/outputs/apk/debug/app-debug.apk`

5. **Instale no tablet:**
   - Transfira o APK para o tablet (USB, email, etc)
   - No tablet: Configurações → Segurança → Ativar "Fontes desconhecidas"
   - Abra o arquivo APK e instale

### Método 2: Linha de Comando (Gradle)

**Pré-requisitos:**
- Java JDK 11+
- Android SDK (via Android Studio ou standalone)

```bash
# 1. Prepare os arquivos
bash build_apk.sh

# 2. Entre no diretório
cd android_app

# 3. Gere o APK
./gradlew assembleDebug

# 4. O APK estará em:
# app/build/outputs/apk/debug/app-debug.apk
```

### Método 3: Build Online (Sem instalar Android Studio)

Você pode usar serviços como:
- **GitHub Actions** (se tiver o código no GitHub)
- **Appetize.io** para testar
- Ou pedir para alguém compilar para você

## 📲 Como Usar o APK

1. **Instale o Termux primeiro:**
   - Baixe do F-Droid (recomendado): https://f-droid.org/packages/com.termux/
   - Ou da Play Store (versão limitada)

2. **Abra o app "Tuya Installer"**

3. **Siga os passos na ordem:**
   - **1️⃣ Abrir Termux** - Abre o Termux
   - **2️⃣ Copiar Arquivos** - Copia os arquivos Python para o Termux
   - **3️⃣ Instalar Python** - Mostra/compartilha comandos para instalar Python
   - **4️⃣ Instalar Dependências** - Mostra/compartilha comandos para instalar Flask e Tinytuya
   - **5️⃣ Iniciar Servidor** - Mostra/compartilha comando para iniciar o servidor

4. **Cole os comandos no Termux** quando solicitado

## ⚙️ Funcionalidades do App

- **Cópia Automática:** Copia os arquivos Python diretamente para o Termux
- **Comandos Prontos:** Todos os comandos necessários em um só lugar
- **Interface Simples:** Botões grandes e fáceis de usar
- **Log de Operações:** Veja o que está acontecendo em tempo real
- **Compartilhamento:** Compartilhe comandos com outros apps

## 🔧 Estrutura do Projeto

```
android_app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/tuya/installer/
│   │       │   └── MainActivity.kt      # Código principal
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml  # Interface
│   │       │   └── values/
│   │       │       └── strings.xml
│   │       └── assets/                    # Arquivos Python (copiar aqui!)
│   │           ├── tuya_server_enhanced.py
│   │           ├── requirements.txt
│   │           ├── start_server.sh
│   │           └── stop_server.sh
│   └── build.gradle
├── build.gradle
└── settings.gradle
```

## ⚠️ Notas Importantes

1. **Permissões:** O app precisa acessar o Termux. Algumas operações podem precisar de permissões especiais.

2. **Termux API (Opcional):** Para execução automática de comandos, instale também o "Termux:Task" do F-Droid. Sem ele, o app copiará os comandos para você colar manualmente.

3. **Root não necessário:** O app funciona sem root, mas algumas operações podem precisar de permissões de escrita.

4. **Versão do Android:** Requer Android 7.0 (API 24) ou superior.

## 🐛 Troubleshooting

**Problema: "Termux não encontrado"**
- Instale o Termux primeiro do F-Droid ou Play Store

**Problema: "Erro ao copiar arquivos"**
- Verifique se o Termux está instalado
- Tente abrir o Termux manualmente primeiro
- Alguns dispositivos podem precisar de permissões especiais

**Problema: "Comandos não executam automaticamente"**
- Instale o "Termux:Task" do F-Droid
- Ou copie e cole os comandos manualmente no Termux

**Problema: APK não instala**
- Ative "Fontes desconhecidas" nas configurações de segurança
- Verifique se há espaço suficiente no dispositivo

## 📝 Próximos Passos Após Instalação

Depois de usar o app e instalar tudo:

1. No Termux, execute:
   ```bash
   python3 tuya_server_enhanced.py
   ```

2. Para rodar em background:
   ```bash
   pkg install tmux
   tmux new -d -s tuya 'python3 tuya_server_enhanced.py'
   ```

3. Para ver logs:
   ```bash
   tail -f tuya_server.log
   ```

4. Teste o servidor:
   ```bash
   curl http://localhost:8000/health
   ```

## 🎯 Resumo Rápido

1. Execute `bash build_apk.sh` para preparar
2. Abra Android Studio → Open → `android_app`
3. Build → Build APK(s)
4. Instale o APK no tablet
5. Use o app para instalar tudo no Termux
6. Pronto! 🎉

