package dev.atick.sms.data

import android.content.ContentResolver
import android.provider.Telephony
import dev.atick.storage.room.data.SafetyDao
import dev.atick.storage.room.data.models.FallIncident
import javax.inject.Inject

class SmsDataSourceImpl @Inject constructor(
    private val safetyDao: SafetyDao,
    private val contentResolver: ContentResolver
) : SmsDataSource {
    override suspend fun syncEmergencyMessages() {
        val emergencyNumbers = safetyDao.getEmergencyNumbers()
        val fallIncidents = mutableListOf<FallIncident>()

        val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.DATE)
        val selection = StringBuilder()
        emergencyNumbers.forEachIndexed { idx, phone ->
            selection.append("\'")
            selection.append(phone)
            selection.append("\'")
            if (idx < emergencyNumbers.size - 1)
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
                    val contact = safetyDao.getContactFromPhone(phone)
                    fallIncidents.add(
                        FallIncident(
                            victimName = contact.name,
                            highRisk = contact.highRisk,
                            readByUser = false,
                            timestamp = timestamp
                        )
                    )
                    cursor.moveToNext()
                }
            }
            cursor.close()
        }

        safetyDao.insertAllFallIncidents(fallIncidents)
    }
}