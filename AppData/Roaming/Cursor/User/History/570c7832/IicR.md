# Conversor de Site para PDF

Este script converte o site do jornal escolar em um arquivo PDF.

## Instalação

1. Instale as dependências:
```bash
pip install -r requirements.txt
```

2. Instale os navegadores do Playwright:
```bash
playwright install chromium
```

## Uso

Execute o script:
```bash
python converter_para_pdf.py
```

O PDF será gerado como `jornal_completo.pdf` no mesmo diretório.

## Notas

- O script aguarda alguns segundos para que o JavaScript carregue o conteúdo
- Se o PDF não capturar todo o conteúdo, você pode aumentar o tempo de espera no script
- O formato do PDF é A4 com margens pequenas

