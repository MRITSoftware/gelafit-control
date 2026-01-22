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
import com.bootreceiver.app.ui.GelaFitWorkspaceActivity
import com.bootreceiver.app.utils.AppLauncher
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PreferenceManager
import com.bootreceiver.app.utils.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Serviço que monitora o modo kiosk do dispositivo
 * 
 * Este serviço:
 * 1. Verifica periodicamente se kiosk_mode está ativo no Supabase
 * 2. Se ativo, previne que o app configurado seja minimizado
 * 3. Se o app estiver minimizado e kiosk_mode for ativado, traz de volta
 */
class KioskModeService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    private val supabaseManager = SupabaseManager()
    private lateinit var deviceId: String
    private lateinit var preferenceManager: PreferenceManager
    private var lastKioskMode: Boolean? = null
    private var lastIsActive: Boolean? = null
    private var lastWorkspaceLaunchMs: Long = 0
    private var workspaceMonitoringJob: Job? = null // Job para monitoramento contínuo do workspace
    private val SYNC_INTERVAL_MS = 15 * 60 * 1000L // 15 minutos
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "KioskModeService criado")
        deviceId = DeviceIdManager.getDeviceId(this)
        preferenceManager = PreferenceManager(this)
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            Log.d(TAG, "Serviço já está rodando")
            return START_STICKY
        }
        
        try {
            isRunning = true
            Log.d(TAG, "KioskModeService iniciado para dispositivo: $deviceId")
            
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
                "Modo Kiosk",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Monitora modo kiosk do dispositivo"
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
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("GelaFit Control - Modo Kiosk")
            .setContentText("Monitorando modo kiosk...")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(false)
            .build()
    }
    
    private suspend fun startMonitoring() {
        var firstSyncDone = false
        while (isRunning) {
            try {
                // Sempre faz 1ª sincronização imediata com o Supabase
                val lastSync = preferenceManager.getStatusLastSync()
                val now = System.currentTimeMillis()
                val needsSync = !firstSyncDone || (now - lastSync) >= SYNC_INTERVAL_MS
                
                val isActive: Boolean
                val kioskMode: Boolean
                
                if (needsSync) {
                    Log.d(TAG, "🔄 Sincronizando status com Supabase (firstSync=${!firstSyncDone})...")
                    val status = supabaseManager.getDeviceStatus(deviceId)
                    isActive = status?.isActive ?: false
                    kioskMode = status?.kioskMode ?: false
                    preferenceManager.saveIsActiveCached(isActive)
                    preferenceManager.saveKioskModeCached(kioskMode)
                    preferenceManager.saveStatusLastSync(now)
                    firstSyncDone = true
                    Log.d(TAG, "✅ Cache atualizado: is_active=$isActive, kiosk_mode=$kioskMode")
                } else {
                    // Usa cache local entre sincronizações de 15 min
                    isActive = preferenceManager.getIsActiveCached()
                    kioskMode = preferenceManager.getKioskModeCached()
                    Log.d(TAG, "📦 Usando cache local: is_active=$isActive, kiosk_mode=$kioskMode")
                }

                val changed = (lastIsActive != isActive) || (lastKioskMode != kioskMode)

                if (changed) {
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                    Log.d(TAG, "is_active: $isActive | kiosk_mode: $kioskMode")
                }

                // Controle da casca (is_active)
                if (isActive) {
                    startAppBlocking()
                    setOverlayEnabled(true)

                    if (!kioskMode) {
                        // Mostra a casca simples para o usuário escolher abrir o app
                        maybeLaunchWorkspace()
                        
                        // Inicia monitoramento contínuo do workspace se ainda não estiver ativo
                        if (workspaceMonitoringJob == null || !workspaceMonitoringJob!!.isActive) {
                            startWorkspaceMonitoring()
                        }
                    } else {
                        // Se kiosk_mode está ativo, para o monitoramento do workspace
                        stopWorkspaceMonitoring()
                    }
                } else {
                    stopAppBlocking()
                    setOverlayEnabled(false)
                    stopWorkspaceMonitoring()
                }

                // Controle do kiosk do app-alvo
                if (lastKioskMode != kioskMode) {
                    if (kioskMode) {
                        Log.d(TAG, "🔒 MODO KIOSK DO APP ATIVADO")
                        applyKioskMode()
                    } else {
                        Log.d(TAG, "🔓 MODO KIOSK DO APP DESATIVADO")
                        removeKioskMode()
                    }
                } else if (kioskMode) {
                    // Se kiosk está ativo, verifica constantemente se o app está rodando
                    ensureAppIsRunning()
                }

                lastIsActive = isActive
                lastKioskMode = kioskMode

                if (changed) {
                    Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                }

                // Verifica a cada 2 segundos (resposta muito mais rápida quando setado manualmente)
                delay(2 * 1000L)
            } catch (e: Exception) {
                Log.e(TAG, "Erro no monitoramento: ${e.message}", e)
                delay(ERROR_RETRY_DELAY_MS)
            }
        }
    }
    
    /**
     * Aplica o modo kiosk: garante que o app configurado esteja rodando
     * e inicia monitoramento agressivo para prevenir minimização
     */
    private fun applyKioskMode() {
        val preferenceManager = PreferenceManager(this)
        val targetPackage = preferenceManager.getTargetPackageName()
        
        if (targetPackage.isNullOrEmpty()) {
            Log.w(TAG, "⚠️ Nenhum app configurado. Não é possível aplicar modo kiosk.")
            return
        }
        
        Log.d(TAG, "🔒 Aplicando modo kiosk para: $targetPackage")
        
        // FORÇA a abertura do app (mesmo que já esteja rodando, garante que está em foreground)
        serviceScope.launch {
            try {
                Log.d(TAG, "📱 Abrindo app configurado: $targetPackage")
                val appLauncher = AppLauncher(this@KioskModeService)
                
                // Tenta abrir o app múltiplas vezes para garantir
                var success = false
                for (i in 1..3) {
                    success = appLauncher.launchApp(targetPackage)
                    if (success) {
                        Log.d(TAG, "✅ App aberto com sucesso na tentativa $i")
                        break
                    } else {
                        Log.w(TAG, "⚠️ Tentativa $i falhou, tentando novamente...")
                        delay(500)
                    }
                }
                
                if (!success) {
                    Log.e(TAG, "❌ Falha ao abrir app após 3 tentativas")
                }
                
                // Aguarda um pouco e verifica se está rodando
                delay(1000)
                if (isAppRunning(targetPackage)) {
                    Log.d(TAG, "✅ App confirmado rodando")
                } else {
                    Log.w(TAG, "⚠️ App não está rodando após abertura")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao abrir app: ${e.message}", e)
            }
        }
        
        // Inicia overlay para interceptar gestos (requer permissão SYSTEM_ALERT_WINDOW)
        try {
            val overlayIntent = Intent(this, com.bootreceiver.app.service.KioskOverlayService::class.java).apply {
                putExtra("kiosk_enabled", true)
            }
            startService(overlayIntent)
            Log.d(TAG, "📡 Overlay de kiosk iniciado")
        } catch (e: Exception) {
            Log.w(TAG, "Não foi possível iniciar overlay (pode precisar de permissão): ${e.message}")
        }
        
        // Inicia monitoramento agressivo em uma coroutine separada
        serviceScope.launch {
            aggressiveKioskMonitoring(targetPackage)
        }
    }
    
    /**
     * Monitoramento agressivo do app quando kiosk está ativo
     * Verifica constantemente e reabre imediatamente se minimizado ou fechado
     */
    private suspend fun aggressiveKioskMonitoring(targetPackage: String) {
        var consecutiveFailures = 0
        while (isRunning) {
            try {
                val kioskMode = supabaseManager.getKioskMode(deviceId)
                if (kioskMode == true) {
                    if (!isAppRunning(targetPackage)) {
                        consecutiveFailures++
                        Log.d(TAG, "🚨 APP FECHADO/MINIMIZADO! REABRINDO IMEDIATAMENTE... (tentativa $consecutiveFailures)")
                        val appLauncher = AppLauncher(this@KioskModeService)
                        
                        // Tenta abrir o app múltiplas vezes rapidamente
                        appLauncher.launchApp(targetPackage)
                        delay(300) // Aguarda 300ms
                        
                        // Se ainda não está rodando, tenta novamente
                        if (!isAppRunning(targetPackage)) {
                            Log.d(TAG, "⚠️ Tentativa 2: Reabrindo app...")
                            appLauncher.launchApp(targetPackage)
                            delay(500)
                        }
                        
                        // Se ainda não está rodando, tenta mais uma vez
                        if (!isAppRunning(targetPackage)) {
                            Log.d(TAG, "⚠️ Tentativa 3: Reabrindo app...")
                            appLauncher.launchApp(targetPackage)
                        }
                    } else {
                        // App está rodando, reseta contador de falhas
                        if (consecutiveFailures > 0) {
                            Log.d(TAG, "✅ App reaberto com sucesso após $consecutiveFailures tentativas")
                            consecutiveFailures = 0
                        }
                    }
                    delay(CHECK_INTERVAL_MS) // Verifica muito frequentemente
                } else {
                    // Se kiosk foi desativado, para o monitoramento agressivo
                    Log.d(TAG, "🔓 Kiosk desativado - parando monitoramento agressivo")
                    break
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro no monitoramento agressivo: ${e.message}", e)
                delay(ERROR_RETRY_DELAY_MS)
            }
        }
    }
    
    /**
     * Remove o modo kiosk (permite minimizar normalmente)
     */
    private fun removeKioskMode() {
        Log.d(TAG, "🔓 Modo kiosk removido. App pode ser minimizado normalmente.")
        
        // Remove overlay
        try {
            val overlayIntent = Intent(this, com.bootreceiver.app.service.KioskOverlayService::class.java).apply {
                putExtra("kiosk_enabled", false)
            }
            startService(overlayIntent)
            Log.d(TAG, "📡 Overlay de kiosk removido")
        } catch (e: Exception) {
            Log.w(TAG, "Erro ao remover overlay: ${e.message}")
        }
    }

    /**
     * Liga/desliga overlay transparente para bloquear gestos do sistema.
     */
    private fun setOverlayEnabled(enabled: Boolean) {
        try {
            val overlayIntent = Intent(this, com.bootreceiver.app.service.KioskOverlayService::class.java).apply {
                putExtra("kiosk_enabled", enabled)
            }
            startService(overlayIntent)
        } catch (e: Exception) {
            Log.w(TAG, "Não foi possível atualizar overlay: ${e.message}")
        }
    }

    /**
     * Inicia bloqueio de apps quando is_active = true.
     */
    private fun startAppBlocking() {
        try {
            val intent = Intent(this, com.bootreceiver.app.service.AppBlockingService::class.java).apply {
                putExtra("is_active", true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(intent)
            } else {
                startService(intent)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Erro ao iniciar bloqueio de apps: ${e.message}")
        }
    }

    /**
     * Para bloqueio de apps quando is_active = false.
     */
    private fun stopAppBlocking() {
        try {
            val intent = Intent(this, com.bootreceiver.app.service.AppBlockingService::class.java).apply {
                putExtra("is_active", false)
            }
            startService(intent)
        } catch (e: Exception) {
            Log.w(TAG, "Erro ao parar bloqueio de apps: ${e.message}")
        }
    }

    /**
     * Lança a tela simples do control quando apenas is_active está ativo.
     */
    private fun maybeLaunchWorkspace() {
        val now = System.currentTimeMillis()
        // evita abrir a cada ciclo; no máximo a cada 3s
        if (now - lastWorkspaceLaunchMs < 3000) return
        lastWorkspaceLaunchMs = now

        try {
            val intent = Intent(this, GelaFitWorkspaceActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
            startActivity(intent)
        } catch (e: Exception) {
            Log.w(TAG, "Não foi possível abrir a workspace: ${e.message}")
        }
    }
    
    /**
     * Inicia monitoramento contínuo do GelaFitWorkspaceActivity quando is_active = true
     * Garante que a activity seja reaberta automaticamente se fechar
     */
    private fun startWorkspaceMonitoring() {
        // Para monitoramento anterior se existir
        stopWorkspaceMonitoring()
        
        Log.d(TAG, "🔄 Iniciando monitoramento contínuo do GelaFitWorkspaceActivity")
        
        workspaceMonitoringJob = serviceScope.launch {
            aggressiveWorkspaceMonitoring()
        }
    }
    
    /**
     * Para o monitoramento contínuo do workspace
     */
    private fun stopWorkspaceMonitoring() {
        workspaceMonitoringJob?.cancel()
        workspaceMonitoringJob = null
        Log.d(TAG, "🛑 Monitoramento do workspace parado")
    }
    
    /**
     * Monitoramento agressivo do GelaFitWorkspaceActivity quando is_active = true
     * Verifica constantemente e reabre imediatamente se fechar
     */
    private suspend fun aggressiveWorkspaceMonitoring() {
        var consecutiveFailures = 0
        val workspacePackage = packageName // Package do próprio GelaFit Control
        val workspaceActivity = "com.bootreceiver.app.ui.GelaFitWorkspaceActivity"
        
        while (isRunning) {
            try {
                // Verifica cache para resposta rápida
                val cachedIsActive = preferenceManager.getIsActiveCached()
                val cachedKioskMode = preferenceManager.getKioskModeCached()
                
                // Só monitora se is_active = true e kiosk_mode = false
                if (cachedIsActive && !cachedKioskMode) {
                    if (!isWorkspaceRunning()) {
                        consecutiveFailures++
                        Log.d(TAG, "🚨 GELAFIT WORKSPACE FECHADO! REABRINDO IMEDIATAMENTE... (tentativa $consecutiveFailures)")
                        
                        // Tenta abrir a workspace múltiplas vezes rapidamente
                        try {
                            val intent = Intent(this@KioskModeService, GelaFitWorkspaceActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                        Intent.FLAG_ACTIVITY_SINGLE_TOP
                            }
                            startActivity(intent)
                            delay(300) // Aguarda 300ms
                            
                            // Se ainda não está rodando, tenta novamente
                            if (!isWorkspaceRunning()) {
                                Log.d(TAG, "⚠️ Tentativa 2: Reabrindo workspace...")
                                startActivity(intent)
                                delay(500)
                            }
                            
                            // Se ainda não está rodando, tenta mais uma vez
                            if (!isWorkspaceRunning()) {
                                Log.d(TAG, "⚠️ Tentativa 3: Reabrindo workspace...")
                                startActivity(intent)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Erro ao reabrir workspace: ${e.message}", e)
                        }
                    } else {
                        // Workspace está rodando, reseta contador de falhas
                        if (consecutiveFailures > 0) {
                            Log.d(TAG, "✅ Workspace reaberto com sucesso após $consecutiveFailures tentativas")
                            consecutiveFailures = 0
                        }
                    }
                    delay(CHECK_INTERVAL_MS) // Verifica muito frequentemente
                } else {
                    // Se is_active foi desativado ou kiosk_mode foi ativado, para o monitoramento
                    Log.d(TAG, "🔓 Condições mudaram - parando monitoramento do workspace")
                    break
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro no monitoramento do workspace: ${e.message}", e)
                delay(ERROR_RETRY_DELAY_MS)
            }
        }
    }
    
    /**
     * Verifica se o GelaFitWorkspaceActivity está rodando
     */
    private fun isWorkspaceRunning(): Boolean {
        try {
            val activityManager = getSystemService(ActivityManager::class.java)
            val workspaceActivity = "com.bootreceiver.app.ui.GelaFitWorkspaceActivity"
            
            // Método 1: Verifica a activity no topo (mais confiável)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val runningTasks = activityManager.getAppTasks()
                if (runningTasks != null && runningTasks.isNotEmpty()) {
                    for (task in runningTasks) {
                        val taskInfo = task.taskInfo
                        if (taskInfo != null && taskInfo.topActivity != null) {
                            val activityName = taskInfo.topActivity!!.className
                            if (activityName == workspaceActivity) {
                                return true
                            }
                        }
                    }
                }
            } else {
                // Método alternativo para versões antigas
                @Suppress("DEPRECATION")
                val runningTasks = activityManager.getRunningTasks(1)
                if (runningTasks.isNotEmpty()) {
                    val topActivity = runningTasks[0].topActivity
                    if (topActivity != null && topActivity.className == workspaceActivity) {
                        return true
                    }
                }
            }
            
            // Método 2: Verifica processos em foreground
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val runningProcesses = activityManager.runningAppProcesses
                val isForeground = runningProcesses?.any { 
                    it.processName == packageName && 
                    (it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                     it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE)
                } == true
                
                if (isForeground) {
                    // Verifica se a activity específica está rodando
                    // Se o processo está em foreground, assume que a workspace pode estar rodando
                    return true
                }
            }
            
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar se workspace está rodando: ${e.message}", e)
            return false
        }
    }
    
    /**
     * Garante que o app configurado esteja rodando (se kiosk estiver ativo)
     * Verifica mais frequentemente quando kiosk está ativo
     */
    private fun ensureAppIsRunning() {
        val preferenceManager = PreferenceManager(this)
        val targetPackage = preferenceManager.getTargetPackageName()
        
        if (targetPackage.isNullOrEmpty()) {
            return
        }
        
        serviceScope.launch {
            if (!isAppRunning(targetPackage)) {
                Log.d(TAG, "⚠️⚠️⚠️ APP MINIMIZADO COM KIOSK ATIVO! REABRINDO IMEDIATAMENTE...")
                val appLauncher = AppLauncher(this@KioskModeService)
                
                // Tenta múltiplas vezes rapidamente
                for (i in 1..3) {
                    val success = appLauncher.launchApp(targetPackage)
                    if (success && isAppRunning(targetPackage)) {
                        Log.d(TAG, "✅ App reaberto com sucesso na tentativa $i")
                        break
                    } else {
                        Log.w(TAG, "⚠️ Tentativa $i falhou, tentando novamente...")
                        delay(300)
                    }
                }
            }
        }
    }
    
    /**
     * Verifica se um app está rodando em foreground
     * Melhorado para detectar se o app foi fechado (não apenas minimizado)
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
            
            // Método 2: Verifica a activity no topo (mais confiável)
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
                // Método alternativo para versões antigas
                @Suppress("DEPRECATION")
                val runningTasks = activityManager.getRunningTasks(1)
                if (runningTasks.isNotEmpty()) {
                    val topActivity = runningTasks[0].topActivity
                    if (topActivity != null && topActivity.packageName == packageName) {
                        return true
                    }
                }
            }
            
            // Método 3: Verifica se o processo existe (mesmo em background)
            // Se o processo não existe, o app foi fechado
            try {
                val packageManager = packageManager
                val appInfo = packageManager.getApplicationInfo(packageName, 0)
                val pid = android.os.Process.getUidForName(packageName)
                
                // Se chegou aqui, o app está instalado, mas verifica se está rodando
                val runningProcesses = activityManager.runningAppProcesses
                val processExists = runningProcesses?.any { 
                    it.processName == packageName
                } == true
                
                // Se o processo não existe, o app foi fechado
                if (!processExists) {
                    Log.d(TAG, "📱 Processo do app não existe - app foi fechado")
                    return false
                }
            } catch (e: Exception) {
                // Se não conseguiu obter info do app, assume que não está rodando
                Log.d(TAG, "📱 Não foi possível verificar processo: ${e.message}")
            }
            
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar se app está rodando: ${e.message}", e)
            return false
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "⚠️ KioskModeService destruído - tentando reiniciar...")
        
        // Para monitoramento do workspace
        stopWorkspaceMonitoring()
        
        // Sempre tenta reiniciar o serviço (não depende do kiosk mode)
        // Isso garante que o serviço sempre esteja rodando
        serviceScope.launch {
            try {
                delay(1000) // Aguarda 1 segundo
                Log.d(TAG, "🔄 Reiniciando KioskModeService...")
                val restartIntent = Intent(this@KioskModeService, KioskModeService::class.java)
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
        val restartIntent = Intent(this, KioskModeService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(restartIntent)
        } else {
            startService(restartIntent)
        }
        
        // O serviço continua rodando mesmo se o app for fechado
        // START_STICKY garante que será reiniciado se necessário
    }
    
    companion object {
        private const val TAG = "KioskModeService"
        private const val CHANNEL_ID = "kiosk_mode_channel"
        private const val NOTIFICATION_ID = 2
        private const val CHECK_INTERVAL_MS = 500L // Verifica a cada 500ms quando kiosk ativo (muito rápido para prevenir minimização)
        private const val ERROR_RETRY_DELAY_MS = 2000L // Em caso de erro, aguarda 2 segundos
    }
}
