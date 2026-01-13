# Servidorzinho - Gateway Tuya Local

Servidor HTTP local para controlar dispositivos Tuya na sua rede local.

## 🚀 Instalação e Execução no Termux (Android)

### Método 1: Script Automático (Recomendado)

1. Copie os arquivos para o Termux:
   - `servidorzinho.py`
   - `instalar_e_rodar.sh`
   - `requirements.txt`

2. Execute o script:
```bash
chmod +x instalar_e_rodar.sh
./instalar_e_rodar.sh
```

O script irá:
- ✅ Instalar Python3 (se necessário)
- ✅ Instalar pip (se necessário)
- ✅ Instalar dependências (tinytuya)
- ✅ Iniciar o servidor automaticamente

### Método 2: Manual

```bash
# Instalar Python e pip
pkg update && pkg install -y python

# Instalar dependências
pip install -r requirements.txt

# Executar servidor
python3 servidorzinho.py
```

## 📡 API Endpoints

### GET /status
Retorna status do gateway:
```json
{
  "status": "ok",
  "site_name": "Cozinha",
  "devices_count": 2,
  "port": 8080
}
```

### GET /devices
Lista todos os dispositivos salvos:
```json
[
  {
    "tuya_device_id": "abc123",
    "name": "Tomada Sala",
    "local_key": "xyz789",
    "lan_ip": "192.168.1.100",
    "version": 3.3
  }
]
```

### POST /command
Envia comando para dispositivo Tuya:

**Body:**
```json
{
  "tuya_device_id": "abc123",
  "action": "on",
  "local_key": "xyz789",
  "lan_ip": "192.168.1.100",
  "device_name": "Tomada Sala",
  "version": 3.3
}
```

**Resposta:**
```json
{
  "success": true,
  "message": "Comando on enviado com sucesso",
  "device_name": "Tomada Sala"
}
```

## 📝 Notas

- Na primeira execução, será solicitado o nome do site
- A configuração é salva em `local_config.json`
- Os dispositivos são salvos automaticamente após o primeiro uso
- O servidor roda na porta 8080 por padrão

