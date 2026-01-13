# 🚀 Servidorzinho

Servidor HTTP local para controlar dispositivos Tuya, otimizado para rodar 24h em tablets Android.

## 📦 Arquivos Essenciais

- `servidor_auto.py` - Servidor com auto-reinício
- `iniciar_auto.sh` - Inicia o servidor em background
- `parar.sh` - Para o servidor
- `INSTALAR_AUTO.sh` - Instala dependências
- `setup_boot.sh` - Configura auto-inicialização
- `requirements.txt` - Dependências Python
- `preparar_apk.sh` - Prepara arquivos para compilar APK

## 📱 Criar APK

```bash
# 1. Preparar arquivos
bash preparar_apk.sh

# 2. Abrir no Android Studio
# File → Open → Pasta "android"

# 3. Compilar
# Build → Build Bundle(s) / APK(s) → Build APK(s)
```

Veja `CRIAR_APK.md` para detalhes.

## 🔧 Instalação Manual (Termux)

```bash
cd ~/servidorzinho
bash INSTALAR_AUTO.sh
servidor-auto  # Inicia o servidor
```

## 📡 API

- `GET /status` - Status do servidor
- `GET /devices` - Lista dispositivos
- `POST /command` - Envia comando para dispositivo Tuya

## 📝 Documentação

- `CRIAR_APK.md` - Guia completo para criar APK
- `RESUMO_APK.md` - Resumo rápido do APK

