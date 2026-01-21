package com.bootreceiver.app.service

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.bootreceiver.app.utils.AppLauncher
import com.bootreceiver.app.utils.PreferenceManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Serviço que verifica conexão com internet e abre o app configurado
 * 
 * Este serviço:
 * 1. Aguarda alguns segundos após o boot (para garantir que o sistema está pronto)
 * 2. Verifica se há conexão com internet
 * 3. Se houver internet, abre o app configurado
 * 4. Se não houver, aguarda e tenta novamente em intervalos
 */
class BootService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.Main + Job())
    private var isRunning = false
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "BootService criado")
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (isRunning) {
            Log.d(TAG, "Serviço já está rodando")
            return START_STICKY
        }
        
        isRunning = true
        Log.d(TAG, "BootService iniciado")
        
        // Inicia o processo de verificação em uma coroutine
        serviceScope.launch {
            processBootSequence()
        }
        
        // Retorna START_STICKY para que o serviço seja reiniciado se for morto
        return START_STICKY
    }
    
    /**
     * Processa a sequência de boot:
     * 1. Aguarda delay inicial
     * 2. Verifica internet e abre app
     */
    private suspend fun processBootSequence() {
        val preferenceManager = PreferenceManager(this)
        val targetPackageName = preferenceManager.getTargetPackageName()
        
        if (targetPackageName.isNullOrEmpty()) {
            Log.w(TAG, "Nenhum app configurado. Parando serviço.")
            stopSelf()
            return
        }
        
        // Delay inicial após boot (10 segundos)
        // Isso garante que o sistema Android está completamente inicializado
        // e que o WiFi tenha tempo de conectar
        Log.d(TAG, "Aguardando ${DELAY_AFTER_BOOT_MS}ms (${DELAY_AFTER_BOOT_MS / 1000} segundos) após boot...")
        delay(DELAY_AFTER_BOOT_MS)
        Log.d(TAG, "Delay concluído. Iniciando verificação de internet...")
        
        // Tenta verificar internet e abrir o app
        tryOpenAppWithInternetCheck(targetPackageName)
    }
    
    /**
     * Verifica internet e tenta abrir o app
     * Se não houver internet, agenda nova tentativa
     */
    private suspend fun tryOpenAppWithInternetCheck(packageName: String) {
        var attempts = 0
        val maxAttempts = MAX_RETRY_ATTEMPTS
        
        while (attempts < maxAttempts && isRunning) {
            attempts++
            Log.d(TAG, "Tentativa $attempts/$maxAttempts: Verificando conexão com internet...")
            
            if (isInternetAvailable()) {
                Log.d(TAG, "Internet disponível! Tentando abrir app: $packageName")
                
                val appLauncher = AppLauncher(this)
                val success = appLauncher.launchApp(packageName)
                
                if (success) {
                    Log.d(TAG, "App aberto com sucesso!")
                    stopSelf()
                    return
                } else {
                    Log.w(TAG, "Falha ao abrir app. Verificando se está instalado...")
                    // Se o app não foi aberto, pode ser que não esteja instalado
                    // Aguarda um pouco e tenta novamente
                    delay(RETRY_DELAY_MS)
                }
            } else {
                Log.w(TAG, "Internet não disponível. Aguardando ${RETRY_DELAY_MS}ms antes de tentar novamente...")
                delay(RETRY_DELAY_MS)
            }
        }
        
        if (attempts >= maxAttempts) {
            Log.e(TAG, "Número máximo de tentativas atingido. Parando serviço.")
        }
        
        stopSelf()
    }
    
    /**
     * Verifica se há conexão ativa com internet
     * 
     * @return true se houver internet, false caso contrário
     */
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
               capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        Log.d(TAG, "⚠️ BootService destruído - tentando reiniciar após alguns segundos...")
        
        // Auto-restart após alguns segundos
        serviceScope.launch {
            try {
                delay(3000) // Aguarda 3 segundos antes de reiniciar
                Log.d(TAG, "🔄 Reiniciando BootService...")
                val restartIntent = Intent(this@BootService, BootService::class.java)
                startService(restartIntent)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao tentar reiniciar serviço: ${e.message}", e)
            }
        }
    }
    
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Log.d(TAG, "⚠️ App removido da lista de tarefas - reiniciando BootService...")
        
        // Reinicia o serviço após alguns segundos
        serviceScope.launch {
            try {
                delay(2000) // Aguarda 2 segundos
                val restartIntent = Intent(this@BootService, BootService::class.java)
                startService(restartIntent)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao reiniciar serviço após task removed: ${e.message}", e)
            }
        }
    }
    
    companion object {
        private const val TAG = "BootService"
        private const val DELAY_AFTER_BOOT_MS = 10000L // 10 segundos após boot
        private const val RETRY_DELAY_MS = 10000L // 10 segundos entre tentativas
        private const val MAX_RETRY_ATTEMPTS = 60 // Máximo de 60 tentativas (10 minutos)
    }
}
