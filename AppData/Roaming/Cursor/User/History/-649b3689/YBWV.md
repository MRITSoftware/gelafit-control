# 🚀 Build Automático no GitHub - Guia Completo

## ✅ Sim! Você pode fazer o build no GitHub!

O GitHub Actions vai compilar o APK automaticamente sempre que você fizer push do código.

---

## 📋 Como Funciona

1. **Você faz push do código** para o GitHub
2. **GitHub Actions detecta** a mudança
3. **Build automático** é executado em um servidor Linux
4. **APK é gerado** e disponibilizado para download
5. **Você baixa o APK** na seção "Actions" do GitHub

---

## 🎯 Passo a Passo

### Passo 1: Fazer Push do Código

No terminal/PowerShell, na pasta do projeto:

```bash
# Se ainda não inicializou o git
git init
git add .
git commit -m "Adiciona build automático no GitHub"

# Se já tem repositório remoto
git push origin main
```

**OU se ainda não tem repositório no GitHub:**

```bash
# Criar repositório no GitHub primeiro (via site github.com)
# Depois conectar:
git remote add origin https://github.com/SEU_USUARIO/mritlocal.git
git branch -M main
git push -u origin main
```

### Passo 2: Aguardar o Build

1. Vá para o seu repositório no GitHub
2. Clique na aba **"Actions"** (no topo)
3. Você verá o workflow **"Build Android APK"** rodando
4. Aguarde alguns minutos (15-30 min na primeira vez)

### Passo 3: Baixar o APK

1. Quando o build terminar, clique no workflow que acabou de executar
2. Role até o final da página
3. Na seção **"Artifacts"**, clique em **"mritgateway-apk"**
4. O arquivo `.zip` será baixado
5. Extraia o `.zip` para obter o arquivo `.apk`

---

## 🔄 Acionar Build Manualmente

Você também pode acionar o build manualmente sem fazer push:

1. Vá para a aba **"Actions"** no GitHub
2. Clique em **"Build Android APK"** (no menu lateral)
3. Clique em **"Run workflow"** (botão no topo direito)
4. Escolha a branch (geralmente `main`)
5. Clique em **"Run workflow"**
6. Aguarde o build completar

---

## 📁 Estrutura Criada

O arquivo `.github/workflows/build-apk.yml` foi criado com:

- ✅ Build automático em servidor Ubuntu
- ✅ Instalação automática de dependências
- ✅ Geração do APK
- ✅ Upload do APK como artifact
- ✅ Retenção de 30 dias dos artifacts

---

## 🎨 Interface do GitHub Actions

```
GitHub Repository
    ↓
Aba "Actions" (no topo)
    ↓
"Build Android APK" (workflow)
    ↓
Execução em andamento (amarelo) ou concluída (verde)
    ↓
Clique na execução
    ↓
Role até "Artifacts"
    ↓
Download do APK ✅
```

---

## ⚡ Vantagens do Build no GitHub

✅ **Não precisa instalar nada** no seu computador  
✅ **Não precisa de Linux/WSL2**  
✅ **Build sempre atualizado** com o código  
✅ **Histórico de builds** no GitHub  
✅ **APK disponível para download** por 30 dias  
✅ **Funciona em qualquer sistema** (Windows, Mac, Linux)  

---

## 🔧 Personalizar o Build

Se quiser modificar o build, edite o arquivo:

```
.github/workflows/build-apk.yml
```

### Exemplos de personalização:

**Mudar a versão do Python:**
```yaml
python-version: '3.11'  # em vez de '3.10'
```

**Build apenas em tags:**
```yaml
on:
  push:
    tags:
      - 'v*'
```

**Build em múltiplas branches:**
```yaml
on:
  push:
    branches: [ main, master, develop ]
```

---

## 🆘 Solução de Problemas

### Build falhou?

1. Clique na execução que falhou
2. Veja os logs para identificar o erro
3. Erros comuns:
   - **Dependências faltando**: Adicione no `buildozer.spec`
   - **Erro de permissão**: Verifique as licenças do Android SDK
   - **Memória insuficiente**: GitHub Actions tem limite de recursos

### APK não aparece nos artifacts?

1. Verifique se o build foi concluído com sucesso (verde)
2. Role até o final da página de execução
3. Artifacts aparecem apenas em builds bem-sucedidos

### Build demora muito?

- Primeira vez: 20-30 minutos (baixa dependências)
- Próximas vezes: 10-15 minutos
- É normal! O GitHub Actions precisa compilar tudo do zero

---

## 📝 Comandos Rápidos

```bash
# Adicionar arquivos e fazer commit
git add .
git commit -m "Atualização do app"

# Fazer push (dispara build automático)
git push origin main

# Ver status do git
git status

# Ver histórico de commits
git log --oneline
```

---

## 🎉 Pronto!

Agora você só precisa:

1. ✅ Fazer push do código
2. ✅ Aguardar o build no GitHub
3. ✅ Baixar o APK pronto!

**Não precisa mais instalar Buildozer, WSL2 ou nada no seu computador!** 🚀

---

© MRIT Software

