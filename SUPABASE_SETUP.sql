-- ============================================
-- Script de Setup do Supabase para MRIT Control
-- ============================================
-- Este script cria as tabelas necessárias para o sistema de controle remoto
-- Execute este script no SQL Editor do Supabase
-- ============================================

-- ============================================
-- 1. Tabela de Dispositivos
-- ============================================
CREATE TABLE IF NOT EXISTS devices (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id TEXT NOT NULL UNIQUE,
    unit_name TEXT,
    registered_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_seen TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Índices para a tabela devices
CREATE INDEX IF NOT EXISTS idx_devices_device_id ON devices(device_id);
CREATE INDEX IF NOT EXISTS idx_devices_is_active ON devices(is_active);
CREATE INDEX IF NOT EXISTS idx_devices_last_seen ON devices(last_seen DESC);

-- ============================================
-- 2. Tabela de Comandos para Dispositivos
-- ============================================
CREATE TABLE IF NOT EXISTS device_commands (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    device_id TEXT NOT NULL,
    command TEXT NOT NULL,
    executed BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    executed_at TIMESTAMP WITH TIME ZONE
);

-- Índices para a tabela device_commands
CREATE INDEX IF NOT EXISTS idx_device_commands_device_id ON device_commands(device_id);
CREATE INDEX IF NOT EXISTS idx_device_commands_command ON device_commands(command);
CREATE INDEX IF NOT EXISTS idx_device_commands_executed ON device_commands(executed);
CREATE INDEX IF NOT EXISTS idx_device_commands_pending ON device_commands(device_id, command, executed) 
WHERE executed = false;

-- ============================================
-- 3. Função para atualizar updated_at automaticamente
-- ============================================
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para atualizar updated_at na tabela devices
DROP TRIGGER IF EXISTS update_devices_updated_at ON devices;
CREATE TRIGGER update_devices_updated_at
    BEFORE UPDATE ON devices
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ============================================
-- 4. Row Level Security (RLS) - OPCIONAL
-- ============================================
-- Descomente as linhas abaixo se quiser habilitar RLS
-- Por padrão, o app funciona sem RLS (acesso público via anon key)

/*
-- Habilitar RLS
ALTER TABLE device_commands ENABLE ROW LEVEL SECURITY;
ALTER TABLE devices ENABLE ROW LEVEL SECURITY;

-- Política para permitir leitura de comandos
CREATE POLICY "Permitir leitura de comandos"
ON device_commands
FOR SELECT
USING (true);

-- Política para permitir atualização de comandos
CREATE POLICY "Permitir atualização de comandos"
ON device_commands
FOR UPDATE
USING (true);

-- Política para permitir inserção de comandos
CREATE POLICY "Permitir inserção de comandos"
ON device_commands
FOR INSERT
WITH CHECK (true);

-- Política para permitir leitura de dispositivos
CREATE POLICY "Permitir leitura de dispositivos"
ON devices
FOR SELECT
USING (true);

-- Política para permitir inserção de dispositivos
CREATE POLICY "Permitir inserção de dispositivos"
ON devices
FOR INSERT
WITH CHECK (true);

-- Política para permitir atualização de dispositivos
CREATE POLICY "Permitir atualização de dispositivos"
ON devices
FOR UPDATE
USING (true);
*/

-- ============================================
-- 5. Comentários nas Tabelas
-- ============================================
COMMENT ON TABLE devices IS 'Registro de dispositivos Android conectados ao sistema';
COMMENT ON TABLE device_commands IS 'Comandos enviados para dispositivos (ex: reboot)';

COMMENT ON COLUMN devices.device_id IS 'Android ID único do dispositivo';
COMMENT ON COLUMN devices.unit_name IS 'Nome personalizado da unidade (ex: Sala 01, Recepção)';
COMMENT ON COLUMN devices.last_seen IS 'Última vez que o dispositivo se conectou ao sistema';

COMMENT ON COLUMN device_commands.device_id IS 'Referência ao device_id da tabela devices';
COMMENT ON COLUMN device_commands.command IS 'Tipo de comando (ex: reboot)';
COMMENT ON COLUMN device_commands.executed IS 'Se o comando foi executado pelo dispositivo';

-- ============================================
-- FIM DO SCRIPT
-- ============================================
-- Após executar este script:
-- 1. Verifique se as tabelas foram criadas: Table Editor → devices, device_commands
-- 2. Teste inserindo um dispositivo: INSERT INTO devices (device_id) VALUES ('test123');
-- 3. Teste inserindo um comando: INSERT INTO device_commands (device_id, command) VALUES ('test123', 'reboot');
-- ============================================
