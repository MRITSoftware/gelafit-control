## Banco de Dados - Configuração Consolidada

Documento de referência com todos os objetos do banco e policies. Mantenha este arquivo atualizado a cada mudança de schema ou RLS.

### Sumário rápido
- Tabelas principais: `mr_perfis`, `mr_documentos`, `mr_comentarios`, `mr_historico_documentos`
- Complementares: categorias/tags, workflows, compartilhamento, notificações, suporte, configurações, auditoria
- Assinaturas digitais: `mr_assinaturas`, `mr_assinatura_validacoes`, `mr_certificados_digitais`
- Backup: `mr_backups`, `mr_restauracoes`, `mr_configuracoes_backup`
- Settings: `mr_configuracoes`
- Buckets Storage: `documents`, `signatures`, (opcional) `backups`

---

### 1) Tabelas base (já existentes)

Arquivos SQL (referência neste repo):
- `supabase-setup.sql` — base do projeto
- `add-status-to-documents.sql` — adiciona status em `mr_documentos`

Tabelas envolvidas (campos principais):
- `mr_perfis (id, nome, email, role, criado_em, atualizado_em)`
- `mr_documentos (id, dono_id, titulo, descricao, caminho_storage, tipo_mime, tamanho_bytes, status, criado_em, atualizado_em)`
- `mr_comentarios (id, documento_id, autor_id, conteudo, criado_em, atualizado_em)`
- `mr_historico_documentos (id, documento_id, acao, detalhes, feito_por, criado_em)`

RLS: definidas nos respectivos scripts de setup e correções (`fix-mr-perfis-rls.sql`, etc.).

---

### 2) Categorias e Tags

Arquivo: `add-categories-tags.sql`
- Tabelas: `mr_categorias`, `mr_tags`, `mr_documento_categorias`, `mr_documento_tags`
- Policies: leitura por usuários autorizados; escrita por admin/equipe/dono

---

### 3) Compartilhamento de Documentos

Arquivo: `create-document-sharing.sql`
- Tabelas: `mr_documento_compartilhamentos`, `mr_links_compartilhamento`
- Policies: acesso por dono, compartilhado, admin/equipe

---

### 4) Workflows de Aprovação

Arquivo: `create-workflow-approval.sql`
- Tabelas: `mr_workflows`, `mr_workflow_etapas`, `mr_workflow_aprovadores`, `mr_documento_workflows`, `mr_documento_aprovacoes`
- Policies: admin/equipe e participantes

---

### 5) Notificações In-App

Arquivo: `create-notifications-table.sql`
- Tabela: `mr_notificacoes`

---

### 6) Suporte em Tempo Real

Arquivos: `create-support-tables.sql`, `update-support-tables.sql`, `enable-realtime.sql`
- Tabelas: `mr_chamados_suporte`, `mr_mensagens_suporte`
- Campos extras: `resolvido`, `avaliacao_nota`, `avaliacao_comentario`, `avaliado_em`
- Realtime: adicionar tabelas à publicação `supabase_realtime`

---

### 7) Configurações do Sistema

Arquivo: `create-system-settings.sql`
- Tabela: `mr_configuracoes (id, chave, valor, tipo, descricao, categoria, criado_em, atualizado_em)`
- Policies: leitura geral; gestão apenas por admin
- Seeds de configurações (upload, segurança, notificações, backup, geral)

---

### 8) Auditoria

Arquivo: `create-audit-log.sql`
- Tabela: `mr_logs_auditoria`
- Função/trigger: `registrar_log_auditoria()`

---

### 9) Assinaturas Digitais

Arquivo: `create-digital-signature.sql`
- Tabelas:
  - `mr_assinaturas (id, documento_id, assinante_id, assinado_por, tipo_assinatura, hash_assinatura, certificado_digital, assinatura_desenhada_path, ip_address, user_agent, localizacao, assinado_em, validada, validada_em, validada_por, invalida, motivo_invalidacao, criado_em)`
  - `mr_assinatura_validacoes (id, assinatura_id, validada_por, resultado, motivo, hash_original, hash_validado, certificado_verificado, detalhes_verificacao, validado_em)`
  - `mr_certificados_digitais (id, usuario_id, numero_serie, emisor, validade_inicio, validade_fim, status, arquivo_certificado, hash_certificado, criado_em, atualizado_em)`
- Índices e Policies: conforme script

Coluna opcional caso ainda não exista:
```sql
alter table public.mr_assinaturas
add column if not exists assinatura_desenhada_path text;
```

---

### 10) Backup e Recuperação

Arquivo: `create-backup-system.sql`
- Tabelas:
  - `mr_backups (id, tipo, descricao, caminho_storage, tamanho_bytes, documentos_contidos, status, iniciado_por, iniciado_em, concluido_em, hash_verificacao, erro, metadata, criado_em, atualizado_em)`
  - `mr_restauracoes (id, backup_id, tipo_restauracao, documentos_restaurados, status, restaurado_por, restaurado_em, concluida_em, erro, detalhes_restauracao, criado_em)`
  - `mr_documentos_restaurados (id, restauracao_id, documento_original_id, documento_restaurado_id, versao_restaurada, status, erro, criado_em)`
  - `mr_configuracoes_backup (id, backup_automatico_ativado, frequencia_backup, hora_backup, dia_semana, dia_mes, retencao_dias, max_backups, incluir_documentos, incluir_usuarios, incluir_comentarios, incluir_historico, compactar_backup, criptografar_backup, atualizado_por, atualizado_em)`
- Policies: admin

---

### 11) Buckets e Policies (Storage)

Bucket de documentos (já existente, vide `setup-storage-bucket-corrigido.sql`): `documents`

Bucket de assinaturas: `signatures`
```sql
insert into storage.buckets (id, name, public)
values ('signatures', 'signatures', false)
on conflict (id) do nothing;

create policy "signatures_read_owner_or_staff"
on storage.objects
for select to authenticated
using (
  bucket_id = 'signatures' and (
    owner = auth.uid() or exists (
      select 1 from public.mr_perfis p where p.id = auth.uid() and p.role in ('admin','equipe')
    )
  )
);

create policy "signatures_insert_authenticated"
on storage.objects
for insert to authenticated
with check (bucket_id = 'signatures');

create policy "signatures_update_owner_or_staff"
on storage.objects
for update to authenticated
using (
  bucket_id = 'signatures' and (
    owner = auth.uid() or exists (
      select 1 from public.mr_perfis p where p.id = auth.uid() and p.role in ('admin','equipe')
    )
  )
)
with check (bucket_id = 'signatures');

create policy "signatures_delete_owner_or_staff"
on storage.objects
for delete to authenticated
using (
  bucket_id = 'signatures' and (
    owner = auth.uid() or exists (
      select 1 from public.mr_perfis p where p.id = auth.uid() and p.role in ('admin','equipe')
    )
  )
);
```

Bucket de backups (opcional): `backups` — políticas semelhantes às de `signatures`, restritas a admin.

---

### 12) Realtime

Arquivo: `enable-realtime.sql`
```sql
alter publication supabase_realtime add table public.mr_mensagens_suporte;
alter publication supabase_realtime add table public.mr_chamados_suporte;
```

---

### 13) Atualizações de Role/Perfil

Arquivos: `update-user-role.sql`, `upsert-user-role.sql`, `create-or-update-user-simple.sql`
- Uso de bloco DO $$ para buscar `auth.users.email` → upsert em `mr_perfis`.

---

### 14) Claim/Aceite de Documentos

Arquivo: `create-claim-workflow.sql`
- Colunas: `responsavel_id`, `aceito_em` em `mr_documentos`
- Funções: `public.is_admin()`, `public.is_staff()`
- Gatilhos: `trg_set_aceito_em_on_claim`, `trg_prevent_reassign_if_not_admin`
- Policies RLS:
  - Leitura: dono, responsável, staff
  - Inserção: dono ou staff
  - Update claim: staff pode pegar quando `responsavel_id is null` (com check `responsavel_id = auth.uid()`)
  - Update edição: responsável ou admin (troca de responsável só admin via trigger)
  - Delete: apenas admin

Exemplo de update seguro para aceitar documento (evita corrida):
```sql
update public.mr_documentos
set responsavel_id = auth.uid(), status = 'em_andamento', atualizado_em = now(), aceito_em = now()
where id = $1 and responsavel_id is null
returning *;
```

---

### 15) Observações Operacionais
- Sempre executar scripts com cuidado em produção.
- Após alterações em RLS/policies, testar com usuários de cada role.
- Para novas features, adicionar subseção aqui e o SQL correspondente no repo.


