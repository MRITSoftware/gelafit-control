# 🚀 Como Fazer Upload para Hostinger

## Opção 1: Via FileZilla/WinSCP (MAIS FÁCIL) ⭐ RECOMENDADO

### 1. Baixar FileZilla
- Download: https://filezilla-project.org/
- Ou WinSCP: https://winscp.net/

### 2. Conectar ao Servidor
Na Hostinger, acesse o painel e encontre as credenciais FTP/SSH:
- **Host:** Seu domínio ou IP do servidor
- **Usuário:** Usuário SSH (geralmente algo como `u123456789` ou similar)
- **Senha:** Senha SSH
- **Porta:** 22 (SSH) ou 21 (FTP)
- **Protocolo:** SFTP (recomendado) ou FTP

### 3. Navegar até a Pasta
- No lado esquerdo: Seus arquivos locais
- No lado direito: Servidor → `public_html` → `esign`

### 4. Fazer Upload
Arraste e solte ou selecione e arraste os seguintes:

**Pastas:**
- `.next/`
- `app/`
- `components/`
- `lib/`
- `utils/`
- `types/` (se existir)

**Arquivos:**
- `package.json`
- `package-lock.json`
- `next.config.js`
- `tsconfig.json`
- `tailwind.config.js`
- `postcss.config.js`
- `middleware.ts`
- `next-env.d.ts`

### 5. Aguardar Upload Concluir
O upload pode demorar alguns minutos dependendo da conexão.

---

## Opção 2: Via SSH/SCP (Linha de Comando)

### Se tiver SSH habilitado e acesso via terminal:

```powershell
# No PowerShell do Windows
# Instalar OpenSSH se não tiver (Windows 10+ geralmente já vem)

# Exemplo de comando:
scp -r .next usuario@seudominio.com.br:public_html/esign/
scp -r app usuario@seudominio.com.br:public_html/esign/
scp -r components usuario@seudominio.com.br:public_html/esign/
scp -r lib usuario@seudominio.com.br:public_html/esign/
scp -r utils usuario@seudominio.com.br:public_html/esign/
scp package.json usuario@seudominio.com.br:public_html/esign/
scp package-lock.json usuario@seudominio.com.br:public_html/esign/
scp next.config.js usuario@seudominio.com.br:public_html/esign/
scp tsconfig.json usuario@seudominio.com.br:public_html/esign/
scp tailwind.config.js usuario@seudominio.com.br:public_html/esign/
scp postcss.config.js usuario@seudominio.com.br:public_html/esign/
scp middleware.ts usuario@seudominio.com.br:public_html/esign/
scp next-env.d.ts usuario@seudominio.com.br:public_html/esign/
```

**OU use o script criado:**

```powershell
.\upload-hostinger.ps1 -Host seudominio.com.br -User usuario -Port 22 -RemotePath "public_html/esign"
```

---

## Opção 3: Via Painel da Hostinger (File Manager)

Alguns planos permitem upload direto pelo painel:

1. Acesse o painel da Hostinger
2. File Manager → `public_html` → `esign`
3. Botão "Upload"
4. Selecione os arquivos/pastas

**Nota:** Pode ser lento para muitos arquivos, mas funciona.

---

## ✅ Checklist Após Upload:

- [ ] Todas as pastas foram enviadas
- [ ] Todos os arquivos foram enviados
- [ ] Estrutura de pastas está correta
- [ ] Pasta `.next/` está presente
- [ ] Arquivos de configuração estão presentes

---

## 🐛 Problemas Comuns:

### Upload muito lento
- Use SFTP ao invés de FTP (mais seguro e rápido)
- Faça upload em partes se necessário
- Verifique sua conexão

### Erro de permissão
- Verifique se a pasta `esign` tem permissão 755
- Verifique se você tem acesso de escrita

### Arquivos não aparecem
- Recarregue a página do File Manager
- Verifique se está na pasta correta (`public_html/esign`)

---

## 📝 Depois do Upload:

Depois de fazer upload, você precisará:

1. **Conectar via SSH** à Hostinger
2. **Navegar até a pasta:**
   ```bash
   cd public_html/esign
   ```

3. **Criar arquivo .env:**
   ```bash
   nano .env
   # Cole o conteúdo do .env.example com suas credenciais
   ```

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

**Recomendação:** Use **FileZilla/WinSCP** - é mais fácil e visual! 🎯

