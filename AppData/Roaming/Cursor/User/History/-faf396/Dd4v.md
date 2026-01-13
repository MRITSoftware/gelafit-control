# 📱 Instruções: Auto-Start no Android

## ✅ O que foi implementado

### 1. **Auto-início no JavaScript** (Já funcionando!)
- ✅ Detecta automaticamente se está rodando em Android
- ✅ Tenta iniciar automaticamente se houver código salvo
- ✅ Salva o código automaticamente quando você inicia manualmente pela primeira vez
- ✅ Funciona mesmo se o app Android já abrir o navegador

### 2. **Documentação para App Android**
- ✅ Guia completo de implementação (`ANDROID_AUTO_START.md`)
- ✅ Exemplos de código completos (`EXEMPLO_CODIGO_ANDROID.md`)

## 🚀 Como Funciona Agora

### Cenário 1: App Android já abre o navegador
1. **Primeira vez**: Usuário digita o código manualmente
2. **Código é salvo** automaticamente no localStorage
3. **Próximas vezes**: O sistema detecta Android + código salvo e inicia automaticamente!

### Cenário 2: App Android precisa iniciar no boot
1. **Modificar o código Android** seguindo os guias fornecidos
2. **Adicionar BootReceiver** que escuta o evento de boot
3. **App abre automaticamente** quando o Android iniciar

## 📋 Passo a Passo

### Para usar o auto-início JavaScript (Já está funcionando!)

1. **Acesse o site** `https://www.muraltv.com.br/` no Android
2. **Digite o código da tela** e clique em "Iniciar"
3. **Pronto!** O código será salvo automaticamente
4. **Na próxima vez** que abrir, iniciará automaticamente

### Para fazer o app Android iniciar no boot

1. **Abra o projeto Android** do seu APK
2. **Siga o guia** em `ANDROID_AUTO_START.md`
3. **Copie os exemplos** de `EXEMPLO_CODIGO_ANDROID.md`
4. **Compile e instale** o novo APK
5. **Teste reiniciando** o dispositivo

## 🔍 Como Testar

### Teste 1: Auto-início JavaScript
1. Acesse `https://www.muraltv.com.br/` no Android
2. Digite um código válido e inicie
3. Feche o navegador/app
4. Abra novamente
5. **Resultado esperado**: Deve iniciar automaticamente com o código salvo

### Teste 2: Auto-início no Boot
1. Instale o app Android modificado
2. Reinicie o dispositivo completamente
3. **Resultado esperado**: App deve abrir automaticamente após o boot

## 📱 Funcionalidades Adicionais

### Detecção de Android
O sistema detecta automaticamente se está rodando em Android através do User-Agent.

### Código salvo
O código é salvo em `localStorage` com a chave `mrit_codigo_tela`.

### Fallback
Se o auto-início falhar, a interface normal aparece para inserção manual do código.

## ⚙️ Configurações Avançadas

### Limpar código salvo
```javascript
// No console do navegador:
localStorage.removeItem('mrit_codigo_tela');
```

### Verificar código salvo
```javascript
// No console do navegador:
localStorage.getItem('mrit_codigo_tela');
```

### Forçar auto-início manualmente
```javascript
// No console do navegador:
tentarIniciarAutomaticamente();
```

## 🔐 Permissões Necessárias no Android

### AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

### Configurações do Dispositivo
Alguns fabricantes requerem configurações adicionais:

- **Xiaomi**: Apps protegidos → Permitir iniciar automaticamente
- **Huawei**: Apps protegidos → Permitir iniciar automaticamente  
- **Samsung**: Apps que não serão colocados em espera
- **Oppo/OnePlus**: Gerenciamento de energia → Permitir iniciar automaticamente

## 🐛 Problemas Comuns

### ❌ Auto-início não funciona
**Solução**: 
- Verifique se o código foi salvo: `localStorage.getItem('mrit_codigo_tela')`
- Verifique se está em Android: `navigator.userAgent`
- Verifique o console do navegador para erros

### ❌ App não inicia no boot
**Solução**:
- Verifique se a permissão foi adicionada no AndroidManifest.xml
- Verifique se o BootReceiver está registrado
- Verifique logs: `adb logcat | grep BootReceiver`
- Configure permissões extras em fabricantes específicos

### ❌ Código não é salvo
**Solução**:
- Verifique se o localStorage está habilitado no WebView
- Verifique se há erros no console
- Tente limpar o cache e tentar novamente

## 📞 Suporte

Se precisar de ajuda adicional:
1. Verifique os logs do console do navegador
2. Verifique os logs do Android: `adb logcat`
3. Consulte os guias detalhados:
   - `ANDROID_AUTO_START.md` - Guia de implementação
   - `EXEMPLO_CODIGO_ANDROID.md` - Exemplos de código completos

## 🎯 Resumo

✅ **Auto-início JavaScript**: Já está funcionando! Basta usar o sistema uma vez e ele salvará o código.

🔧 **Auto-início no Boot**: Requer modificação do código Android seguindo os guias fornecidos.

📱 **Compatibilidade**: Funciona em todos os dispositivos Android modernos (API 21+).

