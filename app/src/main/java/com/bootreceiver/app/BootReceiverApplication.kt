package com.bootreceiver.app

import android.app.Application
import android.util.Log

/**
 * Application class para inicialização global do app
 * Útil para configurações que precisam ser feitas antes de qualquer Activity
 */
class BootReceiverApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "BootReceiverApplication iniciado")
    }
    
    companion object {
        private const val TAG = "BootReceiverApp"
    }
}
