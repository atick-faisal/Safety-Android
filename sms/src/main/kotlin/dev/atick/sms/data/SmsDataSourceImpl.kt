package dev.atick.sms.data

import android.content.ContentResolver
import android.provider.Telephony
import dev.atick.sms.data.models.EmergencyMessage
import javax.inject.Inject

class SmsDataSourceImpl @Inject constructor(
    private val contentResolver: ContentResolver
) : SmsDataSource {
    override suspend fun getEmergencyMessages(filterBy: List<String>): List<EmergencyMessage> {

        val emergencyMessages = mutableListOf<EmergencyMessage>()

        val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.DATE)
        val selection = StringBuilder()
        filterBy.forEachIndexed { idx, phone ->
            selection.append("\'")
            selection.append(phone)
            selection.append("\'")
            if (idx < filterBy.size - 1)
                selection.append(",")
        }

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection,
            "${Telephony.Sms.ADDRESS} IN (${selection})",
            null,
            null
        )

        cursor?.let {
            if (cursor.moveToFirst()) {
                repeat(cursor.count) {
                    val smsDate =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val phone =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val timestamp = smsDate.toLong()
                    emergencyMessages.add(EmergencyMessage(phone, timestamp))
                    cursor.moveToNext()
                }
            }
            cursor.close()
        }

        return emergencyMessages.toList()
    }
}