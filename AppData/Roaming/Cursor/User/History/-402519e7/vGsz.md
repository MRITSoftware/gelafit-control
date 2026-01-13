# Exemplo de Personalização do Site GelaFit

## 🎨 Como Personalizar Cores

### 1. Alterando a Cor Principal
No arquivo `config.js`, modifique:

```javascript
colors: {
    primary: "#2563eb",  // Azul atual
    // Mude para:
    primary: "#e11d48",  // Rosa
    primary: "#059669",  // Verde
    primary: "#7c3aed",  // Roxo
    primary: "#dc2626",  // Vermelho
}
```

### 2. Personalizando o Gradiente
No arquivo `styles.css`, procure por `--gradient-primary` e modifique:

```css
--gradient-primary: linear-gradient(135deg, #2563eb 0%, #06b6d4 100%);
/* Mude para: */
--gradient-primary: linear-gradient(135deg, #e11d48 0%, #f97316 100%);
```

## 📝 Alterando Textos

### 1. Título Principal
No `config.js`:

```javascript
texts: {
    hero: {
        title: "Geladeiras Autônomas Inteligentes para o Futuro",
        // Mude para:
        title: "Sua Empresa, Nossa Tecnologia",
    }
}
```

### 2. Descrição da Empresa
```javascript
about: {
    description: "A GelaFit é pioneira no desenvolvimento...",
    // Mude para:
    description: "Sua descrição personalizada aqui...",
}
```

## 🏢 Informações de Contato

### 1. Telefone e Email
```javascript
contact: {
    phone: "+55 (11) 99999-9999",
    email: "contato@gelafit.com.br",
    // Mude para:
    phone: "+55 (11) 12345-6789",
    email: "vendas@gelafit.com.br",
}
```

### 2. Endereço
```javascript
contact: {
    address: "São Paulo, SP - Brasil",
    // Mude para:
    address: "Rio de Janeiro, RJ - Brasil",
}
```

## 📊 Estatísticas da Empresa

```javascript
stats: {
    clients: 500,        // Número de clientes
    cities: 50,          // Cidades atendidas
    satisfaction: 99,    // % de satisfação
    yearsExperience: 4,  // Anos de experiência
    projectsCompleted: 1200  // Projetos concluídos
}
```

## 🛍️ Produtos e Serviços

### 1. Adicionando um Novo Produto
```javascript
products: [
    // ... produtos existentes
    {
        id: "premium",
        name: "GelaFit Premium",
        description: "Nossa solução mais avançada...",
        icon: "fas fa-crown",
        features: [
            "Recurso 1",
            "Recurso 2",
            "Recurso 3"
        ],
        price: "A partir de R$ 25.000",
        popular: true
    }
]
```

### 2. Modificando Produto Existente
```javascript
{
    id: "pro",
    name: "GelaFit Pro",
    description: "Nova descrição do produto...",
    price: "A partir de R$ 12.000",  // Novo preço
    popular: true  // Tornar popular
}
```

## 🎯 Recursos/Serviços

### 1. Adicionando Novo Recurso
```javascript
features: [
    // ... recursos existentes
    {
        title: "IA Integrada",
        description: "Inteligência artificial para otimização automática.",
        icon: "fas fa-brain"
    }
]
```

## 🌐 Redes Sociais

```javascript
socialMedia: {
    facebook: "https://facebook.com/suaempresa",
    instagram: "https://instagram.com/suaempresa",
    linkedin: "https://linkedin.com/company/suaempresa",
    youtube: "https://youtube.com/suaempresa",
    twitter: "https://twitter.com/suaempresa"
}
```

## 🖼️ Imagens

### 1. Logo
- Substitua o arquivo `logo_gelafit.png`
- Mantenha o mesmo nome ou atualize no HTML

### 2. Imagens de Produtos
- Adicione imagens na pasta do projeto
- Atualize os caminhos no HTML

## 📱 Personalização Mobile

O site já é responsivo, mas você pode ajustar:

### 1. Tamanhos de Fonte
No `styles.css`, procure por media queries:

```css
@media (max-width: 768px) {
    .hero-title {
        font-size: 2.5rem;  // Ajuste conforme necessário
    }
}
```

### 2. Espaçamentos
```css
@media (max-width: 768px) {
    .hero-container {
        gap: 2rem;  // Reduzir espaçamento
    }
}
```

## 🔧 Configurações Avançadas

### 1. Desabilitar Animações
```javascript
animations: {
    enableParallax: false,
    enableCounters: false,
    enableScrollAnimations: false,
}
```

### 2. Configurar Formulário
```javascript
form: {
    enableNotifications: true,
    autoRedirect: true,
    redirectUrl: "obrigado.html",
    requiredFields: ["name", "email", "interest"],
}
```

### 3. SEO
```javascript
seo: {
    title: "Seu Título SEO",
    description: "Sua descrição SEO",
    keywords: "palavra1, palavra2, palavra3",
}
```

## 🚀 Dicas de Personalização

1. **Teste Sempre**: Após cada alteração, teste o site
2. **Backup**: Faça backup antes de grandes mudanças
3. **Cores**: Use ferramentas como Adobe Color para combinações
4. **Imagens**: Otimize imagens para web (WebP, JPEG otimizado)
5. **Performance**: Mantenha o site leve e rápido

## 📞 Suporte

Para dúvidas sobre personalização:
- Consulte o arquivo `README.md`
- Verifique o arquivo `config.js` para todas as opções
- Teste as alterações em ambiente local primeiro
