package dev.atick.safety.data.devices

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice

data class SafetyDevice(
    val address: String,
    val name: String? = null,
    val connected: Boolean = false
)

@SuppressLint("MissingPermission")
fun BluetoothDevice.asSafetyDevice(): SafetyDevice {
    return SafetyDevice(
        address = address,
        name = name
    )
}