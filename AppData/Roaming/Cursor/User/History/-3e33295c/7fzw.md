# 🎯 Funcionalidade de Seleção Manual de Região

## Visão Geral

O sistema MRIT Orion agora oferece duas opções para extrair tabelas de PDFs:

1. **Extração Automática** 🤖 - O sistema tenta encontrar tabelas automaticamente
2. **Seleção Manual** 🎯 - O usuário visualiza o PDF e seleciona manualmente a região da tabela

## Como Usar a Seleção Manual

### Passo 1: Upload do PDF
- Faça upload do seu arquivo PDF normalmente
- Após o upload, você verá duas opções de extração

### Passo 2: Escolher Seleção Manual
- Clique em "Selecionar Manualmente" 
- Uma nova janela será aberta com o visualizador de PDF

### Passo 3: Navegar pelas Páginas
- Use os botões "Página Anterior" e "Próxima Página" para navegar
- O sistema mostra quantas páginas o PDF possui

### Passo 4: Selecionar a Região da Tabela
- Clique e arraste sobre a região da tabela que deseja extrair
- Uma caixa de seleção amarela aparecerá mostrando a área selecionada
- As coordenadas da seleção serão exibidas na parte inferior

### Passo 5: Extrair a Tabela
- Clique em "Extrair Tabela da Região Selecionada"
- O sistema processará apenas a região selecionada
- Os resultados serão exibidos na página principal

## Recursos da Interface

### Navegação
- **Página Anterior/Próxima**: Navegue entre as páginas do PDF
- **Informações da Página**: Mostra "Página X de Y"

### Seleção
- **Clique e Arraste**: Selecione a região da tabela
- **Visualização em Tempo Real**: Veja a área selecionada enquanto arrasta
- **Coordenadas**: Visualize as coordenadas exatas da seleção

### Controles
- **Limpar Seleção**: Remove a seleção atual
- **Extrair Tabela**: Processa a região selecionada
- **Voltar**: Retorna à página principal

## Vantagens da Seleção Manual

1. **Precisão**: Você escolhe exatamente qual parte do PDF processar
2. **Controle**: Evita processar áreas desnecessárias
3. **Flexibilidade**: Funciona com PDFs complexos ou mal formatados
4. **Visualização**: Você vê exatamente o que está sendo processado

## Casos de Uso Ideais

- PDFs com múltiplas tabelas onde você quer apenas uma específica
- Tabelas em posições não convencionais
- PDFs com formatação complexa que confunde a detecção automática
- Quando a extração automática não encontra a tabela desejada

## Dicas de Uso

1. **Selecione com Precisão**: Inclua apenas a tabela, evitando cabeçalhos ou rodapés desnecessários
2. **Teste Diferentes Regiões**: Se não funcionar, tente selecionar uma região ligeiramente diferente
3. **Use o Zoom do Navegador**: Para PDFs pequenos, use Ctrl+Scroll para ampliar
4. **Verifique as Coordenadas**: As coordenadas mostram exatamente o que foi selecionado

## Suporte Técnico

O sistema converte automaticamente as coordenadas da tela para coordenadas do PDF, garantindo que a extração seja precisa independentemente do tamanho da tela ou resolução.
