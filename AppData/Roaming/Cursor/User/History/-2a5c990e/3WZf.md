# 🚀 Guia de Hospedagem - Extrator de Tabelas PDF

## 📋 Pré-requisitos para Hospedagem

### Hostinger
- ✅ Conta de hospedagem compartilhada ou VPS
- ✅ Suporte a Python 3.8+
- ✅ Acesso SSH (recomendado)
- ✅ Java 8+ instalado no servidor

### Hostgator
- ✅ Conta de hospedagem compartilhada ou VPS
- ✅ Suporte a Python/Flask
- ✅ Acesso ao cPanel
- ✅ Java 8+ instalado no servidor

## 🛠️ Instalação Passo a Passo

### 1. Preparação do Servidor

#### Via SSH (Recomendado)
```bash
# Conectar via SSH
ssh usuario@seu-dominio.com

# Navegar para o diretório do site
cd public_html

# Verificar versão do Python
python3 --version

# Verificar se Java está instalado
java -version
```

#### Via cPanel (Hostgator)
1. Acesse o cPanel
2. Vá em "Terminal" ou "SSH"
3. Navegue para `public_html`

### 2. Upload dos Arquivos

#### Opção A: Upload via cPanel
1. Acesse o "Gerenciador de Arquivos"
2. Navegue para `public_html`
3. Faça upload de todos os arquivos do projeto
4. Extraia o arquivo ZIP se necessário

#### Opção B: Upload via SSH
```bash
# Usando SCP
scp -r * usuario@seu-dominio.com:public_html/

# Ou usando Git
git clone https://github.com/seu-usuario/extrator-tabelas-pdf.git
cd extrator-tabelas-pdf
cp -r * ../public_html/
```

### 3. Instalação das Dependências

#### Via SSH
```bash
# Instalar dependências Python
pip3 install -r requirements.txt

# Ou usando virtual environment (recomendado)
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

#### Via cPanel (Hostgator)
1. Acesse "Python App" no cPanel
2. Crie uma nova aplicação Python
3. Configure o diretório como `public_html`
4. Instale as dependências:
   ```bash
   pip install -r requirements.txt
   ```

### 4. Configuração do Servidor

#### Hostinger
1. Acesse o painel de controle
2. Vá em "Python" ou "Aplicações"
3. Configure:
   - **Domínio**: seu-dominio.com
   - **Arquivo de entrada**: `wsgi.py`
   - **Versão Python**: 3.8+
   - **Diretório**: `public_html`

#### Hostgator
1. No cPanel, vá em "Python App"
2. Configure:
   - **App Name**: extrator-tabelas
   - **Python Version**: 3.8+
   - **App Directory**: `public_html`
   - **App URL**: `seu-dominio.com`
   - **Startup File**: `wsgi.py`

### 5. Configuração de Permissões

```bash
# Definir permissões corretas
chmod 755 wsgi.py
chmod 755 install.py
chmod 755 test_example.py
chmod -R 755 uploads/
chmod -R 755 templates/

# Criar pastas se não existirem
mkdir -p uploads
mkdir -p templates
```

### 6. Configuração do .htaccess

O arquivo `.htaccess` já está configurado, mas verifique se está na raiz do domínio:

```apache
RewriteEngine On
RewriteCond %{REQUEST_FILENAME} !-f
RewriteCond %{REQUEST_FILENAME} !-d
RewriteRule ^(.*)$ wsgi.py/$1 [QSA,L]
```

### 7. Teste da Instalação

```bash
# Executar script de teste
python3 test_example.py

# Ou testar a aplicação diretamente
python3 app.py
```

## 🔧 Configurações Específicas por Provedor

### Hostinger
- **Limite de memória**: 512MB (padrão)
- **Timeout**: 30 segundos
- **Upload máximo**: 8MB
- **Processamento**: Limitado a 10 páginas por PDF

### Hostgator
- **Limite de memória**: 256MB (padrão)
- **Timeout**: 60 segundos
- **Upload máximo**: 16MB
- **Processamento**: Limitado a 5 páginas por PDF

## 🚨 Solução de Problemas

### Erro: "Java not found"
```bash
# Verificar se Java está instalado
java -version

# Se não estiver, instalar (Ubuntu/Debian)
sudo apt-get update
sudo apt-get install openjdk-8-jdk

# Configurar JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
```

### Erro: "Module not found"
```bash
# Reinstalar dependências
pip3 install --upgrade -r requirements.txt

# Verificar se está no diretório correto
pwd
ls -la
```

### Erro: "Permission denied"
```bash
# Corrigir permissões
chmod 755 wsgi.py
chmod -R 755 uploads/
chmod -R 755 templates/
```

### Erro: "Memory limit exceeded"
1. Reduza o tamanho dos PDFs
2. Configure limite menor no `config.py`
3. Use PDFs com menos páginas

### Erro: "Timeout"
1. Aumente o timeout no servidor
2. Use PDFs menores
3. Configure timeout maior no `config.py`

## 📊 Monitoramento

### Logs de Erro
```bash
# Ver logs do Apache
tail -f /var/log/apache2/error.log

# Ver logs da aplicação
tail -f /var/log/python-app.log
```

### Uso de Recursos
```bash
# Ver uso de memória
free -h

# Ver uso de CPU
top

# Ver espaço em disco
df -h
```

## 🔒 Segurança

### Configurações Recomendadas
1. **Limite de upload**: 8MB máximo
2. **Timeout**: 5 minutos máximo
3. **Limpeza automática**: Ativada
4. **Validação de arquivos**: Ativada
5. **Headers de segurança**: Configurados

### Firewall
```bash
# Bloquear acesso direto a arquivos Python
# (já configurado no .htaccess)
```

## 📈 Otimização

### Para Hospedagem Compartilhada
1. **Reduzir memória Java**: `-Xmx512m`
2. **Limitar páginas**: Máximo 5 páginas
3. **Timeout menor**: 2 minutos
4. **Limpeza frequente**: A cada 12 horas

### Para VPS/Dedicado
1. **Aumentar memória Java**: `-Xmx2g`
2. **Processar mais páginas**: Até 20 páginas
3. **Timeout maior**: 10 minutos
4. **Cache de resultados**: Ativado

## 🆘 Suporte

### Logs Importantes
- `/var/log/apache2/error.log`
- `/var/log/python-app.log`
- `uploads/` (arquivos de erro)

### Informações para Suporte
1. Versão do Python: `python3 --version`
2. Versão do Java: `java -version`
3. Logs de erro completos
4. Tamanho e tipo do PDF testado
5. Configurações do servidor

### Contato
- **Email**: suporte@seu-dominio.com
- **Documentação**: README.md
- **Issues**: GitHub Issues
