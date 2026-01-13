# 📁 Como Ver Arquivos Ocultos no FileZilla

## ✅ Método 1: Forçar Mostrar Arquivos Ocultos

1. No FileZilla, vá em **Servidor** → **Forçar exibição de arquivos ocultos**
2. OU: Menu **Servidor** → **Force show hidden files**
3. Isso fará aparecer arquivos/pastas com `.` no início

---

## ✅ Método 2: Configurações do FileZilla

1. Menu **Editar** → **Configurações** (ou **Edit** → **Settings**)
2. Vá em **Transferências** ou **Transfers**
3. Procure por **"Mostrar arquivos ocultos"** ou **"Show hidden files"**
4. Marque a opção
5. Clique em **OK**
6. Recarregue a lista de arquivos (F5 ou botão de atualizar)

---

## ✅ Método 3: Verificar via SSH/Terminal

Se o FileZilla não mostrar, verifique via terminal:

1. Acesse cPanel: https://br838.hostgator.com.br:2083/
2. Procure **"Terminal"** ou **"SSH Access"**
3. Execute:

```bash
cd public_html/esign
ls -la
```

Isso mostrará TODOS os arquivos, incluindo `.env` e `.next`

---

## ⚠️ IMPORTANTE:

- **`.env`** DEVE ter o ponto (é assim que funciona)
- **`.next`** DEVE ter o ponto (pasta de build do Next.js)
- Mesmo que não apareçam no FileZilla, eles estão lá se você criou

---

## 🔍 Verificar se Existem:

Se você criou o `.env`, mesmo que não apareça, ele existe. Verifique pelo terminal SSH.

**Importante:** Os arquivos ocultos (com `.`) são essenciais e devem ser mantidos com o ponto!

