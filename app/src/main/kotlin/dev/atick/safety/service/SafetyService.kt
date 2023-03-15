package dev.atick.safety.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.data.BtDataSource
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.core.ui.extensions.showToast
import dev.atick.safety.R
import dev.atick.safety.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SafetyService : LifecycleService() {

    companion object {
        const val PERSISTENT_NOTIFICATION_ID = 4007
        const val PERSISTENT_NOTIFICATION_CHANNEL_ID = "dev.atick.safety.persistent"
        const val ACTION_STOP_SERVICE = "dev.atick.safety.stop"
    }

    @Inject
    lateinit var btDataSource: BtDataSource

    override fun onCreate() {
        super.onCreate()
        collectWithLifecycle(btDataSource.getDeviceState()) {
            Logger.d("DEVICE STATE: ${it.isConnected}")
            if (it.isConnected) {
                showToast("CONNECTED")
            } else {
                showToast("DISCONNECTED")
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val stopSelf = Intent(this, SafetyService::class.java)
        stopSelf.action = ACTION_STOP_SERVICE
        val stopServiceIntent = PendingIntent.getService(
            this, 101, stopSelf, PendingIntent.FLAG_IMMUTABLE
        )

        if (intent?.action == ACTION_STOP_SERVICE) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            return START_NOT_STICKY
        }

        Logger.d("START SERVICE: ${intent?.getStringExtra("ADDRESS")}")


        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(this, PERSISTENT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(
                    Icon.createWithResource(
                        this,
                        R.drawable.app_icon
                    )
                )
                .setContentText("Safety Notification")
                .setContentText("HOLA AMIGOS")
                .setContentIntent(pendingIntent)
                .addAction(
                    Notification.Action.Builder(
                        Icon.createWithResource(
                            this,
                            R.drawable.app_icon
                        ), "STOP", stopServiceIntent
                    ).build()
                )
                .setOngoing(true)
                .build()
        } else {
            NotificationCompat.Builder(this, PERSISTENT_NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentText("Safety Notification")
                .setContentText("HOLA AMIGOS")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.app_icon, "STOP", stopServiceIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
                .build()
        }

        val address = intent?.getStringExtra("ADDRESS")
        address?.let {
            collectWithLifecycle(btDataSource.listenForIncomingMessages(address)) { result ->
                if (result.isSuccess) {
                    Logger.d("${result.getOrNull()}")
                } else {
                    Logger.e("${result.exceptionOrNull()}")
                }
            }
        }


        startForeground(PERSISTENT_NOTIFICATION_ID, notification)

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        btDataSource.close()
        super.onDestroy()
    }

}