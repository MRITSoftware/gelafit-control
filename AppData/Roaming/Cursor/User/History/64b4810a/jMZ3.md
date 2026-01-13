# Configuração de Feeds RSS - Rodapé de Notícias

## Visão Geral

O sistema de rodapé de notícias foi implementado para exibir informações em tempo real na parte inferior da tela, dividido em duas seções:

1. **Lado Esquerdo**: Informação primária (dólar, euro, etc.)
2. **Lado Direito**: Scroll automático de notícias RSS

## Como Configurar

### 1. Configuração no Banco de Dados

Na tabela `displays`, configure a coluna `feed` como um array de URLs RSS:

```sql
-- Exemplo de configuração
UPDATE displays 
SET feed = ARRAY[
  'https://g1.globo.com/rss/g1/',
  'https://www.uol.com.br/rss/',
  'https://www.estadao.com.br/rss/',
  'https://www.folha.uol.com.br/rss/'
]
WHERE codigo_unico = 'SEU_CODIGO_TELA';
```

### 2. URLs de Feeds RSS Recomendados

#### Notícias Nacionais
- G1: `https://g1.globo.com/rss/g1/`
- UOL: `https://www.uol.com.br/rss/`
- Estadão: `https://www.estadao.com.br/rss/`
- Folha: `https://www.folha.uol.com.br/rss/`
- Veja: `https://veja.abril.com.br/feed/`

#### Notícias Internacionais
- BBC Brasil: `http://feeds.bbci.co.uk/portuguese/rss.xml`
- CNN Brasil: `https://www.cnnbrasil.com.br/rss/`

#### Economia/Finanças
- InfoMoney: `https://www.infomoney.com.br/feed/`
- Valor Econômico: `https://valor.globo.com/rss/`

### 3. Funcionalidades Implementadas

#### Informação Primária (Lado Esquerdo)
- **Dados de Câmbio**: USD e EUR em tempo real
- **Atualização**: A cada 5 minutos
- **Fallback**: Valores padrão em caso de erro

#### Scroll de Notícias (Lado Direito)
- **Scroll Automático**: Da direita para esquerda
- **Velocidade**: 30 segundos por ciclo completo
- **Pausa no Hover**: Para leitura
- **Separadores**: Bullets entre notícias
- **Limite**: 10 notícias por feed

### 4. Características Visuais

- **Transparência**: `rgba(0, 0, 0, 0.7)` com blur
- **Altura**: 60px (50px em mobile)
- **Z-index**: 2 (acima do vídeo)
- **Responsivo**: Adapta-se a diferentes telas
- **Fonte**: Arial, tamanhos otimizados

### 5. Controle de Exibição

O rodapé de notícias:
- ✅ **Aparece** quando há feeds configurados
- ✅ **Oculta** na tela de login
- ✅ **Atualiza** automaticamente a cada 5 minutos
- ✅ **Para** quando o player é encerrado

### 6. Exemplo de Configuração Completa

```sql
-- Configurar uma tela com feeds de notícias
UPDATE displays 
SET feed = ARRAY[
  'https://g1.globo.com/rss/g1/',
  'https://www.uol.com.br/rss/',
  'https://www.estadao.com.br/rss/'
]
WHERE codigo_unico = 'TELA001';

-- Verificar configuração
SELECT codigo_unico, feed 
FROM displays 
WHERE codigo_unico = 'TELA001';
```

### 7. Troubleshooting

#### Problemas Comuns

1. **Rodapé não aparece**
   - Verifique se a coluna `feed` tem URLs válidas
   - Confirme se o array não está vazio
   - Verifique o console para erros de CORS

2. **Notícias não carregam**
   - URLs RSS podem estar indisponíveis
   - Problemas de CORS (usamos proxy)
   - Verifique se as URLs estão corretas

3. **Performance**
   - Muitos feeds podem causar lentidão
   - Recomendado: máximo 5 feeds por tela
   - Atualização a cada 5 minutos

### 8. Personalização

Para personalizar o rodapé, edite o CSS em `index.html`:

```css
.news-footer {
  height: 60px; /* Altura do rodapé */
  background: rgba(0, 0, 0, 0.7); /* Cor de fundo */
}

.news-primary-info {
  flex: 0 0 200px; /* Largura da seção primária */
}

.news-scroll {
  animation: scrollNews 30s linear infinite; /* Velocidade do scroll */
}
```

## Status da Implementação

✅ **Concluído**:
- Consulta da coluna `feed` da tabela `displays`
- HTML para rodapé com duas seções
- CSS responsivo com scroll automático
- Funções JavaScript para processar RSS
- Integração com sistema do player
- Controle de exibição/ocultação
- Atualização automática de dados
- Limpeza ao encerrar player

🎯 **Pronto para uso** - Configure os feeds na tabela `displays` e o sistema funcionará automaticamente!
