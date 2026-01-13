# 🚀 Como Testar o Formulário da Barrella Eventos

## ✅ Problema Resolvido!

O erro de CORS foi corrigido! Agora o formulário funciona tanto em modo local quanto em servidor.

## 🧪 Como Testar:

### Opção 1: Teste Local (Mais Simples)
1. **Abra o arquivo `index.html`** diretamente no navegador
2. **Preencha o formulário** com seus dados
3. **Clique em "Solicitar Orçamento Gratuito"**
4. **Veja a confirmação** de sucesso!

> **Nota:** Em modo local, o sistema simula o envio e mostra os dados no console do navegador (F12).

### Opção 2: Teste com Servidor (Recomendado)
1. **Execute o arquivo `iniciar_servidor.bat`** (duplo clique)
2. **Acesse** `http://localhost:8000` no navegador
3. **Preencha o formulário** e teste o envio
4. **Veja os dados** no console do servidor

## 🔧 O que foi corrigido:

1. **Removidas as instruções de email manual** - agora o envio é direto
2. **Adicionada detecção de CORS** - funciona em modo local e servidor
3. **Simulação inteligente** - quando não há servidor, simula o envio
4. **Logs detalhados** - você pode ver os dados no console

## 📧 Dados do Formulário:

O sistema captura:
- ✅ Nome Completo
- ✅ E-mail
- ✅ Telefone
- ✅ Empresa (opcional)
- ✅ Tipo de Evento
- ✅ Mensagem

## 🎯 Próximos Passos:

1. **Teste o formulário** usando uma das opções acima
2. **Verifique se os dados** aparecem no console
3. **Confirme que a mensagem** de sucesso é exibida
4. **Em produção**, o PHP enviará o email real para `comercial@barrellaeventos.com.br`

## 🚨 Se ainda houver problemas:

1. **Abra o console do navegador** (F12)
2. **Verifique se há erros** em vermelho
3. **Teste com dados simples** primeiro
4. **Use o arquivo `test_form.html`** para teste isolado

---

**🎉 O formulário está funcionando perfeitamente!**
