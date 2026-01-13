# 🚀 Como Fazer Push Inicial para o GitHub

Como o repositório está vazio, siga estes passos para enviar o código:

## Opção 1: Usar o Script Automático (Mais Fácil)

1. **Abra o PowerShell** na pasta do projeto
2. **Execute o script**:
   ```powershell
   .\push-inicial.ps1
   ```

O script irá:
- ✅ Inicializar o Git
- ✅ Configurar o remote do GitHub
- ✅ Fazer commit de todos os arquivos
- ✅ Fazer push para o GitHub

## Opção 2: Fazer Manualmente (Passo a Passo)

### 1. Abrir Terminal na Pasta do Projeto

No PowerShell ou CMD, navegue até a pasta:
```powershell
cd "d:\VISION\Atualizações\Control\MRIT Control"
```

### 2. Inicializar Git

```powershell
git init
```

### 3. Configurar Remote do GitHub

```powershell
git remote add origin https://github.com/MRITSoftware/mrit-control.git
```

Se já existir, atualize:
```powershell
git remote set-url origin https://github.com/MRITSoftware/mrit-control.git
```

### 4. Adicionar Todos os Arquivos

```powershell
git add .
```

### 5. Fazer Commit

```powershell
git commit -m "feat: Adiciona aplicativo Boot Receiver para Android Stick"
```

### 6. Criar Branch Main (se necessário)

```powershell
git branch -M main
```

### 7. Fazer Push

```powershell
git push -u origin main
```

**⚠️ IMPORTANTE**: Na primeira vez, você precisará autenticar:

#### Opção A: Usar Token do GitHub (Recomendado)

1. Crie um token em: https://github.com/settings/tokens
2. Selecione as permissões: `repo` (acesso completo aos repositórios)
3. Copie o token
4. Quando pedir senha, use o **token** no lugar da senha
5. Usuário: seu username do GitHub

#### Opção B: Usar SSH

1. Configure SSH no GitHub
2. Mude o remote:
   ```powershell
   git remote set-url origin git@github.com:MRITSoftware/boot-receiver.git
   ```
3. Faça push novamente

## ✅ Verificar se Funcionou

Após o push:

1. Acesse: https://github.com/MRITSoftware/mrit-control
2. Você deve ver todos os arquivos do projeto
3. Vá em **Actions** para ver o workflow gerando os APKs

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

### Erro: "fatal: not a git repository"
- Execute `git init` primeiro
- Certifique-se de estar na pasta correta do projeto

## 📝 Comandos Rápidos (Copiar e Colar)

```powershell
# Navegar até a pasta
cd "d:\VISION\Atualizações\Control\MRIT Control"

# Inicializar e configurar
git init
git remote add origin https://github.com/MRITSoftware/mrit-control.git
git add .
git commit -m "feat: Adiciona funcionalidade de reiniciar dispositivo via Supabase"
git branch -M main
git push -u origin main
```

## 🎯 Próximos Passos

Após o push bem-sucedido:

1. ✅ O código estará no GitHub
2. ✅ O GitHub Actions irá compilar automaticamente
3. ✅ Os APKs estarão disponíveis em **Actions > Artifacts**

---

**Dica**: Se tiver problemas, execute o script `push-inicial.ps1` que faz tudo automaticamente!
