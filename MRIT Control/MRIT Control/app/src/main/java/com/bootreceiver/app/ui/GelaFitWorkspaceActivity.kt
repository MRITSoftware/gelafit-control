package com.bootreceiver.app.ui

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootreceiver.app.R
import com.bootreceiver.app.utils.AppLauncher
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PreferenceManager
import com.bootreceiver.app.utils.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity principal que serve como "área de trabalho" do GelaFit Control
 * 
 * Esta Activity:
 * 1. Se is_active = true: mostra grid de apps selecionados e não permite fechar/minimizar
 * 2. Se modo_kiosk = true: app selecionado fica fixo na tela sem possibilidade de fechar/minimizar
 * 3. Monitora constantemente o status de is_active e modo_kiosk no Supabase
 */
class GelaFitWorkspaceActivity : AppCompatActivity() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private val supabaseManager = SupabaseManager()
    private lateinit var deviceId: String
    private lateinit var preferenceManager: PreferenceManager
    private var isActive: Boolean? = null
    private var kioskMode: Boolean? = null
    private var isMonitoring = false
    private lateinit var appsGridRecyclerView: RecyclerView
    private val selectedApps = mutableListOf<AppInfo>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gelafit_workspace)
        
        deviceId = DeviceIdManager.getDeviceId(this)
        preferenceManager = PreferenceManager(this)
        
        // Configura a Activity para ocupar toda a tela
        setupFullScreen()
        
        // Inicializa RecyclerView do grid
        appsGridRecyclerView = findViewById(R.id.appsGridRecyclerView)
        appsGridRecyclerView.layoutManager = GridLayoutManager(this, 3)
        appsGridRecyclerView.adapter = AppsGridAdapter(selectedApps) { app ->
            openConfiguredApp(app.packageName)
        }
        
        // Verifica se há app configurado
        val targetPackage = preferenceManager.getTargetPackageName()
        if (targetPackage.isNullOrEmpty()) {
            Log.w(TAG, "Nenhum app configurado. Redirecionando para seleção...")
            val intent = Intent(this, AppSelectionActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
            return
        }
        
        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        Log.d(TAG, "🏢 GelaFit Workspace iniciado")
        Log.d(TAG, "📱 App configurado: $targetPackage")
        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
        
        // Carrega apps selecionados
        loadSelectedApps()
        
        // Mostra o grid por padrão (será ajustado conforme is_active)
        appsGridRecyclerView.visibility = View.VISIBLE
        
        // Inicia monitoramento de is_active e modo_kiosk (verifica status inicial também)
        startMonitoring()
    }
    
    /**
     * Configura a Activity para ocupar toda a tela (fullscreen)
     */
    private fun setupFullScreen() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        
        // Remove barra de navegação e status bar
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = (
                android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                or android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            )
        }
    }
    
    /**
     * Carrega apps selecionados para exibir no grid
     */
    private fun loadSelectedApps() {
        val targetPackage = preferenceManager.getTargetPackageName() ?: return
        
        serviceScope.launch {
            try {
                val appInfo = withContext(Dispatchers.IO) {
                    try {
                        val pm = packageManager
                        val appInfo = pm.getApplicationInfo(targetPackage, 0)
                        val appName = pm.getApplicationLabel(appInfo).toString()
                        AppInfo(appName, targetPackage)
                    } catch (e: Exception) {
                        Log.e(TAG, "Erro ao carregar info do app: ${e.message}", e)
                        null
                    }
                }
                
                if (appInfo != null) {
                    selectedApps.clear()
                    selectedApps.add(appInfo)
                    appsGridRecyclerView.adapter?.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao carregar apps selecionados: ${e.message}", e)
            }
        }
    }
    
    /**
     * Abre o app configurado
     */
    private fun openConfiguredApp(packageName: String) {
        try {
            Log.d(TAG, "🚀 Abrindo app: $packageName")
            val appLauncher = AppLauncher(this)
            val success = appLauncher.launchApp(packageName)
            
            if (success) {
                Log.d(TAG, "✅ App aberto com sucesso")
            } else {
                Log.e(TAG, "❌ Falha ao abrir app")
            }
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao abrir app: ${e.message}", e)
        }
    }
    
    /**
     * Inicia monitoramento do status is_active e modo_kiosk no Supabase
     */
    private fun startMonitoring() {
        if (isMonitoring) {
            Log.d(TAG, "Monitoramento já está ativo")
            return
        }
        
        isMonitoring = true
        serviceScope.launch {
            // Verifica status inicial imediatamente
            try {
                val initialIsActive = supabaseManager.getIsActive(deviceId)
                val initialKioskMode = supabaseManager.getKioskMode(deviceId)
                Log.d(TAG, "Status inicial - is_active: $initialIsActive, modo_kiosk: $initialKioskMode")
                
                isActive = initialIsActive
                kioskMode = initialKioskMode
                
                // Aplica configurações iniciais
                applyInitialSettings()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao verificar status inicial: ${e.message}", e)
            }
            
            // Loop de monitoramento contínuo
            while (isMonitoring) {
                try {
                    val currentIsActive = supabaseManager.getIsActive(deviceId)
                    val currentKioskMode = supabaseManager.getKioskMode(deviceId)
                    
                    // Se mudou o status, aplica as mudanças
                    if (isActive != currentIsActive || kioskMode != currentKioskMode) {
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        if (currentIsActive == true) {
                            Log.d(TAG, "🔒 IS_ACTIVE ATIVADO - Bloqueando acesso a outros apps")
                            applyAppBlocking()
                            showAppsGrid()
                        } else {
                            Log.d(TAG, "🔓 IS_ACTIVE DESATIVADO - Liberando acesso")
                            removeAppBlocking()
                            hideAppsGrid()
                        }
                        
                        if (currentKioskMode == true) {
                            Log.d(TAG, "🔒 MODO_KIOSK ATIVADO - App fixo na tela")
                            enableKioskMode()
                            // Quando modo_kiosk está ativo, abre o app automaticamente
                            val targetPackage = preferenceManager.getTargetPackageName()
                            if (!targetPackage.isNullOrEmpty()) {
                                openConfiguredApp(targetPackage)
                            }
                        } else {
                            Log.d(TAG, "🔓 MODO_KIOSK DESATIVADO")
                            disableKioskMode()
                        }
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        
                        isActive = currentIsActive
                        kioskMode = currentKioskMode
                    }
                    
                    // Se modo_kiosk está ativo, garante que o app está sempre em foreground
                    // Se apenas is_active está ativo, não força abertura do app (usuário escolhe pelo grid)
                    if (currentKioskMode == true) {
                        ensureAppInForeground()
                    } else if (currentIsActive == true) {
                        // Quando apenas is_active está ativo, garante que apenas o app configurado pode estar aberto
                        // mas não força a abertura - o usuário escolhe pelo grid
                        ensureOnlyConfiguredAppIsOpen()
                    }
                    
                    // Se modo_kiosk está ativo, garante que o app está sempre em foreground
                    if (currentKioskMode == true) {
                        ensureAppInForeground()
                    }
                    
                    delay(CHECK_INTERVAL_MS)
                } catch (e: Exception) {
                    Log.e(TAG, "Erro no monitoramento: ${e.message}", e)
                    delay(ERROR_RETRY_DELAY_MS)
                }
            }
        }
    }
    
    /**
     * Aplica configurações iniciais baseadas no status atual
     */
    private fun applyInitialSettings() {
        if (isActive == true) {
            applyAppBlocking()
            showAppsGrid() // Sempre mostra o grid quando is_active está ativo
        } else {
            hideAppsGrid()
        }
        
        if (kioskMode == true) {
            enableKioskMode()
            // Quando modo_kiosk está ativo, abre o app automaticamente e mantém fixo
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
        }
        // Não abre o app automaticamente quando apenas is_active está ativo
        // O usuário deve clicar no grid para abrir o app
    }
    
    /**
     * Mostra o grid de apps selecionados
     */
    private fun showAppsGrid() {
        runOnUiThread {
            appsGridRecyclerView.visibility = View.VISIBLE
        }
    }
    
    /**
     * Esconde o grid de apps selecionados
     */
    private fun hideAppsGrid() {
        runOnUiThread {
            appsGridRecyclerView.visibility = View.GONE
        }
    }
    
    /**
     * Habilita modo kiosk completo (app fixo sem possibilidade de fechar/minimizar)
     */
    private fun enableKioskMode() {
        runOnUiThread {
            // Impede fechamento da activity
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            
            // Abre o app configurado e mantém em foreground
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
        }
    }
    
    /**
     * Desabilita modo kiosk
     */
    private fun disableKioskMode() {
        runOnUiThread {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            window.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
            window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        }
    }
    
    /**
     * Garante que o app configurado está sempre em foreground quando modo_kiosk está ativo
     */
    private suspend fun ensureAppInForeground() {
        val targetPackage = preferenceManager.getTargetPackageName() ?: return
        
        try {
            val activityManager = getSystemService(android.app.ActivityManager::class.java)
            
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val runningTasks = activityManager.getAppTasks()
                if (runningTasks != null && runningTasks.isNotEmpty()) {
                    val topTask = runningTasks[0]
                    val taskInfo = topTask.taskInfo
                    if (taskInfo != null && taskInfo.topActivity != null) {
                        val topPackage = taskInfo.topActivity!!.packageName
                        
                        // Se não é o app configurado, reabre
                        if (topPackage != targetPackage && topPackage != packageName) {
                            Log.w(TAG, "⚠️ App não autorizado em foreground: $topPackage")
                            Log.d(TAG, "🔄 Reabrindo app configurado...")
                            delay(500)
                            openConfiguredApp(targetPackage)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar app em foreground: ${e.message}", e)
        }
    }
    
    /**
     * Aplica bloqueio de acesso a outros apps
     */
    private fun applyAppBlocking() {
        Log.d(TAG, "🔒 Aplicando bloqueio de apps...")
        
        // Inicia o serviço de bloqueio de apps
        try {
            val blockingIntent = Intent(this, com.bootreceiver.app.service.AppBlockingService::class.java).apply {
                putExtra("is_active", true)
            }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(blockingIntent)
            } else {
                startService(blockingIntent)
            }
            Log.d(TAG, "✅ Serviço de bloqueio iniciado")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao iniciar serviço de bloqueio: ${e.message}", e)
        }
    }
    
    /**
     * Remove bloqueio de acesso a outros apps
     */
    private fun removeAppBlocking() {
        Log.d(TAG, "🔓 Removendo bloqueio de apps...")
        
        // Para o serviço de bloqueio
        try {
            val blockingIntent = Intent(this, com.bootreceiver.app.service.AppBlockingService::class.java).apply {
                putExtra("is_active", false)
            }
            startService(blockingIntent)
            Log.d(TAG, "✅ Serviço de bloqueio parado")
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao parar serviço de bloqueio: ${e.message}", e)
        }
    }
    
    /**
     * Garante que apenas o app configurado está aberto
     * Se outro app estiver aberto, fecha e reabre o app configurado
     */
    private suspend fun ensureOnlyConfiguredAppIsOpen() {
        val targetPackage = preferenceManager.getTargetPackageName() ?: return
        
        try {
            val activityManager = getSystemService(android.app.ActivityManager::class.java)
            
            // Verifica qual app está em foreground
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                val runningTasks = activityManager.getAppTasks()
                if (runningTasks != null && runningTasks.isNotEmpty()) {
                    val topTask = runningTasks[0]
                    val taskInfo = topTask.taskInfo
                    if (taskInfo != null && taskInfo.topActivity != null) {
                        val topPackage = taskInfo.topActivity!!.packageName
                        
                        // Se não é o app configurado nem o próprio GelaFit Control, fecha o app não autorizado
                        // Mas não abre o app configurado automaticamente quando apenas is_active está ativo
                        if (topPackage != targetPackage && topPackage != packageName) {
                            Log.w(TAG, "⚠️ App não autorizado detectado: $topPackage")
                            
                            // Fecha o app não autorizado
                            try {
                                activityManager.killBackgroundProcesses(topPackage)
                                Log.d(TAG, "🔄 App não autorizado fechado")
                            } catch (e: Exception) {
                                Log.w(TAG, "Não foi possível fechar app: ${e.message}")
                            }
                            
                            // Não abre o app automaticamente - apenas mostra o grid
                            // O usuário escolhe quando abrir o app pelo grid
                            delay(500)
                            showAppsGrid()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar app em foreground: ${e.message}", e)
        }
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume - Garantindo que tela do control está visível")
        
        // Se is_active está ativo, mostra o grid
        if (isActive == true) {
            showAppsGrid()
        }
        
        // Se modo_kiosk está ativo, garante que o app está em foreground
        if (kioskMode == true) {
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
        }
    }
    
    override fun onPause() {
        super.onPause()
        
        // Se is_active está ativo, impede que a activity seja pausada (minimizada)
        // Mas não abre o app automaticamente - apenas garante que a tela do control está visível
        if (isActive == true && kioskMode != true) {
            Log.d(TAG, "🔒 Tentativa de pausar bloqueada (is_active = true)")
            // Não abre o app, apenas mostra o grid
            showAppsGrid()
        } else if (kioskMode == true) {
            // Quando modo_kiosk está ativo, abre o app automaticamente
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Se is_active está ativo, impede que a activity seja destruída
        if (isActive == true) {
            Log.d(TAG, "🔒 Tentativa de destruir bloqueada (is_active = true)")
            // Recria a activity
            val intent = Intent(this, GelaFitWorkspaceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            return
        }
        
        Log.d(TAG, "⚠️ GelaFitWorkspaceActivity destruída")
        isMonitoring = false
    }
    
    override fun onBackPressed() {
        // Se is_active está ativo, bloqueia o botão voltar mas não abre o app
        if (isActive == true && kioskMode != true) {
            Log.d(TAG, "🔒 Botão voltar bloqueado (is_active = true)")
            // Apenas mostra o grid, não abre o app
            showAppsGrid()
            return
        }
        
        // Se modo_kiosk está ativo, bloqueia o botão voltar e abre o app
        if (kioskMode == true) {
            Log.d(TAG, "🔒 Botão voltar bloqueado (modo_kiosk = true)")
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
            return
        }
        
        // Se is_active está desativado, permite comportamento normal
        super.onBackPressed()
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Se is_active ou modo_kiosk está ativo, bloqueia botão Home
        if (keyCode == KeyEvent.KEYCODE_HOME && (isActive == true || kioskMode == true)) {
            Log.d(TAG, "🔒 Botão Home bloqueado")
            if (kioskMode == true) {
                // Quando modo_kiosk está ativo, abre o app
                val targetPackage = preferenceManager.getTargetPackageName()
                if (!targetPackage.isNullOrEmpty()) {
                    openConfiguredApp(targetPackage)
                }
            } else {
                // Quando apenas is_active está ativo, apenas mostra o grid
                showAppsGrid()
            }
            return true
        }
        
        return super.onKeyDown(keyCode, event)
    }
    
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        
        // Se is_active ou modo_kiosk está ativo, impede saída da activity
        if (isActive == true || kioskMode == true) {
            Log.d(TAG, "🔒 Tentativa de sair bloqueada")
            if (kioskMode == true) {
                // Quando modo_kiosk está ativo, abre o app
                val targetPackage = preferenceManager.getTargetPackageName()
                if (!targetPackage.isNullOrEmpty()) {
                    openConfiguredApp(targetPackage)
                }
            } else {
                // Quando apenas is_active está ativo, apenas mostra o grid
                showAppsGrid()
            }
        }
    }
    
    /**
     * Adapter para o grid de apps
     */
    private class AppsGridAdapter(
        private val apps: List<AppInfo>,
        private val onAppClick: (AppInfo) -> Unit
    ) : RecyclerView.Adapter<AppsGridAdapter.AppViewHolder>() {
        
        class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val appName: TextView = itemView.findViewById(R.id.appName)
            val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_app_grid, parent, false)
            return AppViewHolder(view)
        }
        
        override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
            val app = apps[position]
            holder.appName.text = app.name
            
            // Carrega ícone do app
            try {
                val pm = holder.itemView.context.packageManager
                val appInfo = pm.getApplicationInfo(app.packageName, 0)
                holder.appIcon.setImageDrawable(pm.getApplicationIcon(appInfo))
            } catch (e: Exception) {
                holder.appIcon.setImageResource(android.R.drawable.sym_def_app_icon)
            }
            
            holder.itemView.setOnClickListener {
                onAppClick(app)
            }
        }
        
        override fun getItemCount(): Int = apps.size
    }
    
    /**
     * Classe de dados para representar um app
     */
    data class AppInfo(
        val name: String,
        val packageName: String
    )
    
    companion object {
        private const val TAG = "GelaFitWorkspace"
        private const val CHECK_INTERVAL_MS = 5000L // Verifica a cada 5 segundos
        private const val ERROR_RETRY_DELAY_MS = 10000L // Em caso de erro, aguarda 10 segundos
    }
}
