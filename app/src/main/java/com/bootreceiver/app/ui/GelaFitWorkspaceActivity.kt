package com.bootreceiver.app.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.MotionEvent
import android.view.KeyEvent
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import android.widget.RelativeLayout
import android.os.Handler
import android.os.Looper
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
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val supabaseManager = SupabaseManager()
    private lateinit var deviceId: String
    private lateinit var preferenceManager: PreferenceManager
    private var isActive: Boolean? = null
    private var kioskMode: Boolean? = null
    private var isMonitoring = false
    private var realtimeJob: Job? = null // Job para gerenciar subscription Realtime
    private var isOpeningAllowedActivity = false // Flag para permitir abrir activities permitidas
    private lateinit var appsGridRecyclerView: RecyclerView
    private val selectedApps = mutableListOf<AppInfo>()
    private val appAddedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val packageName = intent?.getStringExtra("package_name")
            if (packageName != null) {
                addAppToGrid(packageName)
            }
        }
    }
    
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
        
        // Configura botão de ativar modo kiosk
        setupKioskButton()
        
        // Configura menu de 3 pontinhos
        setupMenuButton()

        // Círculo de desbloqueio removido
        
        // Mostra o grid por padrão (será ajustado conforme is_active)
        appsGridRecyclerView.visibility = View.VISIBLE
        
        // Inicia monitoramento de is_active e modo_kiosk (verifica status inicial também)
        startMonitoring()
        
        // Registra receiver para atualizar grid quando app for adicionado
        // Para Android 13+ (API 33+), é necessário especificar RECEIVER_NOT_EXPORTED
        val filter = IntentFilter("com.bootreceiver.app.APP_ADDED_TO_GRID")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(appAddedReceiver, filter, Context.RECEIVER_NOT_EXPORTED)
        } else {
            @Suppress("DEPRECATION")
            registerReceiver(appAddedReceiver, filter)
        }
    }
    
    /**
     * Configura o botão para ativar modo kiosk
     */
    private fun setupKioskButton() {
        val activateKioskButton = findViewById<Button>(R.id.activateKioskButton)
        activateKioskButton.setOnClickListener {
            activateKioskMode()
        }
        
        // Configura botão de bloquear área de trabalho
        val blockWorkspaceButton = findViewById<Button>(R.id.blockWorkspaceButton)
        blockWorkspaceButton.setOnClickListener {
            blockWorkspace()
        }
    }
    
    /**
     * Atualiza a visibilidade dos botões (ativar modo kiosk e bloquear área de trabalho)
     */
    private fun updateKioskButtonVisibility(isActive: Boolean, kioskMode: Boolean) {
        runOnUiThread {
            val activateKioskButton = findViewById<Button>(R.id.activateKioskButton)
            val blockWorkspaceButton = findViewById<Button>(R.id.blockWorkspaceButton)
            
            // Botão "Ativar Modo Kiosk" aparece quando is_active=true e kiosk_mode=false
            if (isActive && !kioskMode) {
                activateKioskButton.visibility = View.VISIBLE
            } else {
                activateKioskButton.visibility = View.GONE
            }
            
            // Botão "Bloquear Área de Trabalho" aparece quando is_active=false
            if (!isActive) {
                blockWorkspaceButton.visibility = View.VISIBLE
            } else {
                blockWorkspaceButton.visibility = View.GONE
            }
        }
    }
    
    /**
     * Bloqueia a área de trabalho (seta is_active = true)
     */
    private fun blockWorkspace() {
        serviceScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    supabaseManager.updateIsActive(deviceId, true)
                }
                
                if (success) {
                    // Atualiza cache local
                    preferenceManager.saveIsActiveCached(true)
                    preferenceManager.saveGelaFitUnlocked(false) // Reseta desbloqueio
                    preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                    
                    // Atualiza variáveis locais
                    isActive = true
                    
                    // Aplica modo kiosk do GelaFit Control
                    enableGelaFitKioskMode()
                    applyAppBlocking()
                    showAppsGrid()
                    
                    // Atualiza UI
                    runOnUiThread {
                        vibrateShort()
                        updateKioskButtonVisibility(true, kioskMode == true)
                        Toast.makeText(this@GelaFitWorkspaceActivity, "Área de trabalho bloqueada", Toast.LENGTH_LONG).show()
                    }
                    
                    Log.d(TAG, "✅ Área de trabalho bloqueada (is_active=true)")
                } else {
                    runOnUiThread {
                        Toast.makeText(this@GelaFitWorkspaceActivity, "Erro ao bloquear área de trabalho", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao bloquear área de trabalho: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@GelaFitWorkspaceActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    
    

    /**
     * Desbloqueio individual - permite escolher o que desbloquear
     */
    private fun performEmergencyUnlock() {
        runOnUiThread {
            val options = mutableListOf<String>()
            val actions = mutableListOf<() -> Unit>()
            
            // Opção para desbloquear GelaFit Control
            if (isActive == true) {
                options.add("Desbloquear GelaFit Control (permite minimizar)")
                actions.add {
                    unlockGelaFitControl()
                }
            }
            
            // Opção para desbloquear app escolhido
            if (kioskMode == true) {
                options.add("Desbloquear App Escolhido (permite minimizar)")
                actions.add {
                    unlockTargetApp()
                }
            }
            
            // Se ambos estão bloqueados, oferece desbloquear tudo
            if (options.isEmpty()) {
                options.add("Desbloquear Tudo")
                actions.add {
                    unlockEverything()
                }
            }
            
            if (options.isNotEmpty()) {
                AlertDialog.Builder(this)
                    .setTitle("Desbloquear")
                    .setItems(options.toTypedArray()) { _, which ->
                        actions[which]()
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        }
    }
    
    /**
     * Desbloqueia apenas o GelaFit Control (permite minimizar)
     */
    private fun unlockGelaFitControl() {
        serviceScope.launch {
            try {
                // Atualiza Supabase
                val activeResult = supabaseManager.updateIsActive(deviceId, false)
                
                // Atualiza cache local
                preferenceManager.saveIsActiveCached(false)
                preferenceManager.saveGelaFitUnlocked(true)
                preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                
                // Atualiza variáveis locais
                isActive = false
                
                // Remove modo kiosk do GelaFit Control
                disableGelaFitKioskMode()
                removeAppBlocking()
                hideAppsGrid()
                
                // Atualiza UI
                runOnUiThread {
                    vibrateShort()
                    updateKioskButtonVisibility(false, kioskMode == true)
                    Toast.makeText(this@GelaFitWorkspaceActivity, "GelaFit Control desbloqueado - você pode minimizar", Toast.LENGTH_LONG).show()
                }
                
                Log.d(TAG, "🔓 GelaFit Control desbloqueado (is_active=false). Supabase ok? active=$activeResult")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao desbloquear GelaFit Control: ${e.message}", e)
            }
        }
    }
    
    /**
     * Desbloqueia apenas o app escolhido (permite minimizar)
     */
    private fun unlockTargetApp() {
        serviceScope.launch {
            try {
                // Atualiza Supabase
                val kioskResult = supabaseManager.updateKioskMode(deviceId, false)
                
                // Atualiza cache local
                preferenceManager.saveKioskModeCached(false)
                preferenceManager.saveTargetAppUnlocked(true)
                preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                
                // Atualiza variáveis locais
                kioskMode = false
                
                // Remove modo kiosk do app
                disableKioskMode()
                
                // Atualiza UI
                runOnUiThread {
                    vibrateShort()
                    updateKioskButtonVisibility(isActive == true, false)
                    Toast.makeText(this@GelaFitWorkspaceActivity, "App escolhido desbloqueado - você pode minimizar", Toast.LENGTH_LONG).show()
                }
                
                Log.d(TAG, "🔓 App escolhido desbloqueado (kiosk_mode=false). Supabase ok? kiosk=$kioskResult")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao desbloquear app escolhido: ${e.message}", e)
            }
        }
    }
    
    /**
     * Desbloqueia tudo (GelaFit Control e app escolhido)
     */
    private fun unlockEverything() {
        serviceScope.launch {
            try {
                // Atualiza Supabase
                val kioskResult = supabaseManager.updateKioskMode(deviceId, false)
                val activeResult = supabaseManager.updateIsActive(deviceId, false)
                
                // Atualiza cache local
                preferenceManager.saveKioskModeCached(false)
                preferenceManager.saveIsActiveCached(false)
                preferenceManager.saveGelaFitUnlocked(true)
                preferenceManager.saveTargetAppUnlocked(true)
                preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                
                // Atualiza variáveis locais
                isActive = false
                kioskMode = false
                
                // Remove modo kiosk de tudo
                disableGelaFitKioskMode()
                disableKioskMode()
                removeAppBlocking()
                hideAppsGrid()
                
                // Atualiza UI
                runOnUiThread {
                    vibrateShort()
                    updateKioskButtonVisibility(false, false)
                    Toast.makeText(this@GelaFitWorkspaceActivity, "Tudo desbloqueado", Toast.LENGTH_LONG).show()
                }
                
                Log.d(TAG, "🔓 Tudo desbloqueado (kiosk=false, is_active=false). Supabase ok? kiosk=$kioskResult, active=$activeResult")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao desbloquear tudo: ${e.message}", e)
            }
        }
    }
    
    private fun vibrateShort() {
        try {
            val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(100)
            }
        } catch (_: Exception) {
        }
    }

    /**
     * Ativa o modo kiosk do app escolhido
     */
    private fun activateKioskMode() {
        AlertDialog.Builder(this)
            .setTitle("Ativar modo kiosk?")
            .setMessage("O app ficará fixo na tela. Deseja ativar?")
            .setPositiveButton("Ativar") { _, _ ->
                serviceScope.launch {
                    try {
                        val success = withContext(Dispatchers.IO) {
                            supabaseManager.updateKioskMode(deviceId, true)
                        }
                        
                        if (success) {
                            // Atualiza cache local imediatamente e reseta estado de desbloqueio
                            preferenceManager.saveKioskModeCached(true)
                            preferenceManager.saveTargetAppUnlocked(false) // Reseta desbloqueio ao ativar kiosk
                            preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                            
                            // Atualiza variáveis locais da Activity imediatamente
                            kioskMode = true
                            
                            // Aplica as mudanças imediatamente
                            enableKioskMode()
                            updateKioskButtonVisibility(isActive == true, true)
                            
                            val targetPackage = preferenceManager.getTargetPackageName()
                            if (!targetPackage.isNullOrEmpty()) {
                                openConfiguredApp(targetPackage)
                            }
                            
                            Log.d(TAG, "✅ Modo kiosk ativado - banco atualizado, cache atualizado, variáveis locais atualizadas")
                        } else {
                            runOnUiThread {
                                AlertDialog.Builder(this@GelaFitWorkspaceActivity)
                                    .setTitle("Erro")
                                    .setMessage("Não foi possível ativar o modo kiosk no servidor.")
                                    .setPositiveButton("OK", null)
                                    .show()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Erro ao ativar modo kiosk: ${e.message}", e)
                        runOnUiThread {
                            AlertDialog.Builder(this@GelaFitWorkspaceActivity)
                                .setTitle("Erro")
                                .setMessage("Não foi possível ativar o modo kiosk: ${e.message}")
                                .setPositiveButton("OK", null)
                                .show()
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    /**
     * Configura o menu de 3 pontinhos
     */
    private fun setupMenuButton() {
        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton.setOnClickListener {
            showMenuPopup(it)
        }
    }
    
    /**
     * Mostra o popup menu com opções
     */
    private fun showMenuPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.workspace_menu, popup.menu)
        
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_check_settings -> {
                    checkSettings()
                    true
                }
                R.id.menu_add_product -> {
                    addProductToGrid()
                    true
                }
                // Removido: configuração de desbloqueio não é mais necessária (círculo fixo no centro)
                else -> false
            }
        }
        
        popup.show()
    }
    
    /**
     * Verifica configurações necessárias
     */
    private fun checkSettings() {
        // Permite abrir mesmo com kiosk ativo (para testes)
        isOpeningAllowedActivity = true // Marca que estamos abrindo uma activity permitida
        val intent = Intent(this, SettingsCheckActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        // Reseta a flag após um delay para permitir que a activity abra
        Handler(Looper.getMainLooper()).postDelayed({
            isOpeningAllowedActivity = false
        }, 500)
    }
    
    /**
     * Adiciona produto ao grid
     */
    private fun addProductToGrid() {
        // Permite abrir mesmo com kiosk ativo (para testes)
        isOpeningAllowedActivity = true // Marca que estamos abrindo uma activity permitida
        val intent = Intent(this, AddProductActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        // Reseta a flag após um delay para permitir que a activity abra
        Handler(Looper.getMainLooper()).postDelayed({
            isOpeningAllowedActivity = false
        }, 500)
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
        serviceScope.launch {
            try {
                val selectedPackages = withContext(Dispatchers.IO) {
                    val savedApps = preferenceManager.getSelectedAppsList()
                    if (savedApps.isEmpty()) {
                        // Se não há lista salva, usa o app principal configurado
                        val targetPackage = preferenceManager.getTargetPackageName()
                        if (!targetPackage.isNullOrEmpty()) {
                            setOf(targetPackage)
                        } else {
                            emptySet()
                        }
                    } else {
                        savedApps
                    }
                }
                
                val appInfos = mutableListOf<AppInfo>()
                selectedPackages.forEach { packageName ->
                    try {
                        val pm = packageManager
                        val appInfo = pm.getApplicationInfo(packageName, 0)
                        val appName = pm.getApplicationLabel(appInfo).toString()
                        appInfos.add(AppInfo(appName, packageName))
                    } catch (e: Exception) {
                        Log.w(TAG, "App não encontrado: $packageName")
                    }
                }
                
                selectedApps.clear()
                selectedApps.addAll(appInfos)
                appsGridRecyclerView.adapter?.notifyDataSetChanged()
                
                Log.d(TAG, "Apps carregados no grid: ${selectedApps.size}")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao carregar apps selecionados: ${e.message}", e)
            }
        }
    }
    
    /**
     * Adiciona um app ao grid e atualiza imediatamente
     */
    fun addAppToGrid(packageName: String) {
        // Recarrega todos os apps da lista persistente para garantir que todos apareçam
        loadSelectedApps()
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
     * Inicia monitoramento REALTIME do status is_active e modo_kiosk no Supabase
     * Usa Supabase Realtime em vez de polling para receber mudanças instantaneamente
     */
    private fun startMonitoring() {
        if (isMonitoring) {
            Log.d(TAG, "Monitoramento já está ativo")
            return
        }
        
        isMonitoring = true
        
        // Primeiro, busca status inicial do banco
        serviceScope.launch {
            try {
                val status = withContext(Dispatchers.IO) {
                    supabaseManager.getDeviceStatus(deviceId)
                }
                
                val freshIsActive = status?.isActive
                val freshKiosk = status?.kioskMode
                Log.d(TAG, "Status inicial (DIRETO DO BANCO) - is_active: $freshIsActive, modo_kiosk: $freshKiosk")

                if (freshIsActive != null) {
                    isActive = freshIsActive
                    preferenceManager.saveIsActiveCached(freshIsActive)
                }
                if (freshKiosk != null) {
                    kioskMode = freshKiosk
                    preferenceManager.saveKioskModeCached(freshKiosk)
                }
                preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                
                // Aplica configurações após buscar do banco
                applyInitialSettings()
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao verificar status inicial: ${e.message}", e)
                // Em caso de erro, usa cache como fallback
                val cachedIsActive = preferenceManager.getIsActiveCached()
                val cachedKioskMode = preferenceManager.getKioskModeCached()
                isActive = cachedIsActive
                kioskMode = cachedKioskMode
                applyInitialSettings()
            }
        }
        
        // Agora inicia subscription REALTIME
        realtimeJob = serviceScope.launch {
            try {
                supabaseManager.subscribeToDeviceChanges(deviceId)
                    .onEach { status ->
                        // Recebe mudanças em tempo real do banco
                        val currentIsActive = status.isActive
                        val currentKioskMode = status.kioskMode
                        
                        Log.d(TAG, "🔄 REALTIME: Mudança detectada - is_active: $currentIsActive, modo_kiosk: $currentKioskMode")
                        
                        // Atualiza cache
                        preferenceManager.saveIsActiveCached(currentIsActive)
                        preferenceManager.saveKioskModeCached(currentKioskMode)
                        preferenceManager.saveStatusLastSync(System.currentTimeMillis())
                        
                        // Atualiza visibilidade dos botões
                        updateKioskButtonVisibility(currentIsActive == true, currentKioskMode == true)
                        
                        // Se mudou o status, aplica as mudanças
                        if (isActive != currentIsActive || kioskMode != currentKioskMode) {
                            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            if (currentIsActive == true) {
                                Log.d(TAG, "🔒 IS_ACTIVE ATIVADO - Bloqueando acesso a outros apps e mantendo GelaFit Control em modo kiosk")
                                applyAppBlocking()
                                enableGelaFitKioskMode()
                                showAppsGrid()
                            } else {
                                Log.d(TAG, "🔓 IS_ACTIVE DESATIVADO - Liberando acesso")
                                removeAppBlocking()
                                disableGelaFitKioskMode()
                                hideAppsGrid()
                            }
                            
                            if (currentKioskMode == true) {
                                Log.d(TAG, "🔒 MODO_KIOSK ATIVADO - App fixo na tela")
                                enableKioskMode()
                                val targetPackage = preferenceManager.getTargetPackageName()
                                if (!targetPackage.isNullOrEmpty()) {
                                    openConfiguredApp(targetPackage)
                                }
                            } else {
                                Log.d(TAG, "🔓 MODO_KIOSK DESATIVADO")
                                disableKioskMode()
                                if (currentIsActive == true) {
                                    enableGelaFitKioskMode()
                                }
                            }
                            Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                            
                            isActive = currentIsActive
                            kioskMode = currentKioskMode
                        }
                        
                        // Garante que o app está em foreground se necessário
                        if (currentKioskMode == true) {
                            ensureAppInForeground()
                        } else if (currentIsActive == true) {
                            enableGelaFitKioskMode()
                            ensureOnlyConfiguredAppIsOpen()
                        } else {
                            disableGelaFitKioskMode()
                        }
                    }
                    .catch { e ->
                        Log.e(TAG, "❌ Erro no Realtime: ${e.message}", e)
                        // Em caso de erro, tenta reconectar após delay
                        delay(ERROR_RETRY_DELAY_MS)
                        if (isMonitoring) {
                            // Tenta reconectar
                            realtimeJob?.cancel()
                            startMonitoring()
                        }
                    }
                    .launchIn(this)
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erro ao iniciar subscription Realtime: ${e.message}", e)
                isMonitoring = false
            }
        }
    }
    
    /**
     * Para o monitoramento Realtime
     */
    private fun stopMonitoring() {
        isMonitoring = false
        realtimeJob?.cancel()
        realtimeJob = null
        
        serviceScope.launch {
            try {
                supabaseManager.unsubscribeFromDeviceChanges(deviceId)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao parar monitoramento: ${e.message}", e)
            }
        }
    }
    
    /**
     * Aplica configurações iniciais baseadas no status atual
     */
    private fun applyInitialSettings() {
        // Atualiza visibilidade dos botões
        updateKioskButtonVisibility(isActive == true, kioskMode == true)
        
        if (isActive == true) {
            // Aplica bloqueio e modo kiosk IMEDIATAMENTE quando is_active = true
            applyAppBlocking()
            enableGelaFitKioskMode() // Mantém GelaFit Control em modo kiosk quando is_active = true
            showAppsGrid() // Sempre mostra o grid quando is_active está ativo
            Log.d(TAG, "✅ Configurações iniciais aplicadas: is_active=true, modo kiosk do GelaFit Control ativado")
        } else {
            disableGelaFitKioskMode() // Remove modo kiosk do GelaFit Control quando is_active = false
            removeAppBlocking()
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
     * Usado quando kiosk_mode = true (abre o app configurado automaticamente)
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
     * Habilita modo kiosk do GelaFit Control quando is_active = true
     * Mantém o GelaFit Control ativo sem permitir minimizar, mas não abre o app configurado
     */
    private fun enableGelaFitKioskMode() {
        runOnUiThread {
            // Impede fechamento da activity do GelaFit Control
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
            window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
            window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            // Não abre o app configurado automaticamente - apenas mantém o GelaFit Control em modo kiosk
        }
    }
    
    /**
     * Desabilita modo kiosk do GelaFit Control quando is_active = false
     */
    private fun disableGelaFitKioskMode() {
        runOnUiThread {
            // Só remove as flags se kiosk_mode também não estiver ativo
            if (kioskMode != true) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                window.clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
                window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
                window.clearFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
            }
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
     * Se outro app estiver aberto, fecha o app não autorizado mas não abre o app configurado automaticamente
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
        
        // Recarrega apps quando volta para a tela (caso tenha sido adicionado enquanto estava em outra tela)
        loadSelectedApps()
        
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
        
        // Se estamos abrindo uma activity permitida (SettingsCheck ou AddProduct), não bloqueia
        if (isOpeningAllowedActivity) {
            Log.d(TAG, "🔓 Pausa permitida (abrindo activity permitida)")
            return
        }
        
        // Verifica se está desbloqueado individualmente (usa cache local para resposta imediata)
        val gelafitUnlocked = preferenceManager.isGelaFitUnlocked()
        val targetAppUnlocked = preferenceManager.isTargetAppUnlocked()
        
        // Se is_active está ativo E não está desbloqueado, impede que a activity seja pausada (minimizada)
        if (isActive == true && !gelafitUnlocked && kioskMode != true) {
            Log.d(TAG, "🔒 Tentativa de pausar bloqueada (is_active = true, não desbloqueado)")
            // Reabre INSTANTANEAMENTE sem delay para resposta imediata
            Handler(Looper.getMainLooper()).post {
                showAppsGrid()
                // Garante que a activity está em foreground
                if (!isFinishing) {
                    val intent = Intent(this, GelaFitWorkspaceActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
            }
        } else if (kioskMode == true && !targetAppUnlocked) {
            // Quando modo_kiosk está ativo E não está desbloqueado, abre o app automaticamente
            Handler(Looper.getMainLooper()).postDelayed({
                val targetPackage = preferenceManager.getTargetPackageName()
                if (!targetPackage.isNullOrEmpty()) {
                    openConfiguredApp(targetPackage)
                }
            }, 100) // Delay mínimo para resposta rápida
        } else {
            // Se está desbloqueado, permite minimizar normalmente
            Log.d(TAG, "🔓 Pausa permitida (desbloqueado)")
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
        // Para monitoramento Realtime
        stopMonitoring()
        
        // Desregistra receiver
        try {
            unregisterReceiver(appAddedReceiver)
        } catch (e: Exception) {
            // Receiver pode não estar registrado
        }
        
        // Verifica se está desbloqueado
        val gelafitUnlocked = preferenceManager.isGelaFitUnlocked()
        
        // Se is_active está ativo E não está desbloqueado, impede que a activity seja destruída
        if (isActive == true && !gelafitUnlocked) {
            Log.d(TAG, "🔒 Tentativa de destruir bloqueada (is_active = true, não desbloqueado)")
            // Recria a activity
            val intent = Intent(this, GelaFitWorkspaceActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            return
        }
        
        // Cancela scope
        serviceScope.cancel()
        
        Log.d(TAG, "⚠️ GelaFitWorkspaceActivity destruída")
        isMonitoring = false
    }
    
    override fun onBackPressed() {
        // Verifica se está desbloqueado individualmente
        val gelafitUnlocked = preferenceManager.isGelaFitUnlocked()
        val targetAppUnlocked = preferenceManager.isTargetAppUnlocked()
        
        // Se is_active está ativo E não está desbloqueado, bloqueia o botão voltar
        if (isActive == true && !gelafitUnlocked && kioskMode != true) {
            Log.d(TAG, "🔒 Botão voltar bloqueado (is_active = true, não desbloqueado)")
            // Apenas mostra o grid, não abre o app
            showAppsGrid()
            return
        }
        
        // Se modo_kiosk está ativo E não está desbloqueado, bloqueia o botão voltar e abre o app
        if (kioskMode == true && !targetAppUnlocked) {
            Log.d(TAG, "🔒 Botão voltar bloqueado (modo_kiosk = true, não desbloqueado)")
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
            return
        }
        
        // Se está desbloqueado ou ambos estão desativados, permite comportamento normal
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
        
        // Se estamos abrindo uma activity permitida (SettingsCheck ou AddProduct), não bloqueia
        if (isOpeningAllowedActivity) {
            Log.d(TAG, "🔓 Saída permitida (abrindo activity permitida)")
            return
        }
        
        // Verifica se está desbloqueado (usa cache local para resposta imediata)
        val gelafitUnlocked = preferenceManager.isGelaFitUnlocked()
        val targetAppUnlocked = preferenceManager.isTargetAppUnlocked()
        
        // Se is_active ou modo_kiosk está ativo E não está desbloqueado, impede saída da activity
        if ((isActive == true && !gelafitUnlocked) || (kioskMode == true && !targetAppUnlocked)) {
            Log.d(TAG, "🔒 Tentativa de sair bloqueada")
            Handler(Looper.getMainLooper()).post {
                if (kioskMode == true && !targetAppUnlocked) {
                    // Quando modo_kiosk está ativo, abre o app
                    val targetPackage = preferenceManager.getTargetPackageName()
                    if (!targetPackage.isNullOrEmpty()) {
                        openConfiguredApp(targetPackage)
                    }
                } else if (isActive == true && !gelafitUnlocked) {
                    // Quando apenas is_active está ativo, apenas mostra o grid INSTANTANEAMENTE
                    showAppsGrid()
                    val intent = Intent(this, GelaFitWorkspaceActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(intent)
                }
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
        private const val STATUS_SYNC_INTERVAL_MS = 15 * 60 * 1000L // 15 minutos
    }
}
