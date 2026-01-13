# Configuração Local - MR Food

## 🚀 Início Rápido

### 1. Instalar Dependências

```bash
npm install
```

### 2. Inicializar Banco de Dados

Execute um dos comandos:

```bash
npm run db:init
```

Ou acesse após iniciar o servidor:
```
http://localhost:3000/api/init-db
```

### 3. Iniciar Servidor

```bash
npm run dev
```

### 4. Acessar a Aplicação

Abra [http://localhost:3000](http://localhost:3000)

## 📊 Banco de Dados

O banco de dados SQLite será criado automaticamente na pasta `data/mr-food.db`.

### Estrutura

- **Restaurantes**: Informações do restaurante
- **Usuários**: Staff do restaurante com autenticação
- **Clientes**: Base de clientes com histórico
- **Cardápio**: Itens do menu
- **Pedidos**: Todos os pedidos
- **Entregadores**: Cadastro de entregadores
- **Notificações**: Sistema de notificações

## 🔐 Primeiro Acesso

1. Acesse `/register`
2. Preencha os dados do restaurante
3. Faça login com suas credenciais
4. Comece a usar!

## 🛠️ Comandos Úteis

- `npm run dev` - Iniciar servidor de desenvolvimento
- `npm run build` - Build para produção
- `npm run db:init` - Inicializar banco de dados
- `npm run lint` - Verificar código

## 📝 Notas

- O banco de dados é local e fica na pasta `data/`
- Todos os dados são armazenados no seu computador
- Para produção, considere migrar para Supabase ou outro banco

## 🔄 Migração Futura

O código foi estruturado para facilitar migração para Supabase:
- Substitua os clientes em `lib/supabase/` pelos clientes reais
- Execute o script SQL em `supabase-setup.sql`
- Configure as variáveis de ambiente


