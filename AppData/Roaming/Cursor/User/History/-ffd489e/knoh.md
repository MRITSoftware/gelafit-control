# 🧪 Como Testar a API

## ✅ **Problema Resolvido!**

O servidor PHP embutido agora está configurado com um router (`router.php`) que:

1. ✅ Processa todas as rotas `/api/*`
2. ✅ Redireciona para o router da API (`api/index.php`)
3. ✅ Serve arquivos estáticos normalmente
4. ✅ Processa rotas limpas (sem .php)

## 🔧 **Servidor Reiniciado**

O servidor foi reiniciado com o router. Agora você pode:

1. **Acessar:** http://localhost:8000/login.php
2. **Tentar fazer login** com um CPF
3. **A API deve funcionar** agora!

## 📝 **Teste Manual da API**

Se quiser testar a API diretamente:

```bash
# Teste de login
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"cpf":"44966991846"}'
```

## 🐛 **Se ainda não funcionar:**

1. Verifique se o servidor está rodando
2. Verifique os logs do servidor
3. Teste acessando: http://localhost:8000/api/auth/login (deve retornar JSON)

---

**Status:** ✅ Router configurado e servidor reiniciado
