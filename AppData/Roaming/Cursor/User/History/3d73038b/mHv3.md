# Barrella Gestão

Sistema de gestão para empresa Barrella com interface desktop PySide6.

## Estrutura do Projeto

```
barrella-gestao/
├── app.py                    # Ponto de entrada da aplicação
├── requirements.txt          # Dependências Python
├── barrella/
│   ├── core/                # Lógica de negócio
│   │   ├── models/          # Modelos Pydantic
│   │   ├── repo/            # Repositórios de dados
│   │   └── services/        # Serviços de negócio
│   ├── ui/                  # Interface PySide6
│   │   ├── views/           # Telas da aplicação
│   │   └── widgets/         # Componentes reutilizáveis
│   └── assets/              # Templates e recursos
└── data/                    # Dados JSON (persistência)
```

## Como Executar

1. Criar ambiente virtual:
```bash
python -m venv .venv
```

2. Ativar ambiente virtual:
```bash
# Windows
.venv\Scripts\activate

# Linux/Mac
source .venv/bin/activate
```

3. Instalar dependências:
```bash
pip install -r requirements.txt
```

4. Executar aplicação:
```bash
python app.py
```

## Funcionalidades

- **Clientes**: Gestão de clientes (MVP em construção)
- **Produtos**: Gestão de produtos (MVP em construção)  
- **Orçamentos**: Gestão de orçamentos (MVP em construção)

## Tecnologias

- Python 3.11+
- PySide6 (Interface desktop)
- Pydantic (Modelos de domínio)
- orjson (IO rápido de JSON)
- Jinja2 (Templates PDF)
