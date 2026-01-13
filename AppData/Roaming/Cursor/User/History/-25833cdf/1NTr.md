# GelaFit - Site Corporativo

Site moderno e responsivo para a GelaFit, empresa especializada em geladeiras autônomas inteligentes.

## 🚀 Características

- **Design Moderno**: Interface limpa e profissional
- **Totalmente Responsivo**: Funciona perfeitamente em todos os dispositivos
- **Fácil Personalização**: Arquivo de configuração dedicado
- **Animações Suaves**: Efeitos visuais elegantes
- **SEO Otimizado**: Estrutura otimizada para mecanismos de busca
- **Formulário de Contato**: Sistema de contato funcional
- **Performance**: Carregamento rápido e otimizado

## 📁 Estrutura de Arquivos

```
├── index.html          # Página principal
├── styles.css          # Estilos CSS
├── script.js           # JavaScript principal
├── config.js           # Arquivo de configuração
├── logo_gelafit.png    # Logo da empresa
└── README.md           # Este arquivo
```

## ⚙️ Personalização

### 1. Informações Básicas

Edite o arquivo `config.js` para personalizar:

```javascript
const GelaFitConfig = {
    company: {
        name: "GelaFit",
        tagline: "Geladeiras Autônomas Inteligentes",
        // ... outras configurações
    },
    contact: {
        phone: "+55 (11) 99999-9999",
        email: "contato@gelafit.com.br",
        address: "São Paulo, SP - Brasil",
        // ... outras informações
    }
};
```

### 2. Cores do Site

Personalize as cores editando a seção `colors` no `config.js`:

```javascript
colors: {
    primary: "#2563eb",      // Cor principal
    secondary: "#64748b",    // Cor secundária
    accent: "#06b6d4",       // Cor de destaque
    // ... outras cores
}
```

### 3. Produtos e Serviços

Adicione ou modifique produtos na seção `products`:

```javascript
products: [
    {
        name: "GelaFit Pro",
        description: "Descrição do produto...",
        features: ["Recurso 1", "Recurso 2"],
        price: "A partir de R$ 15.000",
        popular: false
    }
    // ... outros produtos
]
```

### 4. Textos Personalizados

Modifique textos na seção `texts`:

```javascript
texts: {
    hero: {
        title: "Seu Título Personalizado",
        subtitle: "Seu subtítulo personalizado",
        // ... outros textos
    }
}
```

## 🎨 Personalização Visual

### Logo
- Substitua o arquivo `logo_gelafit.png` pelo seu logo
- Mantenha o mesmo nome do arquivo ou atualize no HTML

### Imagens
- Adicione imagens na pasta do projeto
- Atualize os caminhos no HTML conforme necessário

### Cores
- Use o arquivo `config.js` para alterar cores
- As cores são aplicadas automaticamente via CSS variables

## 📱 Responsividade

O site é totalmente responsivo e se adapta a:
- **Desktop**: 1200px+
- **Tablet**: 768px - 1199px
- **Mobile**: 320px - 767px

## 🔧 Funcionalidades

### Formulário de Contato
- Validação de campos obrigatórios
- Validação de email
- Notificações de sucesso/erro
- Dados são exibidos no console (para desenvolvimento)

### Animações
- Contadores animados nas estatísticas
- Efeitos de scroll
- Animações de entrada
- Efeitos parallax

### Navegação
- Menu responsivo
- Scroll suave entre seções
- Indicador de seção ativa

## 🚀 Como Usar

1. **Desenvolvimento Local**:
   - Abra o arquivo `index.html` em um navegador
   - Ou use um servidor local (Live Server, Python, etc.)

2. **Personalização**:
   - Edite o arquivo `config.js`
   - Modifique cores, textos e informações
   - Substitua imagens conforme necessário

3. **Deploy**:
   - Faça upload dos arquivos para seu servidor
   - Configure o servidor para servir arquivos estáticos
   - Teste todas as funcionalidades

## 📞 Suporte

Para dúvidas ou suporte técnico:
- Email: contato@gelafit.com.br
- Telefone: +55 (11) 99999-9999

## 📄 Licença

Este projeto foi desenvolvido especificamente para a GelaFit. Todos os direitos reservados.

---

**GelaFit** - Transformando o futuro da refrigeração com tecnologia inteligente.
