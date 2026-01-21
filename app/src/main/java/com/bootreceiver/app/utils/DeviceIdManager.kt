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
     * Obtém um ID único e estável do dispositivo.
     *
     * Regras:
     * 1) Usa primeiro o ID já salvo (garante estabilidade em execuções futuras).
     * 2) Se não houver salvo, deriva um ID determinístico baseado em ANDROID_ID (quando válido)
     *    ou em atributos de hardware, e então salva. Isso evita mudar a cada reinstalação
     *    no mesmo hardware.
     */
    fun getDeviceId(context: Context): String {
        val prefs: SharedPreferences = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )

        // 1) Se já temos salvo, devolve
        prefs.getString(KEY_DEVICE_ID, null)?.let {
            Log.d(TAG, "Device ID recuperado do SharedPreferences: $it")
            return it
        }

        // 2) Deriva ID determinístico
        val androidId = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        )

        val stableSeed = if (!androidId.isNullOrBlank() && androidId != "9774d56d682e549c") {
            "aid:$androidId"
        } else {
            // Fallback determinístico com dados de hardware
            val hw = listOf(
                android.os.Build.MANUFACTURER,
                android.os.Build.MODEL,
                android.os.Build.DEVICE,
                android.os.Build.BOARD,
                android.os.Build.HARDWARE,
                android.os.Build.BRAND
            ).joinToString("|") { it.orEmpty() }
            "hw:${hw.lowercase()}"
        }

        val derivedId = UUID.nameUUIDFromBytes(stableSeed.toByteArray())
            .toString().replace("-", "")

        // Salva para futuras execuções
        prefs.edit().putString(KEY_DEVICE_ID, derivedId).apply()
        Log.d(TAG, "Device ID derivado e salvo: $derivedId (seed=$stableSeed)")

        return derivedId
    }

    private const val TAG = "DeviceIdManager"
}
