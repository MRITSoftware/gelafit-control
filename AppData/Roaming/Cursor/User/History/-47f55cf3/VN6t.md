# Sistema Avançado de Leitura de Extratos Bancários

## 🚀 Funcionalidades Principais

Este sistema avançado é capaz de:

- **Detecção Automática de Colunas**: Identifica automaticamente todas as colunas das tabelas PDF de extratos bancários
- **Recriação da Estrutura Original**: Recria tabelas exatamente como aparecem no PDF original
- **Suporte a Múltiplos Formatos**: Funciona com diferentes formatos de extratos bancários
- **Análise Detalhada**: Mostra estatísticas completas da estrutura dos dados
- **Interface Web Intuitiva**: Interface Streamlit moderna e responsiva
- **Exportação Avançada**: Gera relatórios com estrutura original preservada

## 📋 Colunas Detectadas Automaticamente

O sistema identifica automaticamente as seguintes colunas:

- **Data**: Data da transação (DD/MM/YYYY, DD-MM-YYYY, etc.)
- **Descrição**: Descrição do lançamento/transação
- **Documento**: Número do documento/referência
- **Crédito**: Valores de entrada
- **Débito**: Valores de saída
- **Saldo**: Saldo da conta
- **Valor**: Valor geral da transação

## 🔧 Instalação

### Requisitos
- Python 3.8 ou superior
- pip (gerenciador de pacotes Python)

### Instalação Automática (Windows)
```bash
run_avancado.bat
```

### Instalação Automática (Linux/Mac)
```bash
chmod +x run_avancado.sh
./run_avancado.sh
```

### Instalação Manual
```bash
pip install -r requirements_avancado.txt
```

## 🚀 Como Usar

### 1. Executar o Sistema
```bash
streamlit run interface_avancada.py
```

### 2. Acessar a Interface
Abra seu navegador e acesse: `http://localhost:8501`

### 3. Upload do PDF
- Clique em "Selecione o arquivo PDF do extrato"
- Escolha o arquivo PDF do seu extrato bancário
- O sistema processará automaticamente

### 4. Visualizar Resultados
O sistema oferece 5 abas:

- **🔍 Estrutura**: Mostra as colunas detectadas e estatísticas
- **📊 Resumo**: Resumo financeiro das transações
- **📋 Tabela Original**: Tabela recriada exatamente como no PDF
- **📈 Gráficos**: Análises gráficas dos dados
- **📄 Exportar**: Exportar para Excel ou PDF

## 📊 Formatos Suportados

O sistema suporta diversos formatos de cabeçalho de extratos:

- `Data Lancamento Dcto. Credito (R$) Debito (R$) Saldo (R$)`
- `Dt. Historico Documento Creditos Debitos Saldo`
- `Data Descricao Nº Documento C D Saldos`
- `Balancete Lancamentos Ref. Credito Debito Saldo`
- `Data Operação Descrição Valor Saldo`
- `Data Transação Histórico Documento Crédito Débito Saldo Atual`
- E muitos outros...

## 🎯 Tipos de Transação Identificados

O sistema classifica automaticamente os tipos de transação:

- **PIX**: Transferências PIX
- **TED**: Transferências Eletrônicas
- **DOC**: Documento de Ordem de Crédito
- **DÉBITO**: Saques, compras, pagamentos
- **CRÉDITO**: Depósitos, transferências recebidas
- **TARIFA**: Tarifas bancárias
- **JUROS**: Juros e rendimentos
- **SALDO**: Saldo inicial/anterior
- **PAGAMENTO**: Pagamentos diversos
- **CARTAO**: Transações de cartão
- **INVESTIMENTO**: Aplicações financeiras
- **EMPRESTIMO**: Empréstimos e financiamentos

## 📈 Funcionalidades da Interface

### Detecção de Estrutura
- Mostra todas as colunas identificadas no PDF
- Exibe estatísticas de cada coluna
- Indica o tipo de dados (texto/número)

### Tabela Original
- Recria a tabela exatamente como no PDF
- Mantém a formatação original
- Permite filtros por tipo, direção e data
- Mostra todas as colunas detectadas

### Análise Gráfica
- Gráfico de pizza por tipo de transação
- Gráfico de linha temporal
- Gráfico de barras por valores

### Exportação
- **Excel**: Exporta dados com estrutura original
- **PDF**: Gera relatório completo com estrutura detectada

## 🔍 Exemplo de Uso

```python
from extrato_reader_avancado import ExtratoBancarioAvancado

# Criar instância do leitor
reader = ExtratoBancarioAvancado()

# Processar extrato
df = reader.processar_extrato("extrato.pdf")

# Ver estrutura detectada
print("Colunas detectadas:", df.columns.tolist())

# Ver primeiras linhas
print(df.head())
```

## 🧪 Teste do Sistema

Execute o teste completo:
```bash
python teste_sistema_avancado.py
```

Este teste verifica:
- Detecção de colunas
- Processamento de linhas
- Classificação de tipos
- Extração de valores

## 📁 Estrutura de Arquivos

```
├── extrato_reader_avancado.py    # Sistema principal
├── interface_avancada.py         # Interface Streamlit
├── teste_sistema_avancado.py     # Testes do sistema
├── requirements_avancado.txt     # Dependências
├── run_avancado.bat             # Execução Windows
├── run_avancado.sh              # Execução Linux/Mac
└── README_AVANCADO.md           # Este arquivo
```

## 🆚 Diferenças do Sistema Anterior

### Sistema Anterior
- Detecção limitada de colunas
- Estrutura fixa
- Menos flexível

### Sistema Avançado
- ✅ Detecção automática de todas as colunas
- ✅ Recria estrutura exatamente como no PDF
- ✅ Suporte a múltiplos formatos
- ✅ Análise detalhada da estrutura
- ✅ Interface mais rica
- ✅ Exportação com estrutura original

## 🐛 Solução de Problemas

### Erro de Dependências
```bash
pip install --upgrade pip
pip install -r requirements_avancado.txt
```

### Erro de Permissão (Linux/Mac)
```bash
chmod +x run_avancado.sh
```

### PDF não Processado
- Verifique se o PDF contém texto (não é imagem)
- Tente com um PDF de extrato bancário válido
- Verifique se o arquivo não está corrompido

## 📞 Suporte

Para dúvidas ou problemas:
1. Execute o teste: `python teste_sistema_avancado.py`
2. Verifique os logs de erro
3. Teste com diferentes formatos de PDF

## 🎉 Conclusão

Este sistema avançado oferece uma solução completa para leitura e análise de extratos bancários, recriando exatamente a estrutura original do PDF e fornecendo ferramentas poderosas de análise e exportação.

**Execute agora**: `streamlit run interface_avancada.py`
