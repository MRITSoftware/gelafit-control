# 🚀 Onde Executar o Buildozer - Guia Rápido

## 📍 Você está no Windows? Use WSL2!

### Passo 1: Abrir o Terminal/PowerShell

**No Windows:**
1. Pressione `Windows + X`
2. Escolha **"Windows PowerShell"** ou **"Terminal"**
3. OU pressione `Windows + R`, digite `powershell` e pressione Enter

### Passo 2: Verificar se tem WSL2 instalado

No PowerShell, execute:
```powershell
wsl --status
```

**Se aparecer erro ou não tiver WSL2:**

### Passo 3: Instalar WSL2 (se não tiver)

No PowerShell **como Administrador** (clique com botão direito > Executar como administrador):

```powershell
wsl --install
```

**IMPORTANTE:** Reinicie o computador quando solicitado!

### Passo 4: Abrir o Ubuntu (WSL2)

Depois de reiniciar, abra o PowerShell novamente e execute:

```powershell
wsl
```

Ou abra diretamente o **Ubuntu** no menu Iniciar.

### Passo 5: Navegar até a pasta do projeto

No terminal do Ubuntu (WSL2), execute:

```bash
# Se o projeto está em D:\mritlocal no Windows
cd /mnt/d/mritlocal

# OU se está em C:\mritlocal
cd /mnt/c/mritlocal

# Verificar se está na pasta certa
ls -la
```

Você deve ver os arquivos: `main.py`, `buildozer.spec`, etc.

### Passo 6: Seguir os passos de instalação

Agora siga os passos do arquivo `INSTALACAO_SERVIDOR.md` a partir do **Passo 2** (já está no Linux via WSL2).

---

## 🐧 Alternativa: Máquina Linux (Ubuntu/Debian)

Se você tem acesso a uma máquina Linux ou servidor Linux:

### Passo 1: Abrir o Terminal

No Linux, pressione `Ctrl + Alt + T` ou procure por "Terminal" no menu.

### Passo 2: Navegar até a pasta do projeto

```bash
cd /caminho/para/mritlocal
```

### Passo 3: Seguir os passos de instalação

Siga todos os passos do arquivo `INSTALACAO_SERVIDOR.md` começando do **Passo 1**.

---

## 💻 Resumo Visual - Onde Executar

```
┌─────────────────────────────────────────┐
│  WINDOWS (seu computador atual)         │
│  ┌───────────────────────────────────┐  │
│  │ PowerShell > wsl                  │  │
│  │ (abre Ubuntu dentro do Windows)  │  │
│  └───────────────────────────────────┘  │
│           ↓                              │
│  ┌───────────────────────────────────┐  │
│  │ Ubuntu (WSL2)                     │  │
│  │ cd /mnt/d/mritlocal               │  │
│  │ buildozer android debug           │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

---

## ✅ Verificação Rápida

Execute estes comandos para verificar se está no lugar certo:

```bash
# Verificar se está no Linux
uname -a

# Verificar se está na pasta do projeto
pwd
ls -la main.py buildozer.spec

# Se aparecer os arquivos, está no lugar certo! ✅
```

---

## 🆘 Problemas Comuns

### "comando não encontrado" ou "command not found"

Você precisa instalar o Buildozer primeiro:
```bash
pip install buildozer
```

### "Permission denied"

Alguns comandos precisam de `sudo`:
```bash
sudo apt update
```

### Não consegue acessar a pasta do Windows

No WSL2, as pastas do Windows ficam em `/mnt/c/` ou `/mnt/d/`:
```bash
# Para acessar D:\mritlocal
cd /mnt/d/mritlocal

# Para acessar C:\mritlocal  
cd /mnt/c/mritlocal
```

---

## 📝 Comandos Completos (Copiar e Colar)

**No PowerShell do Windows (primeira vez):**
```powershell
wsl --install
# Reiniciar o computador
```

**Depois de reiniciar, no Ubuntu (WSL2):**
```bash
cd /mnt/d/mritlocal
sudo apt update && sudo apt upgrade -y
sudo apt install -y python3 python3-pip python3-venv git zip unzip openjdk-11-jdk autoconf libtool pkg-config zlib1g-dev libncurses5-dev libncursesw5-dev libtinfo5 cmake libffi-dev libssl-dev
pip install buildozer
buildozer android debug
```

---

© MRIT Software

