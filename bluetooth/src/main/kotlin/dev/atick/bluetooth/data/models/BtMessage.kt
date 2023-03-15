package dev.atick.bluetooth.data.models

import java.util.*

data class BtMessage(
    val timestamp: Long = Date().time,
    val message: String
)
