package com.bootreceiver.app.ui

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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
    private var unlockHandler: Handler? = null
    private lateinit var appsGridRecyclerView: RecyclerView
    private val selectedApps = mutableListOf<AppInfo>()
    private val unlockRunnable = Runnable {
        performEmergencyUnlock()
    }
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

        // Configura gesto de desbloqueio (verifica se já está configurado, senão configura)
        if (!preferenceManager.isUnlockHotspotConfigured()) {
            showUnlockHotspotSetupDialog()
        } else {
            setupUnlockHotspot()
        }
        
        // Mostra o grid por padrão (será ajustado conforme is_active)
        appsGridRecyclerView.visibility = View.VISIBLE
        
        // Inicia monitoramento de is_active e modo_kiosk (verifica status inicial também)
        startMonitoring()
        
        // Registra receiver para atualizar grid quando app for adicionado
        registerReceiver(appAddedReceiver, IntentFilter("com.bootreceiver.app.APP_ADDED_TO_GRID"))
    }
    
    /**
     * Configura o botão para ativar modo kiosk
     */
    private fun setupKioskButton() {
        val activateKioskButton = findViewById<Button>(R.id.activateKioskButton)
        activateKioskButton.setOnClickListener {
            activateKioskMode()
        }
        
        // Mostra o botão quando is_active está ativo mas kiosk_mode não está
        serviceScope.launch {
            val status = supabaseManager.getDeviceStatus(deviceId)
            val isActive = status?.isActive ?: false
            val kioskMode = status?.kioskMode ?: false
            
            runOnUiThread {
                if (isActive && !kioskMode) {
                    activateKioskButton.visibility = View.VISIBLE
                } else {
                    activateKioskButton.visibility = View.GONE
                }
            }
        }
    }
    
    /**
     * Atualiza a visibilidade do botão de ativar modo kiosk
     */
    private fun updateKioskButtonVisibility(isActive: Boolean, kioskMode: Boolean) {
        runOnUiThread {
            val activateKioskButton = findViewById<Button>(R.id.activateKioskButton)
            if (isActive && !kioskMode) {
                activateKioskButton.visibility = View.VISIBLE
            } else {
                activateKioskButton.visibility = View.GONE
            }
        }
    }
    
    /**
     * Mostra/esconde botão de fixar GelaFit Kiosk
     */
    private fun showFixGelaFitButton() {
        runOnUiThread {
            val fixGelaFitButton = findViewById<Button>(R.id.fixGelaFitButton)
            if (isActive == false) {
                fixGelaFitButton.visibility = View.VISIBLE
                fixGelaFitButton.setOnClickListener {
                    fixGelaFitKiosk()
                }
            } else {
                fixGelaFitButton.visibility = View.GONE
            }
        }
    }
    
    /**
     * Fixa o GelaFit Control em modo kiosk (seta is_active = true)
     */
    private fun fixGelaFitKiosk() {
        serviceScope.launch {
            try {
                val success = withContext(Dispatchers.IO) {
                    supabaseManager.updateIsActive(deviceId, true)
                }
                
                if (success) {
                    // Atualiza cache local e reseta estado de desbloqueio
                    preferenceManager.saveIsActiveCached(true)
                    preferenceManager.saveGelaFitUnlocked(false) // Reseta desbloqueio ao fixar
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
                        showFixGelaFitButton() // Esconde o botão
                        Toast.makeText(this@GelaFitWorkspaceActivity, "GelaFit Control fixado em modo kiosk", Toast.LENGTH_LONG).show()
                    }
                    
                    Log.d(TAG, "✅ GelaFit Control fixado (is_active=true)")
                } else {
                    runOnUiThread {
                        Toast.makeText(this@GelaFitWorkspaceActivity, "Erro ao fixar GelaFit Control", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao fixar GelaFit Control: ${e.message}", e)
                runOnUiThread {
                    Toast.makeText(this@GelaFitWorkspaceActivity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * Mostra diálogo para configurar área de desbloqueio na primeira vez
     */
    private fun showUnlockHotspotSetupDialog() {
        AlertDialog.Builder(this)
            .setTitle("Configurar Área de Desbloqueio")
            .setMessage("Escolha onde você quer configurar a área de desbloqueio. Depois, toque e segure por 5 segundos nesse local para desbloquear.")
            .setItems(arrayOf("Canto Superior Esquerdo", "Canto Superior Direito", "Canto Inferior Esquerdo", "Canto Inferior Direito")) { _, which ->
                val positions = arrayOf("top_left", "top_right", "bottom_left", "bottom_right")
                val positionNames = arrayOf("Canto Superior Esquerdo", "Canto Superior Direito", "Canto Inferior Esquerdo", "Canto Inferior Direito")
                val selectedPosition = positions[which]
                preferenceManager.saveUnlockHotspotPosition(selectedPosition)
                setupUnlockHotspot()
                
                // Mostra instrução para testar
                AlertDialog.Builder(this)
                    .setTitle("Área Configurada")
                    .setMessage("Área configurada: ${positionNames[which]}\n\nToque e segure por 5 segundos nessa área para desbloquear.")
                    .setPositiveButton("OK", null)
                    .show()
            }
            .setCancelable(false)
            .show()
    }
    
    /**
     * Configura a área de desbloqueio por long press (5s - posição configurável)
     */
    private fun setupUnlockHotspot() {
        val hotspot = findViewById<View>(R.id.unlockHotspot)
        unlockHandler = Handler(Looper.getMainLooper())
        
        // Aplica posição configurada
        val position = preferenceManager.getUnlockHotspotPosition()
        val layoutParams = hotspot.layoutParams as RelativeLayout.LayoutParams
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_START)
        layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_END)
        
        when (position) {
            "top_left" -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                layoutParams.setMargins(16, 16, 0, 0)
            }
            "top_right" -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParams.setMargins(0, 16, 16, 0)
            }
            "bottom_left" -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_START)
                layoutParams.setMargins(16, 0, 0, 16)
            }
            "bottom_right" -> {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParams.setMargins(0, 0, 16, 16)
            }
            else -> {
                // Default: bottom_right
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END)
                layoutParams.setMargins(0, 0, 16, 16)
            }
        }
        hotspot.layoutParams = layoutParams
        
        hotspot.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    unlockHandler?.postDelayed(unlockRunnable, UNLOCK_HOLD_MS)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_MOVE -> {
                    unlockHandler?.removeCallbacks(unlockRunnable)
                }
            }
            true
        }
    }
    
    /**
     * Configura a posição da área de desbloqueio
     */
    private fun configureUnlockHotspot() {
        val positions = arrayOf("Canto Superior Esquerdo", "Canto Superior Direito", "Canto Inferior Esquerdo", "Canto Inferior Direito")
        val positionValues = arrayOf("top_left", "top_right", "bottom_left", "bottom_right")
        
        val currentPosition = preferenceManager.getUnlockHotspotPosition()
        val currentIndex = positionValues.indexOf(currentPosition).takeIf { it >= 0 } ?: 3
        
        AlertDialog.Builder(this)
            .setTitle("Configurar Área de Desbloqueio")
            .setMessage("Escolha onde tocar e segurar por 5 segundos para desbloquear:")
            .setSingleChoiceItems(positions, currentIndex) { dialog, which ->
                preferenceManager.saveUnlockHotspotPosition(positionValues[which])
                setupUnlockHotspot() // Reconfigura o hotspot
                dialog.dismiss()
                Toast.makeText(this, "Área de desbloqueio configurada: ${positions[which]}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
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
                    showFixGelaFitButton()
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
                    showFixGelaFitButton()
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
                R.id.menu_configure_unlock -> {
                    configureUnlockHotspot()
                    true
                }
                else -> false
            }
        }
        
        popup.show()
    }
    
    /**
     * Verifica configurações necessárias
     */
    private fun checkSettings() {
        val intent = Intent(this, SettingsCheckActivity::class.java)
        startActivity(intent)
    }
    
    /**
     * Adiciona produto ao grid
     */
    private fun addProductToGrid() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
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
        serviceScope.launch {
            try {
                val appInfo = withContext(Dispatchers.IO) {
                    try {
                        val pm = packageManager
                        val info = pm.getApplicationInfo(packageName, 0)
                        val appName = pm.getApplicationLabel(info).toString()
                        AppInfo(appName, packageName)
                    } catch (e: Exception) {
                        Log.e(TAG, "Erro ao carregar info do app: ${e.message}", e)
                        null
                    }
                }
                
                if (appInfo != null) {
                    // Adiciona à lista local
                    if (!selectedApps.any { it.packageName == packageName }) {
                        selectedApps.add(appInfo)
                    }
                    
                    // Salva na lista persistente
                    val currentApps = preferenceManager.getSelectedAppsList().toMutableSet()
                    currentApps.add(packageName)
                    preferenceManager.saveSelectedAppsList(currentApps)
                    
                    // Atualiza o grid imediatamente
                    runOnUiThread {
                        appsGridRecyclerView.adapter?.notifyDataSetChanged()
                        Log.d(TAG, "App adicionado ao grid: ${appInfo.name}")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao adicionar app ao grid: ${e.message}", e)
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
                // Usa cache primeiro para resposta imediata
                val cachedIsActive = preferenceManager.getIsActiveCached()
                val cachedKioskMode = preferenceManager.getKioskModeCached()
                isActive = cachedIsActive
                kioskMode = cachedKioskMode
                applyInitialSettings()

                // Sincroniza com Supabase se último sync passou de 15 minutos OU se nunca foi sincronizado (lastSync = 0)
                val now = System.currentTimeMillis()
                val lastSync = preferenceManager.getStatusLastSync()
                val needsSync = lastSync == 0L || (now - lastSync > STATUS_SYNC_INTERVAL_MS)

                if (needsSync) {
                    val status = supabaseManager.getDeviceStatus(deviceId)
                    val freshIsActive = status?.isActive
                    val freshKiosk = status?.kioskMode
                    Log.d(TAG, "Status inicial (sync) - is_active: $freshIsActive, modo_kiosk: $freshKiosk")

                    if (freshIsActive != null) {
                        isActive = freshIsActive
                        preferenceManager.saveIsActiveCached(freshIsActive)
                    }
                    if (freshKiosk != null) {
                        kioskMode = freshKiosk
                        preferenceManager.saveKioskModeCached(freshKiosk)
                    }
                    preferenceManager.saveStatusLastSync(now)
                    applyInitialSettings()
                } else {
                    Log.d(TAG, "Usando cache recente (menos de 15 min); último sync: ${lastSync}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao verificar status inicial: ${e.message}", e)
            }
            
            // Loop de monitoramento contínuo
            while (isMonitoring) {
                try {
                    // Usa cache e sincroniza apenas se passar do intervalo
                    val nowLoop = System.currentTimeMillis()
                    val lastSyncLoop = preferenceManager.getStatusLastSync()
                    var currentIsActive = preferenceManager.getIsActiveCached()
                    var currentKioskMode = preferenceManager.getKioskModeCached()

                    val shouldSync = nowLoop - lastSyncLoop > STATUS_SYNC_INTERVAL_MS
                    if (shouldSync) {
                        val status = supabaseManager.getDeviceStatus(deviceId)
                        currentIsActive = status?.isActive ?: currentIsActive
                        currentKioskMode = status?.kioskMode ?: currentKioskMode
                        preferenceManager.saveIsActiveCached(currentIsActive)
                        preferenceManager.saveKioskModeCached(currentKioskMode)
                        preferenceManager.saveStatusLastSync(nowLoop)
                        Log.d(TAG, "Sincronizado status com Supabase (loop)")
                    }
                    
                    // Atualiza visibilidade dos botões
                    updateKioskButtonVisibility(currentIsActive == true, currentKioskMode == true)
                    showFixGelaFitButton()
                    
                    // Se mudou o status, aplica as mudanças
                    if (isActive != currentIsActive || kioskMode != currentKioskMode) {
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        if (currentIsActive == true) {
                            Log.d(TAG, "🔒 IS_ACTIVE ATIVADO - Bloqueando acesso a outros apps e mantendo GelaFit Control em modo kiosk")
                            applyAppBlocking()
                            enableGelaFitKioskMode() // Mantém GelaFit Control em modo kiosk quando is_active = true
                            showAppsGrid()
                        } else {
                            Log.d(TAG, "🔓 IS_ACTIVE DESATIVADO - Liberando acesso")
                            removeAppBlocking()
                            disableGelaFitKioskMode() // Remove modo kiosk do GelaFit Control quando is_active = false
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
                            // Se is_active ainda está ativo, mantém modo kiosk do GelaFit Control
                            if (currentIsActive == true) {
                                enableGelaFitKioskMode()
                            }
                        }
                        Log.d(TAG, "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━")
                        
                        isActive = currentIsActive
                        kioskMode = currentKioskMode
                    }
                    
                    // Se modo_kiosk está ativo, garante que o app está sempre em foreground
                    // Se apenas is_active está ativo, mantém modo kiosk do GelaFit Control e não força abertura do app (usuário escolhe pelo grid)
                    if (currentKioskMode == true) {
                        ensureAppInForeground()
                    } else if (currentIsActive == true) {
                        // Quando apenas is_active está ativo, garante que apenas o app configurado pode estar aberto
                        // mas não força a abertura - o usuário escolhe pelo grid
                        // Mantém modo kiosk do GelaFit Control ativo
                        enableGelaFitKioskMode()
                        ensureOnlyConfiguredAppIsOpen()
                    } else {
                        // Se is_active está desativado, remove modo kiosk do GelaFit Control
                        disableGelaFitKioskMode()
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
        // Atualiza visibilidade dos botões
        updateKioskButtonVisibility(isActive == true, kioskMode == true)
        showFixGelaFitButton()
        
        if (isActive == true) {
            applyAppBlocking()
            enableGelaFitKioskMode() // Mantém GelaFit Control em modo kiosk quando is_active = true
            showAppsGrid() // Sempre mostra o grid quando is_active está ativo
        } else {
            disableGelaFitKioskMode() // Remove modo kiosk do GelaFit Control quando is_active = false
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
        
        // Verifica se está desbloqueado individualmente
        val gelafitUnlocked = preferenceManager.isGelaFitUnlocked()
        val targetAppUnlocked = preferenceManager.isTargetAppUnlocked()
        
        // Se is_active está ativo E não está desbloqueado, impede que a activity seja pausada (minimizada)
        if (isActive == true && !gelafitUnlocked && kioskMode != true) {
            Log.d(TAG, "🔒 Tentativa de pausar bloqueada (is_active = true, não desbloqueado)")
            // Não abre o app, apenas mostra o grid
            showAppsGrid()
        } else if (kioskMode == true && !targetAppUnlocked) {
            // Quando modo_kiosk está ativo E não está desbloqueado, abre o app automaticamente
            val targetPackage = preferenceManager.getTargetPackageName()
            if (!targetPackage.isNullOrEmpty()) {
                openConfiguredApp(targetPackage)
            }
        } else {
            // Se está desbloqueado, permite minimizar normalmente
            Log.d(TAG, "🔓 Pausa permitida (desbloqueado)")
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        
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
        private const val STATUS_SYNC_INTERVAL_MS = 15 * 60 * 1000L // 15 minutos
        private const val UNLOCK_HOLD_MS = 5000L // 5 segundos para desbloquear
    }
}
