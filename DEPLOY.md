# 🚀 Guia de Deploy no GitHub

## Pré-requisitos

1. Conta no GitHub
2. Git instalado
3. Acesso ao repositório: https://github.com/MRITSoftware/boot-receiver

## Passos para Fazer Push do Código

### 1. Inicializar Git (se ainda não foi feito)

```bash
# Navegar até a pasta do projeto
cd "d:\VISION\Atualizações\Boot Receiver"

# Inicializar repositório Git
git init

# Adicionar remote do GitHub
git remote add origin https://github.com/MRITSoftware/boot-receiver.git
```

### 2. Adicionar e Fazer Commit dos Arquivos

```bash
# Adicionar todos os arquivos
git add .

# Fazer commit inicial
git commit -m "Initial commit: Boot Receiver app para Android Stick"

# Ou se preferir uma mensagem mais detalhada:
git commit -m "feat: Adiciona aplicativo Boot Receiver

- BroadcastReceiver para BOOT_COMPLETED
- Tela de seleção de app na primeira vez
- Serviço para verificar internet e abrir app
- Retry automático quando não há internet
- Compatível com Android TV/Stick
- Documentação completa incluída"
```

### 3. Fazer Push para o GitHub

```bash
# Verificar branch atual
git branch

# Se estiver em 'master', renomear para 'main' (opcional)
git branch -M main

# Fazer push para o GitHub
git push -u origin main
```

**Nota**: Se o repositório já tiver conteúdo, você pode precisar fazer pull primeiro:

```bash
git pull origin main --allow-unrelated-histories
git push -u origin main
```

## 🔄 Workflow Automático

Após fazer push, o GitHub Actions irá:

1. **Detectar o push** automaticamente
2. **Configurar ambiente** (JDK 17, Android SDK)
3. **Compilar o projeto** (debug e release)
4. **Gerar APKs** automaticamente
5. **Fazer upload como artefatos** disponíveis para download

## 📥 Como Baixar os APKs Gerados

1. Acesse o repositório: https://github.com/MRITSoftware/boot-receiver
2. Clique na aba **Actions**
3. Selecione o workflow mais recente (Build APK)
4. Role até a seção **Artifacts**
5. Baixe:
   - `app-debug-apk` - APK de debug (para testes)
   - `app-release-apk` - APK de release (para produção)

## 🎯 Executar Build Manualmente

Você também pode executar o workflow manualmente:

1. Vá para **Actions** no GitHub
2. Selecione **Build APK**
3. Clique em **Run workflow**
4. Escolha:
   - **debug**: Apenas APK de debug
   - **release**: Apenas APK de release
   - **both**: Ambos os APKs
5. Clique em **Run workflow**

## 🔐 Assinar APK de Release (Opcional)

Para gerar um APK de release assinado, você precisa:

1. Criar uma keystore:
```bash
keytool -genkey -v -keystore bootreceiver.keystore -alias bootreceiver -keyalg RSA -keysize 2048 -validity 10000
```

2. Adicionar secrets no GitHub:
   - Vá em **Settings > Secrets and variables > Actions**
   - Adicione:
     - `KEYSTORE_FILE` (base64 do arquivo)
     - `KEYSTORE_PASSWORD`
     - `KEY_ALIAS`
     - `KEY_PASSWORD`

3. Modificar o workflow para usar a keystore (ver exemplo em `EXEMPLOS_USO.md`)

## 📝 Estrutura de Branches Recomendada

```
main/master     → Código de produção
develop         → Desenvolvimento
feature/*       → Novas funcionalidades
```

## 🔄 Atualizações Futuras

Para fazer atualizações:

```bash
# Fazer alterações nos arquivos...

# Adicionar mudanças
git add .

# Commit
git commit -m "feat: Descrição da mudança"

# Push
git push origin main
```

O workflow será executado automaticamente e gerará novos APKs!

## ⚠️ Troubleshooting

### Erro: "Repository not found"
- Verifique se você tem permissão de escrita no repositório
- Confirme a URL do remote: `git remote -v`

### Erro: "Workflow não executa"
- Verifique se o arquivo `.github/workflows/build.yml` está no repositório
- Confirme que está fazendo push para a branch `main` ou `master`

### Erro no build
- Verifique os logs na aba **Actions**
- Confirme que todas as dependências estão corretas no `build.gradle.kts`

---

**Dica**: Mantenha o histórico de commits organizado com mensagens descritivas!
