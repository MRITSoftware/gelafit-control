package com.bootreceiver.app.service

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.bootreceiver.app.R

/**
 * Serviço que cria um overlay invisível para interceptar gestos de minimização
 * quando o modo kiosk está ativo
 * 
 * O overlay cobre toda a tela e intercepta eventos de toque que poderiam minimizar o app
 */
class KioskOverlayService : Service() {
    
    private var overlayView: View? = null
    private var windowManager: WindowManager? = null
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "KioskOverlayService criado")
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val kioskEnabled = intent?.getBooleanExtra("kiosk_enabled", false) ?: false
        
        if (kioskEnabled) {
            showOverlay()
        } else {
            hideOverlay()
        }
        
        return START_NOT_STICKY
    }
    
    private fun showOverlay() {
        if (overlayView != null) {
            Log.d(TAG, "Overlay já está visível")
            return
        }
        
        try {
            Log.d(TAG, "🔒 Mostrando overlay de kiosk...")
            
            // Cria uma view invisível que cobre toda a tela
            overlayView = FrameLayout(this).apply {
                setOnTouchListener { _, event ->
                    // Intercepta todos os eventos de toque
                    // Se for um gesto de Home (swipe up), bloqueia
                    if (event.action == MotionEvent.ACTION_UP) {
                        val y = event.y
                        val screenHeight = resources.displayMetrics.heightPixels
                        
                        // Se o gesto foi na parte inferior da tela (onde fica o botão Home)
                        // e foi um swipe para cima, bloqueia
                        if (y > screenHeight * 0.8f) {
                            Log.d(TAG, "🔒 Gesto de Home detectado e bloqueado!")
                            return@setOnTouchListener true // Consome o evento
                        }
                    }
                    false
                }
            }
            
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    @Suppress("DEPRECATION")
                    WindowManager.LayoutParams.TYPE_PHONE
                },
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSPARENT
            ).apply {
                gravity = Gravity.TOP or Gravity.START
                x = 0
                y = 0
            }
            
            windowManager?.addView(overlayView, params)
            Log.d(TAG, "✅ Overlay de kiosk mostrado")
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao mostrar overlay: ${e.message}", e)
        }
    }
    
    private fun hideOverlay() {
        overlayView?.let { view ->
            try {
                windowManager?.removeView(view)
                overlayView = null
                Log.d(TAG, "🔓 Overlay de kiosk removido")
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao remover overlay: ${e.message}", e)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        hideOverlay()
        Log.d(TAG, "KioskOverlayService destruído")
    }
    
    companion object {
        private const val TAG = "KioskOverlayService"
    }
}
