# 📤 Instruções para Push no GitHub

Devido a problemas de encoding no caminho do projeto, siga estas instruções para fazer o commit e push manualmente.

## 🚀 Passos para Fazer Push

### 1. Abra o Terminal/PowerShell no diretório do projeto

Navegue até o diretório do projeto:
```powershell
cd "D:\VISION\Atualizações\Control\MRIT Control"
```

### 2. Inicialize o repositório Git (se ainda não foi feito)

```powershell
git init
```

### 3. Configure o remote

```powershell
git remote add origin https://github.com/MRITSoftware/mrit-control.git
```

Ou se já existir:
```powershell
git remote set-url origin https://github.com/MRITSoftware/mrit-control.git
```

### 4. Adicione os arquivos

```powershell
git add .
```

### 5. Faça o commit

```powershell
git commit -m "feat: Adiciona integração com Supabase para reinicialização remota e registro de dispositivos

- Adiciona tabela devices para registro automático de dispositivos
- Implementa DeviceRegistry para gerenciar dispositivos
- Adiciona funcionalidade de reinicialização remota via comandos Supabase
- Cria workflow GitHub Actions para build automático de APK
- Adiciona configuração de nome de unidade para dispositivos"
```

### 6. Configure a branch e faça push

```powershell
git branch -M main
git push -u origin main
```

## 📋 Arquivos Importantes que Serão Commitados

- ✅ Código fonte do app (`app/`)
- ✅ Configurações do Gradle (`build.gradle.kts`, `settings.gradle.kts`)
- ✅ Workflow do GitHub Actions (`.github/workflows/build-apk.yml`)
- ✅ Script SQL do Supabase (`SUPABASE_SETUP.sql`)
- ✅ Documentação (`*.md`)
- ✅ Arquivos de configuração (`.gitignore`)

## ⚠️ Arquivos que Serão Ignorados (via .gitignore)

- ❌ Arquivos de build (`.gradle/`, `build/`)
- ❌ APKs gerados (`*.apk`)
- ❌ Arquivos locais (`local.properties`)
- ❌ Cache do Gradle

## 🔄 Após o Push

O workflow do GitHub Actions será executado automaticamente e gerará o APK. Você pode:

1. Ver o progresso em: **Actions** → **Build APK**
2. Baixar o APK gerado na aba **Artifacts** após o build completar

## 🐛 Se Houver Problemas

### Erro de autenticação
Se pedir credenciais, você pode:
- Usar um Personal Access Token do GitHub
- Configurar SSH keys
- Usar GitHub CLI (`gh auth login`)

### Erro de branch
Se a branch `main` não existir no repositório remoto:
```powershell
git push -u origin main --force
```

### Verificar status
```powershell
git status
git remote -v
```
