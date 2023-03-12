package dev.atick.sms.data

import dev.atick.sms.data.models.EmergencyMessage

interface SmsDataSource {
    suspend fun getEmergencyMessages(filterBy: List<String>): List<EmergencyMessage>
}