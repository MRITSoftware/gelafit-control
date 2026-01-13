# 📦 Guia Completo - Instalador com Inno Setup

## 🎯 O Que Foi Criado

Um **instalador profissional** que:
- ✅ **Detecta** instalação anterior automaticamente
- ✅ **Faz backup** dos dados antes de atualizar
- ✅ **Restaura** dados após instalação
- ✅ **Preserva** tudo automaticamente
- ✅ **Interface** amigável em português

**Usuário não precisa fazer NADA manualmente!** 🎉

---

## 🚀 Como Gerar o Instalador

### Método Automático (RECOMENDADO):

```batch
# Execute com duplo clique:
gerar_instalador.bat
```

**O script fará TUDO:**
1. ✅ Verifica Python e PyInstaller
2. ✅ Gera executável (dist/NoAzul.exe)
3. ✅ Compila instalador com Inno Setup
4. ✅ Calcula hash SHA256
5. ✅ Oferece testar automaticamente

**Tempo: 2-3 minutos** ⏱️

---

### Método Manual:

#### 1. Instalar Inno Setup

```
📥 Baixe: https://jrsoftware.org/isdl.php
📦 Instale: Inno Setup 6 (versão mais recente)
📂 Padrão: C:\Program Files (x86)\Inno Setup 6\
```

#### 2. Gerar Executável

```bash
python setup.py
```

Resultado: `dist/NoAzul.exe`

#### 3. Compilar Instalador

```
1. Abra: installer_inno_com_backup.iss
2. Clique: Build → Compile
3. Aguarde compilação
```

Resultado: `installer_output/NoAzul_Setup_v2.0.0.exe`

---

## 📋 Arquivos Necessários

### Para Compilar:

```
Projeto/
├── dist/
│   └── NoAzul.exe                    ← Gerado pelo PyInstaller
├── noazul_logo.ico                   ← Ícone do app
├── noazul_logo.png                   ← Logo PNG
├── inicio_noazul.gif                 ← GIF de loading
├── config.json                       ← Config padrão
├── data.json                         ← Dados padrão (vazio)
├── README.md                         ← Documentação
├── GUIA_ATUALIZACAO.md              ← Guia de atualização
├── ATUALIZAR_RAPIDO.txt             ← Guia rápido
├── LICENSE.txt                       ← Licença (criar se não tiver)
└── installer_inno_com_backup.iss    ← Script Inno Setup
```

---

## 🎨 Como Funciona o Instalador

### Fluxo Completo:

#### 1️⃣ **Início da Instalação**

```
Usuário executa: NoAzul_Setup_v2.0.0.exe

↓

Instalador detecta instalação anterior?
```

#### 2️⃣ **Se NÃO tem instalação anterior:**

```
✅ Instalação limpa normal
✅ Cria estrutura de pastas
✅ Instala executável
✅ Cria ícones
✅ Pronto!
```

#### 3️⃣ **Se TEM instalação anterior:**

```
📊 Mostra tela informando:
   "Atualização Detectada"
   "Seus dados serão preservados automaticamente"

↓

💾 BACKUP AUTOMÁTICO:
   • Cria: backup_atualizacao_YYYYMMDD_HHMMSS/
   • Copia: data.json
   • Copia: senha.json
   • Copia: config.json
   • Copia: backups/ (pasta inteira)
   • Copia: logs/ (pasta inteira)

↓

📦 INSTALAÇÃO:
   • Substitui NoAzul.exe
   • Atualiza arquivos do sistema
   • Mantém dados antigos

↓

♻️ RESTAURAÇÃO AUTOMÁTICA:
   • Restaura data.json
   • Restaura senha.json
   • Restaura config.json
   • Restaura backups/
   • Restaura logs/

↓

✅ CONCLUÍDO:
   "Seus dados foram preservados!"
   "Backup em: [caminho]"
```

---

## 🔧 Personalizar o Instalador

### Alterar Informações Básicas:

```pascal
// Em: installer_inno_com_backup.iss

#define MyAppName "No Azul"
#define MyAppVersion "2.0.0"           ← Mudar aqui!
#define MyAppPublisher "MRIT Software"  ← Seu nome
#define MyAppURL "https://www.mrit.com.br"  ← Seu site
```

### Alterar Ícone:

```pascal
SetupIconFile=noazul_logo.ico  ← Seu ícone aqui
```

### Adicionar Arquivos:

```pascal
[Files]
Source: "seu_arquivo.ext"; DestDir: "{app}"; Flags: ignoreversion
```

### Mudar Pasta de Instalação:

```pascal
DefaultDirName={autopf}\{#MyAppName}  ← C:\Program Files\
DefaultDirName={localappdata}\{#MyAppName}  ← AppData\Local\
```

---

## 📊 Funcionalidades Implementadas

### 1. ✅ Detecção Automática

```pascal
function TemInstalacaoAnterior(): Boolean;
```

- Verifica registro do Windows
- Procura por `data.json` na instalação
- Define se é atualização ou instalação limpa

### 2. ✅ Backup Automático

```pascal
function CriarBackupDados(): Boolean;
```

- Cria pasta com timestamp
- Copia todos arquivos importantes
- Loga todas as operações
- Trata erros graciosamente

### 3. ✅ Restauração Automática

```pascal
function RestaurarDadosBackup(): Boolean;
```

- Restaura arquivos do backup
- Sobrescreve arquivos novos
- Mantém dados do usuário
- Informa localização do backup

### 4. ✅ Desinstalação Inteligente

```pascal
procedure CurUninstallStepChanged();
```

- Pergunta se quer manter dados
- Permite reinstalação sem perda
- Ou remove tudo se usuário escolher

### 5. ✅ Interface em Português

```pascal
[Languages]
Name: "brazilianportuguese";
```

- Todas mensagens em PT-BR
- Mensagens personalizadas
- Informações claras

---

## 🧪 Testar o Instalador

### Teste 1: Instalação Limpa

```
1. Máquina virtual ou PC sem No Azul
2. Execute: NoAzul_Setup_v2.0.0.exe
3. Siga instalação normalmente
4. Verifique se app abre
5. Crie alguns dados de teste
```

### Teste 2: Atualização

```
1. Com instalação existente (Teste 1)
2. Execute: NoAzul_Setup_v2.0.0.exe (novo)
3. Verifique mensagem "Atualização Detectada"
4. Complete instalação
5. Abra app e verifique dados preservados
6. Confirme pasta de backup criada
```

### Teste 3: Desinstalação

```
1. Vá em: Adicionar ou Remover Programas
2. Selecione: No Azul
3. Clique: Desinstalar
4. Escolha: "Sim" para manter dados
5. Reinstale e verifique dados ainda lá
```

---

## 📦 Distribuir o Instalador

### Arquivos para Enviar:

```
NoAzul_Instalador_v2.0.zip
│
├── NoAzul_Setup_v2.0.0.exe    ← Instalador
├── SHA256.txt                  ← Hash de verificação
├── LEIA-ME.txt                 ← Instruções
└── NOVIDADES.txt               ← Lista de novidades
```

### Criar LEIA-ME.txt:

```txt
═══════════════════════════════════════════════════════════
 📦 NO AZUL - INSTALADOR v2.0.0
═══════════════════════════════════════════════════════════

COMO INSTALAR:

1️⃣  Execute: NoAzul_Setup_v2.0.0.exe
2️⃣  Siga o assistente de instalação
3️⃣  Pronto!

⚠️  SE VOCÊ JÁ TEM O NO AZUL INSTALADO:

✅ Seus dados serão preservados AUTOMATICAMENTE!
✅ O instalador faz backup antes de atualizar
✅ Depois restaura tudo sozinho
✅ Você não precisa fazer nada!

REQUISITOS:

• Windows 10 ou 11 (64-bit)
• 100 MB de espaço livre
• Permissões de administrador

SUPORTE:

📧 matheus@mrit.com.br
📱 (19) 97134-9642
🌐 www.mrit.com.br

═══════════════════════════════════════════════════════════
```

---

## 🔐 Segurança

### Hash SHA256:

Sempre inclua o hash do instalador:

```batch
# Gerado automaticamente por gerar_instalador.bat
# Arquivo: installer_output/SHA256.txt
```

Usuários podem verificar:

```powershell
Get-FileHash NoAzul_Setup_v2.0.0.exe -Algorithm SHA256
```

---

## ⚠️ Problemas Comuns

### "Inno Setup não encontrado"

**Solução:**
```
1. Baixe: https://jrsoftware.org/isdl.php
2. Instale em: C:\Program Files (x86)\Inno Setup 6\
3. Ou ajuste caminho em gerar_instalador.bat
```

### "Executável não encontrado"

**Solução:**
```
1. Execute: python setup.py
2. Verifique: dist/NoAzul.exe existe
3. Execute novamente gerar_instalador.bat
```

### "Erro ao compilar"

**Solução:**
```
1. Verifique todos arquivos necessários estão presentes
2. Abra .iss no Inno Setup Compiler
3. Veja erros detalhados
4. Corrija caminhos se necessário
```

### "Instalador não preserva dados"

**Solução:**
```
1. Verifique logs: C:\Users\[Nome]\AppData\Local\Temp\
2. Procure por: Setup Log YYYY-MM-DD #XXX.txt
3. Veja erros de backup/restore
4. Ajuste script se necessário
```

---

## 📈 Versionamento

### Atualizar Versão:

```pascal
// Em: installer_inno_com_backup.iss
#define MyAppVersion "2.0.0"  ← Mude aqui

// Também mude em:
// - setup.py (se tiver __version__)
// - atualizacao.py (VERSAO_ATUAL)
// - README.md
```

### Formato: MAJOR.MINOR.PATCH

```
2.0.0 → 2.0.1  (Correção de bug)
2.0.1 → 2.1.0  (Nova funcionalidade)
2.1.0 → 3.0.0  (Mudança grande)
```

---

## 🎉 Resultado Final

### O Que o Usuário Vê:

```
1. Baixa: NoAzul_Setup_v2.0.0.exe
2. Executa (duplo clique)
3. Vê: "Atualização detectada, dados serão preservados"
4. Clica: "Avançar" algumas vezes
5. Pronto! App atualizado, dados intactos!
```

**Experiência: ⭐⭐⭐⭐⭐**

---

## 📞 Suporte

Dúvidas sobre o instalador?

- 📧 **Email:** matheus@mrit.com.br
- 📱 **WhatsApp:** (19) 97134-9642
- 📖 **Docs Inno:** https://jrsoftware.org/ishelp/

---

## ✅ Checklist Final

Antes de distribuir:

- [ ] Executável gerado e testado
- [ ] Instalador compilado
- [ ] Teste em instalação limpa
- [ ] Teste em atualização
- [ ] Hash SHA256 calculado
- [ ] LEIA-ME.txt criado
- [ ] NOVIDADES.txt criado
- [ ] Tudo empacotado em ZIP
- [ ] Testado por outra pessoa

---

**🎉 Seu instalador profissional está pronto! 🎉**

---

**Desenvolvido com ❤️ por MRIT Software © 2025**

