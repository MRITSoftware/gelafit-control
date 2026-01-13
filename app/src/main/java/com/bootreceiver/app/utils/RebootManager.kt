package com.bootreceiver.app.utils

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log

/**
 * Gerenciador para reiniciar o dispositivo
 * 
 * IMPORTANTE: Para reiniciar o dispositivo, o app precisa ser
 * configurado como Device Admin. Isso requer ação do usuário.
 */
class RebootManager(private val context: Context) {
    
    private val devicePolicyManager: DevicePolicyManager? =
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as? DevicePolicyManager
    
    private val deviceAdminComponent: ComponentName =
        ComponentName(context, DeviceAdminReceiver::class.java)
    
    /**
     * Verifica se o app está configurado como Device Admin
     */
    fun isDeviceAdminActive(): Boolean {
        return devicePolicyManager?.isAdminActive(deviceAdminComponent) == true
    }
    
    /**
     * Solicita ao usuário que configure o app como Device Admin
     * Retorna true se já está ativo, false caso contrário
     */
    fun requestDeviceAdmin(): Boolean {
        if (isDeviceAdminActive()) {
            Log.d(TAG, "Device Admin já está ativo")
            return true
        }
        
        Log.d(TAG, "Solicitando permissão de Device Admin...")
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponent)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Este app precisa de permissão de Device Admin para reiniciar o dispositivo remotamente.")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        
        try {
            context.startActivity(intent)
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao solicitar Device Admin: ${e.message}", e)
            return false
        }
    }
    
    /**
     * Tenta reiniciar o dispositivo
     * 
     * @return true se o comando foi enviado com sucesso, false caso contrário
     */
    fun reboot(): Boolean {
        if (!isDeviceAdminActive()) {
            Log.w(TAG, "Device Admin não está ativo. Não é possível reiniciar.")
            return false
        }
        
        try {
            Log.d(TAG, "Reiniciando dispositivo...")
            
            // Tenta usar DevicePolicyManager.reboot() (requer API 24+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                devicePolicyManager?.reboot(deviceAdminComponent)
                Log.d(TAG, "Comando de reiniciar enviado via DevicePolicyManager")
                return true
            } else {
                // Para versões antigas, tenta usar PowerManager (requer permissão REBOOT)
                Log.w(TAG, "API level muito antigo. Reiniciar pode não funcionar.")
                return false
            }
        } catch (e: SecurityException) {
            Log.e(TAG, "Sem permissão para reiniciar: ${e.message}", e)
            return false
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao reiniciar: ${e.message}", e)
            return false
        }
    }
    
    companion object {
        private const val TAG = "RebootManager"
    }
}
