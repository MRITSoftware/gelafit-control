# MRIT Orion - Editor de Extratos Bancários

## 📋 Descrição

O **MRIT Orion** é um sistema web inovador que permite o upload de PDFs de extratos bancários e os converte em tabelas editáveis. Com interface intuitiva e funcionalidades avançadas, você pode facilmente extrair, editar e exportar dados de extratos bancários.

## ✨ Funcionalidades

- **Upload de PDFs**: Interface drag-and-drop para upload fácil de arquivos
- **Extração Automática**: Detecção e extração automática de tabelas de PDFs
- **Edição Inline**: Clique em qualquer célula para editar o conteúdo
- **Interface Responsiva**: Design moderno e adaptável a diferentes dispositivos
- **Exportação**: Exporte os dados editados em formato JSON
- **Visualização Inteligente**: Preview das tabelas com paginação automática

## 🚀 Tecnologias Utilizadas

### Backend
- **Python 3.8+**
- **Flask** - Framework web
- **PyMuPDF (fitz)** - Processamento de PDFs
- **pdfplumber** - Extração de tabelas
- **pandas** - Manipulação de dados
- **Werkzeug** - Utilitários web

### Frontend
- **HTML5** - Estrutura
- **CSS3** - Estilização com animações
- **JavaScript ES6+** - Interatividade
- **Bootstrap 5** - Framework CSS
- **Font Awesome** - Ícones

## 📦 Instalação

### Pré-requisitos
- Python 3.8 ou superior
- pip (gerenciador de pacotes Python)

### Passos de Instalação

1. **Clone ou baixe o projeto**
   ```bash
   cd "D:\Projetos\mrit orion"
   ```

2. **Instale as dependências**
   ```bash
   pip install -r requirements.txt
   ```

3. **Execute o aplicativo**
   ```bash
   python app.py
   ```

4. **Acesse no navegador**
   ```
   http://localhost:5000
   ```

## 🎯 Como Usar

### 1. Upload do PDF
- Arraste e solte seu PDF na área de upload
- Ou clique em "Selecionar Arquivo" para escolher um arquivo
- Aguarde o processamento automático

### 2. Visualização das Tabelas
- O sistema extrai automaticamente todas as tabelas do PDF
- Cada tabela é exibida em um card separado
- Visualize as primeiras 5 linhas de cada tabela

### 3. Edição dos Dados
- Clique no botão "Editar" da tabela desejada
- Clique em qualquer célula para editá-la
- Pressione Enter ou clique fora para salvar
- Use o botão "Salvar Alterações" para confirmar

### 4. Exportação
- Clique em "Exportar" para baixar os dados editados
- Os dados são exportados em formato JSON
- Mantenha a estrutura original das tabelas

## 🔧 Configuração Avançada

### Limites de Upload
- Tamanho máximo: 16MB por arquivo
- Formatos aceitos: PDF apenas

### Personalização
- Modifique `static/css/style.css` para alterar o visual
- Ajuste `static/js/app.js` para funcionalidades adicionais
- Configure `app.py` para mudanças no backend

## 📁 Estrutura do Projeto

```
mrit-orion/
├── app.py                 # Aplicação Flask principal
├── requirements.txt       # Dependências Python
├── README.md             # Documentação
├── templates/
│   └── index.html        # Template HTML principal
├── static/
│   ├── css/
│   │   └── style.css     # Estilos personalizados
│   └── js/
│       └── app.js        # JavaScript da aplicação
└── uploads/              # Diretório temporário (criado automaticamente)
```

## 🐛 Solução de Problemas

### Erro de Dependências
```bash
pip install --upgrade pip
pip install -r requirements.txt --force-reinstall
```

### Erro de Permissão
- Certifique-se de que o Python tem permissão para criar arquivos
- Execute como administrador se necessário

### PDF não Processado
- Verifique se o PDF contém tabelas legíveis
- Tente com PDFs de diferentes bancos
- Certifique-se de que o arquivo não está corrompido

## 🔮 Funcionalidades Futuras

- [ ] Suporte a múltiplos formatos de exportação (Excel, CSV)
- [ ] Validação automática de dados bancários
- [ ] Integração com APIs bancárias
- [ ] Histórico de processamentos
- [ ] Autenticação de usuários
- [ ] Processamento em lote

## 📄 Licença

Este projeto é de uso livre para fins educacionais e comerciais.

## 🤝 Contribuição

Contribuições são bem-vindas! Sinta-se à vontade para:
- Reportar bugs
- Sugerir melhorias
- Enviar pull requests
- Compartilhar feedback

## 📞 Suporte

Para dúvidas ou suporte, entre em contato através dos issues do projeto.

---

**MRIT Orion** - Transformando extratos bancários em dados editáveis! 🚀
