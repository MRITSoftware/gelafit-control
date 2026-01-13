# Disparador WhatsApp - Envio Automático

Sistema inteligente para envio automático de mensagens via WhatsApp Web com detecção avançada de carregamento, interface manual e leitura de PDF.

## 🚀 Funcionalidades

- ✅ **Modo Automático**: Leitura de arquivos Excel com números e mensagens
- ✅ **Modo Manual**: Interface para colar links do WhatsApp diretamente
- ✅ **Modo PDF**: Extração automática de links do WhatsApp de arquivos PDF
- ✅ **Abertura de Guias**: Abre guias no navegador atual para envio manual
- ✅ **Detecção Inteligente**: Verifica se o WhatsApp Web carregou completamente
- ✅ **Geração Automática**: Cria links do WhatsApp Web automaticamente
- ✅ **Abertura Automática**: Abre o navegador e faz login
- ✅ **Envio Inteligente**: Múltiplos seletores para botão de envio
- ✅ **Intervalo Configurável**: 5 minutos entre envios
- ✅ **Interface Tripla**: Automática (Excel), Manual (Links) e PDF (Links)
- ✅ **Log Detalhado**: Acompanha todas as atividades em tempo real
- ✅ **Controle Total**: Pode parar/iniciar quando quiser
- ✅ **Histórico de Envios**: Controle de links já enviados

## 📋 Pré-requisitos

- Python 3.7 ou superior
- Google Chrome instalado
- Arquivo Excel com as colunas: `numero` e `mensagem` (modo automático)
- Arquivo PDF com links do WhatsApp (modo PDF)

## 🛠️ Instalação

1. **Instalar dependências:**
```bash
pip install -r requirements.txt
```

2. **Executar o programa:**
```bash
python whatsapp_disparador.py
```

## 📊 Formato do Excel

O arquivo Excel deve ter as seguintes colunas:

| numero | mensagem |
|--------|----------|
| 11999999999 | Olá! Aqui está sua mensagem diária. |
| 11888888888 | Bom dia! Lembre-se de verificar suas tarefas. |

**Importante:**
- A coluna `numero` deve conter apenas números (com ou sem DDD)
- A coluna `mensagem` deve conter o texto a ser enviado
- O sistema adiciona automaticamente o código do país (55) se necessário

## 🎯 Como Usar

### **Modo Automático (Excel):**

1. **Preparar o arquivo Excel:**
   - Crie um arquivo com as colunas `numero` e `mensagem`
   - Use o arquivo `exemplo_clientes.xlsx` como referência

2. **Executar o programa:**
   - Abra o `whatsapp_disparador.py`
   - Selecione "Automático (Excel)"
   - Clique em "Procurar" e selecione seu arquivo Excel
   - Clique em "Abrir WhatsApp Web" para fazer login
   - Clique em "Iniciar Envios"

### **Modo Manual (Links):**

1. **Preparar os links:**
   - Gere links do WhatsApp Web (ex: https://web.whatsapp.com/send?phone=5511999999999&text=Olá!)
   - Cole o link no campo "Link do WhatsApp"

2. **Enviar:**
   - Selecione "Manual (Links)"
   - Cole o link no campo
   - Clique em "Enviar Link"
   - O sistema abrirá o WhatsApp e enviará a mensagem

### **Modo PDF (Links):**

1. **Preparar o PDF:**
   - Crie um PDF contendo links do WhatsApp
   - Use o arquivo `exemplo_links.pdf` como referência
   - Os links podem estar em qualquer formato: web.whatsapp.com, api.whatsapp.com, wa.me

2. **Extrair e Enviar:**
   - Selecione "PDF (Links)"
   - Clique em "Procurar PDF" e selecione seu arquivo
   - Clique em "Extrair Links do PDF"
   - Clique em "Iniciar Envios"
   - O sistema abrirá guias no seu navegador atual para cada link
   - Você terá 30 segundos para enviar cada mensagem manualmente
   - Após o tempo, o sistema prosseguirá para o próximo link

### **Primeira Execução:**
- O Chrome será aberto automaticamente
- Faça login no WhatsApp Web
- O sistema detectará automaticamente quando estiver pronto

### **Controle:**
- Use "Parar Envios" para interromper o processo
- Acompanhe o progresso na barra de progresso
- Monitore o log de atividades detalhado

## ⚙️ Configurações

- **Intervalo entre envios:** 5 minutos (fixo)
- **Navegador:** Google Chrome (automático)
- **Formato de número:** Adiciona código 55 automaticamente

## 🔧 Solução de Problemas

### **Detecção de Carregamento**
- O sistema verifica automaticamente se o WhatsApp carregou
- Se não detectar, tente fazer login manualmente primeiro
- Use o botão "Abrir WhatsApp Web" para garantir o carregamento

### **Erro de Driver**
- O sistema baixa automaticamente a versão correta do ChromeDriver
- Certifique-se de ter o Google Chrome instalado e atualizado

### **Erro de Login**
- Faça login manualmente no WhatsApp Web na primeira execução
- Mantenha a aba do WhatsApp Web aberta
- O sistema detectará automaticamente quando estiver logado

### **Erro de Envio**
- Verifique se o número está correto no Excel
- Certifique-se de que o WhatsApp Web está logado
- Verifique a conexão com a internet
- Use o modo manual para testar links específicos

### **Modo Manual**
- Cole links completos do WhatsApp Web
- O sistema validará o formato do link
- Use para testar envios individuais

## 📝 Logs

O sistema mantém um log detalhado de todas as atividades:
- Carregamento do arquivo
- Envios realizados
- Erros encontrados
- Status do processo

## ⚠️ Avisos Importantes

1. **Uso Responsável:** Respeite os termos de uso do WhatsApp
2. **Intervalo:** O intervalo de 5 minutos é para evitar spam
3. **Backup:** Mantenha backup dos seus dados
4. **Teste:** Teste primeiro com poucos números

## 🆘 Suporte

Em caso de problemas:
1. Verifique se todas as dependências estão instaladas
2. Confirme se o arquivo Excel está no formato correto
3. Verifique se o Chrome está instalado e atualizado
4. Consulte o log de atividades para mais detalhes

---

**Desenvolvido para facilitar o envio de mensagens diárias via WhatsApp Web de forma automatizada e controlada.**
