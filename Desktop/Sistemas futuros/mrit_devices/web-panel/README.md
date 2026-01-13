# Painel Web de Gerenciamento de Tablets

Este é o painel web para gerenciamento remoto de tablets Android em modo quiosque.

## Funcionalidades

- Dashboard com status dos tablets
- Configuração de aplicativos por tablet
- Envio de comandos remotos
- Monitoramento de status
- Integração com Supabase

## Requisitos

- Node.js 16+
- PostgreSQL/Supabase
- Navegador web moderno

## Instalação

1. Clone o repositório
2. Instale as dependências do backend:
```bash
cd web-panel
npm install
```

3. Instale as dependências do frontend:
```bash
cd client
npm install
```

4. Configure as variáveis de ambiente:
```bash
cp .env.example .env
```

5. Edite o arquivo .env com suas configurações

## Configuração

1. Configure as credenciais do Supabase no arquivo .env
2. Configure a URL da API no frontend (src/components/TabletList.js)
3. Inicie o servidor de desenvolvimento:
```bash
npm run dev:full
```

## Estrutura do Projeto

- `/src/server`: Backend Node.js
- `/client`: Frontend React
- `/client/src/components`: Componentes React
- `/client/src/services`: Serviços de API

## API Endpoints

- GET /api/tablets: Lista todos os tablets
- POST /api/tablets/:id/command: Envia comando para um tablet
- PUT /api/tablets/:id/app: Atualiza o aplicativo de um tablet

## Segurança

- Autenticação JWT
- HTTPS
- Proteção de rotas
- Validação de dados

## Desenvolvimento

Para desenvolver ou modificar o painel:

1. Clone o repositório
2. Instale as dependências
3. Configure as variáveis de ambiente
4. Execute o servidor de desenvolvimento

## Licença

Este projeto é proprietário e confidencial. 