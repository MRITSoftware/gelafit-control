package com.bootreceiver.app.utils

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import java.util.UUID

/**
 * Gerenciador para obter um ID único do dispositivo
 * Gera um ID único e persistente que não muda mesmo após reinstalação
 */
object DeviceIdManager {
    
    private const val PREFS_NAME = "device_id_prefs"
    private const val KEY_DEVICE_ID = "device_id"
    
    /**
     * Obtém um ID único do dispositivo
     * Usa Android ID como base (persiste mesmo após desinstalação)
     * Se Android ID não estiver disponível, gera UUID único e salva em SharedPreferences
     * Este ID permanece o mesmo mesmo após reinstalar o app
     */
    fun getDeviceId(context: Context): String {
        // Primeiro tenta usar Android ID diretamente (persiste mesmo após desinstalação)
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        
        if (!androidId.isNullOrBlank() && androidId != "9774d56d682e549c") {
            // Android ID válido (não é o valor padrão de emuladores antigos)
            // Usa diretamente sem salvar em SharedPreferences para garantir persistência
            Log.d(TAG, "Device ID obtido do Android ID: $androidId")
            return androidId
        }
        
        // Se Android ID não estiver disponível, usa SharedPreferences como fallback
        val prefs: SharedPreferences = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
        
        // Tenta obter ID salvo
        var deviceId = prefs.getString(KEY_DEVICE_ID, null)
        
        if (deviceId == null) {
            // Gera novo UUID único
            deviceId = UUID.randomUUID().toString().replace("-", "")
            
            // Salva o ID para uso futuro
            prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
            Log.d(TAG, "Novo Device ID gerado e salvo (fallback): $deviceId")
        } else {
            Log.d(TAG, "Device ID recuperado do SharedPreferences: $deviceId")
        }
        
        return deviceId
    }
    
    private const val TAG = "DeviceIdManager"
}
