package com.bootreceiver.app

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.bootreceiver.app.service.RebootMonitorService

/**
 * Application class para inicialização global do app
 * Útil para configurações que precisam ser feitas antes de qualquer Activity
 * 
 * IMPORTANTE: Esta classe garante que o BroadcastReceiver seja registrado
 * quando o app é aberto pela primeira vez.
 */
class BootReceiverApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "BootReceiverApplication iniciado")
        
        // Força o registro do receiver ao abrir o app
        // Isso garante que o receiver esteja ativo mesmo em Android 10+
        try {
            val pm = packageManager
            val componentName = android.content.ComponentName(
                this,
                "com.bootreceiver.app.receiver.BootReceiver"
            )
            
            // Verifica se o receiver está habilitado
            val state = pm.getComponentEnabledSetting(componentName)
            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER) {
                Log.w(TAG, "Receiver estava desabilitado. Habilitando...")
                pm.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP
                )
            }
            
            Log.d(TAG, "Receiver verificado e habilitado")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao verificar receiver: ${e.message}", e)
        }
        
        // Inicia o serviço de monitoramento de comandos de reiniciar
        try {
            val monitorIntent = Intent(this, RebootMonitorService::class.java)
            startService(monitorIntent)
            Log.d(TAG, "RebootMonitorService iniciado")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao iniciar RebootMonitorService: ${e.message}", e)
        }
    }
    
    companion object {
        private const val TAG = "BootReceiverApp"
    }
}
