package com.bootreceiver.app.service

import android.app.ActivityManager
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
import com.bootreceiver.app.ui.GelaFitWorkspaceActivity
import com.bootreceiver.app.utils.AppLauncher
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Serviço que monitora e bloqueia acesso a outros apps quando is_active = true
 * 
 * Este serviço:
 * 1. Monitora constantemente qual app está em foreground
 * 2. Se is_active = true e outro app (não autorizado) estiver aberto, fecha e mostra a tela do control
 * 3. Previne que outros apps sejam abertos quando is_active = true
 */
class AppBlockingService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    private var isActive = false
    private lateinit var deviceId: String
    private lateinit var preferenceManager: PreferenceManager
    private val allowedPackages = setOf(
        "com.bootreceiver.app",  // Próprio GelaFit Control
        "com.android.settings",  // Configurações (pode ser necessário)
        "com.android.systemui"    // System UI (necessário para funcionamento do sistema)
    )
    
    // Activities permitidas mesmo com kiosk ativo
    private val allowedActivities = setOf(
        "com.bootreceiver.app.ui.SettingsCheckActivity",
        "com.bootreceiver.app.ui.AddProductActivity",
        "com.bootreceiver.app.ui.GelaFitWorkspaceActivity"
    )
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "AppBlockingService criado")
        deviceId = DeviceIdManager.getDeviceId(this)
        preferenceManager = PreferenceManager(this)
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Verifica o cache primeiro (fonte de verdade)
        val cachedIsActive = preferenceManager.getIsActiveCached()
        val newIsActive = intent?.getBooleanExtra("is_active", cachedIsActive) ?: cachedIsActive
        
        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        if (newIsActive) {
            Log.d(TAG, "🔒 BLOQUEIO DE APPS ATIVADO (cache: $cachedIsActive)")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        } else {
            Log.d(TAG, "🔓 BLOQUEIO DE APPS DESATIVADO (cache: $cachedIsActive)")
            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        }
        
        // Usa o cache como fonte de verdade
        isActive = cachedIsActive
        
        // CRÍTICO: startForeground() DEVE ser chamado IMEDIATAMENTE quando iniciado como foreground service
        // Isso evita o erro ForegroundServiceDidNotStartInTimeException
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                createNotificationChannel()
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                Log.d(TAG, "✅ Foreground Service iniciado IMEDIATAMENTE")
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erro crítico ao iniciar Foreground Service: ${e.message}", e)
            }
        }
        
        // Sempre inicia o monitoramento (ele verifica o cache a cada 0.5s)
        if (!isRunning) {
            try {
                isRunning = true
                Log.d(TAG, "AppBlockingService iniciado - monitorando cache a cada 0.5s")
                
                // Inicia o monitoramento
                serviceScope.launch {
                    startMonitoring()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro crítico ao iniciar serviço: ${e.message}", e)
                isRunning = false
            }
        }
        
        return START_STICKY
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Bloqueio de Apps",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitora e bloqueia acesso a outros apps"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(): Notification {
        val intent = Intent(this, GelaFitWorkspaceActivity::class.java)
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
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GelaFit Control - Bloqueio Ativo")
            .setContentText("Acesso a outros apps bloqueado")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(false)
            .build()
    }
    
    /**
     * Inicia monitoramento constante de apps em foreground
     * Verifica o cache a cada 0.5s para resposta rápida
     */
    private suspend fun startMonitoring() {
        while (isRunning) {
            try {
                // Verifica o cache a cada 0.5s para resposta rápida
                val cachedIsActive = preferenceManager.getIsActiveCached()
                
                // Se is_active mudou no cache, atualiza a variável local
                if (cachedIsActive != isActive) {
                    isActive = cachedIsActive
                    Log.d(TAG, "🔄 Status atualizado do cache: is_active=$isActive")
                    
                    // Se is_active foi desativado, para o monitoramento
                    if (!isActive) {
                        Log.d(TAG, "is_active desativado no cache - parando monitoramento")
                        isRunning = false
                        stopSelf()
                        break
                    }
                }
                
                // Se is_active está ativo, continua monitorando
                if (isActive) {
                    val targetPackage = preferenceManager.getTargetPackageName()
                    
                    if (targetPackage.isNullOrEmpty()) {
                        Log.w(TAG, "Nenhum app configurado. Aguardando...")
                        delay(CACHE_CHECK_INTERVAL_MS)
                        continue
                    }
                    
                    // Verifica qual app está em foreground
                    val foregroundInfo = getForegroundInfo()
                    
                    if (foregroundInfo != null) {
                        val foregroundPackage = foregroundInfo.packageName
                        val foregroundActivity = foregroundInfo.activityName
                        
                        // Permite activities específicas do GelaFit Control mesmo com kiosk ativo
                        val isAllowedActivity = foregroundActivity != null && allowedActivities.any { 
                            foregroundActivity.contains(it.substringAfterLast("."))
                        }
                        
                        // Se não é o app configurado nem um app permitido, bloqueia
                        if (foregroundPackage != targetPackage && 
                            !allowedPackages.contains(foregroundPackage) && 
                            !isAllowedActivity) {
                            Log.w(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            Log.w(TAG, "🚫 APP NÃO AUTORIZADO DETECTADO: $foregroundPackage")
                            Log.w(TAG, "🔄 Fechando app não autorizado e mostrando tela do control...")
                            Log.w(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            
                            // Fecha o app não autorizado
                            closeApp(foregroundPackage)
                            
                            // Aguarda um pouco e abre a tela do control (não o app diretamente)
                            delay(100)
                            openControlScreen()
                        } else if (isAllowedActivity) {
                            Log.d(TAG, "✅ Activity permitida detectada: $foregroundActivity")
                        }
                    }
                }
                
                delay(CACHE_CHECK_INTERVAL_MS)
            } catch (e: Exception) {
                Log.e(TAG, "Erro no monitoramento: ${e.message}", e)
                delay(ERROR_RETRY_DELAY_MS)
            }
        }
        
        Log.d(TAG, "Monitoramento parado")
        isRunning = false
    }
    
    /**
     * Informação do app em foreground
     */
    data class ForegroundInfo(
        val packageName: String,
        val activityName: String?
    )
    
    /**
     * Obtém informações do app que está em foreground
     */
    private fun getForegroundInfo(): ForegroundInfo? {
        try {
            val activityManager = getSystemService(ActivityManager::class.java)
            
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val runningTasks = activityManager.getAppTasks()
                if (runningTasks != null && runningTasks.isNotEmpty()) {
                    val topTask = runningTasks[0]
                    val taskInfo = topTask.taskInfo
                    if (taskInfo != null && taskInfo.topActivity != null) {
                        return ForegroundInfo(
                            packageName = taskInfo.topActivity!!.packageName,
                            activityName = taskInfo.topActivity!!.className
                        )
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val runningTasks = activityManager.getRunningTasks(1)
                if (runningTasks.isNotEmpty()) {
                    val topActivity = runningTasks[0].topActivity
                    if (topActivity != null) {
                        return ForegroundInfo(
                            packageName = topActivity.packageName,
                            activityName = topActivity.className
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao obter app em foreground: ${e.message}", e)
        }
        
        return null
    }
    
    /**
     * Fecha um app específico
     */
    private fun closeApp(packageName: String) {
        try {
            val activityManager = getSystemService(ActivityManager::class.java)
            
            // Tenta fechar usando killBackgroundProcesses
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activityManager.killBackgroundProcesses(packageName)
            }
            
            // Tenta fechar usando am force-stop (requer permissão)
            try {
                val process = Runtime.getRuntime().exec("am force-stop $packageName")
                process.waitFor()
                Log.d(TAG, "✅ App $packageName fechado")
            } catch (e: Exception) {
                Log.w(TAG, "Não foi possível fechar app usando force-stop: ${e.message}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao fechar app: ${e.message}", e)
        }
    }
    
    /**
     * Abre o app configurado
     */
    private fun openConfiguredApp(packageName: String) {
        try {
            val appLauncher = AppLauncher(this)
            val success = appLauncher.launchApp(packageName)
            
            if (success) {
                Log.d(TAG, "✅ App configurado reaberto: $packageName")
            } else {
                Log.e(TAG, "❌ Falha ao reabrir app configurado")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao abrir app configurado: ${e.message}", e)
        }
    }
    
    /**
     * Abre a tela do control (GelaFitWorkspaceActivity) ao invés do app diretamente
     */
    private fun openControlScreen() {
        try {
            val intent = Intent(this, GelaFitWorkspaceActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or 
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
            Log.d(TAG, "✅ Tela do control aberta")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao abrir tela do control: ${e.message}", e)
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "⚠️ AppBlockingService destruído - tentando reiniciar após alguns segundos...")
        isRunning = false
        
        // Auto-restart após alguns segundos
        serviceScope.launch {
            try {
                delay(3000) // Aguarda 3 segundos antes de reiniciar
                Log.d(TAG, "🔄 Reiniciando AppBlockingService...")
                val restartIntent = Intent(this@AppBlockingService, AppBlockingService::class.java).apply {
                    putExtra("is_active", isActive)
                }
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
        
        // Reinicia o serviço após alguns segundos
        serviceScope.launch {
            try {
                delay(2000) // Aguarda 2 segundos
                val restartIntent = Intent(this@AppBlockingService, AppBlockingService::class.java).apply {
                    putExtra("is_active", isActive)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(restartIntent)
                } else {
                    startService(restartIntent)
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao reiniciar serviço após task removed: ${e.message}", e)
            }
        }
    }
    
    companion object {
        private const val TAG = "AppBlockingService"
        private const val CHANNEL_ID = "app_blocking_channel"
        private const val NOTIFICATION_ID = 3
        private const val CACHE_CHECK_INTERVAL_MS = 500L // Verifica cache a cada 0.5s para resposta rápida
        private const val ERROR_RETRY_DELAY_MS = 5000L // Em caso de erro, aguarda 5 segundos
    }
}
