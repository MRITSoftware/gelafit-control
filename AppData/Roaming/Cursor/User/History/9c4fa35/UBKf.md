# 📊 Extrator de Tabelas de Extrato Bancário

Sistema web para extrair tabelas de PDFs de extratos bancários e exportar para Excel.

## 🚀 Funcionalidades

- **Upload de PDF**: Interface drag-and-drop para upload de arquivos PDF
- **Extração Inteligente**: Reconhece e extrai tabelas automaticamente do PDF
- **Visualização**: Exibe as tabelas exatamente como estão no documento original
- **Exportação Excel**: Permite baixar todas as tabelas em formato Excel
- **Interface Moderna**: Design responsivo e intuitivo

## 🛠️ Instalação

### Pré-requisitos

- Python 3.8 ou superior
- Ghostscript (necessário para o camelot)

### Windows
1. Instale o Ghostscript: https://www.ghostscript.com/download/gsdnld.html
2. Adicione o Ghostscript ao PATH do sistema

### Linux (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install ghostscript
```

### macOS
```bash
brew install ghostscript
```

### Instalação do Projeto

1. Clone ou baixe os arquivos do projeto
2. Instale as dependências:
```bash
pip install -r requirements.txt
```

3. Execute a aplicação:
```bash
python app.py
```

4. Acesse no navegador: http://localhost:5000

## 📋 Como Usar

1. **Upload do PDF**: 
   - Arraste e solte o arquivo PDF na área de upload
   - Ou clique na área para selecionar o arquivo

2. **Processamento**: 
   - O sistema irá processar o PDF automaticamente
   - As tabelas serão extraídas e exibidas na tela

3. **Visualização**: 
   - Visualize todas as tabelas encontradas
   - Cada tabela é exibida exatamente como no PDF original

4. **Exportação**: 
   - Clique em "Baixar Excel" para exportar todas as tabelas
   - O arquivo Excel será baixado automaticamente

## 🔧 Tecnologias Utilizadas

- **Backend**: Flask (Python)
- **Extração de PDF**: Camelot-py
- **Processamento de Dados**: Pandas
- **Exportação Excel**: OpenPyXL
- **Frontend**: HTML5, CSS3, JavaScript

## 📁 Estrutura do Projeto

```
projeto/
├── app.py                 # Aplicação Flask principal
├── templates/
│   └── index.html        # Interface web
├── uploads/              # Pasta para arquivos temporários
├── requirements.txt      # Dependências Python
└── README.md            # Documentação
```

## ⚠️ Limitações

- Funciona melhor com PDFs que contêm tabelas bem estruturadas
- Pode não reconhecer tabelas com formatação muito complexa
- Tamanho máximo de arquivo: 16MB

## 🐛 Solução de Problemas

### Erro de Ghostscript
- Certifique-se de que o Ghostscript está instalado e no PATH
- No Windows, reinicie o terminal após instalar

### Erro de Dependências
- Use um ambiente virtual Python
- Instale as dependências uma por uma se necessário

### Tabelas não detectadas
- Verifique se o PDF contém tabelas bem estruturadas
- Tente com PDFs de diferentes bancos

## 📞 Suporte

Para problemas ou dúvidas, verifique:
1. Se todas as dependências estão instaladas
2. Se o Ghostscript está funcionando
3. Se o PDF contém tabelas legíveis

## 🔄 Atualizações Futuras

- Suporte a mais formatos de exportação
- Melhorias na detecção de tabelas
- Interface para edição das tabelas extraídas
- Suporte a PDFs com senha
