# Endpoint para Verificação de Status Tuya

## Problema
A API Tuya bloqueia requisições CORS do navegador, então não é possível verificar o status online/offline diretamente do frontend.

## Solução
Criar um endpoint no servidor Python (`http://MRITSoftware.pythonanywhere.com`) que faça a verificação de status da Tuya.

## Endpoint Necessário

### URL
```
POST http://MRITSoftware.pythonanywhere.com/verificar_status_tuya
```

### Request Body
```json
{
  "device_ids": [
    "eb7c744110221d4d41d1a5",
    "ebe89a88e847ba1374sgm0",
    "ebf0b460e7a1a9f0611chs",
    "ebe34d39ae57fab606pr16"
  ],
  "credentials": [
    {
      "access_id": "td7tp3cvq3nrc35emwg3",
      "access_key": "bbcdaa3dfe9545fca4326fcfa1cf3e2c",
      "endpoint": "https://openapi.tuyaus.com",
      "uid": "az1715569264750N2mUr"
    },
    {
      "access_id": "wwxsqj37wnfdnp98wu54",
      "access_key": "d7a140221f3b4e8f916601af4fbd6816",
      "endpoint": "https://openapi.tuyaus.com",
      "uid": "az1759235287550HcJRz"
    }
  ]
}
```

### Response Esperada
```json
{
  "eb7c744110221d4d41d1a5": true,
  "ebe89a88e847ba1374sgm0": false,
  "ebf0b460e7a1a9f0611chs": true,
  "ebe34d39ae57fab606pr16": null
}
```

Onde:
- `true` = dispositivo online
- `false` = dispositivo offline
- `null` = status desconhecido (dispositivo não encontrado)

## Implementação Python

Use o código fornecido (`listar_dispositivos_multiplas_contas_online_status.py`) como base:

```python
from flask import Flask, request, jsonify
from tuya_connector import TuyaOpenAPI

app = Flask(__name__)

@app.route('/verificar_status_tuya', methods=['POST'])
def verificar_status_tuya():
    data = request.json
    device_ids = data.get('device_ids', [])
    credentials_list = data.get('credentials', [])
    
    status_map = {}
    
    # Iterar sobre todas as contas Tuya
    for creds in credentials_list:
        api = TuyaOpenAPI(
            creds['endpoint'],
            creds['access_id'],
            creds['access_key']
        )
        api.connect()
        
        # Buscar dispositivos do usuário
        res = api.get(f"/v1.0/users/{creds['uid']}/devices", {})
        
        if res and res.get("success"):
            devices = res.get("result", []) or []
            
            for device in devices:
                dev_id = device.get("id") or device.get("device_id")
                if dev_id in device_ids:
                    online = device.get("online")
                    # Só atualizar se ainda não foi encontrado ou se o status anterior era None
                    if dev_id not in status_map or status_map[dev_id] is None:
                        status_map[dev_id] = online
    
    # Garantir que todos os device_ids solicitados estão no resultado
    for device_id in device_ids:
        if device_id not in status_map:
            status_map[device_id] = None
    
    return jsonify(status_map)

if __name__ == '__main__':
    app.run()
```

## CORS
Certifique-se de que o servidor Python está configurado para aceitar requisições CORS do frontend:

```python
from flask_cors import CORS

app = Flask(__name__)
CORS(app)  # Permitir CORS de qualquer origem
# Ou especificar origem:
# CORS(app, origins=["http://localhost:5173", "https://seu-dominio.com"])
```

## Teste
Após implementar o endpoint, teste com:

```bash
curl -X POST http://MRITSoftware.pythonanywhere.com/verificar_status_tuya \
  -H "Content-Type: application/json" \
  -d '{
    "device_ids": ["eb7c744110221d4d41d1a5"],
    "credentials": [{
      "access_id": "td7tp3cvq3nrc35emwg3",
      "access_key": "bbcdaa3dfe9545fca4326fcfa1cf3e2c",
      "endpoint": "https://openapi.tuyaus.com",
      "uid": "az1715569264750N2mUr"
    }]
  }'
```

