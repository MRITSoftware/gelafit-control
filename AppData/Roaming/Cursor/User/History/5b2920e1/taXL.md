# 📱 Guia Rápido - Servidorzinho

## Para Clientes (Instalação Única)

### Passo 1: Abrir o Termux
Abra o aplicativo **Termux** no seu celular.

### Passo 2: Navegar até a pasta
Digite e pressione Enter:
```bash
cd ~/servidorzinho
```
*(ou o caminho onde você salvou os arquivos)*

### Passo 3: Instalar (APENAS UMA VEZ)
Digite e pressione Enter:
```bash
bash INSTALAR.sh
```

Aguarde a instalação terminar (pode levar 1-2 minutos).

### Passo 4: Fechar e abrir o Termux
Feche completamente o Termux e abra novamente.

---

## Usar o Servidor (Todo Dia)

### Opção 1: Comando Simples (Recomendado)
Depois de instalar, sempre que quiser iniciar o servidor, digite:
```bash
servidor
```

### Opção 2: Script Alternativo
Se o comando `servidor` não funcionar, use:
```bash
bash INICIAR.sh
```

---

## ⚠️ Primeira Vez

Na primeira execução, o sistema vai perguntar:
```
📝 Digite o nome deste site (ex: Cozinha, Sala, Bar):
```

Digite o nome do local e pressione Enter.

---

## 🛑 Parar o Servidor

Para parar o servidor, pressione:
```
Ctrl + C
```

---

## ❓ Problemas?

### "Comando não encontrado"
Execute:
```bash
source ~/.bashrc
```

### "Python não encontrado"
Execute novamente:
```bash
bash INSTALAR.sh
```

### "Porta já em uso"
Alguém já está usando a porta. Feche outras instâncias do servidor.

---

## 📞 Suporte

Se tiver problemas, envie uma mensagem com:
- O erro que apareceu
- O que você estava fazendo quando aconteceu

