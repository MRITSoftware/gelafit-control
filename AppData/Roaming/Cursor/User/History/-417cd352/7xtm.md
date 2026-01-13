# 📖 Instruções de Uso - Extrator de Tabelas PDF

## 🎯 O que o Sistema Faz

Este sistema web extrai automaticamente **todas as tabelas** de PDFs de extratos bancários e permite:

- ✅ **Upload fácil**: Arraste e solte ou clique para selecionar
- ✅ **Extração automática**: Reconhece tabelas usando 3 métodos diferentes
- ✅ **Visualização**: Veja as tabelas exatamente como estão no PDF
- ✅ **Validação inteligente**: Verifica se a extração foi bem-sucedida
- ✅ **Exportação Excel**: Baixe todas as tabelas em um arquivo Excel
- ✅ **Interface moderna**: Design responsivo e intuitivo

## 🚀 Como Usar

### 1. Acesse o Site
- Abra seu navegador
- Vá para: `https://seu-dominio.com`
- A interface carregará automaticamente

### 2. Faça Upload do PDF
- **Opção A**: Arraste o PDF para a área destacada
- **Opção B**: Clique em "Selecionar Arquivo PDF"
- Aguarde o processamento (pode levar alguns minutos)

### 3. Visualize as Tabelas
- O sistema mostrará todas as tabelas encontradas
- Cada tabela terá:
  - Número de identificação
  - Dimensões (linhas × colunas)
  - Método de extração usado
  - Preview das primeiras 5 linhas

### 4. Valide a Qualidade
- Clique em "Validar Tabelas"
- O sistema verificará:
  - Se as tabelas não estão vazias
  - Se têm colunas suficientes
  - Se os dados fazem sentido
  - Fornecerá recomendações

### 5. Exporte para Excel
- Clique em "Exportar para Excel"
- O arquivo será baixado automaticamente
- Cada tabela será uma planilha separada

## 📊 Tipos de Tabelas Suportadas

### ✅ Tabelas que Funcionam Bem
- **Extratos bancários**: Movimentações, saldos, resumos
- **Faturas**: Itens, valores, totais
- **Relatórios financeiros**: Balanços, demonstrações
- **Tabelas com bordas**: Linhas e colunas bem definidas
- **Tabelas sem bordas**: Dados organizados em colunas

### ⚠️ Tabelas que Podem Ter Problemas
- **Tabelas muito pequenas**: Menos de 2 colunas
- **Tabelas com imagens**: Dados em formato de imagem
- **Tabelas complexas**: Múltiplas tabelas sobrepostas
- **PDFs escaneados**: Imagens de baixa qualidade

## 🔧 Configurações Avançadas

### Limites do Sistema
- **Tamanho máximo**: 16MB por PDF
- **Páginas processadas**: Até 10 páginas
- **Tempo limite**: 5 minutos por processamento
- **Formatos aceitos**: Apenas PDF

### Métodos de Extração
1. **Lattice**: Para tabelas com bordas visíveis
2. **Stream**: Para tabelas sem bordas
3. **Por Páginas**: Análise individual de cada página

## 🚨 Solução de Problemas

### "Nenhuma tabela encontrada"
**Possíveis causas:**
- PDF não contém tabelas
- Tabelas são muito pequenas
- PDF é uma imagem escaneada

**Soluções:**
- Verifique se o PDF tem tabelas visíveis
- Tente com um PDF diferente
- Use PDFs com tabelas maiores

### "Erro ao processar PDF"
**Possíveis causas:**
- PDF corrompido
- Arquivo muito grande
- Problema no servidor

**Soluções:**
- Tente com outro PDF
- Reduza o tamanho do arquivo
- Aguarde alguns minutos e tente novamente

### "Tabelas com dados incorretos"
**Possíveis causas:**
- PDF com formatação complexa
- Tabelas sobrepostas
- Qualidade baixa do PDF

**Soluções:**
- Use a validação para verificar problemas
- Tente com PDFs de melhor qualidade
- Verifique se as tabelas estão bem formatadas

### "Exportação Excel não funciona"
**Possíveis causas:**
- Navegador bloqueando downloads
- Problema de permissões
- Arquivo muito grande

**Soluções:**
- Verifique as configurações do navegador
- Tente com outro navegador
- Reduza o tamanho do PDF

## 💡 Dicas para Melhores Resultados

### 1. Prepare o PDF
- Use PDFs de boa qualidade
- Evite PDFs escaneados quando possível
- Certifique-se de que as tabelas estão bem formatadas

### 2. Tabelas Ideais
- **Bordas claras**: Linhas e colunas bem definidas
- **Dados organizados**: Informações em colunas regulares
- **Tamanho adequado**: Pelo menos 2 colunas e algumas linhas
- **Texto legível**: Fonte clara e tamanho adequado

### 3. Verificação
- Sempre use a validação antes de exportar
- Verifique se os dados fazem sentido
- Compare com o PDF original

## 📞 Suporte Técnico

### Informações para Suporte
Quando solicitar ajuda, forneça:
1. **Tipo de erro**: Mensagem exata
2. **Arquivo testado**: Tamanho e tipo do PDF
3. **Navegador**: Chrome, Firefox, Safari, etc.
4. **Sistema**: Windows, Mac, Linux
5. **Screenshot**: Se possível

### Logs do Sistema
O sistema registra automaticamente:
- PDFs processados
- Tabelas extraídas
- Erros encontrados
- Tempo de processamento

### Contato
- **Email**: suporte@seu-dominio.com
- **Documentação**: README.md
- **Issues**: GitHub Issues

## 🔄 Atualizações

### Versão Atual: 1.0.0
- Extração automática de tabelas
- Validação inteligente
- Exportação para Excel
- Interface responsiva
- Compatibilidade com hospedagem

### Próximas Versões
- Suporte a mais formatos de PDF
- Melhor detecção de tabelas complexas
- Exportação para outros formatos
- API para integração

## 📋 Checklist de Uso

Antes de usar o sistema, verifique:
- [ ] PDF é válido e não corrompido
- [ ] Tamanho menor que 16MB
- [ ] Contém tabelas visíveis
- [ ] Conexão com internet estável
- [ ] Navegador atualizado

Durante o uso:
- [ ] Aguarde o processamento completo
- [ ] Use a validação para verificar qualidade
- [ ] Verifique os dados antes de exportar
- [ ] Baixe o Excel quando satisfeito

Após o uso:
- [ ] Verifique se o Excel foi baixado
- [ ] Teste a abertura do arquivo Excel
- [ ] Compare com o PDF original
- [ ] Salve o arquivo em local seguro
