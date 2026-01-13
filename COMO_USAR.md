# 📱 Como Usar o MRIT Control

## 🚀 Instalação e Configuração Rápida

### Passo 1: Instalar o App

1. Baixe o APK mais recente do GitHub Actions:
   - Acesse: https://github.com/MRITSoftware/mrit-control/actions
   - Baixe o APK da última build bem-sucedida

2. Instale no dispositivo Android:
   - Transfira o APK para o dispositivo
   - Abra o arquivo e instale
   - Ou use: `adb install app-release.apk`

### Passo 2: Configurar o App

1. **Abra o app** pela primeira vez
   - O app vai solicitar o **email da unidade** (ex: sala01@empresa.com)
   - Informe o email e confirme

2. **Escolha o app** que deve abrir automaticamente
   - Uma lista de apps instalados será exibida
   - Use a busca para encontrar o app desejado
   - Clique no app para selecioná-lo

3. **Pronto!** 
   - O app está configurado
   - Nos próximos boots, o app escolhido abrirá automaticamente

## 🔄 Como Funciona

### No Boot do Dispositivo

1. Dispositivo liga/reinicia
2. App detecta o boot automaticamente
3. Verifica se há internet (aguarda até 10 minutos se necessário)
4. Abre o app configurado automaticamente

### Se o App Fechar

- **Não se preocupe!** 
- No próximo boot do dispositivo, o app será aberto novamente automaticamente
- Não é necessário fazer nada

## 📋 Verificar Status

### Tela de Status

1. Abra o app **MRIT Control**
2. Procure pela opção de **Status** (se disponível)
3. Verifique:
   - ✅ Serviço está rodando?
   - 📱 Device ID do dispositivo
   - ℹ️ Informações do dispositivo

## 🛠️ Configurações Recomendadas

### Para Digital Signage

1. **Desabilitar bloqueio de tela:**
   - Configurações → Segurança → Bloqueio de tela → Nenhum

2. **Desabilitar sleep da tela:**
   - Configurações → Tela → Timeout → Nunca
   - Ou: `adb shell settings put system screen_off_timeout 2147483647`

3. **Manter WiFi sempre conectado:**
   - Configurações → WiFi → Avançado → Manter WiFi ligado durante sleep → Sempre

4. **Desabilitar atualizações automáticas:**
   - Configurações → Sistema → Atualização do sistema → Desativar

## 🔍 Verificar se Está Funcionando

### Teste Manual

1. **Reinicie o dispositivo manualmente**
2. **Aguarde o boot completar**
3. **Verifique se o app configurado abriu automaticamente**

### Via Logs (ADB)

```bash
# Ver logs do boot
adb logcat | grep -E "BootReceiver|BootService"

# Deve mostrar:
# BootReceiver: Boot detectado!
# BootService: Internet disponível!
# AppLauncher: App aberto com sucesso
```

## ❓ Problemas Comuns

### O app não abre após boot

**Solução:**
1. Abra o app manualmente pelo menos uma vez após instalar
2. Verifique se há um app configurado
3. Verifique se há internet disponível

### O app escolhido não abre

**Solução:**
1. Verifique se o app ainda está instalado
2. Reconfigure o app no MRIT Control
3. Escolha o app novamente na lista

### Internet não detectada

**Solução:**
1. Verifique se o WiFi está conectado
2. O app aguarda até 10 minutos por internet
3. Se não houver internet, o app não abrirá (por segurança)

## 💡 Dicas

- **Primeira vez:** Sempre abra o app manualmente após instalar
- **Mudar app:** Limpe os dados do app e configure novamente
- **Verificar logs:** Use `adb logcat` para debug
- **Recuperação:** Se algo der errado, o próximo boot resolve

## 📞 Suporte

Se precisar de ajuda:
1. Verifique os logs: `adb logcat | grep BootReceiver`
2. Consulte o README.md
3. Verifique a tela de Status do app

---

**Simples assim!** Instale, configure e esqueça. O app cuida do resto! 🚀
