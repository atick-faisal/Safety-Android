package dev.atick.safety.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Icon
import android.media.MediaPlayer
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.data.BtDataSource
import dev.atick.core.extensions.collectWithLifecycle
import dev.atick.core.ui.extensions.cancelNotification
import dev.atick.core.ui.extensions.showNotification
import dev.atick.core.ui.extensions.showToast
import dev.atick.safety.R
import dev.atick.safety.ui.MainActivity
import dev.atick.sms.utils.SmsHelper
import dev.atick.storage.room.data.SafetyDao
import dev.atick.storage.room.data.models.FallIncident
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SafetyService : LifecycleService() {

    companion object {
        const val PERSISTENT_NOTIFICATION_ID = 8088
        const val ALERT_NOTIFICATION_ID = 6502
        const val DEVICE_ADDRESS_KEY = "dev.atick.safety.device.address"
        const val PERSISTENT_NOTIFICATION_CHANNEL_ID = "dev.atick.safety.persistent"
        const val ALERT_NOTIFICATION_CHANNEL_ID = "dev.atick.safety.alert"
        const val ACTION_START_SERVICE = "dev.atick.safety.start.service"
        const val ACTION_STOP_SERVICE = "dev.atick.safety.stop.service"
        const val ACTION_STOP_ALARM = "dev.atick.safety.stop.alarm"
        const val STOP_SERVICE_REQUEST_CODE = 101
        const val STOP_ALARM_REQUEST_CODE = 301
    }

    @Inject
    lateinit var btDataSource: BtDataSource

    @Inject
    lateinit var smsHelper: SmsHelper

    @Inject
    lateinit var safetyDao: SafetyDao

    private lateinit var persistentNotificationBuilder: Notification.Builder
    private lateinit var legacyPersistentNotificationBuilder: NotificationCompat.Builder
    private lateinit var alarmNotificationBuilder: Notification.Builder
    private lateinit var legacyAlarmNotificationBuilder: NotificationCompat.Builder
    private var isServiceRunning = false

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate() {
        super.onCreate()
        collectWithLifecycle(btDataSource.getDeviceState()) {
            if (it.isConnected) showToast("CONNECTED")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                persistentNotificationBuilder
                    .setContentText(
                        if (it.isConnected) "Safety Bracelet is Connected"
                        else "Safety Bracelet is NOT Connected"
                    )
                    .setSmallIcon(
                        if (it.isConnected) Icon.createWithResource(this, R.drawable.ic_connected)
                        else Icon.createWithResource(this, R.drawable.ic_error)
                    )
            } else {
                legacyPersistentNotificationBuilder
                    .setContentText(
                        if (it.isConnected) "Safety Bracelet is Connected"
                        else "Safety Bracelet is NOT Connected"
                    )
                    .setSmallIcon(
                        if (it.isConnected) R.drawable.ic_connected
                        else R.drawable.ic_error
                    )
            }

            if (isServiceRunning) {
                showNotification(PERSISTENT_NOTIFICATION_ID, getPersistentNotification())
            }
        }

        setupPersistentNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        when (intent?.action) {
            ACTION_START_SERVICE -> initializeService(intent)
            ACTION_STOP_SERVICE -> stopService()
            ACTION_STOP_ALARM -> stopAlarm()
        }

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        isServiceRunning = false
        btDataSource.close()
        super.onDestroy()
    }

    private fun initializeService(intent: Intent?) {
        val address = intent?.getStringExtra(DEVICE_ADDRESS_KEY)
        address?.let {
            collectWithLifecycle(btDataSource.listenForIncomingMessages(address)) { result ->
                if (result.isSuccess) {
                    showToast("SAFETY BRACELET: ${result.getOrNull()?.message}")
                    handleFallEvent()
                } else {
                    showToast("SAFETY BRACELET: ${result.exceptionOrNull()}")
                }
            }
        }

        startForeground(PERSISTENT_NOTIFICATION_ID, getPersistentNotification())
        isServiceRunning = true
    }

    private fun getPersistentNotification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            persistentNotificationBuilder.build()
        } else legacyPersistentNotificationBuilder.build()
    }

    private fun getAlarmNotification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmNotificationBuilder.build()
        } else legacyAlarmNotificationBuilder.build()
    }

    private fun handleFallEvent() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    safetyDao.insertFallIncident(
                        FallIncident("You")
                    )
                } catch (exception: Exception) {
                    showToast("Error Saving Fall: ${exception.message}")
                }
                smsHelper.sendEmergencySmsToSelectedContacts()
            }
        }

        playAlarmSound(true)

        setupAlarmNotification()
        showNotification(ALERT_NOTIFICATION_ID, getAlarmNotification())
    }

    private fun setupPersistentNotification() {
        val openAppIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val stopSelf = Intent(this, SafetyService::class.java)
        stopSelf.action = ACTION_STOP_SERVICE
        val stopServiceIntent = PendingIntent.getService(
            this, STOP_SERVICE_REQUEST_CODE, stopSelf, PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            persistentNotificationBuilder = Notification.Builder(
                this,
                PERSISTENT_NOTIFICATION_CHANNEL_ID
            )
                .setContentTitle("Safety Bracelet")
                .setSmallIcon(
                    Icon.createWithResource(
                        this,
                        R.drawable.ic_loading
                    )
                )
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.app_icon))
                .setContentText("Initializing Safety Service ... ")
                .setContentIntent(openAppIntent)
                .addAction(
                    Notification.Action.Builder(
                        Icon.createWithResource(
                            this,
                            R.drawable.ic_close
                        ), "STOP", stopServiceIntent
                    ).build()
                )
                .setOngoing(true)
        } else {
            legacyPersistentNotificationBuilder = NotificationCompat.Builder(
                this,
                PERSISTENT_NOTIFICATION_CHANNEL_ID
            )
                .setContentTitle("Safety Bracelet")
                .setSmallIcon(R.drawable.ic_loading)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.app_icon))
                .setContentText("Initializing Safety Service ... ")
                .setContentIntent(openAppIntent)
                .addAction(R.drawable.ic_close, "STOP", stopServiceIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
        }
    }

    private fun setupAlarmNotification() {
        val openAppIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        val stopAlarm = Intent(this, SafetyService::class.java)
        stopAlarm.action = ACTION_STOP_ALARM
        val stopAlarmIntent = PendingIntent.getService(
            this, STOP_ALARM_REQUEST_CODE, stopAlarm, PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alarmNotificationBuilder = Notification.Builder(
                this,
                PERSISTENT_NOTIFICATION_CHANNEL_ID
            )
                .setContentTitle("!!! FALL ALARM !!!")
                .setSmallIcon(
                    Icon.createWithResource(
                        this,
                        R.drawable.ic_fall_alarm
                    )
                )
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.app_icon))
                .setContentText("A Fall has been Detected by the Safety Bracelet!")
                .setContentIntent(openAppIntent)
                .addAction(
                    Notification.Action.Builder(
                        Icon.createWithResource(
                            this,
                            R.drawable.ic_close
                        ), "STOP ALARM", stopAlarmIntent
                    ).build()
                )
                .setDeleteIntent(stopAlarmIntent)
        } else {
            legacyAlarmNotificationBuilder = NotificationCompat.Builder(
                this,
                PERSISTENT_NOTIFICATION_CHANNEL_ID
            )
                .setContentTitle("!!! FALL ALARM !!!")
                .setSmallIcon(R.drawable.ic_fall_alarm)
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.app_icon))
                .setContentText("A Fall has been Detected by the Safety Bracelet!")
                .setContentIntent(openAppIntent)
                .addAction(R.drawable.ic_close, "STOP ALARM", stopAlarmIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDeleteIntent(stopAlarmIntent)
        }
    }

    private fun playAlarmSound(play: Boolean) {
        if (play) {
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } else {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    private fun stopAlarm() {
        playAlarmSound(false)
        cancelNotification(ALERT_NOTIFICATION_ID)
    }

    private fun stopService() {
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        isServiceRunning = false
    }
}