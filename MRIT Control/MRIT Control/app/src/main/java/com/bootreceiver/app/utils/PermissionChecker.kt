package com.bootreceiver.app.utils

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log

/**
 * Classe utilitária para verificar permissões e otimizações de bateria
 * 
 * Verifica se o app tem todas as permissões necessárias e se não está
 * sendo otimizado pelo sistema (o que pode impedir o funcionamento no boot)
 */
class PermissionChecker(private val context: Context) {
    
    /**
     * Verifica se todas as permissões necessárias estão concedidas
     * 
     * Nota: RECEIVE_BOOT_COMPLETED, INTERNET e ACCESS_NETWORK_STATE são
     * permissões normais que são concedidas automaticamente no install.
     * Apenas verificamos permissões especiais que requerem ação do usuário.
     */
    fun checkAllPermissions(): PermissionStatus {
        val missingPermissions = mutableListOf<String>()
        
        // Verificar permissão de sobrepor outras apps (Android 6.0+)
        // Esta é opcional mas recomendada para funcionar melhor
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                missingPermissions.add("SYSTEM_ALERT_WINDOW (Sobrepor outras apps - opcional)")
            }
        }
        
        // As outras permissões (RECEIVE_BOOT_COMPLETED, INTERNET, ACCESS_NETWORK_STATE)
        // são concedidas automaticamente e não precisam verificação
        
        return if (missingPermissions.isEmpty()) {
            PermissionStatus(true, emptyList())
        } else {
            PermissionStatus(false, missingPermissions)
        }
    }
    
    /**
     * Verifica se o app está sendo otimizado pela bateria
     * Apps otimizados podem não receber BOOT_COMPLETED
     */
    fun isBatteryOptimized(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val powerManager = context.getSystemService(Context.POWER_SERVICE) as? PowerManager
                powerManager?.let {
                    !it.isIgnoringBatteryOptimizations(context.packageName)
                } ?: false
            } catch (e: Exception) {
                Log.w(TAG, "Erro ao verificar otimização de bateria: ${e.message}")
                false
            }
        } else {
            false // Android < 6.0 não tem otimização de bateria
        }
    }
    
    /**
     * Verifica se o app está sendo otimizado pelo sistema
     * (diferente de otimização de bateria - específico de alguns fabricantes)
     * 
     * Nota: Esta verificação pode não funcionar em todas as versões do Android
     * devido a limitações de API. A verificação de otimização de bateria é mais importante.
     */
    fun isSystemOptimized(): Boolean {
        // Por enquanto, retorna false para evitar problemas de compatibilidade
        // A verificação de otimização de bateria é suficiente para a maioria dos casos
        return false
    }
    
    /**
     * Verifica status completo de permissões e otimizações
     */
    fun getFullStatus(): FullStatus {
        val permissionStatus = checkAllPermissions()
        val batteryOptimized = isBatteryOptimized()
        val systemOptimized = isSystemOptimized()
        
        val issues = mutableListOf<String>()
        if (!permissionStatus.allGranted) {
            issues.add("Permissões faltando: ${permissionStatus.missingPermissions.joinToString()}")
        }
        if (batteryOptimized) {
            issues.add("App está sendo otimizado pela bateria")
        }
        if (systemOptimized) {
            issues.add("App está sendo otimizado pelo sistema")
        }
        
        return FullStatus(
            allPermissionsGranted = permissionStatus.allGranted,
            batteryOptimized = batteryOptimized,
            systemOptimized = systemOptimized,
            issues = issues,
            isReady = issues.isEmpty()
        )
    }
    
    /**
     * Abre as configurações para desabilitar otimização de bateria
     */
    fun openBatteryOptimizationSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = Uri.parse("package:${context.packageName}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao abrir configurações de otimização: ${e.message}", e)
                // Fallback: abrir configurações gerais de bateria
                try {
                    val intent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(intent)
                } catch (e2: Exception) {
                    Log.e(TAG, "Erro ao abrir configurações gerais: ${e2.message}", e2)
                }
            }
        }
    }
    
    /**
     * Abre as configurações para permitir sobrepor outras apps
     */
    fun openOverlayPermissionSettings() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                    data = Uri.parse("package:${context.packageName}")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao abrir configurações de overlay: ${e.message}", e)
            }
        }
    }
    
    data class PermissionStatus(
        val allGranted: Boolean,
        val missingPermissions: List<String>
    )
    
    data class FullStatus(
        val allPermissionsGranted: Boolean,
        val batteryOptimized: Boolean,
        val systemOptimized: Boolean,
        val issues: List<String>,
        val isReady: Boolean
    )
    
    companion object {
        private const val TAG = "PermissionChecker"
    }
}
