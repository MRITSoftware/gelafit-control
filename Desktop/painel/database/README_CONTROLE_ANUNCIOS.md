# 📢 Módulo de Controle de Anúncios

Sistema completo para gerenciar anúncios comerciais com controle de clientes, informações financeiras e exibição em Digital Signage.

## 📋 Visão Geral

Este módulo permite:
- Cadastrar clientes anunciantes (PF/PJ)
- Criar anúncios vinculados a clientes
- Gerenciar informações financeiras (valores, vencimentos, pagamentos)
- Controlar onde e como os anúncios são exibidos
- Remover automaticamente anúncios vencidos/cancelados das playlists
- Rastrear histórico completo de alterações

## 🗄️ Estrutura do Banco de Dados

### Tabelas Criadas

1. **`clientes`** - Cadastro de clientes anunciantes
   - Informações pessoais/empresariais
   - CPF/CNPJ único
   - Segmento de atuação
   - Contatos e endereço

2. **`anuncios`** - Anúncios comerciais
   - Vinculado a um cliente
   - Tipo de mídia (imagem/vídeo)
   - Período de exibição
   - Status (ativo, pausado, encerrado, cancelado)

3. **`anuncios_financeiro`** - Informações financeiras
   - Valor e tipo de cobrança
   - Data de vencimento
   - Status financeiro (pago, em aberto, atrasado)
   - Forma de pagamento

4. **`anuncios_exibicao`** - Onde o anúncio está sendo exibido
   - Playlist vinculada
   - Unidade/local
   - Horários e frequência
   - Duração do anúncio

5. **`anuncios_historico`** - Histórico de alterações
   - Todas as mudanças registradas
   - Tipo de alteração
   - Usuário responsável
   - Timestamp

## 🚀 Instalação

### 1. Executar Scripts SQL

Execute os scripts na seguinte ordem no Supabase SQL Editor:

```sql
-- 1. Criar estrutura do banco
\i database/create_controle_anuncios.sql

-- 2. Criar funções de cron (opcional, para verificação periódica)
\i database/controle_anuncios_cron.sql
```

### 2. Configurar Verificação Periódica (Opcional)

Para remover automaticamente anúncios vencidos, configure um cron job ou Edge Function no Supabase:

**Opção 1: Via Supabase Cron (PostgreSQL)**
```sql
-- Executar diariamente às 00:00
SELECT cron.schedule(
  'verificar-anuncios-vencidos',
  '0 0 * * *',
  $$SELECT * FROM verificar_anuncios_vencidos_periodicamente();$$
);
```

**Opção 2: Via Edge Function**
Crie uma Edge Function que chame a função periodicamente.

## 🔄 Funcionalidades Principais

### Remoção Automática de Playlists

Quando um anúncio é:
- **Vencido** (data_termino < hoje)
- **Cancelado** (status = 'cancelado')
- **Encerrado** (status = 'encerrado')

O sistema **automaticamente remove** o anúncio de todas as playlists onde está sendo exibido.

**Como funciona:**
1. Trigger `trigger_remover_anuncio_vencido` monitora mudanças na tabela `anuncios`
2. Quando detecta vencimento/cancelamento, chama `remover_anuncios_vencidos_cancelados()`
3. A função remove todos os itens com o `codigo_anuncio` correspondente das playlists
4. Registra a ação no histórico

### Status do Anúncio

- **ativo**: Anúncio em exibição normal
- **pausado**: Temporariamente pausado (não remove das playlists)
- **encerrado**: Período de exibição terminou (remove das playlists)
- **cancelado**: Cancelado antes do término (remove das playlists)

### Status Financeiro

- **pago**: Pagamento recebido
- **em_aberto**: Aguardando pagamento
- **atrasado**: Vencido e não pago (atualizado automaticamente)
- **cancelado**: Cancelado

## 📊 Relacionamentos

```
clientes (1) ──< (N) anuncios
anuncios (1) ──< (1) anuncios_financeiro
anuncios (1) ──< (N) anuncios_exibicao
anuncios (1) ──< (N) anuncios_historico
```

## 🔐 Segurança (RLS)

Todas as tabelas têm Row Level Security (RLS) habilitado:
- Usuários só veem seus próprios dados
- Baseado no campo `pertence` (email do usuário)
- Políticas de SELECT, INSERT, UPDATE, DELETE configuradas

## 📝 Uso no Frontend

### Acessar o Módulo

1. Faça login no sistema
2. Navegue até **"Controle de Anúncios"** no menu
3. Ou acesse diretamente: `/dashboard/controle-anuncios`

### Criar um Cliente

1. Clique em **"+ Novo Cliente"**
2. Preencha os dados obrigatórios:
   - Nome/Razão Social
   - CPF/CNPJ
   - Tipo de Pessoa (PF/PJ)
3. Opcional: Email, telefone, segmento, endereço
4. Clique em **"Criar Cliente"**

### Criar um Anúncio

1. Clique em **"+ Novo Anúncio"**
2. Preencha informações básicas:
   - Nome do anúncio
   - Cliente (selecione da lista)
   - Tipo de mídia
   - Código do anúncio (vinculado aos conteúdos existentes)
   - Período de exibição
3. Opcional: Informações financeiras
4. Clique em **"Criar Anúncio"**

### Visualizar Detalhes

1. Clique no ícone 👁️ em qualquer anúncio
2. Visualize:
   - Informações completas
   - Dados financeiros
   - Onde está sendo exibido
   - Histórico de alterações

### Alterar Status

No modal de detalhes, use os botões:
- **Ativar**: Marca como ativo
- **Pausar**: Pausa temporariamente
- **Encerrar**: Finaliza o período
- **Cancelar**: Cancela e remove das playlists

## 🔍 Filtros e Busca

- **Status**: Filtrar por status do anúncio
- **Cliente**: Filtrar por cliente específico
- **Busca**: Buscar por nome, código ou cliente

## ⚠️ Validações Importantes

1. **CPF/CNPJ único**: Não permite duplicatas
2. **Data de término**: Deve ser posterior à data de início
3. **Código do anúncio**: Deve existir na tabela `conteudos`
4. **Cliente ativo**: Só permite selecionar clientes ativos

## 🎯 Boas Práticas

1. **Sempre vincule a um cliente**: Facilita rastreamento e gestão
2. **Preencha informações financeiras**: Importante para controle
3. **Use tags**: Organize anúncios por categorias
4. **Monitore histórico**: Acompanhe todas as alterações
5. **Configure exibição**: Defina onde e como o anúncio será exibido

## 🔧 Manutenção

### Verificar Anúncios Vencidos Manualmente

```sql
SELECT * FROM verificar_anuncios_vencidos_periodicamente();
```

### Atualizar Status Financeiro

```sql
SELECT atualizar_status_financeiro();
```

### Ver Anúncios com Informações Completas

```sql
SELECT * FROM vw_anuncios_completos;
```

## 📈 Escalabilidade

O sistema foi projetado para:
- Múltiplos usuários (isolamento por RLS)
- Múltiplas unidades/locais
- Múltiplas playlists por anúncio
- Histórico completo de alterações
- Performance otimizada com índices

## 🐛 Troubleshooting

### Anúncio não está sendo removido da playlist

1. Verifique se o status está como 'encerrado' ou 'cancelado'
2. Verifique se a data_termino já passou
3. Execute manualmente: `SELECT * FROM verificar_anuncios_vencidos_periodicamente();`
4. Verifique os logs do histórico

### Erro ao criar cliente

1. Verifique se o CPF/CNPJ já existe
2. Verifique se todos os campos obrigatórios estão preenchidos
3. Verifique permissões RLS

### Anúncio não aparece na lista

1. Verifique filtros aplicados
2. Verifique se o anúncio pertence ao usuário logado
3. Verifique se o cliente está ativo

## 📞 Suporte

Para dúvidas ou problemas, entre em contato pelo suporte do sistema.

---

**Versão**: 1.0  
**Data**: 2024  
**Autor**: Sistema MRIT Vision
