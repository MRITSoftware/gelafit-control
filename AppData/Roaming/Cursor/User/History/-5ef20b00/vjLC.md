# Sistema de Reconhecimento de Produtos

Este é um sistema de reconhecimento de produtos que permite cadastrar produtos com suas imagens e reconhecer esses produtos em tempo real usando a câmera.

## Funcionalidades

### 1. Cadastro de Produtos
- Cadastre produtos com nome e imagem
- Visualize preview da imagem antes de cadastrar
- Lista todos os produtos cadastrados
- Remova produtos da lista

### 2. Reconhecimento em Tempo Real
- Use a câmera para reconhecer produtos
- Exibe o nome do produto quando reconhecido
- Interface visual com feed da câmera

## Instalação

1. Instale as dependências:
```bash
pip install -r requirements.txt
```

2. Execute o sistema:
```bash
python product_recognition_system.py
```

## Como Usar

### Cadastrando Produtos
1. Vá para a aba "Cadastro de Produtos"
2. Digite o nome do produto
3. Clique em "Selecionar Imagem" e escolha uma foto do produto
4. Clique em "Cadastrar Produto"

### Reconhecendo Produtos
1. Vá para a aba "Reconhecimento"
2. Clique em "Iniciar Reconhecimento"
3. Aponte a câmera para o produto
4. O sistema exibirá o nome do produto quando reconhecido
5. Clique em "Parar Reconhecimento" para parar

## Requisitos do Sistema

- Python 3.7+
- Câmera webcam
- Windows/Linux/MacOS

## Dependências

- opencv-python: Para processamento de imagem e reconhecimento
- numpy: Para operações matemáticas
- tkinter: Para interface gráfica (incluído no Python)
- Pillow: Para manipulação de imagens

## Estrutura de Arquivos

- `product_recognition_system.py`: Arquivo principal do sistema
- `requirements.txt`: Lista de dependências
- `produtos/`: Diretório onde as imagens dos produtos são armazenadas
- `produtos_database.json`: Banco de dados dos produtos cadastrados

## Notas Técnicas

- O sistema usa template matching para reconhecimento
- A similaridade mínima é configurada em 70%
- As imagens são redimensionadas para 100x100 pixels para comparação
- O sistema funciona melhor com produtos que têm características visuais distintas
