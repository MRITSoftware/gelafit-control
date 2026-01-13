# 🔐 Guia Completo - Assinatura Digital de Executáveis

## 📋 Índice

1. [O que é Assinatura Digital?](#o-que-é-assinatura-digital)
2. [Por que é Importante?](#por-que-é-importante)
3. [Opções Disponíveis](#opções-disponíveis)
4. [Como Comprar um Certificado](#como-comprar-um-certificado)
5. [Como Assinar o Executável](#como-assinar-o-executável)
6. [Alternativas Sem Certificado](#alternativas-sem-certificado)

---

## 🔒 O que é Assinatura Digital?

Assinatura digital é como um "selo de autenticidade" que:
- ✅ Prova que o arquivo veio de você
- ✅ Garante que o arquivo não foi modificado
- ✅ É reconhecida pelo Windows como confiável

---

## 💡 Por que é Importante?

### **Com Assinatura Digital:**
```
✅ Instalação sem alertas
✅ Usuários confiam mais
✅ Windows SmartScreen não bloqueia
✅ Antivírus menos rigorosos
✅ Aparência profissional
```

### **Sem Assinatura Digital:**
```
⚠️ "Windows protegeu seu computador"
⚠️ Usuários podem ter medo
⚠️ Mais cliques para instalar
⚠️ Pode ser bloqueado por alguns antivírus
```

---

## 💰 Opções Disponíveis

### **1. Certificado Individual/Empresa (RECOMENDADO)**

#### **SSL.com** (Mais Barato)
- 💰 **Preço:** ~US$ 200/ano (~R$ 1.000/ano)
- 🌐 **Site:** https://www.ssl.com/code-signing/
- ⏱️ **Validação:** 1-3 dias úteis
- ✅ **Reconhecido:** Windows, todos os navegadores
- 📋 **Requer:** CPF/CNPJ, documentos de identidade

#### **Sectigo (Comodo)**
- 💰 **Preço:** ~US$ 300/ano (~R$ 1.500/ano)
- 🌐 **Site:** https://sectigo.com/ssl-certificates-tls/code-signing
- ⏱️ **Validação:** 2-5 dias úteis
- ✅ **Reconhecido:** Windows, todos os navegadores
- 📋 **Requer:** CPF/CNPJ, documentos de identidade

#### **DigiCert** (Mais Caro, Mais Confiável)
- 💰 **Preço:** ~US$ 400/ano (~R$ 2.000/ano)
- 🌐 **Site:** https://www.digicert.com/signing/code-signing-certificates
- ⏱️ **Validação:** 1-3 dias úteis
- ✅ **Reconhecido:** Windows, todos os navegadores
- 📋 **Requer:** CPF/CNPJ, documentos de identidade

---

### **2. Certificado EV (Extended Validation) - Token USB**

#### **O Melhor, Mas Mais Caro:**
- 💰 **Preço:** ~US$ 500-600/ano (~R$ 2.500-3.000/ano)
- 🔑 **Token USB:** Vem com token físico (mais seguro)
- ⏱️ **Validação:** 5-10 dias úteis
- ✅ **Vantagem:** SmartScreen desbloqueia IMEDIATAMENTE
- ✅ **Reconhecimento:** Instantâneo pelo Windows

**Certificados normais levam semanas/meses para o SmartScreen "confiar"**
**Certificados EV são confiados IMEDIATAMENTE!**

---

### **3. Opções Gratuitas (Projetos Open Source)**

#### **SignPath Foundation**
- 💰 **Preço:** 🆓 GRATUITO
- 🌐 **Site:** https://signpath.org
- 📋 **Requisitos:**
  - Projeto open source no GitHub público
  - Sem fins lucrativos
  - Aprovação da equipe SignPath
- ⏱️ **Processo:** 1-2 semanas para aprovação

#### **Certum Open Source**
- 💰 **Preço:** 🆓 GRATUITO
- 🌐 **Site:** https://www.certum.eu/certum/cert,offer_en_open_source_cs.xml
- 📋 **Requisitos:**
  - Projeto open source
  - Licença OSI aprovada (MIT, GPL, etc)
- ⏱️ **Processo:** 2-4 semanas para aprovação

---

## 🛒 Como Comprar um Certificado

### **Passo a Passo (Exemplo: SSL.com)**

#### **1. Escolher o Tipo:**
```
Acesse: https://www.ssl.com/code-signing/
Escolha: "Individual Code Signing Certificate"
```

#### **2. Preencher Dados:**
```
- Nome completo
- Email
- Telefone
- CPF (ou CNPJ se for empresa)
- Endereço completo
```

#### **3. Validação de Identidade:**
```
Enviar documentos:
- RG ou CNH (frente e verso)
- Comprovante de endereço
- Selfie segurando o documento
```

#### **4. Pagamento:**
```
Formas aceitas:
- Cartão de crédito internacional
- PayPal
- Transferência bancária
```

#### **5. Receber o Certificado:**
```
Após aprovação (1-3 dias):
- Você recebe um arquivo .pfx
- Senha para instalar
- Instruções de instalação
```

---

## 🔧 Como Assinar o Executável

### **1. Instalar o Windows SDK**

```
Download: https://developer.microsoft.com/windows/downloads/windows-sdk/

Durante instalação, selecione:
✅ Windows SDK Signing Tools for Desktop Apps
```

### **2. Instalar o Certificado**

```
1. Clique duas vezes no arquivo .pfx
2. Escolha "Máquina Local"
3. Digite a senha fornecida
4. Confirme a instalação
```

### **3. Encontrar o SignTool**

```
Localização padrão:
C:\Program Files (x86)\Windows Kits\10\bin\10.0.xxxxx.0\x64\signtool.exe
```

### **4. Assinar o Executável**

```batch
REM Comando básico:
signtool sign /a /t http://timestamp.digicert.com /fd SHA256 "dist\NoAzul.exe"

REM Comando com descrição:
signtool sign /a /n "MRIT Software" /d "No Azul - Controle Financeiro" ^
    /du "https://www.mrit.com.br" ^
    /t http://timestamp.digicert.com ^
    /fd SHA256 "dist\NoAzul.exe"
```

### **5. Verificar Assinatura**

```batch
REM Ver detalhes da assinatura:
signtool verify /pa /v "dist\NoAzul.exe"

REM Se estiver OK, vai mostrar:
"Successfully verified: dist\NoAzul.exe"
```

---

## 🆓 Alternativas Sem Certificado

### **Enquanto não tiver certificado:**

#### **1. Documentação Clara**
✅ Criamos: `COMO_INSTALAR_SEM_ALERTA.md`
- Explica por que o alerta aparece
- Passo a passo para instalar
- Garante que é seguro

#### **2. Hash de Verificação**
✅ Fornecemos: `NoAzul.sha256.txt`
- Usuários podem verificar autenticidade
- Prova que o arquivo não foi alterado

#### **3. Reputação com Microsoft**
✅ Quanto mais pessoas instalarem:
- Windows "aprende" que o app é seguro
- Alertas diminuem gradualmente
- Leva ~3-6 meses com muitos downloads

#### **4. Distribuição Oficial**
✅ Use canais oficiais:
- Site próprio: www.mrit.com.br
- GitHub Releases
- Link direto (não terceiros)

#### **5. Suporte Ativo**
✅ Ofereça suporte:
- WhatsApp: (19) 97134-9642
- Email: matheus@mrit.com.br
- Responda dúvidas rapidamente

---

## 📊 Comparação Resumida

| Opção | Custo | Tempo | Eficácia | Recomendado |
|-------|-------|-------|----------|-------------|
| **Certificado Pago** | R$ 1.000-2.000/ano | 1-3 dias | ⭐⭐⭐⭐⭐ | ✅ Sim |
| **Certificado EV** | R$ 2.500-3.000/ano | 5-10 dias | ⭐⭐⭐⭐⭐ | ✅ Melhor |
| **SignPath (OSS)** | Grátis | 1-2 semanas | ⭐⭐⭐⭐ | ✅ Se OSS |
| **Auto-assinado** | Grátis | Instantâneo | ⭐ | ❌ Não funciona |
| **Sem certificado** | Grátis | N/A | ⭐⭐ | ⚠️ Temporário |

---

## 🎯 Recomendação Final

### **Para Distribuição Gratuita:**

**Opção 1 (IDEAL):**
```
1. Compre certificado da SSL.com (~R$ 1.000/ano)
2. Assine o executável
3. Zero alertas!
```

**Opção 2 (ALTERNATIVA):**
```
1. Use sem certificado inicialmente
2. Forneça COMO_INSTALAR_SEM_ALERTA.md
3. Construa reputação gradualmente
4. Compre certificado quando tiver receita
```

**Opção 3 (SE FOR OPEN SOURCE):**
```
1. Coloque no GitHub público
2. Solicite SignPath Foundation
3. Assinatura gratuita!
```

---

## 📞 Precisa de Ajuda?

Se tiver dúvidas sobre:
- Qual certificado comprar
- Como configurar
- Problemas na assinatura

**Entre em contato:**
- 📧 matheus@mrit.com.br
- 📱 (19) 97134-9642

---

**Desenvolvido com ❤️ por MRIT Software © 2025**

