# 🚀 Gerar APK no GitHub Actions

## Como Funciona

O GitHub Actions vai compilar o APK automaticamente quando você fizer push no repositório!

## 📋 Passos

### 1. Fazer Push dos Arquivos

Certifique-se de que todos os arquivos estão no repositório:

```bash
git add .
git commit -m "Adicionar projeto Android"
git push origin main
```

### 2. Executar o Workflow

**Opção A - Automático:**
- O workflow executa automaticamente quando você faz push na branch `main` ou `master`

**Opção B - Manual:**
1. Vá para: https://github.com/MRITSoftware/mritlocal/actions
2. Clique em "Build APK (Simple - No Release)" ou "Build Android APK"
3. Clique em "Run workflow"
4. Selecione a branch (geralmente `main`)
5. Clique em "Run workflow" novamente

### 3. Baixar o APK

1. Aguarde o workflow terminar (pode levar 5-10 minutos)
2. Vá para a aba **Actions** no GitHub
3. Clique no workflow que acabou de executar
4. Role até a seção **Artifacts** (lateral direita)
5. Clique em **tuya-installer-apk**
6. Baixe o arquivo **app-debug.apk**

## 📁 Arquivos Necessários no Repositório

O workflow precisa destes arquivos na raiz:

- ✅ `tuya_server_enhanced.py`
- ✅ `requirements.txt`
- ✅ `start_server.sh`
- ✅ `stop_server.sh`
- ✅ `android_app/` (pasta completa do projeto Android)

## 🔄 Workflows Disponíveis

### 1. `build-apk-simple.yml` (Recomendado)
- ✅ Executa manualmente ou no push
- ✅ Gera APK e disponibiliza como artifact
- ✅ Mais simples e rápido
- ✅ Não cria release automático

### 2. `build-apk.yml` (Com Release)
- ✅ Cria release automático no GitHub
- ✅ APK disponível na página de Releases
- ⚠️ Requer permissões de escrita (geralmente já tem)

## 📱 Como Instalar o APK no Tablet

1. **Baixe o APK** do GitHub Actions (Artifacts)
2. **Transfira para o tablet** (USB, email, etc)
3. **No tablet:**
   - Configurações → Segurança → Ativar "Fontes desconhecidas"
   - Abra o arquivo APK
   - Instale

## 🐛 Troubleshooting

### Workflow falha ao copiar arquivos
- Verifique se os arquivos Python estão na raiz do repositório
- Não devem estar em subpastas

### APK não aparece nos Artifacts
- Verifique os logs do workflow
- Procure por erros de compilação
- Certifique-se de que o build foi bem-sucedido

### Erro de permissões
- O workflow usa `GITHUB_TOKEN` que já tem permissões básicas
- Para releases, pode precisar de permissões adicionais (geralmente já funciona)

## 💡 Dicas

- **Primeira vez:** Execute manualmente para testar
- **Depois:** O workflow roda automaticamente em cada push
- **APK grande?** Os artifacts ficam disponíveis por 90 dias
- **Múltiplos builds:** Cada execução gera um novo APK

## 🔗 Links Úteis

- **Actions:** https://github.com/MRITSoftware/mritlocal/actions
- **Releases:** https://github.com/MRITSoftware/mritlocal/releases (se usar build-apk.yml)

## 📝 Exemplo de Uso

```bash
# 1. Fazer alterações
git add .
git commit -m "Atualizar código"

# 2. Push (dispara workflow automaticamente)
git push origin main

# 3. Aguardar workflow (5-10 min)
# 4. Baixar APK dos Artifacts
```

---

**Pronto! Agora é só fazer push e o APK será gerado automaticamente! 🎉**

