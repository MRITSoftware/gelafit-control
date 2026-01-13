# 🚀 Como Gerar APK no GitHub - Guia Rápido

## ✅ Passo a Passo

### 1. Fazer Push dos Arquivos

Certifique-se de que todos os arquivos estão commitados:

```bash
git add .
git commit -m "Adicionar projeto Android e workflows"
git push origin main
```

### 2. Executar o Workflow

**Opção A - Automático (Recomendado):**
- O workflow executa automaticamente quando você faz push na branch `main`

**Opção B - Manual:**
1. Acesse: https://github.com/MRITSoftware/mritlocal/actions
2. Clique em **"Build APK (Simple - No Release)"**
3. Clique no botão **"Run workflow"** (canto superior direito)
4. Selecione a branch `main`
5. Clique em **"Run workflow"** novamente

### 3. Aguardar o Build

- ⏱️ Tempo estimado: **5-10 minutos**
- Você pode acompanhar o progresso na aba **Actions**

### 4. Baixar o APK

1. Vá para: https://github.com/MRITSoftware/mritlocal/actions
2. Clique no workflow que acabou de executar (deve estar no topo)
3. Role a página até encontrar a seção **"Artifacts"** (lateral direita)
4. Clique em **"tuya-installer-apk"**
5. Baixe o arquivo **"app-debug.apk"**

## 📋 Checklist de Arquivos

Antes de fazer push, verifique se estes arquivos estão no repositório:

- ✅ `tuya_server_enhanced.py` (na raiz)
- ✅ `requirements.txt` (na raiz)
- ✅ `start_server.sh` (na raiz)
- ✅ `stop_server.sh` (na raiz)
- ✅ `android_app/` (pasta completa)
- ✅ `.github/workflows/build-apk-simple.yml`

## 🎯 Workflows Disponíveis

### `build-apk-simple.yml` (Recomendado para começar)
- ✅ Mais simples
- ✅ Gera APK e disponibiliza como artifact
- ✅ Não cria release (mais rápido)

### `build-apk.yml` (Com Release)
- ✅ Cria release automático no GitHub
- ✅ APK disponível na página de Releases
- ⚠️ Pode precisar de permissões adicionais

## 📱 Instalar no Tablet

1. **Baixe o APK** dos Artifacts
2. **Transfira para o tablet** (USB, email, Google Drive, etc)
3. **No tablet:**
   - Configurações → Segurança → Ativar **"Fontes desconhecidas"**
   - Abra o arquivo APK
   - Toque em **"Instalar"**

## 🔍 Verificar se Funcionou

Após o workflow terminar, você deve ver:

```
✅ Build APK with Gradle
✅ Check APK exists
✅ Upload APK as artifact
```

Se algum passo falhar, clique nele para ver os logs de erro.

## 💡 Dicas

- **Primeira vez:** Execute manualmente para testar
- **Depois:** O workflow roda automaticamente em cada push
- **APK disponível por:** 90 dias (depois é removido automaticamente)
- **Múltiplos builds:** Cada execução gera um novo APK

## 🐛 Problemas Comuns

### "APK não aparece nos Artifacts"
- Verifique se o build foi bem-sucedido (todos os passos verdes)
- Veja os logs do passo "Build APK with Gradle"

### "Workflow não executa"
- Verifique se você fez push para a branch `main` ou `master`
- Ou execute manualmente via "Run workflow"

### "Erro ao copiar arquivos"
- Certifique-se de que os arquivos Python estão na **raiz** do repositório
- Não devem estar em subpastas

## 🔗 Links Úteis

- **Seu repositório:** https://github.com/MRITSoftware/mritlocal
- **Actions:** https://github.com/MRITSoftware/mritlocal/actions
- **Releases:** https://github.com/MRITSoftware/mritlocal/releases

---

**Pronto! Agora é só fazer push e aguardar o APK ser gerado! 🎉**

