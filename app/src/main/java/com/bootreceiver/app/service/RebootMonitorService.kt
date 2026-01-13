package com.bootreceiver.app.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.bootreceiver.app.R
import com.bootreceiver.app.ui.AppSelectionActivity
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
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            Log.d(TAG, "Serviço já está rodando")
            return START_STICKY
        }
        
        try {
            isRunning = true
            Log.d(TAG, "RebootMonitorService iniciado para dispositivo: $deviceId")
            
            // Garante que o canal de notificação existe antes de criar a notificação
            createNotificationChannel()
            
            // Inicia como Foreground Service para garantir que continue rodando
            try {
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                Log.d(TAG, "Foreground Service iniciado com sucesso")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao iniciar Foreground Service: ${e.message}", e)
                // Tenta continuar mesmo sem foreground service
            }
            
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
        } catch (e: Exception) {
            Log.e(TAG, "Erro crítico ao iniciar serviço: ${e.message}", e)
            isRunning = false
        }
        
        // Retorna START_STICKY para que o serviço seja reiniciado se for morto
        return START_STICKY
    }
    
    /**
     * Cria o canal de notificação (necessário para Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Monitoramento de Comandos",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitora comandos de reiniciar do Supabase"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    /**
     * Cria a notificação para o Foreground Service
     */
    private fun createNotification(): Notification {
        val intent = Intent(this, AppSelectionActivity::class.java)
        val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            pendingIntentFlags
        )
        
        // Usa um ícone simples para notificações (drawable do sistema como fallback seguro)
        val smallIcon = android.R.drawable.ic_dialog_info
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("MRIT Control - Monitorando")
            .setContentText("Monitorando comandos de reiniciar...")
            .setSmallIcon(smallIcon)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(false)
            .build()
    }
    
    /**
     * Inicia o monitoramento periódico do banco de dados
     */
    private suspend fun startMonitoring() {
        while (isRunning) {
            try {
                Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                Log.d(TAG, "🔍 Ciclo de verificação #${System.currentTimeMillis() / CHECK_INTERVAL_MS}")
                Log.d(TAG, "Device ID: $deviceId")
                Log.d(TAG, "Device Admin ativo: ${rebootManager.isDeviceAdminActive()}")
                
                val hasRebootCommand = supabaseManager.checkRebootCommand(deviceId)
                
                if (hasRebootCommand) {
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                    Log.d(TAG, "⚠️⚠️⚠️ COMANDO DE REINICIAR ENCONTRADO! ⚠️⚠️⚠️")
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                    
                    // Verifica Device Admin antes de tentar reiniciar
                    val isDeviceAdminActive = rebootManager.isDeviceAdminActive()
                    Log.d(TAG, "📋 Status Device Admin: $isDeviceAdminActive")
                    
                    if (!isDeviceAdminActive) {
                        Log.e(TAG, "❌❌❌ Device Admin NÃO está ativo! ❌❌❌")
                        Log.e(TAG, "Por favor, ative o Device Admin nas configurações do dispositivo.")
                        Log.e(TAG, "Configurações → Segurança → Administradores do dispositivo → MRIT Control")
                        // Solicita Device Admin novamente
                        rebootManager.requestDeviceAdmin()
                        // Aguarda antes da próxima verificação
                        delay(CHECK_INTERVAL_MS)
                        continue
                    }
                    
                    // Marca como executado antes de reiniciar (para evitar loop)
                    Log.d(TAG, "📝 Marcando comando como executado no Supabase...")
                    val marked = supabaseManager.markCommandAsExecuted(deviceId)
                    Log.d(TAG, "   Resultado: $marked")
                    
                    // Aguarda um pouco para garantir que o comando foi salvo
                    delay(2000)
                    
                    // Tenta reiniciar
                    Log.d(TAG, "🚀 Iniciando processo de reinicialização...")
                    val rebootSuccess = rebootManager.reboot()
                    
                    if (rebootSuccess) {
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        Log.d(TAG, "✅✅✅ COMANDO DE REINICIAR ENVIADO! ✅✅✅")
                        Log.d(TAG, "O dispositivo DEVE reiniciar em alguns segundos...")
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        // O dispositivo será reiniciado, então o serviço será parado
                        delay(3000) // Aguarda um pouco antes de parar o serviço
                        stopSelf()
                        return
                    } else {
                        Log.e(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        Log.e(TAG, "❌❌❌ FALHA AO REINICIAR DISPOSITIVO ❌❌❌")
                        Log.e(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        Log.e(TAG, "Verifique os logs acima para detalhes do erro.")
                        Log.e(TAG, "Possíveis causas:")
                        Log.e(TAG, "  1. Device Admin não está realmente ativo")
                        Log.e(TAG, "  2. Fabricante bloqueou reboot remoto")
                        Log.e(TAG, "  3. Dispositivo não suporta DevicePolicyManager.reboot()")
                        Log.e(TAG, "  4. Permissões insuficientes")
                        // Se falhar, continua monitorando
                    }
                } else {
                    Log.d(TAG, "ℹ️ Nenhum comando de reiniciar pendente")
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
        private const val CHANNEL_ID = "reboot_monitor_channel"
        private const val NOTIFICATION_ID = 1
        private const val CHECK_INTERVAL_MS = 30000L // Verifica a cada 30 segundos
        private const val ERROR_RETRY_DELAY_MS = 60000L // Em caso de erro, aguarda 1 minuto
    }
}
