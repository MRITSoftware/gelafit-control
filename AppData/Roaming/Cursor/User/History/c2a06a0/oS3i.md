# Instalação do MRIT Orion - Dependências Avançadas

## 📋 Dependências Adicionais

Para melhorar a extração de tabelas de PDFs bancários, o MRIT Orion agora inclui várias bibliotecas especializadas. Algumas delas requerem instalação adicional de dependências do sistema.

## 🛠️ Instalação Completa

### 1. Dependências Python
```bash
pip install -r requirements.txt
```

### 2. Dependências do Sistema

#### Windows
```bash
# Instalar Tesseract OCR
# Baixar de: https://github.com/UB-Mannheim/tesseract/wiki
# Adicionar ao PATH: C:\Program Files\Tesseract-OCR

# Instalar Java (necessário para Tabula)
# Baixar de: https://www.java.com/pt_BR/download/

# Instalar Ghostscript (necessário para pdf2image)
# Baixar de: https://www.ghostscript.com/download/gsdnld.html
```

#### Linux (Ubuntu/Debian)
```bash
sudo apt-get update
sudo apt-get install tesseract-ocr
sudo apt-get install openjdk-11-jdk
sudo apt-get install ghostscript
sudo apt-get install poppler-utils
```

#### macOS
```bash
brew install tesseract
brew install openjdk
brew install ghostscript
brew install poppler
```

### 3. Verificar Instalação
```bash
python -c "import pytesseract; print('Tesseract OK')"
python -c "import camelot; print('Camelot OK')"
python -c "import tabula; print('Tabula OK')"
```

## 🔧 Configuração do Tesseract

### Windows
1. Baixe o Tesseract de: https://github.com/UB-Mannheim/tesseract/wiki
2. Instale e adicione ao PATH
3. Configure no código se necessário:
```python
pytesseract.pytesseract.tesseract_cmd = r'C:\Program Files\Tesseract-OCR\tesseract.exe'
```

### Linux/macOS
O Tesseract deve ser encontrado automaticamente no PATH.

## 🚀 Executar o Sistema

```bash
python app.py
```

Acesse: http://localhost:5000

## 🐛 Solução de Problemas

### Erro: "Tesseract not found"
- Verifique se o Tesseract está instalado e no PATH
- No Windows, configure o caminho manualmente

### Erro: "Java not found" (Tabula)
- Instale o Java JDK
- Verifique se está no PATH

### Erro: "Ghostscript not found" (pdf2image)
- Instale o Ghostscript
- No Windows, adicione ao PATH

### Erro: "Camelot not working"
- Verifique se o OpenCV está instalado corretamente
- Tente: `pip install opencv-python-headless`

## 📊 Métodos de Extração Disponíveis

1. **pdfplumber** - Método padrão, bom para tabelas estruturadas
2. **Camelot Lattice** - Ideal para tabelas com bordas definidas
3. **Camelot Stream** - Bom para tabelas sem bordas claras
4. **Tabula** - Útil para PDFs com tabelas em formato de imagem
5. **Padrões Manuais** - Detecta padrões específicos de extratos bancários
6. **OCR** - Para PDFs escaneados ou com texto em imagem

## 🎯 Dicas para Melhor Extração

- **PDFs Digitais**: Use pdfplumber ou Camelot
- **PDFs Escaneados**: Use OCR
- **Tabelas com Bordas**: Use Camelot Lattice
- **Tabelas sem Bordas**: Use Camelot Stream ou Tabula
- **Extratos Bancários**: Use padrões manuais

## 🔄 Atualizações

Para atualizar o sistema:
```bash
git pull
pip install -r requirements.txt --upgrade
```

## 📞 Suporte

Se encontrar problemas:
1. Verifique se todas as dependências estão instaladas
2. Consulte os logs de erro no console
3. Teste com diferentes tipos de PDF
4. Use o botão "Info Extração" para ver quais métodos funcionaram
