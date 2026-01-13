# ⚡ Teste Rápido - 5 Minutos

## Passo 1: Configurar Banco de Dados (2 min)

Edite `config/database.php`:
```php
return [
    'host' => 'localhost',
    'database' => 'SEU_BANCO',
    'username' => 'SEU_USUARIO',
    'password' => 'SUA_SENHA',
    // ...
];
```

## Passo 2: Instalar Dependências (1 min)

```bash
composer install --no-dev --optimize-autoloader
```

## Passo 3: Testar Conexão (1 min)

Acesse no navegador:
```
https://seudominio.com.br/test.php
```

**Esperado**: Ver todas as verificações passando ✅

## Passo 4: Testar API (1 min)

### Teste de Cadastro:
```bash
curl -X POST https://seudominio.com.br/api/auth/cadastro.php \
  -H "Content-Type: application/json" \
  -d '{"cpf":"12345678901","nome":"Teste","email":"teste@teste.com"}'
```

**Esperado**: 
```json
{"success":true,"message":"Cadastro realizado com sucesso!"}
```

### Teste de Login:
```bash
curl -X POST https://seudominio.com.br/api/auth/login.php \
  -H "Content-Type: application/json" \
  -d '{"cpf":"12345678901"}'
```

**Esperado**: 
```json
{"success":true,"token":"eyJ...","cliente":{...}}
```

## ✅ Se tudo funcionou, o sistema está pronto!

### Próximos passos:
1. Alterar `JWT_SECRET` em `config/config.php`
2. Configurar token do Mercado Pago
3. Testar demais funcionalidades

---

**Problemas?** Consulte `GUIA_TESTE.md` para troubleshooting detalhado.

