# MRIT Tuya Gateway - Android APK

Gateway local Tuya para Android que roda um servidor HTTP na porta 8080 para controlar dispositivos Tuya na rede local.

## 📋 Funcionalidades

- **GET /status** - Retorna status do gateway e informações do site
- **GET /devices** - Lista dispositivos Tuya salvos localmente
- **POST /command** - Envia comando (on/off) para dispositivo Tuya

O gateway salva automaticamente dispositivos em `local_config.json`.

## 🚀 Como Gerar o APK

### ⭐ Método Recomendado: Build Automático no GitHub

**O mais fácil!** Não precisa instalar nada no seu computador.

1. Faça push do código para o GitHub:
```bash
git add .
git commit -m "Atualização do gateway"
git push origin main
```

2. Vá para a aba **"Actions"** no GitHub
3. Aguarde o build completar (15-30 minutos na primeira vez)
4. Baixe o APK na seção **"Artifacts"**

📖 **Guia completo:** Veja `BUILD_GITHUB.md` para instruções detalhadas.

### 🔧 Build Local (Alternativa)

Se preferir fazer o build localmente:

#### Pré-requisitos
1. **Linux** (recomendado) ou **WSL2** no Windows
2. **Python 3.8+**
3. **Buildozer** instalado

#### Instalação do Buildozer

```bash
pip install buildozer
sudo apt-get update
sudo apt-get install -y git zip unzip openjdk-11-jdk python3-pip autoconf libtool pkg-config zlib1g-dev libncurses5-dev libncursesw5-dev libtinfo5 cmake libffi-dev libssl-dev
```

#### Build do APK

1. Clone o repositório:
```bash
git clone https://github.com/MRITSoftware/mritlocal.git
cd mritlocal
```

2. Execute o build:
```bash
buildozer android debug
```

O APK será gerado em: `.buildozer/android/platform/build/dists/mritgateway/bin/mritgateway-2.0.0-arm64-v8a-debug.apk`

📖 **Guia completo:** Veja `INSTALACAO_SERVIDOR.md` e `COMO_EXECUTAR.md` para instruções detalhadas.

## 📱 Instalação no Android

1. Ative **Fontes desconhecidas** nas configurações do Android
2. Instale o APK baixado
3. Na primeira execução, o app criará `local_config.json` com nome padrão "Site Local"
4. Para definir um nome personalizado, use variável de ambiente `MRIT_SITE_NAME` ou edite o arquivo manualmente

## 🔧 Configuração

### Definir nome do site

Edite `local_config.json` após a primeira execução:

```json
{
  "site_name": "Cozinha",
  "http_port": 8080,
  "devices": {}
}
```

### Permissões Android

O app precisa de:
- **Internet** - Para comunicação HTTP
- **Wake Lock** - Para manter rodando em background
- **Desativar otimização de bateria** - Para o app não ser encerrado

## 📡 Uso da API

### Status
```bash
curl http://IP_DO_ANDROID:8080/status
```

### Listar dispositivos
```bash
curl http://IP_DO_ANDROID:8080/devices
```

### Enviar comando
```bash
curl -X POST http://IP_DO_ANDROID:8080/command \
  -H "Content-Type: application/json" \
  -d '{
    "tuya_device_id": "xxxxx",
    "action": "on",
    "local_key": "xxxxx",
    "lan_ip": "192.168.1.100",
    "device_name": "Lâmpada",
    "version": 3.3
  }'
```

## 🛠️ Desenvolvimento

### Estrutura do Projeto

- `main.py` - Ponto de entrada do app Android (Kivy)
- `mritserver.py` - Servidor HTTP e lógica do gateway
- `buildozer.spec` - Configuração do build
- `requirements.txt` - Dependências Python

### Testar localmente

```bash
python3 mritserver.py
```

## 📝 Notas

- O servidor roda na porta **8080** (fixa)
- Mantenha o app aberto para o servidor continuar funcionando
- Logs são exibidos no console/logcat do Android
- Dispositivos são salvos automaticamente em `local_config.json`

## 📄 Licença

MRIT Software

