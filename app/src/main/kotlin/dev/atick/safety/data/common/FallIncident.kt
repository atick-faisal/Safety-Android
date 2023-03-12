package dev.atick.safety.data.common

import java.text.SimpleDateFormat
import java.util.*

typealias RoomFallIncident = dev.atick.storage.room.data.models.FallIncident

data class FallIncident(
    val id: Long = 0L,
    val victimName: String,
    val highRisk: Boolean = false,
    val readByUser: Boolean = false,
    val timeStamp: Long = Date().time
) {
    fun getFormattedDate(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.US).format(timeStamp)
    }

    fun getFormattedTime(): String {
        return SimpleDateFormat("hh:mm", Locale.US).format(timeStamp)
    }

    fun asRoomFallIncident(): RoomFallIncident {
        return RoomFallIncident(
            victimName = victimName,
            highRisk = highRisk,
            readByUser = readByUser,
            timestamp = timeStamp
        )
    }
}

fun RoomFallIncident.asFallIncident(): FallIncident {
    return FallIncident(
        victimName = victimName,
        highRisk = highRisk,
        readByUser = readByUser,
        timeStamp = timestamp
    )
}