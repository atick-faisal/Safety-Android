package dev.atick.safety.data.common

import java.text.SimpleDateFormat
import java.util.*

data class FallIncident(
    val victimName: String,
    val highRisk: Boolean = false,
    val timeStamp: Long = Date().time
) {
    fun getFormattedDate(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.US).format(timeStamp)
    }

    fun getFormattedTime(): String {
        return SimpleDateFormat("hh:mm", Locale.US).format(timeStamp)
    }
}