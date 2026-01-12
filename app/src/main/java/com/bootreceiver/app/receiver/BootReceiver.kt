package com.bootreceiver.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.bootreceiver.app.service.BootService
import com.bootreceiver.app.utils.PreferenceManager

/**
 * BroadcastReceiver que escuta o evento de boot completo do Android
 * 
 * Quando o dispositivo é ligado ou reiniciado, o sistema Android envia
 * o broadcast BOOT_COMPLETED. Este receiver captura esse evento e inicia
 * o serviço que verifica internet e abre o app configurado.
 */
class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            "android.intent.action.QUICKBOOT_POWERON" -> {
                Log.d(TAG, "Boot detectado! Iniciando processo...")
                
                // Verifica se já foi configurado um app para iniciar
                val preferenceManager = PreferenceManager(context)
                val targetPackageName = preferenceManager.getTargetPackageName()
                
                if (targetPackageName.isNullOrEmpty()) {
                    Log.w(TAG, "Nenhum app configurado. Abrindo tela de seleção...")
                    // Se não houver app configurado, abre a tela de seleção
                    val selectionIntent = Intent(context, 
                        com.bootreceiver.app.ui.AppSelectionActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(selectionIntent)
                } else {
                    Log.d(TAG, "App alvo configurado: $targetPackageName")
                    // Inicia o serviço que verifica internet e abre o app
                    val serviceIntent = Intent(context, BootService::class.java)
                    context.startService(serviceIntent)
                }
            }
            else -> {
                Log.w(TAG, "Ação desconhecida recebida: ${intent.action}")
            }
        }
    }
    
    companion object {
        private const val TAG = "BootReceiver"
    }
}
