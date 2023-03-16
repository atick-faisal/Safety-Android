package dev.atick.sms.data

import android.content.ContentResolver
import android.provider.Telephony
import dev.atick.sms.config.Config
import dev.atick.storage.room.data.SafetyDao
import dev.atick.storage.room.data.models.FallIncident
import javax.inject.Inject

class SmsDataSourceImpl @Inject constructor(
    private val safetyDao: SafetyDao,
    private val contentResolver: ContentResolver
) : SmsDataSource {
    override suspend fun syncEmergencyMessages() {
        val fallIncidents = mutableListOf<FallIncident>()

        val projection = arrayOf(
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY,
            Telephony.Sms.DATE,
            Telephony.Sms.TYPE
        )

        /* ... QUERY ONLY SAVED CONTACTS
        val emergencyNumbers = safetyDao.getEmergencyNumbers()
        val emergencyNumbersQuery = StringBuilder()
        emergencyNumbers.forEachIndexed { idx, phone ->
            emergencyNumbersQuery.append("\'")
            emergencyNumbersQuery.append(phone)
            emergencyNumbersQuery.append("\'")
            if (idx < emergencyNumbers.size - 1)
                emergencyNumbersQuery.append(",")
        }
        */

        val selectionQuery =
            // "${Telephony.Sms.ADDRESS} IN (${emergencyNumbersQuery})" +
            "${Telephony.Sms.BODY} like '%${Config.SAFETY_SMS_IDENTIFIER}%' " +
                "AND ${Telephony.Sms.TYPE} = ${Telephony.Sms.MESSAGE_TYPE_INBOX}"

        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection,
            selectionQuery,
            null,
            "${Telephony.Sms.DATE} DESC"
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
                            victimName = contact?.name ?: phone,
                            highRisk = contact?.highRisk ?: false,
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