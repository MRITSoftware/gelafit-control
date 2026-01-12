package com.bootreceiver.app.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Gerenciador de preferências para salvar/carregar configurações do app
 * 
 * Usa SharedPreferences para persistir:
 * - Package name do app alvo
 * - Se já foi configurado pela primeira vez
 */
class PreferenceManager(context: Context) {
    
    private val prefs: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME, Context.MODE_PRIVATE
    )
    
    /**
     * Salva o package name do app que deve ser aberto automaticamente
     */
    fun saveTargetPackageName(packageName: String) {
        prefs.edit().putString(KEY_TARGET_PACKAGE, packageName).apply()
    }
    
    /**
     * Retorna o package name do app configurado
     * @return package name ou null se não estiver configurado
     */
    fun getTargetPackageName(): String? {
        return prefs.getString(KEY_TARGET_PACKAGE, null)
    }
    
    /**
     * Verifica se já foi configurado um app alvo
     */
    fun isConfigured(): Boolean {
        return !getTargetPackageName().isNullOrEmpty()
    }
    
    /**
     * Limpa a configuração (útil para testes)
     */
    fun clearConfiguration() {
        prefs.edit().remove(KEY_TARGET_PACKAGE).apply()
    }
    
    companion object {
        private const val PREFS_NAME = "BootReceiverPrefs"
        private const val KEY_TARGET_PACKAGE = "target_package_name"
    }
}
