# 🚀 Guia de Build - No Azul

Este guia explica como criar o executável e instalador do No Azul.

## 📋 Pré-requisitos

### 1. Python 3.8+
```bash
python --version
```

### 2. Dependências Python
```bash
pip install -r requirements.txt
pip install pyinstaller
```

### 3. Inno Setup (para instalador)
- Baixe em: https://jrsoftware.org/isinfo.php
- Instale com as opções padrão

## 🔨 Criando o Executável

### Método 1: Script Automático (Recomendado)
```bash
# Windows
build.bat

# Linux/Mac
python build_exe.py
```

### Método 2: Manual
```bash
# 1. Limpar builds anteriores
rmdir /s /q build dist __pycache__

# 2. Criar executável
pyinstaller --clean NoAzul.spec

# 3. Verificar resultado
dir dist\NoAzul.exe
```

## 📦 Criando o Instalador

### 1. Abrir Inno Setup
- Abra o Inno Setup Compiler
- File → Open
- Selecione `NoAzul_Installer.iss`

### 2. Compilar
- Build → Compile
- Aguarde a compilação
- O instalador será criado em `installer/`

### 3. Testar
- Execute `NoAzul_Setup_v1.0.0.exe`
- Teste a instalação
- Verifique se o app funciona

## 📁 Estrutura de Arquivos

```
MeuFinanceiro/
├── main.py                 # Ponto de entrada
├── NoAzul.spec            # Configuração PyInstaller
├── version_info.txt       # Informações de versão
├── NoAzul_Installer.iss   # Script Inno Setup
├── build_exe.py           # Script de build
├── build.bat              # Script Windows
├── dist/                  # Executável gerado
│   └── NoAzul.exe
└── installer/             # Instalador gerado
    └── NoAzul_Setup_v1.0.0.exe
```

## ⚙️ Configurações Avançadas

### Otimizações do Executável
- **UPX**: Comprime o executável
- **Strip**: Remove símbolos de debug
- **Optimize=2**: Otimização máxima do Python

### Exclusões
- Módulos de teste removidos
- Arquivos de debug excluídos
- Dependências desnecessárias filtradas

## 🐛 Solução de Problemas

### Erro: "Module not found"
```bash
# Adicione ao hiddenimports no .spec
pip install <modulo>
```

### Executável muito grande
```bash
# Verifique exclusões no .spec
# Use UPX para compressão
```

### Erro de permissão
```bash
# Execute como administrador
# Verifique antivírus
```

## 📊 Tamanhos Esperados

- **Executável**: ~50-80 MB
- **Instalador**: ~40-60 MB
- **Tempo de build**: 2-5 minutos

## 🎯 Próximos Passos

1. ✅ Teste o executável
2. ✅ Crie o instalador
3. ✅ Teste a instalação
4. ✅ Distribua o instalador

## 📞 Suporte

- **E-mail**: matheus@mrit.com.br
- **WhatsApp**: (19) 97134-9642
- **Site**: https://www.mritsoftware.com.br

---

**Desenvolvido com ❤️ por MRIT Software**
