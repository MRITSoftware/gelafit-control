# 📦 Guia Completo - Gerar Instalador No Azul

## 🎯 Passo a Passo

### 1️⃣ Preparar o Ambiente

Certifique-se de ter instalado:
- ✅ Python 3.11+
- ✅ PyInstaller (`pip install pyinstaller`)
- ✅ Inno Setup Compiler (https://jrsoftware.org/isdl.php)

### 2️⃣ Executar o Script de Preparação

Execute o arquivo:
```batch
preparar_instalador.bat
```

Este script irá:
- 🧹 Limpar builds antigos
- ✅ Verificar arquivos necessários
- 🚀 Gerar o executável com PyInstaller
- 📁 Preparar arquivos de recursos
- 📝 Criar lista de arquivos

### 3️⃣ Verificar Arquivos Gerados

Após executar o script, verifique se existem:

```
dist/
├── NoAzul.exe          ← Executável principal
├── noazul_logo.ico     ← Ícone (copiado)
├── noazul_logo.png     ← Logo (copiado)
└── inicio_noazul.gif   ← GIF de loading (copiado)
```

### 4️⃣ Abrir o Inno Setup

1. Abra o **Inno Setup Compiler**
2. Abra o arquivo: `installer_simples_funcional.iss`
3. Verifique as configurações:

```iss
AppId={{A1B2C3D4-E5F6-7890-ABCD-EF1234567890}}  ← IMPORTANTE: Mesmo ID!
AppVersion=2.0.0                                ← Atualize a versão
```

### 5️⃣ Compilar o Instalador

1. No Inno Setup, pressione **F9** ou clique em **Build > Compile**
2. O instalador será gerado em: `installer_output\NoAzul_Setup_v2.0.0.exe`

### 6️⃣ Testar o Instalador

1. Execute o instalador gerado
2. Verifique se:
   - ✅ Instala corretamente
   - ✅ Detecta versão anterior (se houver)
   - ✅ Preserva dados em AppData
   - ✅ Abre o aplicativo após instalação

## 📋 Arquivos Necessários

### Obrigatórios:
- ✅ `main.py` - Arquivo principal
- ✅ `main.spec` - Configuração PyInstaller
- ✅ `noazul_logo.ico` - Ícone do aplicativo
- ✅ `noazul_logo.png` - Logo
- ✅ `inicio_noazul.gif` - GIF de loading
- ✅ `installer_simples_funcional.iss` - Script Inno Setup

### Estrutura de Pastas:
```
MeuFinanceiro/
├── main.py
├── main.spec
├── noazul_logo.ico
├── noazul_logo.png
├── inicio_noazul.gif
├── installer_simples_funcional.iss
├── models/
├── views/
├── utils/
└── dist/              ← Gerado pelo PyInstaller
    └── NoAzul.exe
```

## ⚠️ Importante

### AppID Fixo
**NUNCA altere o AppID** no arquivo `.iss`:
```
AppId={{A1B2C3D4-E5F6-7890-ABCD-EF1234567890}}
```

Este ID garante que:
- ✅ O Windows reconheça como a mesma aplicação
- ✅ Os dados sejam preservados em atualizações
- ✅ A atualização funcione corretamente

### Localização dos Dados

Os dados são salvos em:
```
C:\Users\[SEU_NOME]\AppData\Local\No Azul\
├── data.json
├── senha.json
├── config.json
├── logs/
└── backups/
```

**Estes dados NÃO são afetados pela instalação!**

## 🔧 Troubleshooting

### Erro: "Python não encontrado"
- Instale Python 3.11+
- Adicione Python ao PATH do sistema

### Erro: "PyInstaller não encontrado"
```bash
pip install pyinstaller
```

### Executável muito grande
- Normal: 50-100 MB (inclui todas as dependências)
- Use `--onefile` no PyInstaller (já configurado)

### Instalador não detecta versão anterior
- Verifique se o AppID está correto
- Verifique se a versão anterior está instalada

## 📝 Checklist Final

Antes de distribuir o instalador, verifique:

- [ ] Executável gerado com sucesso (`dist\NoAzul.exe`)
- [ ] AppID correto no `.iss`
- [ ] Versão atualizada no `.iss`
- [ ] Instalador compilado sem erros
- [ ] Testado em máquina limpa
- [ ] Dados preservados em atualização
- [ ] Aplicativo abre corretamente após instalação

## 🎉 Pronto!

Seu instalador está pronto para distribuição!

O arquivo final estará em:
```
installer_output\NoAzul_Setup_v2.0.0.exe
```

