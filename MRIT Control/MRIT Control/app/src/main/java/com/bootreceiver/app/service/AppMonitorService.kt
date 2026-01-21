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
import com.bootreceiver.app.ui.AppSelectionActivity
import com.bootreceiver.app.utils.AppLauncher
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Serviço que monitora quando o app escolhido abre/fecha
 * e garante que o GelaFit Control sempre esteja rodando em background
 * 
 * Este serviço:
 * 1. Monitora constantemente se o app escolhido está aberto
 * 2. Quando o app escolhido abre, garante que o GelaFit Control está rodando em background
 * 3. Mantém o serviço sempre ativo mesmo quando o app está fechado
 * 4. Permite reiniciar o app escolhido remotamente via comando
 */
class AppMonitorService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    private lateinit var deviceId: String
    private var targetPackageName: String? = null
    private var lastAppState: Boolean = false // true = app estava aberto, false = fechado
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "AppMonitorService criado")
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
            Log.d(TAG, "AppMonitorService iniciado para dispositivo: $deviceId")
            
            createNotificationChannel()
            
            // Inicia como Foreground Service
            try {
                val notification = createNotification()
                startForeground(NOTIFICATION_ID, notification)
                Log.d(TAG, "Foreground Service iniciado com sucesso")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao iniciar Foreground Service: ${e.message}", e)
            }
            
            // Inicia o monitoramento
            serviceScope.launch {
                startMonitoring()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro crítico ao iniciar serviço: ${e.message}", e)
            isRunning = false
        }
        
        return START_STICKY
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Monitoramento de App",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitora quando o app escolhido abre/fecha"
                setShowBadge(false)
            }
            
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
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
        
        val appName = targetPackageName ?: "Nenhum"
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GelaFit Control - Monitorando")
            .setContentText("Monitorando: $appName")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(false)
            .build()
    }
    
    /**
     * Inicia o monitoramento do app escolhido
     */
    private suspend fun startMonitoring() {
        val preferenceManager = PreferenceManager(this@AppMonitorService)
        targetPackageName = preferenceManager.getTargetPackageName()
        
        if (targetPackageName.isNullOrEmpty()) {
            Log.w(TAG, "⚠️ Nenhum app configurado. Serviço continuará rodando aguardando configuração.")
            // Continua rodando mesmo sem app configurado
            while (isRunning) {
                delay(CHECK_INTERVAL_MS)
                // Verifica novamente se foi configurado
                targetPackageName = preferenceManager.getTargetPackageName()
                if (!targetPackageName.isNullOrEmpty()) {
                    Log.d(TAG, "✅ App configurado: $targetPackageName")
                    break
                }
            }
        }
        
        Log.d(TAG, "🔍 Iniciando monitoramento do app: $targetPackageName")
        
        while (isRunning) {
            try {
                val currentAppState = isAppRunning(targetPackageName!!)
                
                // Se o estado mudou (app abriu ou fechou)
                if (currentAppState != lastAppState) {
                    if (currentAppState) {
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        Log.d(TAG, "✅ APP ESCOLHIDO ABRIU: $targetPackageName")
                        Log.d(TAG, "🔧 Garantindo que GelaFit Control está rodando em background...")
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        
                        // Garante que os serviços do GelaFit Control estão rodando
                        ensureGelaFitControlServicesRunning()
                    } else {
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        Log.d(TAG, "⚠️ APP ESCOLHIDO FECHOU: $targetPackageName")
                        Log.d(TAG, "🔧 GelaFit Control continua rodando em background...")
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                    }
                    
                    lastAppState = currentAppState
                }
                
                // Se o app está aberto, garante que os serviços estão rodando
                if (currentAppState) {
                    ensureGelaFitControlServicesRunning()
                }
                
                delay(CHECK_INTERVAL_MS)
            } catch (e: Exception) {
                Log.e(TAG, "Erro no monitoramento: ${e.message}", e)
                delay(ERROR_RETRY_DELAY_MS)
            }
        }
    }
    
    /**
     * Garante que os serviços do GelaFit Control estão rodando
     */
    private fun ensureGelaFitControlServicesRunning() {
        try {
            // Inicia o serviço de monitoramento de comandos
            val restartMonitorIntent = Intent(this, AppRestartMonitorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(restartMonitorIntent)
            } else {
                startService(restartMonitorIntent)
            }
            
            // Inicia o serviço de modo kiosk
            val kioskIntent = Intent(this, KioskModeService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(kioskIntent)
            } else {
                startService(kioskIntent)
            }
            
            Log.d(TAG, "✅ Serviços do GelaFit Control garantidos")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao garantir serviços: ${e.message}", e)
        }
    }
    
    /**
     * Verifica se um app está rodando em foreground
     */
    private fun isAppRunning(packageName: String): Boolean {
        try {
            val activityManager = getSystemService(ActivityManager::class.java)
            
            // Método 1: Verifica processos em foreground
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val runningProcesses = activityManager.runningAppProcesses
                val isForeground = runningProcesses?.any { 
                    it.processName == packageName && 
                    (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                     it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE)
                } == true
                
                if (isForeground) {
                    return true
                }
            }
            
            // Método 2: Verifica a activity no topo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val runningTasks = activityManager.getAppTasks()
                if (runningTasks != null && runningTasks.isNotEmpty()) {
                    for (task in runningTasks) {
                        val taskInfo = task.taskInfo
                        if (taskInfo != null && taskInfo.topActivity != null) {
                            if (taskInfo.topActivity!!.packageName == packageName) {
                                return true
                            }
                        }
                    }
                }
            } else {
                @Suppress("DEPRECATION")
                val runningTasks = activityManager.getRunningTasks(1)
                if (runningTasks.isNotEmpty()) {
                    val topActivity = runningTasks[0].topActivity
                    if (topActivity != null && topActivity.packageName == packageName) {
                        return true
                    }
                }
            }
            
            // Método 3: Verifica se o processo existe
            val runningProcesses = activityManager.runningAppProcesses
            val processExists = runningProcesses?.any { 
                it.processName == packageName
            } == true
            
            return processExists
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar se app está rodando: ${e.message}", e)
            return false
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d(TAG, "⚠️ AppMonitorService destruído - tentando reiniciar...")
        
        // Tenta reiniciar o serviço automaticamente
        serviceScope.launch {
            try {
                delay(2000)
                val restartIntent = Intent(this@AppMonitorService, AppMonitorService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(restartIntent)
                } else {
                    startService(restartIntent)
                }
                Log.d(TAG, "🔄 AppMonitorService reiniciado")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao reiniciar serviço: ${e.message}", e)
            }
        }
    }
    
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "⚠️ App removido da lista de tarefas - mas serviço continua rodando")
        // O serviço continua rodando mesmo se o app for fechado
    }
    
    companion object {
        private const val TAG = "AppMonitorService"
        private const val CHANNEL_ID = "app_monitor_channel"
        private const val NOTIFICATION_ID = 3
        private const val CHECK_INTERVAL_MS = 2000L // Verifica a cada 2 segundos
        private const val ERROR_RETRY_DELAY_MS = 5000L // Em caso de erro, aguarda 5 segundos
    }
}
