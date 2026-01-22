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
import com.bootreceiver.app.utils.DeviceCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var isRunning = false
    private var monitoringJob: Job? = null
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
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            Log.d(TAG, "🔄 AppRestartMonitorService iniciado")
            Log.d(TAG, "📱 Device ID: $deviceId")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
            
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
     * Inicia o monitoramento REALTIME de comandos de reiniciar
     * Usa polling otimizado (1 segundo) para detectar comandos rapidamente
     */
    private fun startMonitoring() {
        Log.d(TAG, "🔄 Iniciando monitoramento REALTIME de comandos (1 segundo)")
        
        monitoringJob = serviceScope.launch {
            try {
                supabaseManager.subscribeToRestartCommands(deviceId)
                    .onEach { command ->
                        // Recebe comandos em tempo real
                        if (!isRestarting) {
                            Log.d(TAG, "🔄 REALTIME: Comando de reiniciar detectado: ${command.id}")
                            processRestartCommand(command)
                        } else {
                            Log.d(TAG, "⏳ Reinício já em andamento, ignorando comando...")
                        }
                    }
                    .catch { e ->
                        Log.e(TAG, "❌ Erro no monitoramento Realtime: ${e.message}", e)
                        // Em caso de erro, tenta reconectar após delay
                        kotlinx.coroutines.delay(ERROR_RETRY_DELAY_MS)
                        if (isRunning) {
                            // Tenta reconectar
                            monitoringJob?.cancel()
                            startMonitoring()
                        }
                    }
                    .launchIn(this)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erro ao iniciar subscription Realtime: ${e.message}", e)
            }
        }
    }
    
    /**
     * Processa um comando de reiniciar app
     */
    private suspend fun processRestartCommand(command: DeviceCommand) {
        if (isRestarting) {
            Log.d(TAG, "⏳ Reinício já em andamento, ignorando comando...")
            return
        }
        
        val commandId = command.id
        if (commandId != null && processedCommandIds.contains(commandId)) {
            Log.d(TAG, "ℹ️ Comando já foi processado, ignorando...")
            return
        }
        
        isRestarting = true
        
        val preferenceManager = PreferenceManager(this@AppRestartMonitorService)
        val targetPackageName = preferenceManager.getTargetPackageName()
        
        if (targetPackageName.isNullOrEmpty()) {
            Log.w(TAG, "Nenhum app configurado. Não é possível reiniciar.")
            supabaseManager.markCommandAsExecutedById(commandId)
            if (commandId != null) processedCommandIds.add(commandId)
            isRestarting = false
            return
        }
        
        Log.d(TAG, "App configurado: $targetPackageName")
        
        // Marca como executado ANTES de reiniciar
        val marked = supabaseManager.markCommandAsExecutedById(commandId)
        if (!marked) {
            Log.e(TAG, "❌ Falha ao marcar comando como executado!")
            val deleted = supabaseManager.deleteCommandById(commandId)
            if (!deleted) {
                Log.e(TAG, "❌ Também falhou ao deletar. Abortando.")
                isRestarting = false
                return
            }
        }
        
        if (commandId != null) processedCommandIds.add(commandId)
        
        // Reinicia o app
        Log.d(TAG, "🔄 Reiniciando app: $targetPackageName")
        val appLauncher = AppLauncher(this@AppRestartMonitorService)
        
        // Fecha o app primeiro
        try {
            val activityManager = getSystemService(android.app.ActivityManager::class.java)
            activityManager.killBackgroundProcesses(targetPackageName)
            Runtime.getRuntime().exec("am force-stop $targetPackageName").waitFor()
            Log.d(TAG, "✅ App fechado")
        } catch (e: Exception) {
            Log.w(TAG, "Erro ao fechar app: ${e.message}")
        }
        
        delay(1000)
        
        // Reabre o app
        val success = appLauncher.launchApp(targetPackageName)
        if (success) {
            Log.d(TAG, "✅ App reiniciado com sucesso!")
        } else {
            Log.e(TAG, "❌ Falha ao reabrir app")
        }
        
        isRestarting = false
    }
    
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "⚠️ AppRestartMonitorService destruído - tentando reiniciar...")
        
        // Para monitoramento
        isRunning = false
        monitoringJob?.cancel()
        monitoringJob = null
        
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
        private const val CHECK_INTERVAL_MS = 5 * 60 * 1000L // Verifica a cada 5 minutos (economiza requisições)
        private const val ERROR_RETRY_DELAY_MS = 60000L // Em caso de erro, aguarda 1 minuto
    }
}
