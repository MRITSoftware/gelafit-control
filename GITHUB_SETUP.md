# 🚀 Configuração do GitHub e Workflow

Este documento explica como fazer push do código para o GitHub e como o workflow gera os APKs automaticamente.

## 📋 Pré-requisitos

1. **Git instalado** no seu computador
2. **Conta GitHub** com acesso ao repositório: https://github.com/MRITSoftware/mrit-control
3. **Personal Access Token** (se necessário para autenticação)

## 🎯 Opção 1: Usar o Script Automático (Recomendado)

1. Abra o **PowerShell** na pasta do projeto:
   ```powershell
   cd "d:\VISION\Atualizações\Control\MRIT Control"
   ```

2. Execute o script:
   ```powershell
   .\push-inicial.ps1
   ```

O script irá:
- ✅ Verificar se Git está instalado
- ✅ Inicializar o repositório Git (se necessário)
- ✅ Configurar o remote do GitHub
- ✅ Adicionar todos os arquivos
- ✅ Criar commit inicial
- ✅ Fazer push para o GitHub

## 🎯 Opção 2: Fazer Manualmente

### 1. Inicializar Git

```powershell
git init
```

### 2. Configurar Remote

```powershell
git remote add origin https://github.com/MRITSoftware/mrit-control.git
```

### 3. Adicionar Arquivos

```powershell
git add .
```

### 4. Criar Commit

```powershell
git commit -m "feat: Adiciona funcionalidade de reiniciar dispositivo via Supabase"
```

### 5. Fazer Push

```powershell
git branch -M main
git push -u origin main
```

## 🔐 Autenticação

Se pedir autenticação, você tem duas opções:

### Opção A: Personal Access Token (Recomendado)

1. Crie um token em: https://github.com/settings/tokens
2. Selecione as permissões: `repo` (acesso completo)
3. Copie o token
4. Quando pedir senha, use o **token** no lugar da senha
5. Usuário: seu username do GitHub

### Opção B: SSH

1. Configure SSH no GitHub
2. Mude o remote:
   ```powershell
   git remote set-url origin git@github.com:MRITSoftware/mrit-control.git
   ```
3. Faça push novamente

## 🔄 Workflow do GitHub Actions

O workflow está configurado em `.github/workflows/build.yml` e faz o seguinte:

### Triggers (Quando o workflow executa)

1. **Push** para branches: `main`, `master`, `develop`
2. **Pull Request** para branches: `main`, `master`
3. **Manual** via GitHub Actions (workflow_dispatch)

### O que o workflow faz

1. ✅ Faz checkout do código
2. ✅ Configura JDK 17
3. ✅ Configura Android SDK
4. ✅ Configura Gradle 8.2
5. ✅ Gera Gradle Wrapper
6. ✅ Builda APK Debug
7. ✅ Builda APK Release (com continue-on-error)
8. ✅ Faz upload dos APKs como artifacts

### Como acessar os APKs

1. Vá para: https://github.com/MRITSoftware/mrit-control/actions
2. Clique no workflow que você quer (mais recente)
3. Role até o final da página
4. Na seção **Artifacts**, você verá:
   - `app-debug-apk` - APK de debug
   - `app-release-apk` - APK de release (se buildou com sucesso)
5. Clique no artifact para baixar

### Executar Workflow Manualmente

1. Vá para: https://github.com/MRITSoftware/mrit-control/actions
2. Clique em **Build APK** no menu lateral
3. Clique em **Run workflow**
4. Selecione:
   - Branch: `main`
   - Tipo de build: `debug`, `release` ou `both`
5. Clique em **Run workflow**

## 📦 Estrutura do Workflow

```yaml
name: Build APK

on:
  push:
    branches: [ main, master, develop ]
  pull_request:
    branches: [ main, master ]
  workflow_dispatch:
    inputs:
      build_type:
        description: 'Tipo de build (debug ou release)'
        required: true
        default: 'debug'
        type: choice
        options:
          - debug
          - release
          - both
```

## ✅ Verificar se Funcionou

Após o push:

1. ✅ Acesse: https://github.com/MRITSoftware/mrit-control
2. ✅ Você deve ver todos os arquivos do projeto
3. ✅ Vá em **Actions** para ver o workflow em execução
4. ✅ Aguarde alguns minutos para o build completar
5. ✅ Baixe os APKs na seção **Artifacts**

## 🐛 Problemas Comuns

### Erro: "Repository not found"
- Verifique se você tem **permissão de escrita** no repositório
- Confirme que o repositório existe: https://github.com/MRITSoftware/mrit-control

### Erro: "Authentication failed"
- Use um **Personal Access Token** em vez de senha
- Crie em: https://github.com/settings/tokens
- Permissões necessárias: `repo`

### Erro: "Permission denied"
- Você precisa ser **colaborador** do repositório
- Peça ao dono do repositório para adicionar você como colaborador

### Workflow falha no build
- Verifique os logs do workflow em **Actions**
- Procure por erros de compilação
- Verifique se todas as dependências estão corretas no `build.gradle.kts`

### APK Release não aparece
- O build de release pode falhar se não houver keystore configurado
- Isso é normal - o APK debug sempre será gerado
- Para release, você precisa configurar signing (opcional)

## 📝 Próximos Passos

Após o push bem-sucedido:

1. ✅ O código estará no GitHub
2. ✅ O GitHub Actions irá compilar automaticamente
3. ✅ Os APKs estarão disponíveis em **Actions > Artifacts**
4. ✅ Você pode baixar e instalar os APKs nos dispositivos

## 🔗 Links Úteis

- Repositório: https://github.com/MRITSoftware/mrit-control
- Actions: https://github.com/MRITSoftware/mrit-control/actions
- Criar Token: https://github.com/settings/tokens
- Documentação GitHub Actions: https://docs.github.com/en/actions

---

**Dica**: Use o script `push-inicial.ps1` para facilitar o processo!
