package com.bootreceiver.app.receiver

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Device Admin Receiver necessário para permitir que o app
 * reinicie o dispositivo remotamente
 */
class DeviceAdminReceiver : DeviceAdminReceiver() {
    
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        Log.d(TAG, "Device Admin habilitado com sucesso!")
    }
    
    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        Log.w(TAG, "Device Admin desabilitado!")
    }
    
    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        Log.w(TAG, "Usuário tentou desabilitar Device Admin")
        return "Desabilitar o Device Admin impedirá o reinício remoto do dispositivo."
    }
    
    companion object {
        private const val TAG = "DeviceAdminReceiver"
    }
}
