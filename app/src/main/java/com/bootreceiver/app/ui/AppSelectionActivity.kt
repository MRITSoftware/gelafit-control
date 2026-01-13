package com.bootreceiver.app.ui

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bootreceiver.app.R
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PermissionChecker
import com.bootreceiver.app.utils.PreferenceManager
import android.content.ClipData
import android.content.ClipboardManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity que permite ao usuário escolher qual app será aberto automaticamente
 * 
 * Esta tela aparece apenas na primeira vez que o app é aberto manualmente.
 * Após selecionar um app, ele será salvo e usado automaticamente nos próximos boots.
 */
class AppSelectionActivity : AppCompatActivity() {
    
    private lateinit var listView: ListView
    private lateinit var searchEditText: EditText
    private lateinit var deviceIdText: TextView
    private lateinit var preferenceManager: PreferenceManager
    private val appsList = mutableListOf<AppInfo>()
    private val filteredAppsList = mutableListOf<AppInfo>()
    private lateinit var adapter: AppListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_selection)
        
        preferenceManager = PreferenceManager(this)
        
        // Verifica permissões e otimizações
        checkPermissionsAndOptimizations()
        
        // Se já estiver configurado, fecha esta activity e abre o app configurado
        if (preferenceManager.isConfigured()) {
            Log.d(TAG, "App já configurado. Abrindo app configurado...")
            val targetPackage = preferenceManager.getTargetPackageName()
            if (targetPackage != null) {
                val appLauncher = com.bootreceiver.app.utils.AppLauncher(this)
                appLauncher.launchApp(targetPackage)
            }
            finish()
            return
        }
        
        listView = findViewById(R.id.listViewApps)
        searchEditText = findViewById(R.id.searchEditText)
        deviceIdText = findViewById(R.id.deviceIdText)
        
        // Mostra o Device ID no rodapé
        val deviceId = DeviceIdManager.getDeviceId(this)
        deviceIdText.text = "Device ID: $deviceId"
        deviceIdText.setOnClickListener {
            showDeviceIdDialog()
        }
        
        // Mostra diálogo informativo na primeira vez
        if (!preferenceManager.hasSeenDeviceIdInfo()) {
            showDeviceIdDialog()
            preferenceManager.setHasSeenDeviceIdInfo(true)
        }
        
        // Inicializa adapter vazio
        adapter = AppListAdapter(filteredAppsList)
        listView.adapter = adapter
        
        // Configura a barra de pesquisa
        setupSearchBar()
        
        // Carrega lista de apps em background
        loadInstalledApps()
        
        // Configura o click na lista
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedApp = filteredAppsList[position]
            showConfirmationDialog(selectedApp)
        }
    }
    
    /**
     * Mostra diálogo com o Device ID e permite copiar
     */
    private fun showDeviceIdDialog() {
        val deviceId = DeviceIdManager.getDeviceId(this)
        
        AlertDialog.Builder(this)
            .setTitle("Device ID (ID do Dispositivo)")
            .setMessage(
                "Este é o ID único do seu dispositivo:\n\n" +
                "$deviceId\n\n" +
                "Use este ID no Supabase para enviar comandos de reiniciar.\n\n" +
                "Você também pode ver o ID no rodapé desta tela."
            )
            .setPositiveButton("Copiar") { _, _ ->
                val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Device ID", deviceId)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(this, "Device ID copiado para a área de transferência!", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Fechar", null)
            .setCancelable(true)
            .show()
    }
    
    /**
     * Configura a barra de pesquisa para filtrar apps
     */
    private fun setupSearchBar() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterApps(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    /**
     * Filtra a lista de apps baseado no texto de pesquisa
     */
    private fun filterApps(query: String) {
        filteredAppsList.clear()
        
        if (query.isBlank()) {
            filteredAppsList.addAll(appsList)
        } else {
            val lowerQuery = query.lowercase()
            appsList.forEach { app ->
                if (app.name.lowercase().contains(lowerQuery) || 
                    app.packageName.lowercase().contains(lowerQuery)) {
                    filteredAppsList.add(app)
                }
            }
        }
        
        updateAdapter()
    }
    
    /**
     * Atualiza o adapter com a lista filtrada
     */
    private fun updateAdapter() {
        adapter.updateList(filteredAppsList)
    }
    
    /**
     * Mostra diálogo de confirmação antes de selecionar o app
     */
    private fun showConfirmationDialog(app: AppInfo) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.confirm_selection))
            .setMessage(getString(R.string.confirm_message, app.name))
            .setPositiveButton("Confirmar") { _, _ ->
                selectApp(app.packageName, app.name)
            }
            .setNegativeButton("Cancelar", null)
            .setCancelable(true)
            .show()
    }
    
    /**
     * Verifica permissões e otimizações e alerta o usuário se necessário
     */
    private fun checkPermissionsAndOptimizations() {
        val permissionChecker = PermissionChecker(this)
        val status = permissionChecker.getFullStatus()
        
        if (!status.isReady) {
            Log.w(TAG, "Problemas detectados: ${status.issues.joinToString()}")
            
            val message = buildString {
                append("Para garantir que o app funcione corretamente no boot, é necessário:\n\n")
                status.issues.forEach { issue ->
                    append("• $issue\n")
                }
                append("\nDeseja abrir as configurações agora?")
            }
            
            AlertDialog.Builder(this)
                .setTitle("Configurações Necessárias")
                .setMessage(message)
                .setPositiveButton("Abrir Configurações") { _, _ ->
                    // Abre configurações de otimização de bateria
                    if (status.batteryOptimized) {
                        permissionChecker.openBatteryOptimizationSettings()
                    }
                    // Abre configurações de overlay se necessário
                    if (status.issues.any { it.contains("SYSTEM_ALERT_WINDOW") }) {
                        permissionChecker.openOverlayPermissionSettings()
                    }
                }
                .setNegativeButton("Depois", null)
                .setCancelable(true)
                .show()
        } else {
            Log.d(TAG, "Todas as permissões e otimizações estão corretas")
        }
    }
    
    /**
     * Carrega lista de aplicativos instalados
     * Executa em background para não travar a UI
     */
    private fun loadInstalledApps() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val packageManager = packageManager
                val installedPackages = packageManager.getInstalledPackages(PackageManager.GET_META_DATA)
                
                val apps = installedPackages
                    .filter { 
                        // Filtra apenas apps que podem ser abertos
                        val appInfo = it.applicationInfo
                        val launchIntent = packageManager.getLaunchIntentForPackage(it.packageName)
                        // Inclui apps que têm intent de launch (podem ser abertos)
                        launchIntent != null
                    }
                    .map {
                        val appInfo = it.applicationInfo
                        val appName = try {
                            packageManager.getApplicationLabel(appInfo).toString()
                        } catch (e: Exception) {
                            it.packageName
                        }
                        AppInfo(appName, it.packageName)
                    }
                    .sortedBy { it.name }
                    .distinctBy { it.packageName }
                
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "Carregados ${apps.size} aplicativos")
                    appsList.clear()
                    appsList.addAll(apps)
                    filteredAppsList.clear()
                    filteredAppsList.addAll(apps)
                    updateAdapter()
                    
                    if (appsList.isEmpty()) {
                        Toast.makeText(
                            this@AppSelectionActivity,
                            "Nenhum aplicativo encontrado",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Log.d(TAG, "Lista atualizada com sucesso. Total: ${appsList.size} apps")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao carregar apps", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@AppSelectionActivity,
                        "Erro ao carregar aplicativos: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
    
    /**
     * Salva o app selecionado e fecha a activity
     */
    private fun selectApp(packageName: String, appName: String) {
        Log.d(TAG, "App selecionado: $appName ($packageName)")
        
        // Salva o package name
        preferenceManager.saveTargetPackageName(packageName)
        
        Toast.makeText(
            this,
            "App configurado: $appName\nO app será aberto automaticamente no próximo boot.",
            Toast.LENGTH_LONG
        ).show()
        
        // Aguarda um pouco e fecha a activity
        listView.postDelayed({
            finish()
        }, 2000)
    }
    
    /**
     * Adapter customizado para exibir apps na lista
     */
    private inner class AppListAdapter(apps: MutableList<AppInfo>) : BaseAdapter() {
        private val appsList = mutableListOf<AppInfo>().apply { addAll(apps) }
        
        fun updateList(newApps: List<AppInfo>) {
            appsList.clear()
            appsList.addAll(newApps)
            notifyDataSetChanged()
            Log.d(TAG, "Adapter atualizado com ${appsList.size} apps")
        }
        
        override fun getCount(): Int = appsList.size
        
        override fun getItem(position: Int): AppInfo = appsList[position]
        
        override fun getItemId(position: Int): Long = position.toLong()
        
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: layoutInflater.inflate(R.layout.list_item_app, parent, false)
            
            if (position < appsList.size) {
                val app = appsList[position]
                
                val appNameView = view.findViewById<TextView>(R.id.appName)
                val appPackageView = view.findViewById<TextView>(R.id.appPackage)
                
                appNameView?.text = app.name
                appPackageView?.text = app.packageName
            }
            
            return view
        }
    }
    
    /**
     * Classe de dados para representar um app
     */
    data class AppInfo(
        val name: String,
        val packageName: String
    )
    
    companion object {
        private const val TAG = "AppSelectionActivity"
    }
}
