# Instruções para Servidor Local - Player MRIT

## Problema Identificado
O erro `The URL protocol of the current origin ('null') is not supported` acontece porque você está abrindo o arquivo diretamente no navegador (protocolo `file://`), que não suporta Service Workers.

## Solução: Servidor Local

### Opção 1: Usando o arquivo .bat (Windows)
1. **Duplo clique** em `iniciar_servidor.bat`
2. Aguarde a mensagem "Servidor MRIT iniciado!"
3. **Acesse**: http://localhost:8000
4. Teste o cache normalmente

### Opção 2: Usando Python diretamente
```bash
# No terminal/prompt de comando:
python servidor_local.py

# Ou se tiver Python 3:
python3 servidor_local.py

# Para usar porta diferente:
python servidor_local.py 8080
```

### Opção 3: Usando Node.js (se tiver instalado)
```bash
# Instalar servidor simples:
npm install -g http-server

# Executar:
http-server -p 8000 --cors
```

## Como Testar o Cache

### 1. Iniciar o servidor
- Execute `iniciar_servidor.bat` ou `python servidor_local.py`
- Acesse http://localhost:8000

### 2. Carregar a playlist
- Digite o código da tela
- Aguarde a playlist carregar

### 3. Verificar Service Worker
```javascript
// No console do navegador:
mritDebug.verificarSW();
```

### 4. Forçar cache
```javascript
// Cache via Service Worker:
mritDebug.forcarCache();

// Ou cache direto:
mritDebug.forcarCacheDireto();
```

### 5. Verificar cache
```javascript
// Verificar todos os vídeos:
mritDebug.checkAllCache();
```

### 6. Teste offline
- Desligue a internet
- Recarregue a página
- Digite o código da tela
- Os vídeos devem reproduzir do cache

## Logs do Servidor

O servidor mostra logs detalhados:
- 📥 Requisições GET
- 🎬 Requisições de Range (vídeos)
- 🌐 Logs de acesso

## Troubleshooting

### Erro: "Python não encontrado"
- **Solução**: Instale Python 3.6+ em https://python.org
- **Alternativa**: Use `python3` em vez de `python`

### Erro: "Porta já em uso"
- **Solução**: Feche outros servidores ou use porta diferente
- **Comando**: `python servidor_local.py 8080`

### Service Worker ainda não funciona
- **Verificar**: Está acessando via http://localhost:8000?
- **Solução**: Limpe o cache do navegador (Ctrl+Shift+R)
- **Alternativa**: Use modo incógnito

## Comandos Úteis

### Verificar se está funcionando
```javascript
// Verificar Service Worker:
mritDebug.verificarSW();

// Verificar cache:
mritDebug.checkAllCache();

// Status geral:
mritDebug.dump();
```

### Cache direto (sem Service Worker)
```javascript
// Se o SW não funcionar, use cache direto:
mritDebug.forcarCacheDireto();
```

## Estrutura de Arquivos

```
PlayerMRITVision/
├── index.html              # Página principal
├── player.js               # Código do player
├── service-worker.js       # Service Worker
├── servidor_local.py       # Servidor Python
├── iniciar_servidor.bat    # Script Windows
└── INSTRUCOES_SERVIDOR_LOCAL.md
```

## Próximos Passos

1. ✅ Execute o servidor local
2. ✅ Acesse http://localhost:8000
3. ✅ Teste o cache com os comandos acima
4. ✅ Verifique se funciona offline

## Notas Importantes

- **HTTPS**: Service Workers só funcionam em HTTPS ou localhost
- **Cache**: O cache é mantido no IndexedDB do navegador
- **Limpeza**: O cache é limpo quando você troca de código de tela
- **Porta**: Use porta 8000 ou outra disponível

## Suporte

Se ainda tiver problemas:
1. Verifique se está acessando via http://localhost:8000
2. Limpe o cache do navegador
3. Use modo incógnito
4. Verifique os logs do console
