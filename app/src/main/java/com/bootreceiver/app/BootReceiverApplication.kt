package com.bootreceiver.app

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import com.bootreceiver.app.service.RebootMonitorService
import com.bootreceiver.app.utils.DeviceIdManager
import com.bootreceiver.app.utils.PreferenceManager
import com.bootreceiver.app.utils.SupabaseManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        
        // Atualiza registro do dispositivo no Supabase (atualiza last_seen)
        updateDeviceRegistration()
        
        // Inicia o serviço de monitoramento de comandos de reiniciar como Foreground Service
        try {
            val monitorIntent = Intent(this, RebootMonitorService::class.java)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                startForegroundService(monitorIntent)
            } else {
                startService(monitorIntent)
            }
            Log.d(TAG, "RebootMonitorService iniciado como Foreground Service")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao iniciar RebootMonitorService: ${e.message}", e)
        }
    }
    
    /**
     * Atualiza o registro do dispositivo no Supabase (atualiza last_seen)
     * Isso é feito sempre que o app inicia para manter o dispositivo sincronizado
     */
    private fun updateDeviceRegistration() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val preferenceManager = PreferenceManager(this@BootReceiverApplication)
                
                // Só atualiza se o dispositivo já foi registrado (tem email)
                if (preferenceManager.isDeviceRegistered()) {
                    val deviceId = DeviceIdManager.getDeviceId(this@BootReceiverApplication)
                    val unitName = preferenceManager.getUnitName()
                    val supabaseManager = SupabaseManager()
                    
                    val success = supabaseManager.registerDevice(deviceId, unitName)
                    if (success) {
                        Log.d(TAG, "Registro do dispositivo atualizado no Supabase")
                    } else {
                        Log.w(TAG, "Falha ao atualizar registro do dispositivo")
                    }
                } else {
                    Log.d(TAG, "Dispositivo ainda não foi registrado. Será registrado na primeira abertura do app.")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao atualizar registro do dispositivo: ${e.message}", e)
            }
        }
    }
    
    companion object {
        private const val TAG = "BootReceiverApp"
    }
}
