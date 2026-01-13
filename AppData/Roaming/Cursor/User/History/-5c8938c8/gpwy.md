# Sistema de Assinatura Digital de PDF

Sistema web completo para assinatura digital de documentos PDF com validação de integridade.

## 🚀 Funcionalidades

- ✅ Upload de arquivos PDF
- ✅ Visualização de PDFs no navegador
- ✅ Criação de assinatura digital (canvas de desenho)
- ✅ Assinatura de PDFs com imagem da assinatura
- ✅ Adição automática de data/hora e hash SHA-256
- ✅ Verificação de integridade do documento
- ✅ Download do PDF assinado

## 🛠️ Tecnologias Utilizadas

- **Next.js 14** - Framework React
- **TypeScript** - Tipagem estática
- **pdf-lib** - Manipulação de PDFs
- **react-signature-canvas** - Canvas para desenhar assinaturas
- **crypto-js** - Geração de hash SHA-256 para verificação de integridade
- **Tailwind CSS** - Estilização

## 📦 Instalação

1. Instale as dependências:
```bash
npm install
```

2. Execute o servidor de desenvolvimento:
```bash
npm run dev
```

3. Acesse [http://localhost:3000](http://localhost:3000)

## 📝 Como Usar

1. **Carregar PDF**: Clique na área de upload e selecione um arquivo PDF
2. **Visualizar PDF**: O documento será exibido automaticamente
3. **Criar Assinatura**: Clique em "Criar Assinatura" e desenhe sua assinatura no canvas
4. **Assinar PDF**: Clique em "Assinar PDF" para aplicar a assinatura digital
5. **Download**: Baixe o PDF assinado com todas as informações de verificação

## 🔒 Segurança

O sistema gera um hash SHA-256 do documento assinado que é:
- Inserido no próprio PDF na última página
- Usado para verificar a integridade do documento
- Impossível de alterar sem quebrar a verificação

## 📄 Licença

MIT

## 🔮 Próximas Melhorias

- [ ] Integração com certificados digitais (PKI)
- [ ] Armazenamento de assinaturas no Supabase
- [ ] Histórico de documentos assinados
- [ ] Assinatura de múltiplas pessoas
- [ ] Timestamping com servidor de tempo confiável
