package com.bootreceiver.app.utils

import android.content.Context
import android.provider.Settings
import android.util.Log

/**
 * Gerenciador para obter um ID único do dispositivo
 */
object DeviceIdManager {
    
    /**
     * Obtém um ID único do dispositivo
     * Usa Android ID como identificador único
     */
    fun getDeviceId(context: Context): String {
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )
        
        Log.d(TAG, "Device ID obtido: $androidId")
        return androidId ?: "unknown_device"
    }
    
    private const val TAG = "DeviceIdManager"
}
