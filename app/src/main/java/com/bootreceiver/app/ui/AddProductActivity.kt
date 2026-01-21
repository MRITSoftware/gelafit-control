package com.bootreceiver.app.ui

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bootreceiver.app.R
import com.bootreceiver.app.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Activity para adicionar produtos (apps) ao grid do modo kiosk
 */
class AddProductActivity : AppCompatActivity() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var appsRecyclerView: RecyclerView
    private val installedApps = mutableListOf<AppInfo>()
    private val selectedApps = mutableSetOf<String>() // Package names dos apps já selecionados
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Adicionar Produto ao Grid"
        
        appsRecyclerView = findViewById(R.id.appsRecyclerView)
        appsRecyclerView.layoutManager = LinearLayoutManager(this)
        
        // Carrega apps já selecionados
        loadSelectedApps()
        
        // Carrega lista de apps instalados
        loadInstalledApps()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    
    private fun loadSelectedApps() {
        val preferenceManager = PreferenceManager(this)
        val targetPackage = preferenceManager.getTargetPackageName()
        if (!targetPackage.isNullOrEmpty()) {
            selectedApps.add(targetPackage)
        }
        
        // Aqui você pode carregar outros apps salvos se houver uma lista
        // Por enquanto, apenas o app principal configurado
    }
    
    private fun loadInstalledApps() {
        serviceScope.launch {
            try {
                val apps = withContext(Dispatchers.IO) {
                    val pm = packageManager
                    val installedPackages = pm.getInstalledPackages(0)
                    val appList = mutableListOf<AppInfo>()
                    
                    installedPackages.forEach { packageInfo ->
                        try {
                            // Filtra apenas apps do usuário (não do sistema)
                            if (packageInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0) {
                                val appName = pm.getApplicationLabel(packageInfo.applicationInfo).toString()
                                appList.add(AppInfo(appName, packageInfo.packageName))
                            }
                        } catch (e: Exception) {
                            Log.w(TAG, "Erro ao carregar app ${packageInfo.packageName}: ${e.message}")
                        }
                    }
                    
                    appList.sortedBy { it.name }
                }
                
                installedApps.clear()
                installedApps.addAll(apps)
                
                runOnUiThread {
                    appsRecyclerView.adapter = AppsListAdapter(installedApps, selectedApps) { app ->
                        toggleAppSelection(app)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao carregar apps: ${e.message}", e)
            }
        }
    }
    
    private fun toggleAppSelection(app: AppInfo) {
        val preferenceManager = PreferenceManager(this)
        
        if (selectedApps.contains(app.packageName)) {
            // Remove da seleção
            selectedApps.remove(app.packageName)
            
            // Remove da lista persistente
            val currentApps = preferenceManager.getSelectedAppsList().toMutableSet()
            currentApps.remove(app.packageName)
            preferenceManager.saveSelectedAppsList(currentApps)
            
            Log.d(TAG, "App removido: ${app.name}")
        } else {
            // Adiciona à seleção
            selectedApps.add(app.packageName)
            
            // Salva na lista persistente
            val currentApps = preferenceManager.getSelectedAppsList().toMutableSet()
            currentApps.add(app.packageName)
            preferenceManager.saveSelectedAppsList(currentApps)
            
            // Notifica o GelaFitWorkspaceActivity para atualizar o grid imediatamente
            val intent = Intent("com.bootreceiver.app.APP_ADDED_TO_GRID").apply {
                putExtra("package_name", app.packageName)
            }
            sendBroadcast(intent)
            
            Log.d(TAG, "App adicionado ao grid: ${app.name}")
            Toast.makeText(this, "${app.name} adicionado ao grid", Toast.LENGTH_SHORT).show()
        }
        
        // Atualiza a lista
        appsRecyclerView.adapter?.notifyDataSetChanged()
    }
    
    data class AppInfo(
        val name: String,
        val packageName: String
    )
    
    private class AppsListAdapter(
        private val apps: List<AppInfo>,
        private val selectedApps: Set<String>,
        private val onAppClick: (AppInfo) -> Unit
    ) : RecyclerView.Adapter<AppsListAdapter.AppViewHolder>() {
        
        class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val appName: TextView = itemView.findViewById(R.id.appName)
            val appIcon: ImageView = itemView.findViewById(R.id.appIcon)
            val checkMark: ImageView = itemView.findViewById(R.id.checkMark)
        }
        
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
            val view = android.view.LayoutInflater.from(parent.context)
                .inflate(R.layout.item_app_selection, parent, false)
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
            
            // Mostra checkmark se selecionado
            val isSelected = selectedApps.contains(app.packageName)
            holder.checkMark.visibility = if (isSelected) View.VISIBLE else View.GONE
            
            holder.itemView.setOnClickListener {
                onAppClick(app)
            }
        }
        
        override fun getItemCount(): Int = apps.size
    }
    
    companion object {
        private const val TAG = "AddProductActivity"
    }
}
