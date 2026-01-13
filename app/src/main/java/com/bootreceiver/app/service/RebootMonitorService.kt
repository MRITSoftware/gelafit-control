package com.bootreceiver.app.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.RebootManager
import com.bootreceiver.app.utils.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Serviço que monitora periodicamente o Supabase para verificar
 * se há comandos de reiniciar o dispositivo
 * 
 * Este serviço:
 * 1. Verifica a cada X segundos se há um comando de reiniciar
 * 2. Se encontrar, tenta reiniciar o dispositivo
 * 3. Marca o comando como executado após reiniciar
 */
class RebootMonitorService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    private val supabaseManager = SupabaseManager()
    private lateinit var deviceId: String
    private lateinit var rebootManager: RebootManager
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "RebootMonitorService criado")
        deviceId = DeviceIdManager.getDeviceId(this)
        rebootManager = RebootManager(this)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            Log.d(TAG, "Serviço já está rodando")
            return START_STICKY
        }
        
        isRunning = true
        Log.d(TAG, "RebootMonitorService iniciado para dispositivo: $deviceId")
        
        // Verifica se Device Admin está ativo
        if (!rebootManager.isDeviceAdminActive()) {
            Log.w(TAG, "Device Admin não está ativo. Solicitando permissão...")
            rebootManager.requestDeviceAdmin()
            // Continua mesmo assim, pois o usuário pode ativar depois
        }
        
        // Inicia o monitoramento em uma coroutine
        serviceScope.launch {
            startMonitoring()
        }
        
        // Retorna START_STICKY para que o serviço seja reiniciado se for morto
        return START_STICKY
    }
    
    /**
     * Inicia o monitoramento periódico do banco de dados
     */
    private suspend fun startMonitoring() {
        while (isRunning) {
            try {
                Log.d(TAG, "Verificando comando de reiniciar...")
                
                val hasRebootCommand = supabaseManager.checkRebootCommand(deviceId)
                
                if (hasRebootCommand) {
                    Log.d(TAG, "Comando de reiniciar encontrado! Executando...")
                    
                    // Marca como executado antes de reiniciar (para evitar loop)
                    supabaseManager.markCommandAsExecuted(deviceId)
                    
                    // Aguarda um pouco para garantir que o comando foi salvo
                    delay(1000)
                    
                    // Tenta reiniciar
                    val rebootSuccess = rebootManager.reboot()
                    
                    if (rebootSuccess) {
                        Log.d(TAG, "Comando de reiniciar enviado com sucesso!")
                        // O dispositivo será reiniciado, então o serviço será parado
                        stopSelf()
                        return
                    } else {
                        Log.w(TAG, "Falha ao reiniciar. Verifique se Device Admin está ativo.")
                        // Se falhar, continua monitorando
                    }
                } else {
                    Log.d(TAG, "Nenhum comando de reiniciar pendente")
                }
                
                // Aguarda antes da próxima verificação
                delay(CHECK_INTERVAL_MS)
                
            } catch (e: Exception) {
                Log.e(TAG, "Erro no monitoramento: ${e.message}", e)
                // Em caso de erro, aguarda um pouco antes de tentar novamente
                delay(ERROR_RETRY_DELAY_MS)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d(TAG, "RebootMonitorService destruído")
    }
    
    companion object {
        private const val TAG = "RebootMonitorService"
        private const val CHECK_INTERVAL_MS = 30000L // Verifica a cada 30 segundos
        private const val ERROR_RETRY_DELAY_MS = 60000L // Em caso de erro, aguarda 1 minuto
    }
}
