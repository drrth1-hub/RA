package com.example.oldtimerrallytimer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.oldtimerrallytimer.R
import com.example.oldtimerrallytimer.model.Mode
import com.example.oldtimerrallytimer.model.Signal
import kotlinx.coroutines.*
import kotlin.random.Random

typealias SignalCallback = (Signal) -> Unit

class SignalService: Service() {

    companion object {
        private var job: Job? = null
        private var callback: SignalCallback? = null

        fun start(ctx: Context, mode: Mode, simulator: Boolean, cb: SignalCallback) {
            callback = cb
            val intent = Intent(ctx, SignalService::class.java)
            ctx.startForegroundService(intent)

            job?.cancel()
            job = CoroutineScope(Dispatchers.Default).launch {
                while (isActive) {
                    delay(2200L) // >= 2s between signals
                    val sig = Signal(System.currentTimeMillis(), Random.nextInt(1, 6))
                    withContext(Dispatchers.Main) { callback?.invoke(sig) }
                }
            }
        }

        fun stop(ctx: Context) {
            job?.cancel(); job = null; callback = null
            ctx.stopService(Intent(ctx, SignalService::class.java))
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        val nm = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "signals"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            nm.createNotificationChannel(NotificationChannel(channelId, "Signal Service", NotificationManager.IMPORTANCE_LOW))
        }
        val notif: Notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Signal Service")
            .setContentText("Aktiv")
            .setOngoing(true)
            .build()
        startForeground(1, notif)
    }
}