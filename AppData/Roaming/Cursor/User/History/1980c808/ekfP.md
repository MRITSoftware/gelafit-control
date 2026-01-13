# Guia de Deploy - HostGator

Este guia explica como hospedar o Painel Web GelaFit na HostGator.

## 📋 Pré-requisitos

- Conta na HostGator ativa
- Acesso ao cPanel ou FTP
- Node.js instalado localmente (para fazer o build)

## 🚀 Passo a Passo

### 1. Preparar o Build Local

No seu computador, execute os seguintes comandos:

```bash
# Instalar dependências (se ainda não instalou)
npm install

# Fazer o build do projeto
npm run build
```

Isso criará uma pasta `dist` com todos os arquivos otimizados para produção.

### 2. Acessar o cPanel da HostGator

1. Acesse o cPanel da HostGator
2. Faça login com suas credenciais
3. Procure pela seção **"Gerenciador de Arquivos"** ou **"File Manager"**

### 3. Localizar a Pasta Pública

- Se você tem um domínio principal: os arquivos devem ir em `public_html/`
- Se você tem um subdomínio: os arquivos devem ir em `public_html/subdominio/`
- Se você tem um addon domain: os arquivos devem ir em `public_html/addondomain/`

### 4. Fazer Upload dos Arquivos

**Opção A: Via File Manager (cPanel)**

1. No File Manager, navegue até a pasta pública (ex: `public_html`)
2. **IMPORTANTE:** Se já existir um arquivo `index.html`, faça backup ou renomeie
3. Selecione todos os arquivos da pasta `dist` do seu projeto
4. Faça upload usando o botão "Upload" ou arraste os arquivos

**Opção B: Via FTP**

1. Use um cliente FTP (FileZilla, WinSCP, etc.)
2. Conecte-se usando:
   - **Host:** ftp.seusite.com.br (ou IP do servidor)
   - **Usuário:** seu usuário do cPanel
   - **Senha:** sua senha do cPanel
   - **Porta:** 21 (FTP) ou 22 (SFTP)
3. Navegue até a pasta pública
4. Faça upload de todos os arquivos da pasta `dist`

### 5. Configurar o .htaccess

1. No File Manager, certifique-se de que o arquivo `.htaccess` foi enviado junto com os outros arquivos
2. Se não foi enviado, crie um novo arquivo chamado `.htaccess` na pasta pública
3. Cole o conteúdo do arquivo `.htaccess` que está na raiz do projeto

**IMPORTANTE:** Na HostGator, arquivos que começam com ponto (.) podem estar ocultos. No File Manager, ative a opção "Mostrar arquivos ocultos" (Show Hidden Files).

### 6. Verificar Permissões

Certifique-se de que os arquivos têm as permissões corretas:

- Arquivos: `644` ou `644`
- Pastas: `755`
- `.htaccess`: `644`

Para alterar permissões no File Manager:
1. Clique com botão direito no arquivo/pasta
2. Selecione "Change Permissions"
3. Configure conforme acima

### 7. Configurar Variáveis de Ambiente (se necessário)

Se você usa variáveis de ambiente, você pode:

**Opção A: Criar arquivo `.env` na pasta pública** (não recomendado por segurança)

**Opção B: Configurar no cPanel**
1. No cPanel, procure por "Variáveis de Ambiente" ou "Environment Variables"
2. Adicione as variáveis necessárias

**Opção C: Hardcode no código** (para variáveis públicas como URLs do Supabase)

### 8. Testar o Site

1. Acesse seu domínio no navegador
2. Verifique se o site carrega corretamente
3. Teste as rotas (ex: `/login`, `/dashboard`)
4. Verifique se o React Router está funcionando (não deve dar erro 404 ao navegar)

## 🔧 Configurações Adicionais

### Configurar Domínio/Subdomínio

Se você ainda não configurou o domínio:

1. No cPanel, vá em **"Subdomínios"** ou **"Addon Domains"**
2. Adicione seu domínio ou subdomínio
3. Aponte para a pasta onde você fez o upload

### SSL/HTTPS

1. No cPanel, procure por **"SSL/TLS"** ou **"Let's Encrypt"**
2. Instale um certificado SSL gratuito
3. Force HTTPS redirecionando HTTP para HTTPS (pode adicionar no `.htaccess`)

### Otimizações de Performance

O arquivo `.htaccess` já inclui:
- Compressão GZIP
- Cache de arquivos estáticos
- Otimizações de segurança

## 🐛 Solução de Problemas

### Erro 404 ao navegar entre páginas

- Verifique se o arquivo `.htaccess` está na pasta correta
- Verifique se o mod_rewrite está habilitado (geralmente está na HostGator)
- Verifique as permissões do `.htaccess` (deve ser 644)

### Arquivos não carregam (CSS/JS)

- Verifique se todos os arquivos da pasta `dist` foram enviados
- Verifique os caminhos no console do navegador (F12)
- Limpe o cache do navegador (Ctrl+F5)

### Erro de CORS ou Supabase

- Verifique se as URLs do Supabase estão corretas
- Verifique se o domínio está autorizado no Supabase (Dashboard > Settings > API)

### Site mostra página em branco

- Abra o console do navegador (F12) e verifique erros
- Verifique se o arquivo `index.html` está na pasta correta
- Verifique se os arquivos JavaScript foram carregados

## 📝 Checklist Final

- [ ] Build do projeto executado (`npm run build`)
- [ ] Todos os arquivos da pasta `dist` enviados
- [ ] Arquivo `.htaccess` configurado
- [ ] Permissões dos arquivos corretas
- [ ] Domínio/subdomínio configurado
- [ ] SSL/HTTPS configurado (recomendado)
- [ ] Site testado e funcionando
- [ ] Rotas do React Router funcionando
- [ ] Conexão com Supabase funcionando

## 🔄 Atualizações Futuras

Para atualizar o site:

1. Faça as alterações no código local
2. Execute `npm run build` novamente
3. Faça upload apenas dos arquivos alterados (ou todos para garantir)
4. Limpe o cache do navegador e teste

## 📞 Suporte

Se tiver problemas:
1. Verifique os logs de erro no console do navegador (F12)
2. Verifique os logs do servidor no cPanel
3. Entre em contato com o suporte da HostGator se necessário

---

**Nota:** Este guia assume que você está usando um plano de hospedagem compartilhada da HostGator. Se você tem um VPS ou servidor dedicado, os passos podem variar.

