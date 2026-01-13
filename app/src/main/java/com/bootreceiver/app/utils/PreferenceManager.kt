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
    
    /**
     * Verifica se o usuário já viu a informação do Device ID
     */
    fun hasSeenDeviceIdInfo(): Boolean {
        return prefs.getBoolean(KEY_HAS_SEEN_DEVICE_ID_INFO, false)
    }
    
    /**
     * Marca que o usuário já viu a informação do Device ID
     */
    fun setHasSeenDeviceIdInfo(hasSeen: Boolean) {
        prefs.edit().putBoolean(KEY_HAS_SEEN_DEVICE_ID_INFO, hasSeen).apply()
    }
    
    companion object {
        private const val PREFS_NAME = "BootReceiverPrefs"
        private const val KEY_TARGET_PACKAGE = "target_package_name"
        private const val KEY_HAS_SEEN_DEVICE_ID_INFO = "has_seen_device_id_info"
    }
}
