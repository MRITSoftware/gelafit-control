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
import com.bootreceiver.app.utils.AppLauncher
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PreferenceManager
import com.bootreceiver.app.utils.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Serviço que monitora periodicamente o Supabase para verificar
 * se há comandos de reiniciar o app configurado
 * 
 * Este serviço:
 * 1. Verifica a cada 30 segundos se há um comando de reiniciar app
 * 2. Se encontrar, fecha e reabre o app configurado
 * 3. Marca o comando como executado após reiniciar
 */
class AppRestartMonitorService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    private val supabaseManager = SupabaseManager()
    private lateinit var deviceId: String
    private var isRestarting = false // Flag para evitar múltiplos reinícios simultâneos
    private val processedCommandIds = mutableSetOf<String>() // IDs de comandos já processados nesta sessão
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "AppRestartMonitorService criado")
        deviceId = DeviceIdManager.getDeviceId(this)
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            Log.d(TAG, "Serviço já está rodando")
            return START_STICKY
        }
        
        try {
            isRunning = true
            Log.d(TAG, "AppRestartMonitorService iniciado para dispositivo: $deviceId")
            
            // Garante que o canal de notificação existe
            createNotificationChannel()
            
            // Inicia como Foreground Service
            try {
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                Log.d(TAG, "Foreground Service iniciado com sucesso")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao iniciar Foreground Service: ${e.message}", e)
            }
            
            // Inicia o monitoramento em uma coroutine
            serviceScope.launch {
                startMonitoring()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro crítico ao iniciar serviço: ${e.message}", e)
            isRunning = false
        }
        
        return START_STICKY
    }
    
    /**
     * Cria o canal de notificação (necessário para Android 8.0+)
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Monitoramento de App",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitora comandos de reiniciar app do Supabase"
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
        
        val smallIcon = android.R.drawable.ic_dialog_info
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GelaFit Control - Monitorando")
            .setContentText("Monitorando comandos de reiniciar app...")
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
                
                // Verifica se já está reiniciando (evita múltiplos reinícios simultâneos)
                if (isRestarting) {
                    Log.d(TAG, "⏳ Reinício já em andamento, aguardando...")
                    delay(CHECK_INTERVAL_MS)
                    continue
                }
                
                // Busca comando pendente (retorna o ID do comando se houver)
                val commandInfo = supabaseManager.getRestartAppCommand(deviceId)
                
                if (commandInfo != null) {
                    val commandId = commandInfo.id
                    
                    // Verifica se este comando já foi processado nesta sessão
                    if (commandId != null && processedCommandIds.contains(commandId)) {
                        Log.d(TAG, "ℹ️ Comando já foi processado nesta sessão, ignorando...")
                        delay(CHECK_INTERVAL_MS)
                        continue
                    }
                    
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                    Log.d(TAG, "⚠️⚠️⚠️ COMANDO DE REINICIAR APP ENCONTRADO! ⚠️⚠️⚠️")
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                    
                    // Marca que está reiniciando
                    isRestarting = true
                    
                    // Obtém o app configurado
                    val preferenceManager = PreferenceManager(this@AppRestartMonitorService)
                    val targetPackageName = preferenceManager.getTargetPackageName()
                    
                    if (targetPackageName.isNullOrEmpty()) {
                        Log.w(TAG, "Nenhum app configurado. Não é possível reiniciar.")
                        // Marca como executado mesmo assim para não ficar em loop
                        val marked = supabaseManager.markCommandAsExecutedById(commandId)
                        if (marked) {
                            Log.d(TAG, "✅ Comando marcado como executado (sem app configurado)")
                            if (commandId != null) processedCommandIds.add(commandId)
                        } else {
                            Log.e(TAG, "❌ Falha ao marcar comando como executado!")
                        }
                        isRestarting = false
                    } else {
                        Log.d(TAG, "App configurado: $targetPackageName")
                        
                        // CRÍTICO: Marca como executado ANTES de reiniciar
                        // Isso garante que mesmo se o app reiniciar, o comando já está marcado
                        Log.d(TAG, "📝 Marcando comando como executado no Supabase...")
                        val marked = supabaseManager.markCommandAsExecutedById(commandId)
                        
                        if (!marked) {
                            Log.e(TAG, "❌ FALHA CRÍTICA: Não foi possível marcar comando como executado!")
                            Log.e(TAG, "⚠️ Tentando deletar comando como alternativa...")
                            // Tenta deletar como alternativa
                            val deleted = supabaseManager.deleteCommandById(commandId)
                            if (!deleted) {
                                Log.e(TAG, "❌ Também falhou ao deletar comando. Abortando reinício.")
                                delay(ERROR_RETRY_DELAY_MS)
                                isRestarting = false
                                continue
                            } else {
                                Log.d(TAG, "✅ Comando deletado como alternativa")
                            }
                        } else {
                            Log.d(TAG, "✅ Comando marcado como executado com sucesso!")
                        }
                        
                        // Adiciona à lista de comandos processados
                        if (commandId != null) {
                            processedCommandIds.add(commandId)
                        }
                        
                        // Verifica novamente se o comando foi realmente processado (double-check)
                        delay(2000) // Aguarda 2 segundos para garantir que foi salvo no banco
                        val stillHasCommand = supabaseManager.getRestartAppCommand(deviceId)
                        if (stillHasCommand != null && stillHasCommand.id == commandId) {
                            Log.w(TAG, "⚠️ Comando ainda aparece como pendente após processar!")
                            Log.w(TAG, "⚠️ Tentando deletar como fallback...")
                            supabaseManager.deleteCommandById(commandId)
                            delay(1000)
                        }
                        
                        // Reinicia o app
                        Log.d(TAG, "🔄 Reiniciando app: $targetPackageName")
                        val appLauncher = AppLauncher(this@AppRestartMonitorService)
                        val success = appLauncher.restartApp(targetPackageName)
                        
                        if (success) {
                            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            Log.d(TAG, "✅✅✅ APP REINICIADO COM SUCESSO! ✅✅✅")
                            Log.d(TAG, "✅ Comando foi executado e marcado como executado no banco")
                            Log.d(TAG, "ℹ️ Não reiniciará novamente até que um NOVO comando seja criado")
                            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        } else {
                            Log.e(TAG, "❌ Falha ao reiniciar app: $targetPackageName")
                        }
                        
                        // Libera flag de reinício após um tempo
                        delay(5000) // Aguarda 5 segundos antes de liberar
                        isRestarting = false
                    }
                } else {
                    Log.d(TAG, "ℹ️ Nenhum comando de reiniciar app pendente")
                    // Se não há comando, reseta flag de reinício (caso tenha ficado travada)
                    if (isRestarting) {
                        Log.w(TAG, "⚠️ Flag de reinício estava travada, resetando...")
                        isRestarting = false
                    }
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
        Log.d(TAG, "⚠️ AppRestartMonitorService destruído - tentando reiniciar...")
        
        // Sempre tenta reiniciar o serviço para garantir que sempre esteja rodando
        serviceScope.launch {
            try {
                delay(1000) // Aguarda 1 segundo
                Log.d(TAG, "🔄 Reiniciando AppRestartMonitorService...")
                val restartIntent = Intent(this@AppRestartMonitorService, AppRestartMonitorService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(restartIntent)
                } else {
                    startService(restartIntent)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao tentar reiniciar serviço: ${e.message}", e)
            }
        }
        
        isRunning = false
    }
    
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "⚠️ App removido da lista de tarefas - mas serviço continua rodando")
        
        // Reinicia o serviço imediatamente quando o app é removido
        val restartIntent = Intent(this, AppRestartMonitorService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(restartIntent)
        } else {
            startService(restartIntent)
        }
    }
    
    companion object {
        private const val TAG = "AppRestartMonitor"
        private const val CHANNEL_ID = "app_restart_monitor_channel"
        private const val NOTIFICATION_ID = 1
        private const val CHECK_INTERVAL_MS = 30000L // Verifica a cada 30 segundos
        private const val ERROR_RETRY_DELAY_MS = 60000L // Em caso de erro, aguarda 1 minuto
    }
}
