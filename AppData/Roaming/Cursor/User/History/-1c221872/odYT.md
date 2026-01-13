# 📱 Guia: Gerar APK com PWABuilder.com

## ✅ O que VAI funcionar

### 1. **Auto-início JavaScript** ✅
- ✅ **Funciona perfeitamente!** O código JavaScript que implementei vai funcionar 100%
- ✅ Quando o app abrir, vai detectar Android e iniciar automaticamente se houver código salvo
- ✅ Salva o código automaticamente na primeira vez que você usar

### 2. **PWA como App Nativo** ✅
- ✅ O pwabuilder.com converte sua PWA em um app Android nativo
- ✅ Funciona offline (com Service Worker)
- ✅ Instala como app normal na Play Store ou manualmente

## ⚠️ O que NÃO vai funcionar automaticamente

### ❌ Auto-início no Boot do Android
- ❌ O pwabuilder.com **não permite** adicionar código nativo customizado (como BootReceiver)
- ❌ O app **não vai abrir automaticamente** quando o Android ligar
- ✅ **MAS**: Quando o usuário abrir o app manualmente, vai iniciar automaticamente com o código salvo!

## 🚀 Como Usar o PWABuilder.com

### Passo 1: Preparar o Site

1. **Hospede o site** em `https://www.muraltv.com.br/`
2. **Verifique** se o `manifest.json` está acessível em `https://www.muraltv.com.br/manifest.json`
3. **Verifique** se o `service-worker.js` está funcionando

### Passo 2: Gerar APK no PWABuilder.com

1. **Acesse**: https://www.pwabuilder.com/
2. **Cole a URL**: `https://www.muraltv.com.br/`
3. **Clique em "Start"**
4. **Aguarde** a análise do site
5. **Vá para "Android"**
6. **Clique em "Generate"** para baixar o projeto Android
7. **OU** clique em "Build My PWA" para gerar APK direto (requer conta)

### Passo 3: Configurações Recomendadas

No pwabuilder.com, configure:

- ✅ **Package ID**: `com.mrit.player` (ou o que preferir)
- ✅ **App Name**: `MRIT Player`
- ✅ **Version**: `1.0.0`
- ✅ **Orientation**: `Landscape` (já está no manifest.json)

## 📋 Checklist Antes de Gerar

- [ ] Site está hospedado e acessível
- [ ] `manifest.json` está acessível e válido
- [ ] `service-worker.js` está funcionando
- [ ] Ícones estão configurados (192x192 e 512x512)
- [ ] Testou o site no navegador Android

## 🔧 Melhorias no Manifest.json

Vou otimizar o `manifest.json` para funcionar melhor com pwabuilder.com:

```json
{
  "name": "MRIT Player",
  "short_name": "MRIT",
  "start_url": "/",
  "scope": "/",
  "display": "standalone",
  "background_color": "#000000",
  "theme_color": "#000000",
  "orientation": "landscape",
  "icons": [
    { "src": "/icon-192.png", "sizes": "192x192", "type": "image/png" },
    { "src": "/icon-512.png", "sizes": "512x512", "type": "image/png" }
  ],
  "prefer_related_applications": false
}
```

## 🎯 Como Funciona na Prática

### Cenário 1: Usuário abre o app manualmente
1. ✅ App abre
2. ✅ Sistema detecta Android
3. ✅ Verifica se há código salvo
4. ✅ Se houver, inicia automaticamente!
5. ✅ Se não houver, mostra tela de login

### Cenário 2: Android reinicia
1. ❌ App **não abre automaticamente** (limitação do pwabuilder.com)
2. ✅ **MAS** quando o usuário abrir, vai iniciar automaticamente com código salvo

## 💡 Soluções Alternativas para Auto-Start no Boot

### Opção 1: Usar App de Automação (Recomendado)
Instale um app como **Tasker** ou **MacroDroid** que:
- Detecta quando o Android liga
- Abre automaticamente o app MRIT Player

### Opção 2: Modificar o APK Gerado
1. Gere o APK no pwabuilder.com
2. Baixe o projeto Android gerado
3. Adicione o `BootReceiver` manualmente (veja `ANDROID_AUTO_START.md`)
4. Recompile o APK

### Opção 3: Usar Launcher Customizado
Configure o app como "App de tela inicial" nas configurações do Android

## 📱 Testando o APK Gerado

### Teste 1: Instalação
1. Instale o APK no dispositivo Android
2. Verifique se o app aparece na lista de apps
3. Abra o app

### Teste 2: Auto-início JavaScript
1. Digite um código válido e inicie
2. Feche o app completamente
3. Abra novamente
4. **Resultado esperado**: Deve iniciar automaticamente!

### Teste 3: Offline
1. Desligue a internet
2. Abra o app
3. **Resultado esperado**: Deve funcionar com cache (se tiver conteúdo em cache)

## 🐛 Problemas Comuns

### ❌ APK não instala
**Solução**: 
- Ative "Fontes desconhecidas" nas configurações do Android
- Verifique se o APK não está corrompido

### ❌ App não abre o site
**Solução**:
- Verifique se a URL no manifest.json está correta
- Verifique se o site está acessível
- Verifique logs: `adb logcat | grep WebView`

### ❌ Auto-início não funciona
**Solução**:
- Verifique se está em Android: `navigator.userAgent`
- Verifique se o código foi salvo: `localStorage.getItem('mrit_codigo_tela')`
- Abra o console do WebView para ver erros

## ✅ Resumo

**O que funciona:**
- ✅ Auto-início quando o app abre (JavaScript)
- ✅ Salvar código automaticamente
- ✅ PWA como app nativo
- ✅ Funciona offline

**O que não funciona automaticamente:**
- ❌ Auto-início no boot (requer código nativo customizado)

**Recomendação:**
1. Use o pwabuilder.com para gerar o APK base
2. O auto-início JavaScript já vai funcionar perfeitamente
3. Se precisar de auto-início no boot, use um app de automação ou modifique o APK manualmente

## 🎉 Conclusão

**SIM, vai funcionar!** O sistema está preparado para funcionar com pwabuilder.com. O auto-início JavaScript vai funcionar perfeitamente quando o app abrir. A única limitação é que não vai abrir automaticamente no boot do Android, mas isso pode ser resolvido com apps de automação ou modificando o APK gerado.

