# Extrator de Tabelas de PDF - Extratos Bancários

Sistema web para extrair tabelas de PDFs de extratos bancários e exportar para Excel.

## 🚀 Funcionalidades

- **Upload de PDFs**: Interface drag-and-drop intuitiva
- **Extração Automática**: Reconhece e extrai tabelas usando múltiplos algoritmos
- **Validação Inteligente**: Verifica qualidade e viabilidade das tabelas extraídas
- **Exportação Excel**: Exporta todas as tabelas para Excel com um clique
- **Interface Responsiva**: Design moderno e mobile-friendly
- **Compatível com Hospedagem**: Funciona em Hostinger, Hostgator e similares

## 📋 Pré-requisitos

- Python 3.8+
- Java 8+ (necessário para tabula-py)
- Hospedagem compartilhada com suporte a Python/Flask

## 🛠️ Instalação

### 1. Instalar dependências do sistema

```bash
# Ubuntu/Debian
sudo apt-get update
sudo apt-get install python3 python3-pip openjdk-8-jdk

# CentOS/RHEL
sudo yum install python3 python3-pip java-1.8.0-openjdk

# Windows (usando Chocolatey)
choco install python openjdk8
```

### 2. Instalar dependências Python

```bash
pip install -r requirements.txt
```

### 3. Configurar para hospedagem compartilhada

1. Faça upload de todos os arquivos para o servidor
2. Certifique-se de que o arquivo `wsgi.py` está na raiz do domínio
3. Configure o domínio para usar Python/Flask
4. Crie as pastas `uploads` e `templates` se não existirem

## 🚀 Como usar

1. Acesse o site no navegador
2. Faça upload de um PDF de extrato bancário
3. Aguarde o processamento automático
4. Visualize as tabelas extraídas
5. Use a validação para verificar a qualidade
6. Exporte para Excel quando satisfeito

## 📁 Estrutura do Projeto

```
├── app.py              # Aplicação Flask principal
├── wsgi.py             # Configuração WSGI para hospedagem
├── requirements.txt    # Dependências Python
├── .htaccess          # Configurações Apache
├── templates/
│   └── index.html     # Interface web
├── uploads/           # Pasta para arquivos enviados
└── README.md          # Este arquivo
```

## 🔧 Configurações Avançadas

### Limites de Upload
- Tamanho máximo: 16MB
- Formatos aceitos: PDF
- Timeout: 5 minutos

### Algoritmos de Extração
1. **Lattice**: Para tabelas com bordas definidas
2. **Stream**: Para tabelas sem bordas
3. **Por Páginas**: Análise individual de páginas

### Validação de Tabelas
- Verifica se tabela não está vazia
- Conta colunas e linhas
- Identifica colunas vazias
- Detecta tipos de dados (datas, números)
- Fornece recomendações

## 🌐 Hospedagem

### Hostinger
1. Acesse o painel de controle
2. Vá em "Python" ou "Aplicações"
3. Configure o domínio para usar Python
4. Faça upload dos arquivos
5. Configure o arquivo de entrada como `wsgi.py`

### Hostgator
1. Acesse o cPanel
2. Vá em "Python App"
3. Crie uma nova aplicação
4. Configure o diretório e arquivo de entrada
5. Instale as dependências via terminal

## 🐛 Solução de Problemas

### Erro de Java
```
Error: Java not found
```
**Solução**: Instale Java 8+ e configure a variável JAVA_HOME

### Erro de memória
```
MemoryError during PDF processing
```
**Solução**: Aumente o limite de memória ou use PDFs menores

### Erro de timeout
```
Request timeout
```
**Solução**: Aumente o tempo limite no servidor ou use PDFs menores

## 📞 Suporte

Para suporte técnico ou dúvidas:
- Verifique os logs do servidor
- Teste com PDFs menores primeiro
- Certifique-se de que todas as dependências estão instaladas

## 📄 Licença

Este projeto é de código aberto e pode ser usado livremente para fins comerciais e pessoais.
