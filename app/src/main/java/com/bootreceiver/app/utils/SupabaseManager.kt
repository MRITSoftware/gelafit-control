package com.bootreceiver.app.utils

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.FilterOperator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

/**
 * Gerenciador para comunicação com Supabase
 * 
 * Esta classe gerencia a conexão com o Supabase e verifica
 * comandos de reiniciar dispositivo na tabela 'reboot_commands'
 */
class SupabaseManager {
    
    private val client: SupabaseClient = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
    }
    
    /**
     * Verifica se há um comando de reiniciar pendente
     * 
     * @param deviceId ID único do dispositivo (pode ser Android ID)
     * @return true se houver comando pendente, false caso contrário
     */
    suspend fun checkRebootCommand(deviceId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Verificando comando de reiniciar para dispositivo: $deviceId")
            
            val response = client.from("reboot_commands")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                        eq("should_reboot", true)
                        eq("executed", false)
                    }
                }
                .decodeSingle<RebootCommand>()
            
            Log.d(TAG, "Comando encontrado: ${response.id}")
            true
        } catch (e: Exception) {
            // Se não encontrar nenhum comando, retorna false
            if (e.message?.contains("No rows") == true || 
                e.message?.contains("not found") == true) {
                Log.d(TAG, "Nenhum comando de reiniciar pendente")
                false
            } else {
                Log.e(TAG, "Erro ao verificar comando: ${e.message}", e)
                false
            }
        }
    }
    
    /**
     * Marca um comando como executado
     * 
     * @param deviceId ID único do dispositivo
     */
    suspend fun markCommandAsExecuted(deviceId: String): Boolean = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Marcando comando como executado para dispositivo: $deviceId")
            
            // Primeiro, busca o comando
            val command = client.from("reboot_commands")
                .select(columns = Columns.ALL) {
                    filter {
                        eq("device_id", deviceId)
                        eq("should_reboot", true)
                        eq("executed", false)
                    }
                }
                .decodeSingle<RebootCommand>()
            
            // Atualiza o comando como executado
            if (command.id != null) {
                val updateData = RebootCommand(
                    id = command.id,
                    device_id = command.device_id,
                    should_reboot = command.should_reboot,
                    executed = true,
                    created_at = command.created_at,
                    executed_at = System.currentTimeMillis()
                )
                
                client.from("reboot_commands")
                    .update(updateData) {
                        filter {
                            eq("id", command.id)
                        }
                    }
            }
            
            Log.d(TAG, "Comando marcado como executado: ${command.id}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao marcar comando como executado: ${e.message}", e)
            false
        }
    }
    
    companion object {
        private const val TAG = "SupabaseManager"
        private const val SUPABASE_URL = "https://kihyhoqbrkwbfudttevo.supabase.co"
        private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtpaHlob3Ficmt3YmZ1ZHR0ZXZvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTU1NTUwMjcsImV4cCI6MjAzMTEzMTAyN30.XtBTlSiqhsuUIKmhAMEyxofV-dRst7240n912m4O4Us"
    }
}

/**
 * Modelo de dados para comando de reiniciar
 */
@Serializable
data class RebootCommand(
    val id: Long? = null,
    val device_id: String,
    val should_reboot: Boolean = false,
    val executed: Boolean = false,
    val created_at: Long? = null,
    val executed_at: Long? = null
)
