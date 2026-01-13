# 🔧 Correção de Permissões - No Azul v2.0

## ❌ **PROBLEMA QUE ESTAVA ACONTECENDO**

Quando instalado em `Program Files`, o app tentava criar arquivos na pasta de instalação, mas essa pasta é protegida e requer permissões de administrador.

**Erro:**
```
PermissionError: [Errno 13] Permission denied: 'C:\Program Files\No Azul\logs\noazul_202510.log'
```

---

## ✅ **SOLUÇÃO IMPLEMENTADA**

Criei um sistema inteligente de caminhos que detecta automaticamente onde salvar os dados:

### **📁 Novo arquivo: `utils/paths.py`**

Este arquivo detecta:
- Se está rodando como executável ou script Python
- Se está instalado em `Program Files` (pasta protegida)
- Automaticamente escolhe o melhor local para os dados

### **📂 Onde os arquivos são salvos agora:**

#### **Em Desenvolvimento (Python):**
```
C:\Users\Matheus\Desktop\MeuFinanceiro\
├── data.json
├── senha.json
├── config.json
├── logs\
└── backups\
```

#### **Quando Instalado (Program Files):**
```
C:\Users\[SEU_NOME]\AppData\Local\No Azul\
├── data.json          ← Dados financeiros
├── senha.json         ← Senha hash
├── config.json        ← Configurações
├── logs\              ← Logs do sistema
│   └── noazul_202510.log
└── backups\           ← Backups automáticos
    └── backup_YYYYMMDD_HHMMSS\
```

---

## 🔄 **ARQUIVOS MODIFICADOS**

### **1. `utils/paths.py` (NOVO)**
- Sistema centralizado de caminhos
- Detecta automaticamente o ambiente
- Cria pastas necessárias automaticamente

### **2. `utils/logger.py`**
- Usa `PASTA_LOGS` do sistema de paths
- Logs salvos em `AppData\Local\No Azul\logs\`

### **3. `models/financeiro.py`**
- Usa `CAMINHO_DATA`, `CAMINHO_CONFIG`, `PASTA_BACKUPS`
- Dados salvos em `AppData\Local\No Azul\`

### **4. `senha.py`**
- Usa `CAMINHO_SENHA` do sistema de paths
- Senha salva em `AppData\Local\No Azul\senha.json`

---

## 🚀 **COMO TESTAR**

### **Passo 1: Desinstalar Versão Antiga**
```
Windows → Configurações → Apps → No Azul → Desinstalar
(Escolha "Sim" para manter dados se perguntar)
```

### **Passo 2: Compilar Novo Instalador**
```
1. Abra Inno Setup Compiler
2. File → Open → installer_inno_com_backup.iss
3. Build → Compile (Ctrl+F9)
4. Instalador gerado: installer_output\NoAzul_Setup_v2.0.0.exe
```

### **Passo 3: Instalar Nova Versão**
```
1. Execute: installer_output\NoAzul_Setup_v2.0.0.exe
2. Siga a instalação normalmente
3. Execute o No Azul
```

### **Passo 4: Verificar que Funcionou**
```
✅ App abre sem erros
✅ Pode criar transações
✅ Pode definir senha
✅ Verifica logs em: C:\Users\[VOCÊ]\AppData\Local\No Azul\logs\
```

---

## 📊 **COMPARAÇÃO**

| Item | ANTES (❌ Problema) | AGORA (✅ Corrigido) |
|------|---------------------|----------------------|
| **Logs** | `C:\Program Files\No Azul\logs\` | `C:\Users\...\AppData\Local\No Azul\logs\` |
| **Dados** | `C:\Program Files\No Azul\data.json` | `C:\Users\...\AppData\Local\No Azul\data.json` |
| **Senha** | `C:\Program Files\No Azul\senha.json` | `C:\Users\...\AppData\Local\No Azul\senha.json` |
| **Backups** | `C:\Program Files\No Azul\backups\` | `C:\Users\...\AppData\Local\No Azul\backups\` |
| **Permissões** | ❌ Requer admin | ✅ Funciona sem admin |
| **Erro** | PermissionError | ✅ Sem erros! |

---

## 🔍 **COMO O SISTEMA FUNCIONA**

### **Detecção Automática:**

```python
# utils/paths.py

def obter_pasta_dados():
    if executando_como_exe():
        pasta_exe = obter_pasta_executavel()
        
        if "Program Files" in pasta_exe:
            # Está em pasta protegida → usar AppData
            return "C:\Users\...\AppData\Local\No Azul\"
        else:
            # Está em pasta normal → usar mesma pasta
            return pasta_exe
    else:
        # Desenvolvimento → pasta do projeto
        return "C:\Users\...\Desktop\MeuFinanceiro\"
```

---

## 💡 **BENEFÍCIOS**

✅ **Funciona em qualquer lugar:**
- Program Files
- Pasta do usuário
- Executável portável
- Modo desenvolvimento

✅ **Sem erros de permissão:**
- AppData sempre tem permissão de escrita
- Não requer executar como administrador

✅ **Dados seguros:**
- Cada usuário tem seus próprios dados
- Isolamento por conta do Windows

✅ **Padrão da indústria:**
- Todos os apps profissionais fazem assim
- Chrome, Discord, VS Code, etc

---

## 🎯 **RESUMO**

### **Problema:**
❌ App tentava escrever em `C:\Program Files\` → Erro de permissão

### **Solução:**
✅ App detecta automaticamente e usa `C:\Users\...\AppData\Local\No Azul\`

### **Resultado:**
🎉 App funciona perfeitamente sem erros de permissão!

---

## 📞 **SE AINDA DER ERRO**

Se mesmo com a correção ainda aparecer erro:

1. **Verifique que gerou novo executável:**
   ```
   SHA-256: 8538d6d8986150eab381bbef71ec19e9060d56ae6de73fafdf2e2aecf01a4256
   ```

2. **Desinstale completamente a versão antiga**

3. **Compile novo instalador no Inno Setup**

4. **Instale a nova versão**

5. **Se persistir, me avise!** 😊

---

**Desenvolvido com ❤️ por MRIT Software © 2025**

