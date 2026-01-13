# No Azul - Controle Financeiro Pessoal 💰

Um aplicativo moderno e intuitivo para controle financeiro pessoal, desenvolvido em Python com interface gráfica elegante.

## ✨ Funcionalidades

### 📊 Dashboard Inteligente
- **Visão geral** das finanças com cards informativos
- **Filtros por mês/ano** para análise temporal
- **Botão de visibilidade** para ocultar valores sensíveis
- **Estatísticas detalhadas** de despesas pagas vs pendentes

### 💳 Gestão de Transações
- **Rendas**: Registro de salários, freelances, investimentos
- **Despesas Fixas**: Contas mensais recorrentes (aluguel, internet, etc.)
- **Despesas Variáveis**: Gastos esporádicos (compras, lazer, etc.)
- **Status de Pagamento**: Marcar contas como pagas/pendentes com cores visuais

### 🎯 Sistema de Metas
- **Definir objetivos** financeiros com valores e prazos
- **Acompanhamento visual** do progresso
- **Adicionar valores** às metas conforme arrecadação

### 📈 Projeções e Relatórios
- **Projeções futuras** baseadas em dados históricos
- **Relatórios mensais** com gráficos
- **Análise por categorias** de gastos

### 🔧 Ferramentas Auxiliares
- **Calculadora de Dívidas** para planejamento de pagamentos
- **Backup automático** dos dados
- **Sistema de senha** opcional para proteção
- **Interface responsiva** e moderna

## 🚀 Como Usar

### Instalação
1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/no-azul.git
cd no-azul
```

2. Instale as dependências:
```bash
pip install -r requirements.txt
```

3. Execute o aplicativo:
```bash
python main.py
```

### Primeiro Uso
1. **Defina uma senha** (opcional) na primeira execução
2. **Configure suas categorias** de renda e despesas
3. **Adicione suas transações** mensais
4. **Acompanhe o progresso** no dashboard

## 📱 Interface

### Dashboard Principal
- **Cards coloridos** mostram resumo financeiro
- **Filtros temporais** para análise de períodos específicos
- **Botão de visibilidade** para privacidade

### Abas de Gestão
- **Rendas**: Adicionar e gerenciar receitas
- **Despesas**: Controle de gastos fixos e variáveis
- **Metas**: Definição e acompanhamento de objetivos
- **Projeções**: Análise de tendências futuras
- **Relatórios**: Gráficos e relatórios detalhados

### Status de Pagamento
- **Verde**: Contas pagas ✅
- **Laranja**: Contas pendentes ⏳
- **Clique no botão** para alternar status

## 🛠️ Tecnologias Utilizadas

- **Python 3.8+**
- **CustomTkinter**: Interface moderna e responsiva
- **PIL (Pillow)**: Manipulação de imagens e GIFs
- **JSON**: Armazenamento de dados local

## 📁 Estrutura do Projeto

```
No Azul/
├── main.py                 # Arquivo principal
├── financeiro.py          # Lógica de negócio
├── models/
│   └── financeiro.py      # Modelo de dados
├── views/
│   ├── app.py            # Interface principal
│   ├── login.py          # Tela de login
│   ├── dashboard.py      # Dashboard
│   ├── abas/             # Abas de funcionalidades
│   └── popups/           # Janelas modais
├── utils/                # Utilitários
├── data.json            # Dados do usuário
├── config.json          # Configurações
└── backups/             # Backups automáticos
```

## 🔒 Segurança e Privacidade

- **Dados locais**: Todas as informações ficam no seu computador
- **Senha opcional**: Proteção adicional se desejado
- **Backup automático**: Seus dados são protegidos
- **Visibilidade controlada**: Oculte valores quando necessário

## 🎨 Personalização

- **Tema escuro** elegante
- **Cores personalizáveis** por categoria
- **Moeda configurável** (R$, $, €, etc.)
- **Categorias customizáveis**

## 📊 Relatórios Disponíveis

- **Resumo mensal** com totais
- **Gráfico de categorias** de despesas
- **Progresso de metas** visuais
- **Projeções futuras** baseadas em histórico

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature
3. Commit suas mudanças
4. Push para a branch
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 👨‍💻 Desenvolvido por

**MRIT © 2025**

---

*Mantenha suas finanças "No Azul"! 💙*
