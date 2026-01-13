# 🐍 Deploy no PythonAnywhere - orion.mrit.com.br

## 📋 Passo a Passo Completo

### **1. Criar Conta no PythonAnywhere**
- Acesse: https://www.pythonanywhere.com
- Clique em "Sign up for a free account"
- Escolha o plano gratuito (suficiente para começar)

### **2. Configurar Projeto**

**2.1. Acessar o Console:**
- Faça login no PythonAnywhere
- Vá em "Consoles" → "Bash"
- Execute os comandos abaixo:

**2.2. Criar Estrutura de Pastas:**
```bash
# Criar diretório do projeto
mkdir -p ~/mysite
cd ~/mysite

# Criar pastas necessárias
mkdir -p templates
mkdir -p uploads
mkdir -p static
```

**2.3. Upload dos Arquivos:**
- Vá em "Files" no painel do PythonAnywhere
- Navegue até `/home/seu_usuario/mysite/`
- Faça upload dos arquivos:
  - `app.py`
  - `wsgi.py`
  - `requirements.txt`
  - `templates/index.html`
  - `templates/login.html`

### **3. Instalar Dependências**

**No Console Bash:**
```bash
cd ~/mysite
pip3.10 install --user -r requirements.txt
```

### **4. Configurar Aplicação Web**

**4.1. Acessar Web Tab:**
- Vá em "Web" no painel do PythonAnywhere
- Clique em "Add a new web app"

**4.2. Configurações:**
- **Domain name:** `seu_usuario.pythonanywhere.com` (temporário)
- **Python version:** 3.10
- **Framework:** Flask
- **Source code:** `/home/seu_usuario/mysite`
- **WSGI file:** `/home/seu_usuario/mysite/wsgi.py`

### **5. Configurar Domínio Personalizado**

**5.1. No PythonAnywhere:**
- Vá em "Web" → "Web apps" → sua aplicação
- Clique em "Static files"
- Adicione:
  - URL: `/static/`
  - Directory: `/home/seu_usuario/mysite/static/`

**5.2. Configurar HTTPS:**
- Vá em "Web" → "Web apps" → sua aplicação
- Clique em "SSL certificates"
- Clique em "Enable HTTPS"

**5.3. Configurar Domínio:**
- Vá em "Web" → "Web apps" → sua aplicação
- Clique em "Domains"
- Adicione: `orion.mrit.com.br`

### **6. Configurar DNS na HostGator**

**No seu cPanel:**
- Vá em "Domínios" → "Editor de zona DNS"
- Adicione um registro CNAME:
  ```
  Nome: orion
  Tipo: CNAME
  Valor: seu_usuario.pythonanywhere.com
  ```

### **7. Testar Aplicação**

**7.1. Teste Temporário:**
- Acesse: `https://seu_usuario.pythonanywhere.com`
- Deve mostrar sua aplicação funcionando

**7.2. Teste com Domínio:**
- Acesse: `https://orion.mrit.com.br`
- Deve mostrar sua aplicação funcionando

## 🔧 Configurações Adicionais

### **Limitações do Plano Gratuito:**
- 1 aplicação web
- 512MB de RAM
- 1GB de disco
- 100 segundos de CPU por dia
- Domínio personalizado (1 por conta)

### **Upgrade para Plano Pago:**
- Mais CPU
- Mais RAM
- Múltiplos domínios
- Banco de dados MySQL/PostgreSQL

## 📁 Estrutura Final no PythonAnywhere

```
/home/seu_usuario/mysite/
├── app.py
├── wsgi.py
├── requirements.txt
├── templates/
│   ├── index.html
│   └── login.html
├── uploads/
└── static/
```

## 🚨 Troubleshooting

### **Erro 500:**
- Verifique os logs em "Web" → "Web apps" → "Error log"
- Verifique se todas as dependências estão instaladas
- Verifique se o arquivo `wsgi.py` está correto

### **Erro de Import:**
- Verifique se o path no `wsgi.py` está correto
- Verifique se todos os arquivos estão na pasta correta

### **Erro de Domínio:**
- Verifique se o DNS está configurado corretamente
- Aguarde até 24h para propagação do DNS

## 🎯 Próximos Passos

1. **Criar conta no PythonAnywhere**
2. **Fazer upload dos arquivos**
3. **Configurar aplicação web**
4. **Configurar domínio personalizado**
5. **Configurar DNS na HostGator**
6. **Testar aplicação**

## 📞 Suporte

Se precisar de ajuda em qualquer etapa, me avise!
