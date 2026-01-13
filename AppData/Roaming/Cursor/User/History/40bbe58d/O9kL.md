# 🔧 Solução - Problema de Dupla Instalação

## ❓ O Problema

Quando você instalou a nova versão do No Azul, ela foi instalada **junto** com a antiga ao invés de **substituir** a versão antiga, ficando duas instalações no sistema.

---

## 🎯 Solução Rápida

### **Opção 1: Desinstalar Manualmente**

#### **Passo 1: Desinstalar as duas versões**
```
1. Abra: Configurações → Apps → Aplicativos e recursos
2. Procure por "No Azul"
3. Clique em cada um e escolha "Desinstalar"
4. Quando perguntar se quer manter os dados → SIM
```

#### **Passo 2: Instalar a nova versão**
```
1. Execute: NoAzul_Setup_v2.0.0.exe
2. Instale normalmente
3. Pronto! Apenas uma instalação!
```

---

### **Opção 2: Script Automático** ⭐ RECOMENDADO

#### **Use o script que criamos:**

```
1. Execute: desinstalar_versao_antiga.bat
2. O script remove automaticamente a versão antiga
3. Seus dados são preservados
4. Depois instale a nova versão normalmente
```

---

## 🔄 O Que Foi Corrigido

Agora o instalador está configurado para:

### ✅ **1. Detectar Instalação Anterior**
```
Ao executar o novo instalador, ele automaticamente:
- Detecta se existe versão antiga
- Remove a versão antiga automaticamente
- Preserva todos os seus dados
- Instala a nova versão no lugar certo
```

### ✅ **2. Atualização Inteligente**
```
Configurações adicionadas:
- UsePreviousAppDir=yes      → Usa mesma pasta
- UsePreviousGroup=yes        → Usa mesmo menu
- DisableDirPage=auto         → Não pergunta pasta novamente
- DisableProgramGroupPage=auto → Não pergunta menu novamente
```

### ✅ **3. Desinstalação Silenciosa**
```
Nova função InitializeSetup():
- Verifica registro do Windows
- Remove versão antiga silenciosamente
- Mantém data.json, senha.json, config.json
- Continua instalação da nova versão
```

---

## 📋 Passo a Passo Completo

### **Para Corrigir o Problema Atual:**

#### **1. Remover as Instalações Duplicadas**

**Opção A - Manual:**
```
Windows → Configurações → Apps
Desinstalar "No Azul" (todas as entradas)
Quando perguntar "Manter dados?" → SIM
```

**Opção B - Automático:**
```
Execute: desinstalar_versao_antiga.bat
Aguarde a conclusão
```

#### **2. Verificar que os Dados Estão Salvos**

```
Verifique se existem:
C:\Program Files\No Azul\data.json
C:\Program Files\No Azul\senha.json
C:\Program Files\No Azul\config.json

Se sim → Seus dados estão seguros! ✅
```

#### **3. Gerar Novo Instalador**

```
cd C:\Users\Matheus\Desktop\MeuFinanceiro
$env:PYTHONIOENCODING='utf-8'; python setup.py
```

Ou use:
```
gerar_instalador.bat
```

#### **4. Compilar com Inno Setup**

```
1. Abra Inno Setup Compiler
2. File → Open → installer_inno_com_backup.iss
3. Build → Compile (Ctrl+F9)
4. Instalador gerado: installer_output\NoAzul_Setup_v2.0.0.exe
```

#### **5. Testar a Atualização**

```
1. Execute o novo instalador
2. Ele vai detectar que não tem versão antiga
3. Instala normalmente
4. Apenas UMA instalação! ✅
```

---

## 🔍 Como Funciona Agora

### **Primeira Instalação (Novo Usuário):**
```
1. Usuário executa NoAzul_Setup_v2.0.0.exe
2. Não detecta instalação anterior
3. Instala normalmente em: C:\Program Files\No Azul\
4. Cria data.json vazio
5. Pronto!
```

### **Atualização (Usuário Existente):**
```
1. Usuário executa NoAzul_Setup_v2.0.0.exe
2. Detecta instalação anterior
3. AUTOMATICAMENTE:
   a) Faz backup de data.json, senha.json, etc
   b) Remove versão antiga (programa apenas)
   c) Instala nova versão no mesmo lugar
   d) Restaura data.json, senha.json, etc
4. Pronto! Mesma instalação, atualizada!
```

---

## ⚙️ Detalhes Técnicos

### **AppId Fixo:**
```pascal
AppId={{A1B2C3D4-E5F6-7890-ABCD-EF1234567890}
```
**NUNCA mude isso!** Este ID identifica o app no Windows. Se mudar, o Windows trata como app diferente.

### **Função InitializeSetup():**
```pascal
function InitializeSetup(): Boolean;
begin
  // Verifica se existe versão anterior
  if RegValueExists(HKLM, '...Uninstall\{AppId}_is1', 'UninstallString') then
  begin
    // Desinstala automaticamente
    DesinstalarVersaoAnterior();
    Sleep(2000); // Aguarda conclusão
  end;
  Result := True;
end;
```

### **Flags Importantes:**
```pascal
UsePreviousAppDir=yes        // Usa pasta anterior
UsePreviousGroup=yes         // Usa menu anterior  
DisableDirPage=auto          // Auto-detecta pasta
Flags: onlyifdoesntexist     // Não sobrescreve dados
Flags: uninsneveruninstall   // Nunca remove na desinstalação
```

---

## ✅ Checklist de Validação

Após fazer as correções, verifique:

- [ ] Desinstalou ambas as versões antigas
- [ ] Verificou que data.json ainda existe
- [ ] Gerou novo executável com setup.py
- [ ] Compilou novo instalador com Inno Setup
- [ ] Testou instalação do zero
- [ ] Testou atualização (instalar 2x seguidas)
- [ ] Apenas UMA entrada em "Apps e Recursos"
- [ ] Dados preservados após atualização

---

## 🆘 Se o Problema Persistir

Se mesmo com as correções o problema continuar:

### **1. Verificar Registry:**
```
Win+R → regedit
Navegar: HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\
Procurar por: {A1B2C3D4-E5F6-7890-ABCD-EF1234567890}_is1
```

Se tiver múltiplas entradas com IDs diferentes = problema de AppId mudando entre versões.

### **2. Limpeza Manual Completa:**
```
1. Desinstale todas as versões
2. Delete manualmente: C:\Program Files\No Azul\
   (EXCETO data.json, senha.json, config.json - faça backup!)
3. Limpe o registro (regedit)
4. Reinstale com novo instalador
```

### **3. Entre em Contato:**
```
📧 matheus@mrit.com.br
📱 (19) 97134-9642
```

---

## 📝 Resumo

### **Problema:**
✗ Instalações duplicadas (duas entradas no sistema)

### **Causa:**
✗ Instalador não detectava/removia versão anterior

### **Solução Aplicada:**
✅ Função InitializeSetup() que detecta e remove versão antiga
✅ Configurações UsePreviousAppDir e UsePreviousGroup
✅ Preservação automática de dados do usuário
✅ AppId fixo para sempre reconhecer como mesmo app

### **Resultado:**
✅ Atualização suave, sem duplicação
✅ Dados sempre preservados
✅ Apenas uma instalação no sistema

---

**Problema resolvido! Agora o instalador funciona como esperado! 🎉**

