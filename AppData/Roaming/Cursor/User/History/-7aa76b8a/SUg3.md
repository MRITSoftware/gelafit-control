# 🚀 No Azul - Instruções para Gerar Executável

Este guia explica como gerar um executável (.exe) do aplicativo No Azul usando PyInstaller.

## 📋 Pré-requisitos

- Python 3.8 ou superior instalado
- Windows 10 ou superior
- Conexão com internet (para instalar dependências)

## 🛠️ Métodos de Geração

### Método 1: Script Automático (Recomendado)

1. **Execute o arquivo `build.bat`**
   - Clique duas vezes no arquivo `build.bat`
   - O script fará tudo automaticamente

2. **Aguarde a conclusão**
   - O processo pode levar alguns minutos
   - O executável será criado na pasta `dist/`

### Método 2: Script Python

1. **Abra o terminal no diretório do projeto**
   ```cmd
   cd C:\Users\Matheus\Desktop\MeuFinanceiro
   ```

2. **Execute o setup.py**
   ```cmd
   python setup.py
   ```

### Método 3: PyInstaller Direto

1. **Instale as dependências**
   ```cmd
   pip install -r requirements.txt
   ```

2. **Gere o executável**
   ```cmd
   pyinstaller main.spec
   ```

## 📁 Estrutura de Saída

Após a geração, você terá:

```
📦 Projeto/
├── 📁 dist/
│   └── 🚀 NoAzul.exe          # Executável principal
├── 📁 installer/
│   ├── 🚀 NoAzul.exe          # Cópia do executável
│   └── 📄 LEIA-ME.txt         # Instruções para usuários
├── 📁 build/                  # Arquivos temporários (pode ser removido)
└── 📄 main.spec               # Configuração do PyInstaller
```

## ⚙️ Configurações do Executável

O arquivo `main.spec` contém as configurações:

- **Nome**: NoAzul.exe
- **Ícone**: noazul_logo.ico
- **Console**: Desabilitado (interface gráfica apenas)
- **Arquivos incluídos**: 
  - Imagens e ícones
  - Arquivos de configuração
  - Todos os módulos Python necessários

## 🔧 Solução de Problemas

### Erro: "Python não encontrado"
- Instale o Python: https://www.python.org/downloads/
- Marque "Add Python to PATH" durante a instalação

### Erro: "Módulo não encontrado"
- Execute: `pip install -r requirements.txt`
- Verifique se todas as dependências estão instaladas

### Executável muito grande
- O tamanho normal é entre 50-100 MB
- Isso é normal para aplicações com interface gráfica

### Executável não abre
- Verifique se o Windows Defender não está bloqueando
- Execute como administrador se necessário

## 📞 Suporte

Se encontrar problemas:

- **WhatsApp**: (19) 97134-9642
- **Email**: matheus@mrit.com.br
- **Site**: www.mrit.com.br

## 🎯 Dicas Importantes

1. **Teste sempre** o executável antes de distribuir
2. **Mantenha** os arquivos de configuração junto com o executável
3. **Use** o instalador da pasta `installer/` para distribuição
4. **Verifique** se o antivírus não está bloqueando o executável

---

💙 **Desenvolvido com ❤️ por MRIT Software**
