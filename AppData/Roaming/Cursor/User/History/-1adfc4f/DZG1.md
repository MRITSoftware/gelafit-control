# 💻 SGE Barrella Eventos - Software Desktop

## 🚀 Como Instalar e Usar o Software

Este é um **software desktop** que fica instalado no PC, não precisa de internet para funcionar!

### ⚡ Instalação Rápida

#### Windows
1. Execute: `iniciar-desktop.bat`
2. O software abrirá em uma janela do Windows
3. Faça login e use normalmente

#### Linux/macOS
1. Execute: `chmod +x iniciar-desktop.sh && ./iniciar-desktop.sh`
2. O software abrirá em uma janela do sistema
3. Faça login e use normalmente

### 🔧 Instalação Manual

1. **Instalar dependências:**
   ```bash
   npm install
   ```

2. **Executar o software:**
   ```bash
   npm run electron-dev
   ```

### 📦 Criar Instalador

#### Para Windows:
```bash
criar-instalador.bat
```
- Gera: `SGE Barrella Eventos Setup.exe`
- Instala como software normal do Windows

#### Para Linux/macOS:
```bash
chmod +x criar-instalador.sh
./criar-instalador.sh
```
- Gera: `.AppImage` (Linux) ou `.dmg` (macOS)

### 🎯 Características do Software Desktop

#### ✅ Vantagens:
- **Funciona offline** - Não precisa de internet
- **Instala no PC** - Como qualquer software
- **Janela nativa** - Integrado ao sistema operacional
- **Menu nativo** - Menu do Windows/macOS/Linux
- **Atalhos de teclado** - Ctrl+N (Novo Cliente), etc.
- **Ícone na área de trabalho** - Acesso rápido
- **Notificações do sistema** - Alertas nativos
- **Performance melhor** - Mais rápido que web

#### 🖥️ Interface:
- **Janela redimensionável** - Ajuste o tamanho
- **Menu superior** - Arquivo, Editar, Visualizar, etc.
- **Atalhos de teclado** - Navegação rápida
- **Tela de carregamento** - Profissional
- **Design responsivo** - Funciona em qualquer tamanho

### 🔑 Credenciais de Acesso

- **Administrador**: admin@barrella.com.br / admin123
- **Gerente**: gerente@barrella.com.br / admin123
- **Usuário**: usuario@barrella.com.br / admin123

### 📋 Funcionalidades Completas

#### ✅ Módulos Implementados:
- **Dashboard** - Visão geral do negócio
- **Clientes** - Cadastro completo de clientes
- **Orçamentos** - Criação e gestão de orçamentos
- **Produtos** - Catálogo de equipamentos e serviços
- **Pedidos** - Controle de pedidos de venda
- **Usuários** - Gestão de usuários do sistema
- **Relatórios** - Análises e relatórios
- **Configurações** - Configurações do sistema

#### 🎨 Interface Profissional:
- **Design moderno** - Interface limpa e profissional
- **Navegação intuitiva** - Fácil de usar
- **Filtros e buscas** - Encontre informações rapidamente
- **Tabelas organizadas** - Dados bem estruturados
- **Status visuais** - Cores e badges informativos
- **Responsivo** - Funciona em qualquer tamanho de tela

### 🎯 Para Apresentação ao Cliente

#### Roteiro Sugerido:

1. **Instalação** - Mostre como instala facilmente
2. **Abertura** - Software abre em janela nativa
3. **Login** - Tela de login profissional
4. **Dashboard** - Visão geral do sistema
5. **Navegação** - Use o menu e atalhos
6. **Funcionalidades** - Explore cada módulo
7. **Filtros** - Demonstre buscas e filtros
8. **Responsividade** - Redimensione a janela

#### Pontos Fortes a Destacar:
- **Software nativo** - Não é web, é software real
- **Funciona offline** - Não depende de internet
- **Instalação simples** - Um clique para instalar
- **Interface profissional** - Muito mais bonito que o atual
- **Fácil de usar** - Intuitivo e organizado
- **Completo** - Todas as funcionalidades necessárias

### 🔄 Distribuição

#### Para o Cliente:
1. **Desenvolvimento**: Use `npm run electron-dev`
2. **Teste**: Use `iniciar-desktop.bat`
3. **Produção**: Use `criar-instalador.bat`
4. **Distribua**: Envie o arquivo `.exe` gerado

#### Instalador Inclui:
- ✅ Executável principal
- ✅ Dependências necessárias
- ✅ Ícone na área de trabalho
- ✅ Menu Iniciar (Windows)
- ✅ Desinstalador automático
- ✅ Atualizações futuras

### 💡 Dicas para Demonstração

- **Mostre a instalação** - Como instala facilmente
- **Destaque o menu nativo** - Integração com o sistema
- **Use atalhos de teclado** - Ctrl+N, Ctrl+1, etc.
- **Redimensione a janela** - Mostre flexibilidade
- **Demonstre offline** - Desconecte a internet
- **Compare com o atual** - Destaque as melhorias

### 🆘 Suporte Técnico

#### Requisitos Mínimos:
- **Windows**: 10 ou superior
- **macOS**: 10.14 ou superior  
- **Linux**: Ubuntu 18.04 ou superior
- **RAM**: 4GB mínimo
- **Espaço**: 500MB livre

#### Solução de Problemas:
1. **Não abre**: Verifique se tem Node.js instalado
2. **Erro de dependências**: Execute `npm install`
3. **Lento**: Feche outros programas
4. **Erro de permissão**: Execute como administrador

### 🚀 Próximos Passos

Para a versão final:
1. **Backend local** - Banco de dados SQLite
2. **Backup automático** - Salvamento de dados
3. **Relatórios PDF** - Geração de relatórios
4. **Integração email** - Envio de orçamentos
5. **Módulo financeiro** - Contas a pagar/receber
6. **Sincronização** - Backup na nuvem

---

**Desenvolvido para**: Barrella Locação e Instalação de Equipamentos Ltda  
**Tipo**: Software Desktop (Electron)  
**Versão**: 1.0.0  
**Objetivo**: Substituir sistema antigo por solução moderna
