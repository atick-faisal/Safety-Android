package dev.atick.sms.utils

import android.telephony.SmsManager
import dev.atick.location.data.LocationDataSource
import dev.atick.sms.config.Config
import dev.atick.storage.room.data.SafetyDao
import javax.inject.Inject

class SmsHelperImpl @Inject constructor(
    private val safetyDao: SafetyDao,
    private val locationDataSource: LocationDataSource,
    private val smsManager: SmsManager
) : SmsHelper {
    override suspend fun sendEmergencySmsToSelectedContacts() {
        val selectedPhoneNumbers = safetyDao.getSelectedPhoneNumbers()
        val location = locationDataSource.getLastKnownLocation()
        val locationLink = location?.let {
            "https://maps.google.com/?q=${location.latitude},${location.longitude}"
        } ?: "Location Not Available"

        val message = StringBuilder()
            .append(Config.SAFETY_SMS_IDENTIFIER)
            .append("\n")
            .append("Hi, I might need help!")
            .append("\n")
            .append(locationLink)
            .toString()

        selectedPhoneNumbers.forEach { phone ->
            smsManager.sendTextMessage(
                phone, null, message, null, null
            )
        }
    }
}