# 🔧 Solução para Erro do Cryptography no Termux

## Problema

O `cryptography` precisa compilar código Rust, mas o Rust não está instalado no Termux.

## ✅ Solução Rápida

### Opção 1: Instalar Rust e Dependências (Recomendado)

```bash
# 1. Atualizar pacotes
pkg update && pkg upgrade -y

# 2. Instalar dependências de compilação
pkg install rust clang python-dev libffi-dev openssl-dev -y

# 3. Instalar dependências Python
pip install flask tinytuya
```

### Opção 2: Usar Versão Mais Antiga (Mais Rápido)

```bash
# Instalar versão do cryptography que não precisa de Rust
pip install cryptography==41.0.7 flask tinytuya
```

### Opção 3: Instalar Apenas Rust (Mínimo Necessário)

```bash
# Instalar apenas Rust
pkg install rust -y

# Tentar instalar novamente
pip install flask tinytuya
```

## 🔍 Verificar Instalação

```bash
# Verificar se Rust está instalado
rustc --version

# Verificar se Python pode importar
python3 -c "import flask; import tinytuya; print('OK!')"
```

## ⚠️ Notas Importantes

- **Rust é grande:** Pode levar alguns minutos para instalar (~200MB)
- **Compilação demora:** A primeira instalação do cryptography pode levar 5-10 minutos
- **Paciência:** Deixe compilar, não cancele o processo

## 🐛 Se Ainda Der Erro

### Erro: "Unsupported platform"
```bash
# Tentar forçar instalação de versão específica
pip install cryptography==41.0.7 --no-build-isolation
```

### Erro: "Rust not found"
```bash
# Verificar se Rust está no PATH
which rustc

# Se não estiver, adicionar ao PATH
export PATH=$PATH:$HOME/.cargo/bin
```

### Erro: "Permission denied"
```bash
# Usar --user se necessário
pip install --user flask tinytuya
```

## 📝 Comandos Completos (Copiar e Colar)

```bash
# Solução completa em um comando
pkg update && pkg upgrade -y && \
pkg install rust clang python-dev libffi-dev openssl-dev -y && \
pip install flask tinytuya
```

---

**Dica:** Se estiver com pressa, use a Opção 2 (versão antiga do cryptography).

