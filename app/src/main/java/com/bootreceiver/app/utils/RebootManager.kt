package com.bootreceiver.app.utils

import android.app.admin.DevicePolicyManager
import com.bootreceiver.app.receiver.DeviceAdminReceiver
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
     * Tenta reiniciar o dispositivo usando múltiplos métodos
     * 
     * @return true se o comando foi enviado com sucesso, false caso contrário
     */
    fun reboot(): Boolean {
        Log.d(TAG, "Tentando reiniciar dispositivo...")
        
        // Método 1: DevicePolicyManager.reboot() (requer Device Admin e API 24+)
        if (isDeviceAdminActive() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            try {
                Log.d(TAG, "Tentando reiniciar via DevicePolicyManager...")
                devicePolicyManager?.reboot(deviceAdminComponent)
                Log.d(TAG, "✅ Comando de reiniciar enviado via DevicePolicyManager")
                return true
            } catch (e: SecurityException) {
                Log.w(TAG, "DevicePolicyManager.reboot() falhou por segurança: ${e.message}")
            } catch (e: Exception) {
                Log.w(TAG, "DevicePolicyManager.reboot() falhou: ${e.message}")
            }
        } else {
            if (!isDeviceAdminActive()) {
                Log.w(TAG, "Device Admin não está ativo")
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                Log.w(TAG, "API level ${Build.VERSION.SDK_INT} é muito antigo para DevicePolicyManager.reboot()")
            }
        }
        
        // Método 2: PowerManager.reboot() (requer permissão REBOOT - apenas para apps de sistema)
        // Nota: Este método geralmente não funciona em apps normais, apenas em apps de sistema
        try {
            Log.d(TAG, "Tentando reiniciar via PowerManager...")
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as? android.os.PowerManager
            if (powerManager != null) {
                // PowerManager.reboot() requer app de sistema ou permissão especial
                // Na maioria dos casos, isso não funcionará em apps normais
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    powerManager.reboot(null)
                    Log.d(TAG, "✅ Comando de reiniciar enviado via PowerManager")
                    return true
                }
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "PowerManager.reboot() falhou por segurança (esperado em apps normais): ${e.message}")
        } catch (e: NoSuchMethodError) {
            Log.w(TAG, "PowerManager.reboot() não disponível nesta versão do Android")
        } catch (e: Exception) {
            Log.w(TAG, "PowerManager.reboot() falhou: ${e.message}")
        }
        
        // Método 3: Runtime.exec com su (requer root)
        try {
            Log.d(TAG, "Tentando reiniciar via su (requer root)...")
            val process = Runtime.getRuntime().exec("su -c reboot")
            process.waitFor()
            if (process.exitValue() == 0) {
                Log.d(TAG, "✅ Comando de reiniciar enviado via su")
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Runtime.exec('su -c reboot') falhou (dispositivo pode não ter root): ${e.message}")
        }
        
        // Método 4: Runtime.exec com reboot direto (pode funcionar em alguns dispositivos)
        try {
            Log.d(TAG, "Tentando reiniciar via Runtime.exec('reboot')...")
            val process = Runtime.getRuntime().exec("reboot")
            process.waitFor()
            if (process.exitValue() == 0) {
                Log.d(TAG, "✅ Comando de reiniciar enviado via Runtime.exec")
                return true
            }
        } catch (e: Exception) {
            Log.w(TAG, "Runtime.exec('reboot') falhou: ${e.message}")
        }
        
        Log.e(TAG, "❌ Todos os métodos de reiniciar falharam. Verifique:")
        Log.e(TAG, "  1. Device Admin está ativo? ${isDeviceAdminActive()}")
        Log.e(TAG, "  2. Permissões de reboot no device_admin.xml estão corretas?")
        Log.e(TAG, "  3. Dispositivo tem root? (para métodos alternativos)")
        return false
    }
    
    companion object {
        private const val TAG = "RebootManager"
    }
}
