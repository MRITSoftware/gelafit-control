# 🔧 Como Corrigir os Erros do Manifest no PWABuilder

## ❌ Erros Encontrados

1. **Links dos ícones quebrados** (2 erros)
2. **Tipos de ícones incorretos** (2 erros)
3. **Falta descrição** (1 aviso)
4. **Tamanhos de ícones** (1 aviso)
5. **Falta screenshots** (1 aviso)

## ✅ Solução Rápida

### 1. Criar Ícones (Se não tiver)

Você precisa de ícones em:
- `icon-192.png` (192x192 pixels)
- `icon-512.png` (512x512 pixels)

**Opção A: Usar o logo existente**
- Use o `vision_logo.png` como base
- Redimensione para 192x192 e 512x512

**Opção B: Gerar ícones online**
- Acesse: https://www.pwabuilder.com/imageGenerator
- Faça upload do `vision_logo.png`
- Baixe os ícones gerados

### 2. Corrigir o manifest.json

Já corrigi o manifest.json para você! Agora ele:
- ✅ Usa `vision_logo.png` como ícone (temporário)
- ✅ Tem descrição adicionada
- ✅ Tipos de ícones corretos

### 3. Upload dos Ícones

Depois de criar os ícones:
1. Faça upload de `icon-192.png` na raiz do site
2. Faça upload de `icon-512.png` na raiz do site
3. Atualize o manifest.json:

```json
{
  "name": "MRIT Player",
  "short_name": "MRIT",
  "description": "Sistema de reprodução de conteúdo digital para displays MRIT Vision",
  "start_url": "/",
  "scope": "/",
  "display": "standalone",
  "background_color": "#000000",
  "theme_color": "#000000",
  "orientation": "landscape",
  "prefer_related_applications": false,
  "icons": [
    { "src": "/icon-192.png", "sizes": "192x192", "type": "image/png", "purpose": "any" },
    { "src": "/icon-512.png", "sizes": "512x512", "type": "image/png", "purpose": "any" }
  ]
}
```

## 📋 Checklist

- [ ] Criar ícones 192x192 e 512x512
- [ ] Fazer upload dos ícones na raiz do site
- [ ] Verificar se `manifest.json` está acessível
- [ ] Testar no PWABuilder.com novamente

## 🎯 Próximos Passos

1. **Corrigir manifest** (já feito)
2. **Criar/upload ícones** (você precisa fazer)
3. **Gerar APK** no PWABuilder.com
4. **Adicionar auto-start no boot** (veja `SOLUCAO_AUTO_START_BOOT.md`)

