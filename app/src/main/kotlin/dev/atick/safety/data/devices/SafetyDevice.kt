package dev.atick.safety.data.devices

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import dev.atick.bluetooth.data.models.DeviceState

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

@SuppressLint("MissingPermission")
fun DeviceState.asSafetyDevice(): SafetyDevice {
    return SafetyDevice(
        address = bluetoothDevice?.address ?: "Unknown",
        name = bluetoothDevice?.name ?: "Unknown",
        connected = isConnected
    )
}