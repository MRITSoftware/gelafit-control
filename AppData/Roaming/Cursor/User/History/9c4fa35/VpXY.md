# MRIT Orion - Sistema de Extração de Tabelas Bancárias

Sistema web para extrair tabelas de extratos bancários em PDF e exportar para Excel.

## 🚀 Funcionalidades

- **Login simples** sem senha (para facilitar o acesso)
- **Upload de PDFs** com drag & drop
- **Extração automática** de tabelas usando pdfplumber
- **Visualização** das tabelas extraídas
- **Exportação para Excel** com múltiplas abas
- **Interface temática** com logo MRIT Orion
- **Mensagens de carregamento** temáticas espaciais
- **Design responsivo** e moderno

## 🛠️ Instalação Local

1. **Clone o repositório:**
```bash
git clone <seu-repositorio>
cd mrit-orion
```

2. **Instale as dependências:**
```bash
pip install -r requirements.txt
```

3. **Execute o sistema:**
```bash
python app.py
```

4. **Acesse no navegador:**
```
http://localhost:5000
```

## 🌐 Deploy na Hostinger/HostGator

### Opção 1: Deploy via cPanel (Recomendado)

1. **Compacte os arquivos:**
   - Crie um arquivo ZIP com todos os arquivos do projeto
   - Exclua a pasta `__pycache__` e arquivos `.pyc`

2. **Faça upload via cPanel:**
   - Acesse o File Manager do cPanel
   - Navegue até a pasta `public_html`
   - Faça upload do arquivo ZIP
   - Extraia os arquivos

3. **Configure o Python:**
   - No cPanel, vá em "Python App"
   - Crie uma nova aplicação Python
   - Defina o diretório como `public_html/mrit-orion`
   - Configure a versão do Python (3.8+)
   - Instale as dependências:
     ```
     pip install -r requirements.txt
     ```

4. **Configure o WSGI:**
   - Crie um arquivo `passenger_wsgi.py` na raiz:
   ```python
   import sys
   import os
   sys.path.insert(0, os.path.dirname(__file__))
   from app import app as application
   ```

### Opção 2: Deploy via SSH (Se disponível)

1. **Conecte via SSH:**
```bash
ssh usuario@seu-dominio.com
```

2. **Navegue para o diretório:**
```bash
cd public_html
```

3. **Clone/upload dos arquivos:**
```bash
# Se usando Git
git clone <seu-repositorio> mrit-orion
cd mrit-orion
```

4. **Instale as dependências:**
```bash
pip3 install -r requirements.txt
```

5. **Execute:**
```bash
python3 app.py
```

## 📁 Estrutura do Projeto

```
mrit-orion/
├── app.py                 # Aplicação Flask principal
├── requirements.txt       # Dependências Python
├── README.md             # Este arquivo
├── templates/            # Templates HTML
│   ├── login.html        # Página de login
│   └── index.html        # Página principal
└── uploads/              # Pasta para arquivos enviados
```

## 🔧 Configurações

### Variáveis de Ambiente (Opcional)

Crie um arquivo `.env` para configurações personalizadas:

```env
FLASK_ENV=production
SECRET_KEY=sua_chave_secreta_aqui
MAX_FILE_SIZE=16777216
UPLOAD_FOLDER=uploads
```

### Configurações do Servidor

Para produção, considere usar:
- **Gunicorn** como servidor WSGI
- **Nginx** como proxy reverso
- **SSL/HTTPS** para segurança

## 📋 Requisitos do Sistema

- **Python 3.8+**
- **Flask 2.3+**
- **pdfplumber** para extração de PDFs
- **pandas** para manipulação de dados
- **openpyxl** para exportação Excel

## 🎨 Personalização

### Logo e Cores
- Edite os arquivos HTML em `templates/`
- Modifique as cores CSS para personalizar o tema
- Substitua os ícones de constelação conforme necessário

### Mensagens de Carregamento
- Edite o array `messages` no JavaScript do `index.html`
- Adicione suas próprias mensagens temáticas

## 🐛 Solução de Problemas

### Erro de Upload
- Verifique se a pasta `uploads` existe e tem permissões de escrita
- Confirme o tamanho máximo do arquivo (16MB)

### Erro de Extração
- Verifique se o PDF contém tabelas legíveis
- Alguns PDFs podem ter tabelas em formato de imagem (não extraíveis)

### Erro de Deploy
- Confirme se o Python 3.8+ está disponível no servidor
- Verifique se todas as dependências foram instaladas
- Confirme as permissões de arquivo no servidor

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique os logs do servidor
2. Confirme se todas as dependências estão instaladas
3. Teste localmente antes do deploy

## 🔒 Segurança

- O sistema usa login simples sem senha (apenas para demonstração)
- Para produção, implemente autenticação adequada
- Configure HTTPS para proteger os dados
- Limite o tamanho dos arquivos enviados

## 📈 Melhorias Futuras

- [ ] Autenticação com senha
- [ ] Suporte a mais formatos de PDF
- [ ] Histórico de extrações
- [ ] API REST para integração
- [ ] Dashboard de estatísticas
- [ ] Suporte a múltiplos usuários
