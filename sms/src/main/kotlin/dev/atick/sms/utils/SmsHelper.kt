package dev.atick.sms.utils

interface SmsHelper {
    suspend fun sendEmergencySmsToSelectedContacts()
}