# 🔍 Como Ver o Erro Real do Build

## No GitHub Actions

1. **Acesse:** https://github.com/MRITSoftware/mritlocal/actions

2. **Clique no workflow que falhou** (o mais recente)

3. **Procure pelo step "Show build error (CRITICAL)"** - ele mostra o erro real

4. **Ou procure pelo step "Build APK with Gradle"** - role até o final para ver o erro

5. **Copie e cole aqui** o que aparecer, especialmente:
   - Seção "What went wrong"
   - Qualquer linha com "ERROR" ou "FAILED"
   - Mensagens sobre recursos faltando

## O que procurar

- `What went wrong:` - Erro principal
- `AAPT` - Problemas com recursos Android
- `resource` ou `missing` - Recursos faltando
- `AndroidManifest` - Problemas no manifest
- `Execution failed` - Tarefa que falhou

## Exemplo do que preciso ver

```
What went wrong:
Execution failed for task ':app:processDebugResources'.
> A failure occurred while executing com.android.build.gradle.internal.res.ResourceCompilerRunnable
```

Ou qualquer mensagem de erro que apareça!

---

**Cole aqui o erro completo que você viu!** 🔴

