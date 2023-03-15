package dev.atick.safety.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import dev.atick.bluetooth.data.BtDataSource
import dev.atick.core.extensions.collectWithLifecycle
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
        const val PERSISTENT_NOTIFICATION_ID = 4007
        const val DEVICE_ADDRESS_KEY = "dev.atick.safety.device.address"
        const val PERSISTENT_NOTIFICATION_CHANNEL_ID = "dev.atick.safety.persistent"
        const val ACTION_STOP_SERVICE = "dev.atick.safety.stop"
        const val STOP_SERVICE_REQUEST_CODE = 101
    }

    @Inject
    lateinit var btDataSource: BtDataSource

    @Inject
    lateinit var smsHelper: SmsHelper

    @Inject
    lateinit var safetyDao: SafetyDao

    private lateinit var notificationBuilder: Notification.Builder
    private lateinit var legacyNotificationBuilder: NotificationCompat.Builder
    private var isServiceRunning = false


    override fun onCreate() {
        super.onCreate()
        collectWithLifecycle(btDataSource.getDeviceState()) {
            if (it.isConnected) showToast("CONNECTED")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationBuilder
                    .setContentText(
                        if (it.isConnected) "Safety Bracelet is Connected"
                        else "Safety Bracelet is NOT Connected"
                    )
                    .setSmallIcon(
                        if (it.isConnected) Icon.createWithResource(this, R.drawable.ic_connected)
                        else Icon.createWithResource(this, R.drawable.ic_error)
                    )
            } else {
                legacyNotificationBuilder
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
                showNotification(PERSISTENT_NOTIFICATION_ID, getNotification())
            }
        }

        //            ... Setup Notification ...
        // ----------------------------------------------------
        val pendingIntent: PendingIntent =
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
            notificationBuilder = Notification.Builder(
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
                .setContentText("Initializing Safety Service ... ")
                .setContentIntent(pendingIntent)
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
            legacyNotificationBuilder = NotificationCompat.Builder(
                this,
                PERSISTENT_NOTIFICATION_CHANNEL_ID
            )
                .setContentTitle("Safety Bracelet")
                .setSmallIcon(R.drawable.ic_loading)
                .setContentText("Initializing Safety Service ... ")
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.ic_close, "STOP", stopServiceIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        if (intent?.action == ACTION_STOP_SERVICE) {
            stopForeground(STOP_FOREGROUND_REMOVE)
            stopSelf()
            isServiceRunning = false
            return START_NOT_STICKY
        }

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

        startForeground(PERSISTENT_NOTIFICATION_ID, getNotification())
        isServiceRunning = true

        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        isServiceRunning = false
        btDataSource.close()
        super.onDestroy()
    }

    private fun getNotification(): Notification {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.build()
        } else legacyNotificationBuilder.build()
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
    }
}