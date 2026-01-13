# 🚀 Instalação Simplificada - Versão Final

## 📱 Para Clientes (Modo Mais Simples)

### Opção 1: APK Automático (Recomendado)

1. **Instale o APK** "Servidorzinho Installer"
2. **Abra o app**
3. **Clique em "Instalar"**
4. **Siga as instruções na tela**
5. **Pronto!** O servidor roda automaticamente

### Opção 2: Script Manual (Se APK não funcionar)

1. **Instale o Termux** (Play Store)
2. **Abra o Termux**
3. **Execute:**
   ```bash
   cd ~/servidorzinho
   bash INSTALAR_AUTO.sh
   ```
4. **Depois, sempre que quiser iniciar:**
   ```bash
   servidor-auto
   ```

## 🔄 Fluxo Automático

Após a instalação inicial:

- ✅ **Servidor inicia automaticamente** quando o tablet liga
- ✅ **Reinicia sozinho** se a conexão cair
- ✅ **Roda em background** sem precisar do Termux aberto
- ✅ **Não interfere** com o app Gelafit Go

## 📋 Checklist de Instalação

- [ ] APK instalado
- [ ] Termux instalado (via APK ou manualmente)
- [ ] Permissões concedidas
- [ ] Instalação concluída (mensagem de sucesso)
- [ ] Servidor rodando (verificar com `ps aux | grep servidor`)

## 🛠️ Verificação

### Ver se está rodando:
```bash
ps aux | grep servidor_auto
```

### Ver logs:
```bash
tail -f ~/servidorzinho/servidor.log
```

### Testar servidor:
```bash
curl http://localhost:8080/status
```

## ❓ Problemas Comuns

### "Termux não encontrado"
- Instale o Termux manualmente da Play Store
- Abra o Termux uma vez
- Tente novamente

### "Permissão negada"
- Vá em Configurações → Apps → Servidorzinho → Permissões
- Ative todas as permissões

### "Servidor não inicia"
- Abra o Termux
- Execute: `cd ~/servidorzinho && bash iniciar_auto.sh`
- Verifique logs: `tail -f servidor.log`

