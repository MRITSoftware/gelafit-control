# 🚀 Início Rápido - APK Tuya Installer

## Resumo

Criei um **app Android** que automatiza a instalação do servidor Tuya no Termux do seu tablet!

## 📦 O que foi criado:

1. **App Android completo** (`android_app/`)
   - Interface simples com botões
   - Copia arquivos automaticamente
   - Fornece comandos prontos

2. **Scripts de build** (`build_apk.sh`)
   - Prepara tudo automaticamente

3. **Documentação completa**
   - `README_APK.md` - Guia completo
   - `BUILD_APK.md` - Como gerar o APK

## ⚡ Passos Rápidos:

### 1. Preparar os arquivos:
```bash
bash build_apk.sh
```

### 2. Gerar o APK (escolha uma opção):

**Opção A - Android Studio (Recomendado):**
- Abra Android Studio
- File → Open → `android_app`
- Build → Build APK(s)
- Pronto! APK em `android_app/app/build/outputs/apk/debug/`

**Opção B - Linha de Comando:**
```bash
cd android_app
./gradlew assembleDebug
```

### 3. Instalar no tablet:
- Transfira o APK para o tablet
- Ative "Fontes desconhecidas" nas configurações
- Instale o APK

### 4. Usar o app:
1. Instale o **Termux** primeiro (F-Droid ou Play Store)
2. Abra o app "Tuya Installer"
3. Siga os botões na ordem:
   - 1️⃣ Abrir Termux
   - 2️⃣ Copiar Arquivos
   - 3️⃣ Instalar Python
   - 4️⃣ Instalar Dependências
   - 5️⃣ Iniciar Servidor

## 🎯 Funcionalidades do App:

✅ **Copia arquivos** Python para o Termux automaticamente  
✅ **Comandos prontos** - só copiar e colar  
✅ **Interface simples** - botões grandes e fáceis  
✅ **Log em tempo real** - vê o que está acontecendo  
✅ **Fallback inteligente** - funciona mesmo sem permissões especiais  

## 📱 Estrutura:

```
android_app/
├── app/src/main/
│   ├── java/com/tuya/installer/
│   │   └── MainActivity.kt          # Código do app
│   ├── res/layout/
│   │   └── activity_main.xml        # Interface
│   └── assets/                      # ⚠️ COPIE OS ARQUIVOS AQUI!
│       ├── tuya_server_enhanced.py
│       ├── requirements.txt
│       ├── start_server.sh
│       └── stop_server.sh
```

## ⚠️ Importante:

Antes de compilar, **copie os arquivos para assets**:
```bash
mkdir -p android_app/app/src/main/assets
cp tuya_server_enhanced.py android_app/app/src/main/assets/
cp requirements.txt android_app/app/src/main/assets/
cp start_server.sh android_app/app/src/main/assets/
cp stop_server.sh android_app/app/src/main/assets/
```

Ou simplesmente execute: `bash build_apk.sh` (faz isso automaticamente!)

## 🐛 Problemas?

- **Termux não encontrado?** Instale do F-Droid primeiro
- **Erro ao copiar?** O app copia para Downloads como fallback
- **APK não instala?** Ative "Fontes desconhecidas"

## 📚 Mais informações:

- `README_APK.md` - Guia completo e detalhado
- `BUILD_APK.md` - Instruções de build
- `README_TABLET.md` - Guia geral para tablet

---

**Pronto para usar! 🎉**

