# ⚡ CONFIGURAÇÃO IMEDIATA - SUAS CREDENCIAIS

## 📝 Passo 1: Criar arquivo `.env.local`

Crie um arquivo chamado `.env.local` na **raiz do projeto** com este conteúdo:

```env
# ============================================================================
# CONFIGURAÇÃO DO SUPABASE
# ============================================================================

# URL do projeto Supabase
NEXT_PUBLIC_SUPABASE_URL=https://base3.muraltv.com.br

# Chave pública (anon key)
NEXT_PUBLIC_SUPABASE_ANON_KEY=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJyb2xlIjoiYW5vbiIsImlzcyI6InN1cGFiYXNlMiIsImlhdCI6MTc0OTc4NjAwMCwiZXhwIjoxOTE3NTUyNDAwfQ.MYmpgQo5ODwqR4Ihv8Fbwn4t2Ev7LR3fud7GpWWrXbU

# Chave de serviço (adicione quando tiver)
SUPABASE_SERVICE_ROLE_KEY=sua-service-role-key-aqui

# JWT Secret
JWT_SECRET=mrit-documento-sistema-jwt-secret-2024

# URL da aplicação
NEXT_PUBLIC_APP_URL=http://localhost:3000

# Ambiente
NODE_ENV=development
```

## 🚀 Passo 2: Testar a Conexão

O servidor já está rodando! Agora:

1. **Abra o navegador**
2. **Acesse:** http://localhost:3000/teste-supabase
3. **Veja o resultado** da conexão

## ✅ Passo 3: O Que Esperar

### Se deu certo ✅
Você verá:
- ✅ "Conectado com sucesso!"
- Lista de empresas do banco
- Botão verde "Ir para Dashboard"

### Se deu erro ❌
Verifique:
1. O arquivo `.env.local` foi criado na **raiz** (não em subpasta)
2. Não tem espaços antes/depois do `=`
3. As credenciais foram copiadas corretamente
4. Reinicie o servidor: `Ctrl+C` e depois `npm run dev`

## 📊 Passo 4: Executar o Schema SQL

1. Acesse: https://base3.muraltv.com.br/project/_/sql/new
2. Cole o conteúdo do arquivo `supabase-schema.sql`
3. Clique em **Run**
4. Aguarde a execução (pode levar 10-20 segundos)
5. Se der erro, execute linha por linha

## 🎯 Passo 5: Dados de Exemplo (Opcional)

Para ter dados de teste:
1. Nova query no Supabase
2. Cole o conteúdo de `supabase-seed.sql`
3. **Run**
4. Pronto! Você terá:
   - 1 empresa exemplo
   - 3 usuários (senha: 123456)
   - Pastas e tags de exemplo

## 🔐 Usuários de Teste

Após executar o seed, você terá:

```
✅ Super Admin
Email: admin@sistema.com
Senha: 123456

✅ Admin do Cliente
Email: admin@empresaexemplo.com
Senha: 123456

✅ Colaborador
Email: colaborador@empresaexemplo.com
Senha: 123456
```

## 💻 Passo 6: Usar no Código

Agora você pode usar em qualquer componente:

```typescript
import { useDocumentos } from '@/hooks/useSupabase'

const { documentos, carregando } = useDocumentos(empresaId)
```

Ou diretamente:

```typescript
import { buscarDocumentos } from '@/lib/supabase-client'

const { documentos } = await buscarDocumentos(empresaId)
```

## 📚 Documentação

- `COMECE-POR-AQUI.md` - Guia completo
- `RESUMO-INTEGRACAO.md` - Resumo rápido
- `EXEMPLOS-INTEGRACAO.md` - Código pronto

## 🆘 Problemas?

### Erro: "Cannot find module '@/lib/supabase'"
**Solução:** Reinicie o servidor

### Erro: "Variáveis de ambiente não configuradas"
**Solução:** Verifique se `.env.local` está na raiz

### Erro: "relation 'empresas' does not exist"
**Solução:** Execute o `supabase-schema.sql` no Supabase

### Erro: "permission denied"
**Solução:** Verifique as políticas RLS no schema

## ✅ Checklist

- [ ] Criar `.env.local` na raiz
- [ ] Abrir http://localhost:3000/teste-supabase
- [ ] Ver "Conectado com sucesso!" ✅
- [ ] Executar `supabase-schema.sql` no Supabase
- [ ] (Opcional) Executar `supabase-seed.sql`
- [ ] Começar a usar! 🚀

---

## 🎉 Pronto!

Após seguir esses passos, seu sistema estará 100% funcionando com Supabase!

**Próximo:** Comece a migrar seus componentes para usar as funções do Supabase.

