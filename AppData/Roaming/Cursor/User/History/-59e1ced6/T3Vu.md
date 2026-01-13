# 🔧 Solução de Problemas - MRIT Server Local

## Problema: Servidor diz que iniciou mas não está rodando

### Passo 1: Execute o diagnóstico

No Termux, execute:

```bash
cd ~/servidorzinho
bash testar_servidor.sh
```

Isso vai verificar:
- ✅ Se os arquivos estão no lugar certo
- ✅ Se Python está instalado
- ✅ Se tinytuya está instalado
- ✅ Se há erros de sintaxe
- ✅ Se a porta está livre

### Passo 2: Se o diagnóstico falhar

Execute manualmente para ver o erro:

```bash
cd ~/servidorzinho
python3 servidor_auto.py
```

Isso mostra o erro em tempo real.

### Passo 3: Verificar logs

```bash
cd ~/servidorzinho
tail -30 servidor.log
```

### Problemas Comuns

#### 1. "servidor_auto.py não encontrado"
**Solução:**
```bash
bash ~/storage/downloads/MRIT_Server/copy_to_termux.sh
```

#### 2. "Python3 não encontrado"
**Solução:**
```bash
pkg install python
```

#### 3. "tinytuya não encontrado"
**Solução:**
```bash
pip install tinytuya
```

#### 4. "Porta 8080 já em uso"
**Solução:**
```bash
# Ver qual processo está usando
netstat -an | grep 8080
# Ou matar todos os processos Python
pkill -f servidor_auto
```

#### 5. Servidor crasha imediatamente
**Solução:**
Execute manualmente para ver o erro:
```bash
cd ~/servidorzinho
python3 servidor_auto.py
```

### Comandos Úteis

```bash
# Ver se está rodando
ps aux | grep servidor_auto | grep -v grep

# Ver logs em tempo real
tail -f ~/servidorzinho/servidor.log

# Parar servidor
pkill -f servidor_auto

# Reiniciar servidor
cd ~/servidorzinho
bash iniciar_auto.sh
```

