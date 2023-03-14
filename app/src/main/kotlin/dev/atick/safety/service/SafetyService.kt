package dev.atick.safety.service

import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.data.BtDataSource
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.core.ui.extensions.showToast
import dev.atick.safety.ui.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class SafetyService : LifecycleService() {

    companion object {
        const val PERSISTENT_NOTIFICATION_ID = 4007
        const val PERSISTENT_NOTIFICATION_CHANNEL_ID = "dev.atick.safety.persistent"
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

        Logger.d("START SERVICE: ${intent?.getStringExtra("ADDRESS")}")

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val notification =
            NotificationCompat.Builder(this, PERSISTENT_NOTIFICATION_CHANNEL_ID)
                .setContentText("Safety")
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .build()

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
        super.onDestroy()
        btDataSource.close()
    }

}