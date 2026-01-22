package com.bootreceiver.app.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log

/**
 * Classe utilitária para abrir aplicativos pelo package name
 * 
 * Verifica se o app está instalado e tenta abri-lo
 */
class AppLauncher(private val context: Context) {
    
    /**
     * Tenta abrir um aplicativo pelo seu package name
     * 
     * @param packageName Package name do app (ex: "com.example.app")
     * @return true se o app foi aberto com sucesso, false caso contrário
     */
    fun launchApp(packageName: String): Boolean {
        return try {
            // Verifica se o app está instalado
            if (!isAppInstalled(packageName)) {
                Log.e(TAG, "App não está instalado: $packageName")
                return false
            }
            
            // Obtém o intent para abrir o app
            val packageManager = context.packageManager
            val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
            
            if (launchIntent == null) {
                Log.e(TAG, "Não foi possível obter intent para: $packageName")
                return false
            }
            
            // Adiciona flags necessárias para abrir o app
            // FLAG_ACTIVITY_NEW_TASK é essencial para abrir de um contexto não-Activity
            // FLAG_ACTIVITY_CLEAR_TOP garante que não haja múltiplas instâncias
            // FLAG_ACTIVITY_SINGLE_TOP evita recriação se já estiver no topo
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            
            // Abre o app
            context.startActivity(launchIntent)
            Log.d(TAG, "App aberto com sucesso: $packageName")
            true
            
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao abrir app: $packageName", e)
            false
        }
    }
    
    /**
     * Fecha e reabre um aplicativo (reinicia o app)
     * 
     * @param packageName Package name do app
     * @return true se o app foi reiniciado com sucesso
     */
    fun restartApp(packageName: String): Boolean {
        return try {
            Log.d(TAG, "🔄 ========== REINICIANDO APP ==========")
            Log.d(TAG, "Package: $packageName")
            
            // Verifica se o app está instalado
            if (!isAppInstalled(packageName)) {
                Log.e(TAG, "❌ App não está instalado: $packageName")
                return false
            }
            
            // Método 1: Tenta múltiplas formas de fechar o app
            var appClosed = false
            
            // Tenta usar ActivityManager primeiro (requer permissão KILL_BACKGROUND_PROCESSES)
            try {
                Log.d(TAG, "🛑 Tentando fechar app usando ActivityManager...")
                val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
                activityManager.killBackgroundProcesses(packageName)
                Log.d(TAG, "✅ killBackgroundProcesses executado")
                appClosed = true
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ Erro ao usar killBackgroundProcesses: ${e.message}")
            }
            
            // Tenta usar am force-stop (pode não funcionar sem permissões de sistema)
            try {
                Log.d(TAG, "🛑 Tentando fechar app usando am force-stop...")
                val process = Runtime.getRuntime().exec("am force-stop $packageName")
                val exitCode = process.waitFor()
                Log.d(TAG, "✅ am force-stop executado (exit code: $exitCode)")
                appClosed = true
            } catch (e: Exception) {
                Log.w(TAG, "⚠️ Erro ao usar am force-stop (pode ser falta de permissões): ${e.message}")
            }
            
            // Aguarda um pouco para garantir que o app foi fechado
            if (appClosed) {
                Thread.sleep(2000) // Aguarda 2 segundos se conseguiu fechar
                Log.d(TAG, "⏳ Aguardou 2s após tentar fechar app")
            } else {
                Thread.sleep(1000) // Aguarda menos se não conseguiu fechar
                Log.d(TAG, "⏳ Aguardou 1s (não foi possível fechar app completamente)")
            }
            
            // Método 2: Reabrir o app com flags que forçam recriação completa
            try {
                val packageManager = context.packageManager
                val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
                
                if (launchIntent == null) {
                    Log.e(TAG, "❌ Não foi possível obter intent para: $packageName")
                    return false
                }
                
                // Flags para forçar reinício completo e recriação
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                
                // Abre o app
                context.startActivity(launchIntent)
                Log.d(TAG, "✅ App reaberto com sucesso: $packageName")
                return true
                
            } catch (e: Exception) {
                Log.e(TAG, "❌ Erro ao reabrir app: ${e.message}", e)
                return false
            }
            
        } catch (e: Exception) {
            Log.e(TAG, "❌ Erro ao reiniciar app: $packageName", e)
            false
        }
    }
    
    /**
     * Verifica se um app está instalado no dispositivo
     */
    private fun isAppInstalled(packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
    
    companion object {
        private const val TAG = "AppLauncher"
    }
}
