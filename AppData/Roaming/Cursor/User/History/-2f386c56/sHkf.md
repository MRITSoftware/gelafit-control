# Solução para Problemas de Conexão e CORS

## Problemas Identificados ❌

### 1. **Erro de CORS (manifest.json)**
```
Access to internal resource at 'file:///...' from origin 'null' has been blocked by CORS policy
```
**Causa**: Arquivo sendo aberto diretamente no navegador (`file://`)

### 2. **Vídeo não ficou pronto (readyState: 0)**
```
Vídeo não ficou pronto (readyState: 0)
```
**Causa**: Conexão com internet instável

### 3. **ERR_INTERNET_DISCONNECTED**
```
GET https://base.muraltv.com.br/storage/v1/object/public/conteudos/videos%20matheus/1754442834966766.mp4 net::ERR_INTERNET_DISCONNECTED
```
**Causa**: Conexão com internet perdida

### 4. **WebSocket connection failed**
```
WebSocket connection to 'wss://base.muraltv.com.br/realtime/v1/websocket' failed
```
**Causa**: Mesmo problema de rede

## Soluções Implementadas ✅

### **1. Servidor HTTP Local (RESOLVE CORS)**

#### **Opção A: Python (Recomendado)**
```bash
# Na pasta do projeto
python servidor_local.py
```
**Resultado**: Abre automaticamente `http://localhost:8000`

#### **Opção B: Node.js**
```bash
# Instalar servidor
npm install -g http-server

# Executar
http-server -p 8000 --cors
```

#### **Opção C: PHP**
```bash
# Na pasta do projeto
php -S localhost:8000
```

### **2. Melhor Tratamento de Erros de Rede**

#### **Detecção de Conexão**
- ✅ Detecta quando internet cai
- ✅ Detecta quando internet volta
- ✅ Retry automático após reconexão

#### **Logs Melhorados**
```
🌐 Conexão com internet restaurada
⚠️ Conexão com internet perdida
🔍 Verificando conexão com internet...
⚠️ Sem conexão com internet - tentando novamente em 5s
```

### **3. Retry Automático**
- ✅ Tenta novamente após 5 segundos se sem internet
- ✅ Reconecta automaticamente quando internet volta
- ✅ Mantém estado do player durante reconexão

## Como Usar

### **1. Resolver CORS (IMEDIATO)**
```bash
# Execute na pasta do projeto
python servidor_local.py
```

### **2. Acessar o Player**
- Abra: `http://localhost:8000/index.html`
- **NÃO** abra o arquivo diretamente no navegador

### **3. Verificar Logs**
```javascript
// No console (F12)
mritDebug.log(true);
```

## Testando a Solução

### **1. Teste de CORS**
- ✅ Abrir `http://localhost:8000` (não `file://`)
- ✅ Verificar se não há erros de CORS

### **2. Teste de Conexão**
- ✅ Desconectar internet
- ✅ Verificar logs: "⚠️ Conexão com internet perdida"
- ✅ Reconectar internet
- ✅ Verificar logs: "🌐 Conexão com internet restaurada"

### **3. Teste de Retry**
- ✅ Vídeo deve tentar novamente automaticamente
- ✅ Player deve continuar funcionando após reconexão

## Logs de Debug

### **Conexão OK**
```
🌐 Conexão com internet restaurada
✅ Exibindo popup de promoção
```

### **Conexão Perdida**
```
⚠️ Conexão com internet perdida
⚠️ Sem conexão com internet - tentando novamente em 5s
```

### **Vídeo Carregando**
```
🔍 Verificando conexão com internet...
```

## Vantagens

✅ **CORS Resolvido**: Funciona com servidor HTTP  
✅ **Retry Automático**: Tenta novamente se falhar  
✅ **Detecção de Rede**: Sabe quando internet cai/volta  
✅ **Logs Claros**: Mostra exatamente o que está acontecendo  
✅ **Robustez**: Funciona mesmo com conexão instável  

## Próximos Passos

1. **Execute o servidor local**
2. **Acesse via HTTP** (não file://)
3. **Teste com conexão instável**
4. **Verifique os logs** no console

Agora o player deve funcionar corretamente mesmo com problemas de conexão!
