# 📤 Instruções de Upload para PythonAnywhere

## 🎯 **Resumo Rápido:**
1. Criar conta no PythonAnywhere
2. Upload dos arquivos
3. Configurar aplicação web
4. Configurar domínio `orion.mrit.com.br`
5. Configurar DNS na HostGator

## 📋 **Arquivos para Upload:**

### **Arquivos Principais:**
- ✅ `app.py` - Aplicação principal
- ✅ `wsgi.py` - Configuração WSGI
- ✅ `requirements.txt` - Dependências

### **Pastas:**
- ✅ `templates/` - Templates HTML
- ✅ `uploads/` - Pasta para uploads (criar vazia)

## 🚀 **Passo a Passo Detalhado:**

### **1. Criar Conta PythonAnywhere**
- Acesse: https://www.pythonanywhere.com
- Clique em "Sign up for a free account"
- Escolha o plano gratuito

### **2. Upload dos Arquivos**

**2.1. Acessar File Manager:**
- Faça login no PythonAnywhere
- Vá em "Files" no painel
- Navegue até `/home/seu_usuario/`

**2.2. Criar Pasta do Projeto:**
- Clique em "New directory"
- Nome: `mysite`
- Entre na pasta `mysite`

**2.3. Upload dos Arquivos:**
- Clique em "Upload a file"
- Faça upload de cada arquivo:
  - `app.py`
  - `wsgi.py` 
  - `requirements.txt`

**2.4. Criar Pastas:**
- Clique em "New directory" → `templates`
- Clique em "New directory" → `uploads`
- Clique em "New directory" → `static`

**2.5. Upload Templates:**
- Entre na pasta `templates`
- Faça upload de:
  - `index.html`
  - `login.html`

### **3. Instalar Dependências**

**3.1. Acessar Console:**
- Vá em "Consoles" → "Bash"

**3.2. Instalar:**
```bash
cd ~/mysite
pip3.10 install --user -r requirements.txt
```

### **4. Configurar Aplicação Web**

**4.1. Acessar Web Tab:**
- Vá em "Web" no painel
- Clique em "Add a new web app"

**4.2. Configurações:**
- **Domain name:** `seu_usuario.pythonanywhere.com`
- **Python version:** 3.10
- **Framework:** Flask
- **Source code:** `/home/seu_usuario/mysite`
- **WSGI file:** `/home/seu_usuario/mysite/wsgi.py`

### **5. Configurar Domínio Personalizado**

**5.1. Adicionar Domínio:**
- Vá em "Web" → sua aplicação
- Clique em "Domains"
- Adicione: `orion.mrit.com.br`

**5.2. Configurar HTTPS:**
- Vá em "Web" → sua aplicação
- Clique em "SSL certificates"
- Clique em "Enable HTTPS"

### **6. Configurar DNS na HostGator**

**No seu cPanel:**
- Vá em "Domínios" → "Editor de zona DNS"
- Adicione:
  ```
  Nome: orion
  Tipo: CNAME
  Valor: seu_usuario.pythonanywhere.com
  ```

## ✅ **Teste Final:**
- Acesse: `https://orion.mrit.com.br`
- Deve mostrar sua aplicação funcionando!

## 🆘 **Se Der Erro:**
- Verifique os logs em "Web" → "Error log"
- Verifique se todos os arquivos estão na pasta correta
- Verifique se as dependências foram instaladas

## 📞 **Precisa de Ajuda?**
Me avise em qual passo você está e posso te ajudar!
