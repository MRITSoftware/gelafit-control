# Como Monitorar o Status do Cache

## Formas de Verificar se o Cache Está Pronto

### **1. Via Console do Navegador (F12)**

#### **Ativar Logs de Debug**
```javascript
// Ativar logs detalhados
mritDebug.log(true);
```

#### **Ver Vídeos em Cache**
```javascript
// Ver todos os vídeos em cache
mritDebug.dump();
```

#### **Forçar Verificação de Cache**
```javascript
// Verificar status atual
mritDebug.clearAll(); // Limpar e recarregar
```

### **2. Logs Automáticos no Console**

#### **Quando Cache Está Funcionando**
```
[SW] vídeo em cache: https://exemplo.com/video.mp4 tamanho: 52428800 MB: 50.00
[SW] vídeo em cache: https://exemplo.com/video2.mp4 tamanho: 104857600 MB: 100.00
```

#### **Quando Cache Falha**
```
[SW] pulado (arquivo grande) https://exemplo.com/gigante.mp4 tamanho: 600000000 limite: 1073741824
[SW] precache falhou → https://exemplo.com/video.mp4 timeout
```

#### **Status do Player**
```
🔍 Verificando promoção para código: ABC123
✅ Exibindo popup de promoção
⏰ Contador da promoção: 15
```

### **3. Indicadores Visuais**

#### **No Player**
- ✅ **Vídeo reproduz instantaneamente** = Cache funcionando
- ❌ **Vídeo demora para carregar** = Cache não funcionando
- ⚠️ **Vídeo não carrega** = Problema de conexão

#### **No Console**
- ✅ **Logs de cache** aparecem = Sistema funcionando
- ❌ **Nenhum log** = Sistema não iniciado
- ⚠️ **Erros de timeout** = Problema de rede

### **4. Verificação Manual**

#### **Verificar IndexedDB**
```javascript
// Abrir DevTools > Application > IndexedDB > mrit-player-idb > videos
// Ver se há vídeos salvos com chaves como: "CODIGO::URL_DO_VIDEO"
```

#### **Verificar Cache API**
```javascript
// Abrir DevTools > Application > Cache Storage > mrit-player-cache-v12
// Ver se há arquivos de vídeo e imagem salvos
```

#### **Verificar localStorage**
```javascript
// Abrir DevTools > Application > Local Storage
// Ver chaves como: "playlist_cache_CODIGO"
```

## Script de Monitoramento Completo

### **Criar Função de Status**
```javascript
// Cole no console (F12)
async function verificarStatusCache() {
  console.log("🔍 Verificando status do cache...");
  
  // 1. Verificar IndexedDB
  try {
    const db = await new Promise((resolve, reject) => {
      const req = indexedDB.open("mrit-player-idb", 1);
      req.onsuccess = () => resolve(req.result);
      req.onerror = () => reject(req.error);
    });
    
    const tx = db.transaction("videos", "readonly");
    const store = tx.objectStore("videos");
    const keys = await new Promise((resolve, reject) => {
      const req = store.getAllKeys();
      req.onsuccess = () => resolve(req.result);
      req.onerror = () => reject(req.error);
    });
    
    console.log(`📦 IndexedDB: ${keys.length} vídeos em cache`);
    keys.forEach(key => console.log(`  - ${key}`));
  } catch (err) {
    console.error("❌ Erro ao verificar IndexedDB:", err);
  }
  
  // 2. Verificar Cache API
  try {
    const cache = await caches.open("mrit-player-cache-v12");
    const keys = await cache.keys();
    console.log(`🗄️ Cache API: ${keys.length} arquivos em cache`);
    keys.forEach(req => console.log(`  - ${req.url}`));
  } catch (err) {
    console.error("❌ Erro ao verificar Cache API:", err);
  }
  
  // 3. Verificar localStorage
  const playlistKeys = Object.keys(localStorage).filter(k => k.startsWith("playlist_cache_"));
  console.log(`💾 LocalStorage: ${playlistKeys.length} playlists em cache`);
  playlistKeys.forEach(key => console.log(`  - ${key}`));
  
  // 4. Status da conexão
  console.log(`🌐 Conexão: ${navigator.onLine ? 'Online' : 'Offline'}`);
  
  // 5. Status do player
  console.log(`🎬 Player: ${isPlaying ? 'Reproduzindo' : 'Parado'}`);
  console.log(`📺 Código atual: ${codigoAtual || 'Nenhum'}`);
}

// Executar verificação
verificarStatusCache();
```

## Monitoramento em Tempo Real

### **Ativar Monitoramento Contínuo**
```javascript
// Cole no console (F12)
let monitorCache = setInterval(async () => {
  console.log("⏰ Verificação automática do cache...");
  await verificarStatusCache();
}, 30000); // A cada 30 segundos

// Parar monitoramento
// clearInterval(monitorCache);
```

### **Monitorar Eventos de Cache**
```javascript
// Cole no console (F12)
// Interceptar logs do service worker
const originalLog = console.log;
console.log = function(...args) {
  if (args[0] && args[0].includes && args[0].includes("[SW]")) {
    console.log("🔥 CACHE EVENT:", ...args);
  }
  originalLog.apply(console, args);
};
```

## Sinais de que o Cache Está Pronto

### **✅ Cache Funcionando**
- Vídeos reproduzem instantaneamente
- Logs mostram "vídeo em cache"
- IndexedDB tem vídeos salvos
- Player funciona offline

### **❌ Cache Não Funcionando**
- Vídeos demoram para carregar
- Logs mostram "precache falhou"
- IndexedDB vazio
- Player não funciona offline

### **⚠️ Cache Parcial**
- Alguns vídeos em cache, outros não
- Logs mostram "pulado (arquivo grande)"
- Vídeos pequenos funcionam, grandes não

## Troubleshooting

### **Se Cache Não Funciona**
1. Verificar conexão com internet
2. Verificar se está usando servidor HTTP (não file://)
3. Verificar logs de erro no console
4. Tentar limpar cache e recarregar

### **Se Cache Está Lento**
1. Verificar tamanho dos vídeos
2. Verificar velocidade da internet
3. Verificar se há muitos vídeos na playlist
4. Verificar se atingiu limite de 12 vídeos

Agora você tem todas as ferramentas para monitorar o status do cache!
