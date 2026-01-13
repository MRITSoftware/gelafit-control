# 🔄 COMO REVERTER AS MUDANÇAS

Se você quiser **voltar ao estado anterior** (dados mockados), siga estes passos:

---

## 📋 O Que Foi Alterado

Apenas **3 arquivos principais** foram modificados:

1. `lib/auth.ts` - Sistema de autenticação
2. `app/admin/page.tsx` - Painel super admin
3. `app/dashboard/page.tsx` - Dashboard do cliente
4. `app/login/page.tsx` - Correção do redirecionamento

---

## ⏮️ Opção 1: Reverter pelo Git

Se você usa Git:

```bash
# Ver o que foi modificado
git status

# Reverter um arquivo específico
git checkout HEAD -- lib/auth.ts
git checkout HEAD -- app/admin/page.tsx
git checkout HEAD -- app/dashboard/page.tsx
git checkout HEAD -- app/login/page.tsx

# Ou reverter tudo de uma vez
git reset --hard HEAD
```

---

## ⏮️ Opção 2: Reverter Manualmente

### **1. Restaurar `lib/auth.ts`**

O arquivo original usava:
```typescript
import { db } from './database'

// ...

const user = db.getUserByEmail(credentials.email)
```

Basta remover os imports do Supabase e voltar a usar `db.getUserByEmail()`.

### **2. Restaurar `app/admin/page.tsx`**

O arquivo original usava:
```typescript
const allClients = db.getAllClients()
const users = db.getUsersByClient(client.id).length
```

Basta remover as chamadas `supabase.from()` e voltar a usar `db.getAllClients()`.

### **3. Restaurar `app/dashboard/page.tsx`**

O arquivo original usava:
```typescript
const clientData = db.getClientById(parsedUser.clientId)
const clientDocuments = db.getDocumentsByClient(parsedUser.clientId)
```

---

## 🗑️ Arquivos Criados (podem ser deletados)

Estes arquivos foram **criados** e podem ser **deletados** se você quiser:

```
.env.local
lib/supabase.ts
lib/supabase-client.ts
types/database.ts
hooks/useSupabase.ts
app/teste-supabase/page.tsx
app/teste-login/page.tsx

# Documentação
MIGRACAO-SUPABASE-COMPLETA.md
TESTE-AGORA.md
STATUS-MIGRACAO.md
CORRECAO-LOGIN.md
COMO-REVERTER.md
criar-env-local.txt
# ... e outros arquivos de documentação
```

---

## 🎯 Reverter Tudo Rapidamente

Se você quer simplesmente **voltar ao estado anterior**, siga este guia rápido:

### **Passo 1: Parar o servidor**
```bash
# No PowerShell
taskkill /F /IM node.exe
```

### **Passo 2: Reverter arquivos modificados**

**Arquivo: `lib/auth.ts`**
Remova todas as referências ao Supabase e volte a importar:
```typescript
import { db } from './database'
```

E na função `login()`, volte a usar:
```typescript
const user = db.getUserByEmail(credentials.email)
```

**Arquivo: `app/admin/page.tsx`**
Remova os `async` e `await` e volte a usar:
```typescript
const allClients = db.getAllClients()
```

**Arquivo: `app/dashboard/page.tsx`**
Remova os `async` e `await` e volte a usar:
```typescript
const clientData = db.getClientById(clientId)
const clientDocuments = db.getDocumentsByClient(clientId)
```

**Arquivo: `app/login/page.tsx`**
Mantenha como está (a correção do role é necessária de qualquer forma).

### **Passo 3: Deletar arquivos Supabase (opcional)**
```bash
rm .env.local
rm lib/supabase.ts
rm lib/supabase-client.ts
rm types/database.ts
rm hooks/useSupabase.ts
```

### **Passo 4: Reiniciar servidor**
```bash
npm run dev
```

---

## ❓ Por Que Reverter?

Você pode querer reverter se:
- ❌ Não quer usar Supabase
- ❌ Prefere dados mockados para desenvolvimento
- ❌ Quer testar sem banco de dados
- ❌ Está tendo problemas com a migração

---

## ✅ Por Que Manter?

Você deve manter as mudanças se:
- ✅ Quer dados reais persistentes
- ✅ Quer multi-tenant funcionando
- ✅ Quer um sistema em produção
- ✅ Quer usar PostgreSQL

---

## 📝 Backup Rápido

Antes de reverter, faça backup dos arquivos modificados:

```bash
# Criar pasta de backup
mkdir backup-supabase

# Copiar arquivos modificados
cp lib/auth.ts backup-supabase/
cp app/admin/page.tsx backup-supabase/
cp app/dashboard/page.tsx backup-supabase/
```

Assim você pode voltar depois se mudar de ideia!

---

## 💡 Recomendação

**Antes de reverter**, teste a **página de diagnóstico**:

```
http://localhost:3000/teste-login
```

Esta página mostra **exatamente** o que está acontecendo no login e pode ajudar a identificar o problema sem precisar reverter tudo!

