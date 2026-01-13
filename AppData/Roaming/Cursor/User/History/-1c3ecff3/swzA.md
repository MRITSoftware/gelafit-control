# 📋 Instalação do Servidor MRIT Gateway - Passo a Passo

Este guia fornece comandos prontos para copiar e colar para instalar e manter o servidor MRIT Gateway rodando.

## 🖥️ Instalação no Linux/Ubuntu

### Passo 1: Atualizar o sistema
```bash
sudo apt update && sudo apt upgrade -y
```

### Passo 2: Instalar Python e dependências básicas
```bash
sudo apt install -y python3 python3-pip python3-venv git
```

### Passo 3: Instalar dependências do sistema para Buildozer
```bash
sudo apt install -y zip unzip openjdk-11-jdk autoconf libtool pkg-config zlib1g-dev libncurses5-dev libncursesw5-dev libtinfo5 cmake libffi-dev libssl-dev
```

### Passo 4: Criar ambiente virtual (recomendado)
```bash
python3 -m venv venv
source venv/bin/activate
```

### Passo 5: Instalar Buildozer
```bash
pip install buildozer
```

### Passo 6: Instalar dependências Python do projeto
```bash
pip install kivy>=2.0.0 tinytuya>=1.0.0
```

### Passo 7: Clonar ou navegar até o projeto
```bash
cd ~
git clone https://github.com/MRITSoftware/mritlocal.git
cd mritlocal
```

OU se já tiver o projeto:
```bash
cd mritlocal
```

### Passo 8: Gerar o APK
```bash
buildozer android debug
```

**Tempo estimado:** 15-30 minutos na primeira vez (baixa dependências)

### Passo 9: Localizar o APK gerado
```bash
find .buildozer -name "*.apk" -type f
```

O APK estará em:
```
.buildozer/android/platform/build/dists/mritgateway/bin/mritgateway-1.0.0-arm64-v8a-debug.apk
```

---

## 🪟 Instalação no Windows (usando WSL2)

### Passo 1: Instalar WSL2 (se não tiver)
Abra PowerShell como Administrador e execute:
```powershell
wsl --install
```

Reinicie o computador quando solicitado.

### Passo 2: Abrir Ubuntu no WSL2
```bash
wsl
```

### Passo 3: Seguir os passos do Linux acima (Passos 1-9)

---

## 🐳 Instalação usando Docker (Alternativa)

### Passo 1: Instalar Docker
```bash
sudo apt install -y docker.io docker-compose
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER
```

**Importante:** Faça logout e login novamente após adicionar ao grupo docker.

### Passo 2: Usar container Buildozer
```bash
docker pull buildozer/buildozer
docker run --interactive --tty --rm \
  --volume "$(pwd)":/home/user/hostcwd \
  --workdir /home/user/hostcwd \
  buildozer/buildozer android debug
```

---

## 📱 Instalação do APK no Android

### Passo 1: Transferir APK para o Android
```bash
# Via ADB (Android Debug Bridge)
adb install mritgateway-1.0.0-arm64-v8a-debug.apk

# OU copie o arquivo para o dispositivo e instale manualmente
```

### Passo 2: Habilitar instalação de fontes desconhecidas
1. Vá em **Configurações** > **Segurança**
2. Ative **Fontes desconhecidas** ou **Instalar apps desconhecidos**

### Passo 3: Instalar o APK
- Abra o arquivo APK no dispositivo
- Toque em **Instalar**
- Aguarde a conclusão

---

## 🔄 Manter o Servidor Rodando

### No Android (App)
1. Abra o app **MRIT Gateway**
2. Mantenha o app aberto (não feche completamente)
3. O servidor roda automaticamente em background

### No Linux (Servidor Python direto)
```bash
# Executar servidor diretamente
python3 mritserver.py

# OU manter rodando em background com nohup
nohup python3 mritserver.py > server.log 2>&1 &

# Verificar se está rodando
ps aux | grep mritserver

# Ver logs
tail -f server.log

# Parar o servidor
pkill -f mritserver.py
```

### Criar serviço systemd (Linux - Iniciar automaticamente)
```bash
# Criar arquivo de serviço
sudo nano /etc/systemd/system/mritgateway.service
```

Cole o seguinte conteúdo:
```ini
[Unit]
Description=MRIT Tuya Gateway Server
After=network.target

[Service]
Type=simple
User=seu_usuario
WorkingDirectory=/caminho/para/mritlocal
ExecStart=/usr/bin/python3 /caminho/para/mritlocal/mritserver.py
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

Substitua `seu_usuario` e `/caminho/para/mritlocal` pelos valores corretos.

```bash
# Recarregar systemd
sudo systemctl daemon-reload

# Habilitar serviço para iniciar no boot
sudo systemctl enable mritgateway

# Iniciar serviço
sudo systemctl start mritgateway

# Verificar status
sudo systemctl status mritgateway

# Ver logs
sudo journalctl -u mritgateway -f
```

---

## ✅ Verificar se o Servidor Está Funcionando

### Teste 1: Status do servidor
```bash
curl http://IP_DO_DISPOSITIVO:8080/status
```

### Teste 2: Listar dispositivos
```bash
curl http://IP_DO_DISPOSITIVO:8080/devices
```

### Teste 3: Enviar comando de teste
```bash
curl -X POST http://IP_DO_DISPOSITIVO:8080/command \
  -H "Content-Type: application/json" \
  -d '{
    "tuya_device_id": "teste",
    "action": "on",
    "local_key": "teste",
    "lan_ip": "192.168.1.100",
    "device_name": "Teste",
    "version": 3.3
  }'
```

**Substitua `IP_DO_DISPOSITIVO` pelo IP real do seu dispositivo Android ou servidor.**

---

## 🔧 Solução de Problemas

### Problema: Buildozer não encontra dependências
```bash
# Limpar cache e tentar novamente
buildozer android clean
buildozer android debug
```

### Problema: Erro de permissão no Android
```bash
# Verificar permissões do app
adb shell dumpsys package com.mritsoftware.mritgateway | grep permission
```

### Problema: Servidor não inicia
```bash
# Verificar se a porta 8080 está em uso
netstat -tuln | grep 8080
# OU
lsof -i :8080

# Matar processo na porta 8080 (se necessário)
sudo fuser -k 8080/tcp
```

### Problema: APK não instala
```bash
# Desinstalar versão anterior
adb uninstall com.mritsoftware.mritgateway

# Instalar novamente
adb install mritgateway-1.0.0-arm64-v8a-debug.apk
```

---

## 📝 Comandos Rápidos de Referência

```bash
# Build do APK
buildozer android debug

# Limpar build anterior
buildozer android clean

# Ver logs do build
buildozer android debug 2>&1 | tee build.log

# Testar servidor localmente
python3 mritserver.py

# Verificar IP local
hostname -I
# OU
ip addr show | grep "inet " | grep -v 127.0.0.1
```

---

## 📞 Suporte

Para mais informações, consulte o README.md ou entre em contato com a equipe MRIT.

© MRIT Software

