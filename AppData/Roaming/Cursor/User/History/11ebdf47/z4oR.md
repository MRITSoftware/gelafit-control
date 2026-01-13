# Disparador WhatsApp - Envio Automático

Sistema para envio automático de mensagens via WhatsApp Web com intervalo de 5 minutos entre envios.

## 🚀 Funcionalidades

- ✅ Leitura de arquivos Excel com números e mensagens
- ✅ Geração automática de links do WhatsApp Web
- ✅ Abertura automática do navegador
- ✅ Envio automático de mensagens
- ✅ Intervalo configurável de 5 minutos entre envios
- ✅ Interface gráfica intuitiva
- ✅ Log de atividades em tempo real
- ✅ Controle de progresso

## 📋 Pré-requisitos

- Python 3.7 ou superior
- Google Chrome instalado
- Arquivo Excel com as colunas: `numero` e `mensagem`

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

1. **Preparar o arquivo Excel:**
   - Crie um arquivo com as colunas `numero` e `mensagem`
   - Use o arquivo `exemplo_clientes.xlsx` como referência

2. **Executar o programa:**
   - Abra o `whatsapp_disparador.py`
   - Clique em "Procurar" e selecione seu arquivo Excel
   - Clique em "Iniciar Envios"

3. **Primeira execução:**
   - O Chrome será aberto automaticamente
   - Faça login no WhatsApp Web
   - O sistema começará a enviar as mensagens automaticamente

4. **Controle:**
   - Use "Parar Envios" para interromper o processo
   - Acompanhe o progresso na barra de progresso
   - Monitore o log de atividades

## ⚙️ Configurações

- **Intervalo entre envios:** 5 minutos (fixo)
- **Navegador:** Google Chrome (automático)
- **Formato de número:** Adiciona código 55 automaticamente

## 🔧 Solução de Problemas

### Erro de Driver
Se houver erro com o ChromeDriver:
- O sistema baixa automaticamente a versão correta
- Certifique-se de ter o Google Chrome instalado

### Erro de Login
- Faça login manualmente no WhatsApp Web na primeira execução
- Mantenha a aba do WhatsApp Web aberta

### Erro de Envio
- Verifique se o número está correto no Excel
- Certifique-se de que o WhatsApp Web está logado
- Verifique a conexão com a internet

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
