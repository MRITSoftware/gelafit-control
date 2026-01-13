# 📥 Como Usar FileZilla para Fazer Upload

## ✅ Download Correto:

**FileZilla CLIENT** ← Este é o que você precisa!

- Site oficial: https://filezilla-project.org/
- Download: https://filezilla-project.org/download.php?type=client
- Escolha a versão para Windows (geralmente "Download FileZilla Client")

**NÃO baixe:**
- ❌ FileZilla Server (isso é para criar seu próprio servidor FTP)

---

## 🚀 Como Conectar:

### 1. Abrir FileZilla Client

### 2. Preencher Credenciais SSH/SFTP:

No topo do FileZilla, preencha os campos:

- **Host (Servidor):** Seu domínio ou IP da Hostinger
  - Exemplo: `esign.mrit.com.br`
  - Ou: IP fornecido pela Hostinger
  
- **Usuário:** Seu usuário SSH/SFTP da Hostinger
  - Geralmente algo como: `u123456789` ou o nome do usuário
  
- **Senha:** Sua senha SSH/SFTP
  
- **Porta:** 
  - **22** para SFTP (recomendado)
  - **21** para FTP (menos seguro)
  
- **Protocolo:** Selecione **SFTP** (mais seguro)

### 3. Clicar em "Conectar" (ou "Quickconnect")

### 4. Aceitar Certificado (na primeira vez)
- Pode aparecer um aviso sobre certificado
- Clique em "OK" ou "Confiar sempre"

---

## 📁 Navegar até a Pasta:

**Lado Esquerdo (Local):**
- Seus arquivos do computador
- Navegue até: `D:\ass digital`

**Lado Direito (Servidor):**
- Arquivos no servidor Hostinger
- Navegue até: `public_html` → `esign`

---

## 📤 Fazer Upload dos Arquivos:

### Método 1: Arrastar e Soltar
1. Selecione os arquivos/pastas no lado esquerdo
2. Arraste para o lado direito (pasta `esign`)
3. Aguarde o upload concluir

### Método 2: Clique Direito
1. Selecione arquivos/pastas no lado esquerdo
2. Clique direito → "Upload"
3. Aguarde o upload concluir

---

## ✅ Arquivos para Upload:

**Pastas:**
- `.next/`
- `app/`
- `components/`
- `lib/`
- `utils/`
- `types/`

**Arquivos:**
- `package.json`
- `package-lock.json`
- `next.config.js`
- `tsconfig.json`
- `tailwind.config.js`
- `postcss.config.js`
- `middleware.ts`
- `next-env.d.ts`

---

## 🐛 Problemas Comuns:

### Não consegue conectar
- Verifique credenciais na Hostinger
- Tente porta 22 (SFTP) ao invés de 21 (FTP)
- Verifique se SSH está habilitado no painel da Hostinger

### Upload muito lento
- Use SFTP ao invés de FTP
- Verifique sua conexão de internet
- Faça upload em partes se necessário

### Erro de permissão
- Verifique se a pasta `esign` tem permissão 755
- Contate suporte da Hostinger se necessário

---

## 📝 Depois do Upload:

Após fazer upload, você precisará:

1. **Conectar via SSH** à Hostinger (terminal)
2. **Navegar até a pasta:**
   ```bash
   cd public_html/esign
   ```

3. **Criar arquivo .env:**
   ```bash
   nano .env
   ```
   (Cole o conteúdo do .env.example com suas credenciais)

4. **Instalar dependências:**
   ```bash
   npm install --production
   ```

5. **Iniciar com PM2:**
   ```bash
   pm2 start npm --name "esign" -- run production
   pm2 save
   ```

---

**Resumo:** Baixe o **FileZilla Client** e use para fazer upload! 🚀

