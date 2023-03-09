package dev.atick.safety.data.devices

data class SafetyDevice(
    val address: String,
    val name: String? = null,
    val connected: Boolean = false
)
