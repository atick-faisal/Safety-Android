package dev.atick.bluetooth.data.models

import android.bluetooth.BluetoothDevice

data class DeviceState(
    val bluetoothDevice: BluetoothDevice? = null,
    val isConnected: Boolean = false
)