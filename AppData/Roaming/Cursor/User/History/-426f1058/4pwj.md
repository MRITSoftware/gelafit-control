# 🧪 Guia de Teste do APK

## 📱 Passo 1: Abrir o App

1. Abra o app "MRIT Server Local" no seu dispositivo
2. Você verá uma tela com botões:
   - **Instalar** (botão principal)
   - **Abrir Termux**
   - **Copiar: Instalar**
   - **Copiar: Iniciar**
   - **Copiar: Status**
   - **Copiar: Logs**

## 📦 Passo 2: Instalar

1. **Certifique-se que o Termux está instalado** (da Play Store)
2. Clique no botão **"Instalar"**
3. O app irá:
   - Copiar arquivos
   - Abrir o Termux automaticamente
   - Copiar um comando para a área de transferência

## 🔧 Passo 3: No Termux

1. Quando o Termux abrir, você verá o prompt normal
2. **Cole o comando** que foi copiado:
   - **Android**: Long press na tela → Colar (ou Ctrl+Shift+V)
   - **Tablet**: Ctrl+Shift+V
3. Pressione **Enter**

## ⏳ Passo 4: Aguardar Instalação

O comando irá:
1. Criar a pasta `~/servidorzinho`
2. Copiar todos os arquivos
3. Instalar dependências (10-15 minutos):
   - Python
   - Rust
   - cryptography
   - tinytuya
4. Iniciar o servidor automaticamente

**⚠️ IMPORTANTE**: Não feche o Termux durante a instalação!

## ✅ Passo 5: Verificar se Funcionou

### Opção A: Verificar no Termux

No Termux, execute:
```bash
cd ~/servidorzinho
tail -20 servidor.log
```

Você deve ver algo como:
```
[2024-01-01 12:00:00] 🚀 Servidorzinho Auto iniciado
[2024-01-01 12:00:00] 📌 Site: Site Automático
[2024-01-01 12:00:00] 🌐 Porta: 8080
[2024-01-01 12:00:00] ✅ Servidor HTTP ativo
```

### Opção B: Verificar Status do Servidor

No Termux, execute:
```bash
cd ~/servidorzinho
curl http://localhost:8080/status
```

Você deve receber uma resposta JSON:
```json
{
  "status": "ok",
  "site_name": "Site Automático",
  "devices_count": 0,
  "port": 8080,
  "uptime": 123.45
}
```

### Opção C: Verificar Processo

No Termux, execute:
```bash
ps aux | grep servidor_auto | grep -v grep
```

Você deve ver um processo Python rodando.

## 🐛 Se Algo Der Errado

### Problema: "Termux não encontrado"
**Solução**: Instale o Termux da Play Store primeiro

### Problema: "Erro ao copiar arquivos"
**Solução**: 
1. Feche e reabra o app
2. Clique em "Instalar" novamente
3. Verifique as permissões do app nas configurações

### Problema: "cryptography não instalou"
**Solução**:
```bash
cd ~/servidorzinho
bash INSTALAR_AUTO.sh
```

### Problema: "Servidor não iniciou"
**Solução**:
1. Verifique os logs:
   ```bash
   cd ~/servidorzinho
   tail -30 servidor.log
   ```
2. Tente iniciar manualmente:
   ```bash
   cd ~/servidorzinho
   python3 servidor_auto.py
   ```
   (Isso mostrará o erro em tempo real)

### Problema: "Porta 8080 já em uso"
**Solução**:
```bash
cd ~/servidorzinho
pkill -f servidor_auto
bash iniciar_auto.sh
```

## 📋 Comandos Úteis

### Ver logs em tempo real
```bash
cd ~/servidorzinho
tail -f servidor.log
```

### Parar servidor
```bash
cd ~/servidorzinho
pkill -f servidor_auto
# ou
kill $(cat servidor.pid)
```

### Reiniciar servidor
```bash
cd ~/servidorzinho
pkill -f servidor_auto
bash iniciar_auto.sh
```

### Verificar se está rodando
```bash
cd ~/servidorzinho
curl http://localhost:8080/status
```

## ✅ Checklist de Teste

- [ ] App abre sem erros
- [ ] Botão "Instalar" funciona
- [ ] Termux abre automaticamente
- [ ] Comando foi copiado para área de transferência
- [ ] Comando cola no Termux
- [ ] Instalação inicia (mostra progresso)
- [ ] Instalação completa sem erros
- [ ] Servidor inicia automaticamente
- [ ] Logs mostram servidor rodando
- [ ] `curl http://localhost:8080/status` retorna JSON válido
- [ ] Processo Python está rodando

## 🎯 Teste Final

Execute este comando no Termux para verificar tudo:
```bash
cd ~/servidorzinho && \
echo "=== Verificando Instalação ===" && \
python3 --version && \
python3 -c "import tinytuya; print('✅ tinytuya OK')" && \
python3 -c "import cryptography; print('✅ cryptography OK')" && \
echo "" && \
echo "=== Verificando Servidor ===" && \
ps aux | grep servidor_auto | grep -v grep && \
echo "" && \
echo "=== Testando HTTP ===" && \
curl -s http://localhost:8080/status | head -5
```

Se tudo estiver OK, você verá:
- ✅ Versão do Python
- ✅ tinytuya OK
- ✅ cryptography OK
- ✅ Processo rodando
- ✅ Resposta JSON do servidor

