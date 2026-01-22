package com.bootreceiver.app.utils

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.realtime.Realtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

/**
 * Gerenciador para comunicação com Supabase
 * 
 * Esta classe gerencia a conexão com o Supabase e registra
 * dispositivos na tabela 'devices'
 */
class SupabaseManager {
    
    val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Realtime) { }
    }
    
    
    /**
     * Verifica se há um comando de reiniciar app pendente
     * 
     * @param deviceId ID único do dispositivo
     * @return DeviceCommand se houver comando pendente, null caso contrário
     */
    suspend fun getRestartAppCommand(deviceId: String): DeviceCommand? = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔍 Verificando comando de reiniciar app para dispositivo: $deviceId")
            
            val response = client.from("device_commands")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                        eq("command", "restart_app")
                        eq("executed", false)
                    }
                    // Ordena por data de criação (mais recente primeiro)
                    // Limita a 1 resultado (pega apenas o mais recente)
                }
                .decodeSingle<DeviceCommand>()
            
            Log.d(TAG, "✅ Comando encontrado! ID: ${response.id}, Command: ${response.command}, Created: ${response.created_at}, Executed: ${response.executed}")
            response
        } catch (e: Exception) {
            if (e.message?.contains("No rows") == true || 
                e.message?.contains("not found") == true ||
                e.message?.contains("No value") == true) {
                // Log apenas a cada 30 verificações para não poluir o log
                // Log.d(TAG, "ℹ️ Nenhum comando de reiniciar app pendente para device: $deviceId")
                null
            } else {
                Log.e(TAG, "❌ Erro ao verificar comando: ${e.message}", e)
                null
            }
        }
    }
    
    /**
     * Marca um comando específico como executado pelo ID
     * 
     * @param commandId ID do comando
     * @return true se marcou com sucesso, false caso contrário
     */
    suspend fun markCommandAsExecutedById(commandId: String?): Boolean = withContext(Dispatchers.IO) {
        if (commandId == null) {
            Log.w(TAG, "⚠️ Tentando marcar comando sem ID")
            return@withContext false
        }
        
        try {
            Log.d(TAG, "📝 Marcando comando como executado...")
            
            // Atualiza o campo executed para true e executed_at para agora
            val updateData = mapOf(
                "executed" to true,
                "executed_at" to java.time.Instant.now().toString()
            )
            
            val result = client.from("device_commands")
                .update(updateData) {
                    filter {
                        eq("id", commandId)
                        eq("executed", false) // Só atualiza se ainda não foi executado
                    }
                }
            
            // Verifica se realmente atualizou (busca o comando atualizado)
            try {
                val updated = client.from("device_commands")
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("id", commandId)
                        }
                    }
                    .decodeSingle<DeviceCommand>()
                
                if (updated.executed) {
                    Log.d(TAG, "✅ Comando marcado como executado com sucesso! (executed_at: ${updated.executed_at})")
                    return@withContext true
                } else {
                    Log.e(TAG, "❌ Comando não foi atualizado! Ainda está como executed=false")
                    return@withContext false
                }
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erro ao verificar se comando foi atualizado: ${e.message}", e)
                // Mesmo assim retorna true porque tentou atualizar
                return@withContext true
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao marcar comando como executado: ${e.message}", e)
            false
        }
    }
    
    /**
     * Deleta um comando específico pelo ID (alternativa à marcação)
     * 
     * @param commandId ID do comando
     * @return true se deletou com sucesso, false caso contrário
     */
    suspend fun deleteCommandById(commandId: String?): Boolean = withContext(Dispatchers.IO) {
        if (commandId == null) {
            Log.w(TAG, "⚠️ Tentando deletar comando sem ID")
            return@withContext false
        }
        
        try {
            Log.d(TAG, "🗑️ Deletando comando...")
            
            client.from("device_commands")
                .delete {
                    filter {
                        eq("id", commandId)
                    }
                }
            
            Log.d(TAG, "✅ Comando deletado com sucesso!")
            true
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao deletar comando: ${e.message}", e)
            false
        }
    }
    
    /**
     * Marca um comando como executado
     * 
     * @param deviceId ID único do dispositivo
     * @param command Tipo de comando (ex: "restart_app")
     * @return true se marcou com sucesso, false caso contrário
     */
    suspend fun markCommandAsExecuted(deviceId: String, command: String = "restart_app"): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "📝 Marcando comando como executado: device_id=$deviceId, command=$command")
            
            // Busca TODOS os comandos pendentes (pode haver múltiplos)
            val commands = try {
                client.from("device_commands")
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("device_id", deviceId)
                            eq("command", command)
                            eq("executed", false)
                        }
                    }
                    .decodeList<DeviceCommand>()
            } catch (e: Exception) {
                if (e.message?.contains("No rows") == true || 
                    e.message?.contains("not found") == true ||
                    e.message?.contains("No value") == true) {
                    Log.d(TAG, "ℹ️ Nenhum comando pendente encontrado")
                    emptyList()
                } else {
                    Log.e(TAG, "❌ Erro ao buscar comandos: ${e.message}", e)
                    return@withContext false
                }
            }
            
            if (commands.isEmpty()) {
                Log.d(TAG, "ℹ️ Nenhum comando pendente para marcar como executado")
                return@withContext true // Retorna true porque não há nada para fazer
            }
            
            Log.d(TAG, "📋 Encontrados ${commands.size} comando(s) pendente(s)")
            
            // Marca TODOS os comandos pendentes como executados
            var successCount = 0
            for (cmd in commands) {
                if (cmd.id != null) {
                    try {
                        val updateData = mapOf(
                            "executed" to true,
                            "executed_at" to java.time.Instant.now().toString()
                        )
                        
                        client.from("device_commands")
                            .update(updateData) {
                                filter {
                                    eq("id", cmd.id)
                                }
                            }
                        
                        successCount++
                        Log.d(TAG, "✅ Comando ${cmd.id} marcado como executado")
                    } catch (e: Exception) {
                        Log.e(TAG, "❌ Erro ao marcar comando ${cmd.id}: ${e.message}", e)
                    }
                }
            }
            
            if (successCount > 0) {
                Log.d(TAG, "✅ Total: $successCount comando(s) marcado(s) como executado(s)")
                return@withContext true
            } else {
                Log.e(TAG, "❌ Nenhum comando foi marcado como executado")
                return@withContext false
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro crítico ao marcar comando como executado: ${e.message}", e)
            false
        }
    }
    
    /**
     * Verifica se o modo kiosk está ativo para o dispositivo
     * 
     * @param deviceId ID único do dispositivo
     * @return true se kiosk_mode está ativo, false caso contrário, null se erro
     */
    suspend fun getKioskMode(deviceId: String): Boolean? = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔍 Verificando modo kiosk para dispositivo: $deviceId")
            
            val device = client.from("devices")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                    }
                }
                .decodeSingle<Device>()
            
            val kioskMode = device.kiosk_mode ?: false
            Log.d(TAG, "ℹ️ Modo kiosk: $kioskMode")
            return@withContext kioskMode
        } catch (e: Exception) {
            if (e.message?.contains("No rows") == true || 
                e.message?.contains("not found") == true ||
                e.message?.contains("No value") == true) {
                Log.d(TAG, "ℹ️ Dispositivo não encontrado. Modo kiosk: false (padrão)")
                return@withContext false
            } else {
                Log.e(TAG, "❌ Erro ao verificar modo kiosk: ${e.message}", e)
                return@withContext null
            }
        }
    }

    /**
     * Verifica se is_active está ativo para o dispositivo (kiosk da casca)
     *
     * @param deviceId ID único do dispositivo
     * @return true se is_active está ativo, false caso contrário, null se erro
     */
    suspend fun getIsActive(deviceId: String): Boolean? = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔍 Verificando is_active para dispositivo: $deviceId")

            val device = client.from("devices")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                    }
                }
                .decodeSingle<Device>()

            val isActive = device.is_active
            Log.d(TAG, "ℹ️ is_active: $isActive")
            return@withContext isActive
        } catch (e: Exception) {
            if (e.message?.contains("No rows") == true ||
                e.message?.contains("not found") == true ||
                e.message?.contains("No value") == true) {
                Log.d(TAG, "ℹ️ Dispositivo não encontrado. is_active: false (padrão)")
                return@withContext false
            } else {
                Log.e(TAG, "❌ Erro ao verificar is_active: ${e.message}", e)
                return@withContext null
            }
        }
    }

    /**
     * Busca status combinado (is_active + kiosk_mode) em uma chamada.
     */
    suspend fun getDeviceStatus(deviceId: String): DeviceStatus? = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "🔍 Buscando status do dispositivo: $deviceId")

            val device = client.from("devices")
                .select(columns = Columns.ALL) {
                    filter { eq("device_id", deviceId) }
                }
                .decodeSingle<Device>()

            val status = DeviceStatus(
                isActive = device.is_active,
                kioskMode = device.kiosk_mode ?: false
            )
            Log.d(TAG, "ℹ️ Status -> is_active=${status.isActive}, kiosk_mode=${status.kioskMode}")
            return@withContext status
        } catch (e: Exception) {
            if (e.message?.contains("No rows") == true ||
                e.message?.contains("not found") == true ||
                e.message?.contains("No value") == true) {
                Log.d(TAG, "ℹ️ Dispositivo não encontrado. Status padrão (false/false)")
                return@withContext DeviceStatus(isActive = false, kioskMode = false)
            }
            Log.e(TAG, "❌ Erro ao buscar status do dispositivo: ${e.message}", e)
            return@withContext null
        }
    }
    
    /**
     * Atualiza is_active do dispositivo
     */
    suspend fun updateIsActive(deviceId: String, isActive: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "📝 Atualizando is_active para dispositivo: $deviceId -> $isActive")
            val updateData = mapOf("is_active" to isActive)
            client.from("devices")
                .update(updateData) {
                    filter { eq("device_id", deviceId) }
                }
            Log.d(TAG, "✅ is_active atualizado com sucesso")
            true
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao atualizar is_active: ${e.message}", e)
            false
        }
    }
    
    /**
     * Atualiza o modo kiosk do dispositivo
     * 
     * @param deviceId ID único do dispositivo
     * @param kioskMode true para ativar, false para desativar
     * @return true se atualizou com sucesso, false caso contrário
     */
    suspend fun updateKioskMode(deviceId: String, kioskMode: Boolean): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "📝 Atualizando kiosk_mode para dispositivo: $deviceId -> $kioskMode")
            
            val updateData = mapOf(
                "kiosk_mode" to kioskMode
            )
            
            client.from("devices")
                .update(updateData) {
                    filter {
                        eq("device_id", deviceId)
                    }
                }
            
            Log.d(TAG, "✅ kiosk_mode atualizado com sucesso")
            return@withContext true
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao atualizar kiosk_mode: ${e.message}", e)
            return@withContext false
        }
    }
    
    /**
     * Registra ou atualiza um dispositivo na tabela devices
     * 
     * @param deviceId ID único do dispositivo (Android ID)
     * @param unitName Nome da unidade (email ou nome personalizado)
     * @return true se o registro foi bem-sucedido
     */
    suspend fun registerDevice(deviceId: String, unitName: String? = null): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Registrando dispositivo: $deviceId com nome: $unitName")
            
            // Se unitName (email) foi fornecido, verifica se já existe outro dispositivo com esse email
            var deviceWithSameEmail: Device? = null
            if (unitName != null && unitName.isNotBlank()) {
                try {
                    // Busca dispositivo existente com o mesmo email
                    val existingDevicesByEmail = client.from("devices")
                        .select(columns = Columns.ALL) {
                            filter {
                                eq("unit_name", unitName)
                            }
                        }
                        .decodeList<Device>()
                    
                    // Se encontrou dispositivo com o mesmo email mas device_id diferente, sobrescreve
                    deviceWithSameEmail = existingDevicesByEmail.firstOrNull { it.device_id != deviceId }
                    
                    if (deviceWithSameEmail != null) {
                        Log.d(TAG, "Email já existe no dispositivo ${deviceWithSameEmail.device_id}. Sobrescrevendo...")
                        
                        // Remove o email do dispositivo antigo (ou pode deletar se preferir)
                        // Aqui vamos apenas limpar o email do dispositivo antigo
                        try {
                            client.from("devices")
                                .update(mapOf("unit_name" to null as String?)) {
                                    filter {
                                        eq("device_id", deviceWithSameEmail.device_id)
                                    }
                                }
                            Log.d(TAG, "Email removido do dispositivo antigo ${deviceWithSameEmail.device_id}")
                        } catch (e: Exception) {
                            Log.w(TAG, "Erro ao remover email do dispositivo antigo: ${e.message}")
                        }
                    }
                } catch (e: Exception) {
                    // Só loga se a mensagem não for de “nenhuma linha encontrada”
                    val noRows = e.message?.contains("No rows") == true
                    val notFound = e.message?.contains("not found") == true
                    if (!noRows && !notFound) {
                        Log.w(TAG, "Erro ao verificar email existente: ${e.message}")
                    }
                }
            }
            
            // Verifica se o dispositivo já existe pelo device_id
            var existingDevice: Device? = null
            try {
                existingDevice = client.from("devices")
                    .select(columns = Columns.ALL) {
                        filter {
                            eq("device_id", deviceId)
                        }
                    }
                    .decodeSingle<Device>()
                Log.d(TAG, "Dispositivo encontrado no banco pelo device_id")
            } catch (e: Exception) {
                if (e.message?.contains("No rows") == true || 
                    e.message?.contains("not found") == true) {
                    Log.d(TAG, "Dispositivo não encontrado pelo device_id, será criado novo registro")
                } else {
                    Log.w(TAG, "Erro ao verificar dispositivo existente: ${e.message}")
                }
            }
            
            if (existingDevice != null) {
                // Dispositivo já existe, atualiza last_seen, unit_name, is_active e kiosk_mode
                // IMPORTANTE: NUNCA altera o device_id. Seta is_active=true e kiosk_mode=false ao atualizar
                Log.d(TAG, "Dispositivo já existe (device_id: ${existingDevice.device_id}, kiosk_mode: ${existingDevice.kiosk_mode}, is_active: ${existingDevice.is_active}). Atualizando unit_name/last_seen, setando is_active=true e kiosk_mode=false...")
                val updateData = mutableMapOf<String, Any>(
                    "last_seen" to java.time.Instant.now().toString(),
                    "is_active" to true,  // Sempre seta is_active como true ao atualizar
                    "kiosk_mode" to false // Sempre seta kiosk_mode como false ao atualizar
                )
                
                if (unitName != null && unitName.isNotBlank()) {
                    updateData["unit_name"] = unitName
                }
                
                // CRÍTICO: Sempre atualiza pelo device_id original do banco, nunca cria novo
                client.from("devices")
                    .update(updateData) {
                        filter {
                            eq("device_id", existingDevice.device_id) // Usa o device_id do banco, não o fornecido
                        }
                    }
                
                Log.d(TAG, "✅ Dispositivo atualizado (device_id mantido: ${existingDevice.device_id}), is_active=true, kiosk_mode=false")
            } else {
                // Novo dispositivo, cria registro
                Log.d(TAG, "Criando novo registro de dispositivo com device_id: $deviceId")
                val newDevice = Device(
                    device_id = deviceId, // Usa o device_id fornecido
                    unit_name = unitName,
                    is_active = true,  // Por padrão, dispositivo é criado como ativo
                    kiosk_mode = false // Kiosk sempre inicia desativado; só ativa via botão
                )
                
                client.from("devices")
                    .insert(newDevice)
                
                Log.d(TAG, "✅ Dispositivo registrado com sucesso! (device_id: $deviceId, kiosk_mode: false)")
            }
            
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao registrar dispositivo: ${e.message}", e)
            false
        }
    }
    
    /**
     * Cria um Flow que verifica mudanças periodicamente na tabela devices
     * Usa polling otimizado (a cada 5 segundos) - emite apenas quando há mudança real
     * 
     * NOTA: A API Realtime do Supabase Kotlin (v2.3.0) não expõe funções públicas
     * para postgres changes de forma direta. Por enquanto, usamos polling otimizado
     * que reduz significativamente as requisições (de 60/min para 12/min).
     * 
     * Quando a API Realtime estiver disponível ou documentada, podemos migrar facilmente.
     * 
     * @param deviceId ID único do dispositivo para filtrar mudanças
     * @return Flow que emite DeviceStatus quando há mudanças no banco
     */
    fun subscribeToDeviceChanges(deviceId: String): Flow<DeviceStatus> {
        return flow {
            var lastStatus: DeviceStatus? = null
            
            while (true) {
                try {
                    val status = withContext(Dispatchers.IO) {
                        getDeviceStatus(deviceId)
                    }
                    val currentStatus = status ?: DeviceStatus(isActive = false, kioskMode = false)
                    
                    // Só emite se mudou (otimização - evita processamento desnecessário)
                    if (lastStatus == null || lastStatus != currentStatus) {
                        Log.d(TAG, "🔄 Status atualizado - is_active: ${currentStatus.isActive}, modo_kiosk: ${currentStatus.kioskMode}")
                        emit(currentStatus)
                        lastStatus = currentStatus
                    }
                    
                    delay(60000) // Verifica a cada 1 minuto (polling apenas atualiza cache)
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Erro ao verificar status: ${e.message}", e)
                    delay(10000) // Em caso de erro, aguarda mais tempo
                }
            }
        }
    }
    
    /**
     * Remove a subscrição de mudanças
     * Nota: O Flow é automaticamente cancelado quando o collector é cancelado,
     * mas podemos também cancelar manualmente o canal se necessário
     * 
     * @param deviceId ID único do dispositivo (mantido para compatibilidade)
     */
    suspend fun unsubscribeFromDeviceChanges(deviceId: String) {
        // O Flow é automaticamente cancelado quando o collector é cancelado
        // O canal será fechado automaticamente no awaitClose do callbackFlow
        Log.d(TAG, "🔌 Subscription será cancelada automaticamente quando o Flow for cancelado")
    }
    
    /**
     * Cria um Flow que verifica comandos de reiniciar periodicamente
     * Usa polling otimizado (a cada 5 segundos) - emite apenas quando há comando novo
     * 
     * NOTA: A API Realtime do Supabase Kotlin (v2.3.0) não expõe funções públicas
     * para postgres changes de forma direta. Por enquanto, usamos polling otimizado
     * que reduz significativamente as requisições (de 60/min para 12/min).
     * 
     * Quando a API Realtime estiver disponível ou documentada, podemos migrar facilmente.
     * 
     * @param deviceId ID único do dispositivo para filtrar comandos
     * @return Flow que emite DeviceCommand quando há comando pendente
     */
    fun subscribeToRestartCommands(deviceId: String): kotlinx.coroutines.flow.Flow<DeviceCommand> {
        return flow {
            var lastCommandId: String? = null
            var checkCount = 0
            
            while (true) {
                try {
                    checkCount++
                    val command = withContext(Dispatchers.IO) {
                        getRestartAppCommand(deviceId)
                    }
                    
                    // Log a cada 30 verificações (30 segundos) para confirmar que está verificando
                    if (checkCount % 30 == 0) {
                        Log.d(TAG, "🔍 Verificando comandos... (device: $deviceId, verificação #$checkCount)")
                    }
                    
                    // Emite se há comando pendente (não executado) e é um comando novo (ID diferente)
                    if (command != null && command.id != null) {
                        val isNewCommand = command.id != lastCommandId
                        
                        if (isNewCommand && !command.executed) {
                            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            Log.d(TAG, "🔄 COMANDO DE REINICIAR APP DETECTADO!")
                            Log.d(TAG, "📋 ID: ${command.id}")
                            Log.d(TAG, "📱 Device: $deviceId")
                            Log.d(TAG, "📅 Created: ${command.created_at}")
                            Log.d(TAG, "✅ Executed: ${command.executed}")
                            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            emit(command)
                            lastCommandId = command.id
                        } else if (!isNewCommand && command.executed) {
                            // Se o comando foi executado, reseta para permitir detectar novos comandos
                            Log.d(TAG, "ℹ️ Comando ${command.id} já foi executado, resetando estado...")
                            lastCommandId = null
                        }
                    } else {
                        // Se não há comando, reseta o estado para permitir detectar novos comandos
                        if (lastCommandId != null) {
                            Log.d(TAG, "ℹ️ Nenhum comando pendente, resetando estado...")
                            lastCommandId = null
                        }
                    }
                    
                    delay(1000) // Verifica a cada 1 segundo para resposta rápida
                } catch (e: Exception) {
                    Log.e(TAG, "❌ Erro ao verificar comandos: ${e.message}", e)
                    delay(10000) // Em caso de erro, aguarda mais tempo
                }
            }
        }
    }
    
    companion object {
        private const val TAG = "SupabaseManager"
        private const val SUPABASE_URL = "https://kihyhoqbrkwbfudttevo.supabase.co"
        private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtpaHlob3Ficmt3YmZ1ZHR0ZXZvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTU1NTUwMjcsImV4cCI6MjAzMTEzMTAyN30.XtBTlSiqhsuUIKmhAMEyxofV-dRst7240n912m4O4Us"
    }
}

/**
 * Modelo de dados para dispositivo
 * Estrutura corresponde à tabela devices no Supabase
 */
@Serializable
data class Device(
    val id: String? = null,  // UUID
    val device_id: String,
    val unit_name: String? = null,
    val registered_at: String? = null,
    val last_seen: String? = null,
    val is_active: Boolean = true,
    val kiosk_mode: Boolean? = false,  // Modo kiosk (bloqueia minimização)
    val created_at: String? = null,
    val updated_at: String? = null
)

@Serializable
data class DeviceStatus(
    val isActive: Boolean = false,
    val kioskMode: Boolean = false
)

/**
 * Modelo de dados para comando de dispositivo
 * Estrutura corresponde à tabela device_commands no Supabase
 */
@Serializable
data class DeviceCommand(
    val id: String? = null,  // UUID
    val device_id: String,
    val command: String,  // "restart_app" para reiniciar o app
    val executed: Boolean = false,
    val created_at: String? = null,  // TIMESTAMP WITH TIME ZONE
    val executed_at: String? = null  // TIMESTAMP WITH TIME ZONE
)

