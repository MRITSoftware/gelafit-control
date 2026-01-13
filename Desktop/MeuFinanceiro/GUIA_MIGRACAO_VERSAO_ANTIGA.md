# 🔄 Guia de Migração - Versão Antiga para v2.0

## ❓ Quando Usar Este Guia?

Use este guia SE:
- ✅ Você tinha o **No Azul instalado ANTES** de 18/10/2025
- ✅ Instalou a **nova versão v2.0**
- ✅ Seus **dados antigos não aparecem**
- ✅ Quer **recuperar transações antigas**

**NÃO use se:**
- ❌ É primeira instalação
- ❌ Instalou v2.0 e seus dados já estão lá
- ❌ Nunca usou o No Azul antes

---

## 🔍 Como Saber se Preciso Migrar?

### **Teste Rápido:**

1. Abra o No Azul v2.0
2. Vá em **Renda** ou **Despesas**
3. Suas transações antigas aparecem?
   - ✅ **SIM** → Não precisa migrar! Tudo OK!
   - ❌ **NÃO** → Continue lendo

---

## 🚀 Migração Automática (RECOMENDADO)

### **Passo 1: Executar Script**

```
1. Feche o No Azul (se estiver aberto)
2. Execute: migrar_dados_versao_antiga.bat
3. Siga as instruções na tela
4. Aguarde conclusão
```

### **Passo 2: Verificar**

```
1. Abra o No Azul v2.0
2. Verifique se seus dados aparecem
3. ✅ Pronto!
```

---

## 🛠️ Migração Manual (Avançado)

Se preferir fazer manualmente:

### **1. Localizar Dados Antigos:**

Seus dados podem estar em:
```
C:\Program Files\No Azul\
OU
C:\Program Files (x86)\No Azul\
```

Procure por:
- `data.json` (suas transações)
- `senha.json` (sua senha)
- `config.json` (configurações)
- `backups\` (backups automáticos)

### **2. Copiar para Nova Localização:**

```
Destino: C:\Users\[SEU_NOME]\AppData\Local\No Azul\

Copie:
- data.json → para → C:\Users\[SEU_NOME]\AppData\Local\No Azul\data.json
- senha.json → para → C:\Users\[SEU_NOME]\AppData\Local\No Azul\senha.json
- config.json → para → C:\Users\[SEU_NOME]\AppData\Local\No Azul\config.json
- backups\ → para → C:\Users\[SEU_NOME]\AppData\Local\No Azul\backups\
```

**Atalho para AppData:**
```
1. Win + R
2. Digite: %LOCALAPPDATA%
3. Enter
4. Procure a pasta "No Azul"
```

### **3. Reiniciar o App:**

```
1. Feche o No Azul completamente
2. Abra novamente
3. Dados devem aparecer
```

---

## 📋 Checklist de Migração

- [ ] Fechou o No Azul v2.0
- [ ] Localizou pasta antiga (Program Files)
- [ ] Encontrou data.json antigo
- [ ] Executou migrar_dados_versao_antiga.bat (OU copiou manualmente)
- [ ] Verificou que arquivos estão em `%LOCALAPPDATA%\No Azul\`
- [ ] Abriu No Azul v2.0
- [ ] Dados aparecem corretamente
- [ ] Desinstalou versão antiga (opcional)

---

## ⚠️ Problemas Comuns

### **"Não encontro os dados antigos"**

```
Tente procurar em:
- C:\Program Files\No Azul\
- C:\Program Files (x86)\No Azul\
- %LOCALAPPDATA%\Programs\No Azul\
- Desktop (se executou sem instalar)

Use a pesquisa do Windows:
1. Win + S
2. Digite: data.json
3. Procure nos resultados
```

### **"Migrei mas dados não aparecem"**

```
Verifique:
1. Arquivos estão em: %LOCALAPPDATA%\No Azul\ ?
2. Os arquivos têm conteúdo? (não estão vazios)
3. O No Azul foi completamente fechado e reaberto?
4. Você está logado com a mesma conta do Windows?
```

### **"Tenho duas instalações do No Azul"**

```
Isso é normal se tinha versão antiga:
1. Desinstale a versão antiga:
   Windows → Configurações → Apps → No Azul (antiga) → Desinstalar
   
2. Quando perguntar "Manter dados?" → escolha NÃO
   (pois já migrou para nova localização)
   
3. Mantenha apenas a v2.0
```

---

## 🎯 Diferenças Entre Versões

### **Versão Antiga (antes de 18/10/2025):**
```
📂 Instalação: C:\Program Files\No Azul\
📂 Dados: C:\Program Files\No Azul\
⚠️  Requer: Admin para escrever dados
⚠️  Tutorial: Repetia sempre
⚠️  Atualizações: Duplicavam instalação
```

### **Versão Nova (v2.0 - 18/10/2025):**
```
📂 Instalação: C:\Users\...\AppData\Local\Programs\No Azul\
📂 Dados: C:\Users\...\AppData\Local\No Azul\
✅ Requer: Sem necessidade de admin
✅ Tutorial: Para após concluir
✅ Atualizações: Substituem versão antiga automaticamente
✅ Responsividade: Funciona em notebooks
✅ Cards: Maiores e com destaque
```

---

## 🆘 Precisa de Ajuda?

Se os métodos acima não funcionaram:

**Contato:**
- 📱 **WhatsApp:** (19) 97134-9642
- 📧 **Email:** matheus@mrit.com.br
- 🌐 **Site:** www.mrit.com.br

**Envie:**
1. Print da pasta antiga (se encontrou)
2. Print da pasta nova
3. Descrição do problema

Vou te ajudar pessoalmente! 😊

---

## 📝 Resumo Rápido

```
1. Execute: migrar_dados_versao_antiga.bat
2. Siga instruções
3. Abra No Azul v2.0
4. Dados migrados! ✅
```

---

**Desenvolvido com ❤️ por MRIT Software © 2025**

