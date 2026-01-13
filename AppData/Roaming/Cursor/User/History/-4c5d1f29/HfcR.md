# Painel Web GelaFit

Painel administrativo moderno e responsivo para gestão do sistema GelaFit.

## Características

- ✅ Dashboard com KPIs de vendas
- ✅ Gerenciamento de vendas com visualização detalhada
- ✅ Controle de estoque de produtos
- ✅ Gerenciamento de clientes
- ✅ Design moderno e responsivo
- ✅ Integração com Supabase

## Tecnologias

- **Next.js 14** - Framework React
- **TypeScript** - Tipagem estática
- **Tailwind CSS** - Estilização
- **Supabase** - Banco de dados
- **date-fns** - Manipulação de datas
- **lucide-react** - Ícones

## Instalação

1. Instale as dependências:
```bash
npm install
```

2. O projeto já está configurado com as credenciais do Supabase.

3. Execute o servidor de desenvolvimento:
```bash
npm run dev
```

4. Acesse [http://localhost:3000](http://localhost:3000)

## Estrutura do Projeto

```
├── app/
│   ├── dashboard/     # Página de visão geral com KPIs
│   ├── vendas/        # Página de vendas
│   ├── produtos/      # Página de produtos/estoque
│   ├── clientes/      # Página de clientes
│   ├── layout.tsx     # Layout principal
│   └── globals.css    # Estilos globais
├── components/
│   └── Layout.tsx     # Componente de layout com navegação
├── lib/
│   └── supabase/
│       └── client.ts  # Cliente Supabase
└── types/
    └── database.ts    # Tipos TypeScript
```

## Funcionalidades

### Dashboard
- Faturamento do dia
- Total de vendas ontem
- Total de vendas no mês atual
- Total de vendas no mês passado
- Total de vendas no ano

### Vendas
- Lista de vendas recentes
- Filtro por unidade
- Visualização detalhada de cada venda
- Opção de ocultar/mostrar valores

### Produtos
- Lista de produtos por unidade
- Visualização de preço e quantidade
- Produtos em destaque
- Produtos com estoque zero destacados

### Clientes
- Lista completa de clientes
- Busca por nome, CPF, celular ou unidade
- Informações de status e cupons

## Build para Produção

```bash
npm run build
npm start
```

## Licença

© MRIT

