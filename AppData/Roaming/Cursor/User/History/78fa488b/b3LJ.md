# 📊 Sistema de Analytics para Landing Page

Este sistema de analytics foi desenvolvido para fornecer tracking completo de campanhas de tráfego pago e acompanhamento de resultados em tempo real.

## 🚀 Funcionalidades Implementadas

### 1. **Google Analytics 4 (GA4)**
- Tracking de visualizações de página
- Eventos personalizados
- Parâmetros de campanha (UTM)
- Conversões e objetivos

### 2. **Google Tag Manager (GTM)**
- Gerenciamento centralizado de tags
- DataLayer para eventos customizados
- Flexibilidade para adicionar novas tags

### 3. **Facebook Pixel**
- Tracking de visualizações
- Eventos de conversão (Lead)
- Otimização para campanhas no Facebook/Instagram

### 4. **Google Ads Conversion Tracking**
- Tracking de conversões para campanhas pagas
- Valores de conversão
- Rastreamento de ROI

### 5. **Sistema de Analytics Personalizado**
- Métricas em tempo real
- Tracking de scroll depth
- Tempo na página
- Cliques em CTAs
- Interações com vídeos
- Cliques no WhatsApp

### 6. **Dashboard de Métricas**
- Visualização em tempo real
- Gráficos interativos
- Exportação de dados
- Tabela de eventos recentes

## 📋 Configuração

### 1. **Configurar IDs de Tracking**

Edite o arquivo `js/config.js` e substitua os valores pelos seus IDs reais:

```javascript
const ANALYTICS_CONFIG = {
    GA_MEASUREMENT_ID: 'G-XXXXXXXXXX', // Seu ID do GA4
    GTM_CONTAINER_ID: 'GTM-XXXXXXX',   // Seu ID do GTM
    FACEBOOK_PIXEL_ID: '1234567890',   // Seu ID do Pixel
    GOOGLE_ADS_CONVERSION_ID: 'AW-123456789',
    GOOGLE_ADS_CONVERSION_LABEL: 'abc123'
};
```

### 2. **Google Analytics 4**

1. Crie uma propriedade no GA4
2. Obtenha o Measurement ID
3. Substitua `GA_MEASUREMENT_ID` no arquivo `index.html` e `js/config.js`

### 3. **Google Tag Manager**

1. Crie um container no GTM
2. Obtenha o Container ID
3. Substitua `GTM-XXXXXXX` nos arquivos

### 4. **Facebook Pixel**

1. Crie um pixel no Facebook Business Manager
2. Obtenha o Pixel ID
3. Substitua `YOUR_PIXEL_ID` nos arquivos

### 5. **Google Ads Conversion**

1. Crie uma ação de conversão no Google Ads
2. Obtenha o Conversion ID e Label
3. Substitua os valores no arquivo `js/config.js`

## 📊 Métricas Rastreadas

### **Métricas Básicas**
- **Visualizações de Página**: Total de acessos à landing page
- **Visitantes Únicos**: Usuários únicos (baseado em sessão)
- **Taxa de Rejeição**: Percentual de usuários que saem rapidamente
- **Tempo na Página**: Tempo médio de permanência

### **Métricas de Engajamento**
- **Profundidade de Scroll**: Percentual de scroll atingido
- **Cliques em CTAs**: Interações com botões de ação
- **Cliques no WhatsApp**: Interações com botão do WhatsApp
- **Interações com Vídeos**: Cliques nos vídeos da página

### **Métricas de Conversão**
- **Formulários Enviados**: Total de leads gerados
- **Taxa de Conversão**: (Conversões / Visualizações) × 100
- **Custo por Lead**: Investimento / Leads gerados
- **ROI**: Retorno sobre investimento

### **Métricas de Campanha**
- **Fontes de Tráfego**: Origem do tráfego (Google, Facebook, etc.)
- **Canais de Mídia**: Tipo de canal (orgânico, pago, social)
- **Campanhas**: Performance por campanha específica
- **Conteúdo**: Performance por anúncio/conteúdo

## 🎯 Eventos Rastreados

### **Eventos Automáticos**
- `page_view`: Visualização da página
- `scroll_depth`: Profundidade de scroll (25%, 50%, 75%, 90%, 100%)
- `time_on_page`: Tempo gasto na página
- `page_exit`: Saída da página

### **Eventos de Interação**
- `cta_click`: Clique em botões de ação
- `whatsapp_click`: Clique no botão do WhatsApp
- `video_click`: Clique em vídeos
- `form_submission`: Envio de formulário

### **Eventos de Conversão**
- `lead_generated`: Lead gerado via formulário
- `whatsapp_contact`: Contato via WhatsApp
- `conversion`: Conversão geral

## 📈 Dashboard de Métricas

### **Acessar o Dashboard**
Abra o arquivo `dashboard.html` no navegador para visualizar as métricas em tempo real.

### **Funcionalidades do Dashboard**
- **Cards de Métricas**: Visualização rápida dos principais KPIs
- **Gráficos Interativos**: Fontes de tráfego e tipos de dispositivo
- **Tabela de Eventos**: Histórico de eventos recentes
- **Exportação de Dados**: Download dos dados em JSON

### **Atualização de Dados**
- Os dados são atualizados automaticamente
- Use o botão "Atualizar" para forçar uma atualização
- Os dados são salvos no localStorage do navegador

## 🔧 Personalização

### **Adicionar Novos Eventos**
```javascript
// No arquivo js/analytics.js
trackEvent('novo_evento', {
    event_category: 'categoria',
    event_label: 'label',
    custom_parameter: 'valor'
});
```

### **Configurar Novos CTAs**
```html
<button class="cta-button" data-tracking="novo_cta">
    Novo CTA
</button>
```

### **Adicionar Novas Métricas**
```javascript
// No arquivo js/analytics.js
this.metrics.novaMetrica = 0;

// Para rastrear
this.metrics.novaMetrica++;
this.trackEvent('nova_metrica', { valor: this.metrics.novaMetrica });
```

## 📱 Uso com Campanhas

### **UTM Parameters**
O sistema rastreia automaticamente os seguintes parâmetros UTM:
- `utm_source`: Fonte do tráfego (google, facebook, etc.)
- `utm_medium`: Meio (cpc, social, email, etc.)
- `utm_campaign`: Nome da campanha
- `utm_content`: Conteúdo específico do anúncio
- `utm_term`: Palavra-chave (para campanhas de busca)

### **Exemplo de URL com UTM**
```
https://seusite.com/landing?utm_source=google&utm_medium=cpc&utm_campaign=eventos_audiovisual&utm_content=banner_principal&utm_term=locacao_equipamentos
```

### **Rastreamento de Conversões**
1. Configure as conversões no Google Ads
2. Configure os eventos no Facebook Ads Manager
3. Monitore o ROI no dashboard personalizado

## 🚨 Troubleshooting

### **Problemas Comuns**

1. **Dados não aparecem no dashboard**
   - Verifique se o localStorage está habilitado
   - Confirme se os scripts estão carregando corretamente

2. **Eventos não são rastreados**
   - Verifique o console do navegador para erros
   - Confirme se os IDs de tracking estão corretos

3. **Conversões não aparecem no Google Ads**
   - Verifique se o Conversion ID e Label estão corretos
   - Confirme se o evento de conversão está sendo disparado

### **Debug Mode**
Para ativar o modo debug, adicione `?debug=true` à URL:
```
https://seusite.com/landing?debug=true
```

## 📞 Suporte

Para dúvidas ou problemas:
1. Verifique o console do navegador para erros
2. Confirme se todos os IDs estão configurados corretamente
3. Teste em modo incógnito para verificar se não há cache

## 🔄 Atualizações

### **Versão 1.0**
- Sistema básico de analytics
- Integração com GA4, GTM, Facebook Pixel
- Dashboard de métricas
- Tracking de conversões

### **Próximas Versões**
- Integração com LinkedIn Pixel
- Tracking de heatmaps
- A/B testing
- Relatórios automáticos por email
