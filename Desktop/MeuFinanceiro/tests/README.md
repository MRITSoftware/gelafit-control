# 🧪 Testes - No Azul

Documentação sobre como executar e adicionar testes ao sistema No Azul.

## 📋 Requisitos

Instale as dependências de teste:

```bash
pip install -r requirements.txt
```

Isso instalará:
- `pytest` - Framework de testes
- `pytest-cov` - Cobertura de código

## 🚀 Executar Testes

### Todos os Testes
```bash
python -m pytest tests/ -v
```

### Teste Específico
```bash
# Arquivo específico
python -m pytest tests/test_validadores.py -v

# Classe específica
python -m pytest tests/test_validadores.py::TestValidadorValorMonetario -v

# Teste específico
python -m pytest tests/test_validadores.py::TestValidadorValorMonetario::test_valor_valido_com_virgula -v
```

### Com Cobertura de Código
```bash
# Relatório simples
python -m pytest tests/ --cov=utils --cov=models

# Relatório detalhado
python -m pytest tests/ --cov=utils --cov=models --cov-report=term-missing

# Relatório HTML (abre no navegador)
python -m pytest tests/ --cov=utils --cov=models --cov-report=html
# Depois abra: htmlcov/index.html
```

### Modo Verbose com Detalhes
```bash
python -m pytest tests/ -vv -s
```

## 📊 Estrutura de Testes

```
tests/
├── README.md                 # Este arquivo
├── test_validadores.py      # Testes dos validadores
├── test_financeiro.py        # Testes do modelo financeiro
└── temp/                     # Pasta temporária (criada durante testes)
```

## ✅ Testes Implementados

### `test_validadores.py` (25+ testes)
- `TestValidadorValorMonetario`: Testa validação de valores monetários
- `TestValidadorData`: Testa validação de datas
- `TestValidadorTexto`: Testa validação de textos
- `TestValidadorMesAno`: Testa validação de mês/ano
- `TestValidadorSenha`: Testa validação de senhas

### `test_financeiro.py` (15+ testes)
- `TestGerenciadorFinanceiro`: Testa funcionalidades principais
- `TestAlternarStatusPagamento`: Testa status de pagamento

## 📝 Adicionar Novos Testes

### Exemplo de Teste Simples

```python
import pytest
from utils.validadores import validar_valor_monetario, ValidacaoError

def test_valor_valido():
    """Testa se valor válido é aceito"""
    resultado = validar_valor_monetario("100,50")
    assert resultado == 100.50

def test_valor_invalido():
    """Testa se valor inválido lança erro"""
    with pytest.raises(ValidacaoError):
        validar_valor_monetario("abc")
```

### Usando Fixtures

```python
import pytest

@pytest.fixture
def dados_teste():
    """Cria dados de teste"""
    return {
        "nome": "Teste",
        "valor": 100.00
    }

def test_com_fixture(dados_teste):
    """Usa fixture nos testes"""
    assert dados_teste["valor"] == 100.00
```

## 🎯 Boas Práticas

### 1. Nome de Testes Descritivos
```python
# ❌ Ruim
def test_1():
    pass

# ✅ Bom
def test_validar_valor_monetario_com_virgula():
    pass
```

### 2. Um Conceito por Teste
```python
# ❌ Ruim - testa múltiplas coisas
def test_tudo():
    assert validar_valor("100") == 100
    assert validar_data("01/01/2025")[0] is True
    assert validar_texto("teste") == "teste"

# ✅ Bom - um teste por conceito
def test_validar_valor():
    assert validar_valor("100") == 100

def test_validar_data():
    assert validar_data("01/01/2025")[0] is True

def test_validar_texto():
    assert validar_texto("teste") == "teste"
```

### 3. Arrange, Act, Assert
```python
def test_adicionar_transacao():
    # Arrange (preparar)
    gerenciador = GerenciadorFinanceiro()
    
    # Act (executar)
    gerenciador.adicionar_transacao(
        tipo="renda",
        nome="Salário",
        valor="5000",
        categoria="Trabalho"
    )
    
    # Assert (verificar)
    dados = gerenciador.carregar_dados()
    assert len(dados["transacoes"]) > 0
```

### 4. Testar Casos Extremos
```python
def test_valor_zero():
    assert validar_valor("0") == 0.0

def test_valor_muito_grande():
    with pytest.raises(ValidacaoError):
        validar_valor("999999999999")

def test_valor_negativo():
    with pytest.raises(ValidacaoError):
        validar_valor("-100")
```

## 🐛 Debug de Testes

### Ver Output Completo
```bash
python -m pytest tests/ -v -s
```

### Parar no Primeiro Erro
```bash
python -m pytest tests/ -x
```

### Rodar Último Teste que Falhou
```bash
python -m pytest tests/ --lf
```

### Modo Interativo (PDB)
```python
def test_debug():
    import pdb; pdb.set_trace()
    assert True
```

## 📈 Meta de Cobertura

Objetivo: **>80% de cobertura de código**

Verificar cobertura atual:
```bash
python -m pytest tests/ --cov=utils --cov=models --cov-report=term
```

## ⚠️ Problemas Comuns

### "ModuleNotFoundError"
**Solução**: Certifique-se de estar no diretório raiz do projeto

### "No tests collected"
**Solução**: Verifique se os arquivos de teste começam com `test_`

### Testes passam isoladamente mas falham juntos
**Solução**: Problemas com estado compartilhado, use fixtures para isolar

### ImportError em testes
**Solução**: Adicione o caminho do projeto ao sys.path:
```python
import sys
import os
sys.path.insert(0, os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))
```

## 📞 Suporte

Dúvidas sobre testes:
- **Email**: matheus@mrit.com.br
- **Documentação pytest**: https://docs.pytest.org/

---

**Desenvolvido com ❤️ por MRIT Software © 2025**

