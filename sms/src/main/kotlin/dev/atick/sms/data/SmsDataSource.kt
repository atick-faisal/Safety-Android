package dev.atick.sms.data

interface SmsDataSource {
    suspend fun syncEmergencyMessages()
}