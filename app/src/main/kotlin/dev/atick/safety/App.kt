package dev.atick.safety

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.Logger
import dagger.hilt.android.HiltAndroidApp
import dev.atick.safety.service.SafetyService
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var logAdapter: LogAdapter

    override fun onCreate() {
        super.onCreate()
        Logger.addLogAdapter(logAdapter)
        Logger.i("SKYNET INITIATED!")
        createPersistentNotificationChannel()
    }

    private fun createPersistentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                SafetyService.PERSISTENT_NOTIFICATION_CHANNEL_ID,
                "Safety Notification",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Continuously monitor Safety devices"
            }
            val notificationManager = getSystemService(NOTIFICATION_SERVICE)
                as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}