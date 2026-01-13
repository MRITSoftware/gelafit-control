# 📦 Configuração do Storage Bucket - Supabase

## Passo 1: Criar o Bucket no Dashboard

1. Acesse o **Supabase Dashboard**
2. No menu lateral, clique em **Storage**
3. Clique no botão **"New bucket"** ou **"Create bucket"**
4. Preencha os campos:
   - **Nome do bucket**: `documents`
   - **Público**: ❌ **DESMARCADO** (deve ser privado)
   - **File size limit** (opcional): `10485760` (10MB em bytes)
   - **Allowed MIME types** (opcional): `application/pdf`
5. Clique em **"Create bucket"** ou **"Create"**

## Passo 2: Executar as Políticas RLS

1. No Supabase Dashboard, vá em **SQL Editor**
2. Cole e execute o conteúdo do arquivo `setup-storage-bucket.sql`
3. Verifique se todas as políticas foram criadas com sucesso

## Passo 3: Verificar Configuração

Execute esta query no SQL Editor para verificar:

```sql
-- Verificar se o bucket existe
SELECT * FROM storage.buckets WHERE name = 'documents';

-- Verificar políticas criadas
SELECT policyname, cmd, qual, with_check 
FROM pg_policies 
WHERE tablename = 'objects' 
AND schemaname = 'storage';
```

## Estrutura de Pastas

Os arquivos serão armazenados na seguinte estrutura:

```
documents/
  └── {user_id}/
      └── {timestamp}.pdf
```

Exemplo:
```
documents/
  └── 86518a79-bdd2-44f7-aac5-cb6f60a4e998/
      └── 1699123456789.pdf
```

## Troubleshooting

### Erro: "bucket not found"
- Certifique-se de que o bucket `documents` foi criado no Dashboard

### Erro: "new row violates row-level security policy"
- Verifique se todas as políticas foram criadas corretamente
- Certifique-se de que o usuário está autenticado
- Verifique se o usuário tem o role correto (`client`, `equipe`, ou `admin`)

### Erro: "upload failed"
- Verifique se o arquivo é PDF
- Verifique se o tamanho não excede 10MB
- Verifique se o bucket está configurado corretamente

