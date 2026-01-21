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
    
    /**
     * Verifica se o dispositivo já foi registrado no Supabase
     */
    fun isDeviceRegistered(): Boolean {
        return prefs.getBoolean(KEY_DEVICE_REGISTERED, false)
    }
    
    /**
     * Marca que o dispositivo foi registrado no Supabase
     */
    fun setDeviceRegistered(registered: Boolean) {
        prefs.edit().putBoolean(KEY_DEVICE_REGISTERED, registered).apply()
    }
    
    /**
     * Salva o nome da unidade (email)
     */
    fun saveUnitName(unitName: String) {
        prefs.edit().putString(KEY_UNIT_NAME, unitName).apply()
    }
    
    /**
     * Obtém o nome da unidade salvo
     */
    fun getUnitName(): String? {
        return prefs.getString(KEY_UNIT_NAME, null)
    }

    // Cache local de status (economiza requisições)
    fun saveIsActiveCached(isActive: Boolean) {
        prefs.edit().putBoolean(KEY_CACHED_IS_ACTIVE, isActive).apply()
    }

    fun getIsActiveCached(): Boolean {
        return prefs.getBoolean(KEY_CACHED_IS_ACTIVE, false)
    }

    fun saveKioskModeCached(kioskMode: Boolean) {
        prefs.edit().putBoolean(KEY_CACHED_KIOSK_MODE, kioskMode).apply()
    }

    fun getKioskModeCached(): Boolean {
        return prefs.getBoolean(KEY_CACHED_KIOSK_MODE, false)
    }

    fun saveStatusLastSync(timestampMs: Long) {
        prefs.edit().putLong(KEY_STATUS_LAST_SYNC, timestampMs).apply()
    }

    fun getStatusLastSync(): Long {
        return prefs.getLong(KEY_STATUS_LAST_SYNC, 0L)
    }

    // Configuração da área de desbloqueio
    fun saveUnlockHotspotPosition(position: String) {
        prefs.edit().putString(KEY_UNLOCK_HOTSPOT_POSITION, position).apply()
    }

    fun getUnlockHotspotPosition(): String {
        return prefs.getString(KEY_UNLOCK_HOTSPOT_POSITION, "bottom_right") ?: "bottom_right"
    }
    
    fun isUnlockHotspotConfigured(): Boolean {
        return prefs.contains(KEY_UNLOCK_HOTSPOT_POSITION)
    }
    
    // Lista de apps selecionados para o grid
    fun saveSelectedAppsList(apps: Set<String>) {
        prefs.edit().putStringSet(KEY_SELECTED_APPS_LIST, apps).apply()
    }
    
    fun getSelectedAppsList(): Set<String> {
        return prefs.getStringSet(KEY_SELECTED_APPS_LIST, emptySet()) ?: emptySet()
    }
    
    // Estado de desbloqueio individual
    fun saveGelaFitUnlocked(unlocked: Boolean) {
        prefs.edit().putBoolean(KEY_GELAFIT_UNLOCKED, unlocked).apply()
    }
    
    fun isGelaFitUnlocked(): Boolean {
        return prefs.getBoolean(KEY_GELAFIT_UNLOCKED, false)
    }
    
    fun saveTargetAppUnlocked(unlocked: Boolean) {
        prefs.edit().putBoolean(KEY_TARGET_APP_UNLOCKED, unlocked).apply()
    }
    
    fun isTargetAppUnlocked(): Boolean {
        return prefs.getBoolean(KEY_TARGET_APP_UNLOCKED, false)
    }
    
    companion object {
        private const val PREFS_NAME = "BootReceiverPrefs"
        private const val KEY_TARGET_PACKAGE = "target_package_name"
        private const val KEY_HAS_SEEN_DEVICE_ID_INFO = "has_seen_device_id_info"
        private const val KEY_DEVICE_REGISTERED = "device_registered"
        private const val KEY_UNIT_NAME = "unit_name"
        private const val KEY_CACHED_IS_ACTIVE = "cached_is_active"
        private const val KEY_CACHED_KIOSK_MODE = "cached_kiosk_mode"
        private const val KEY_STATUS_LAST_SYNC = "status_last_sync"
        private const val KEY_UNLOCK_HOTSPOT_POSITION = "unlock_hotspot_position"
        private const val KEY_SELECTED_APPS_LIST = "selected_apps_list"
        private const val KEY_GELAFIT_UNLOCKED = "gelafit_unlocked"
        private const val KEY_TARGET_APP_UNLOCKED = "target_app_unlocked"
    }
}
