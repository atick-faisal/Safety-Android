package dev.atick.bluetooth.utils

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BtUtils {
    val isEnabled: Boolean
    fun scannedDevices(): Flow<List<BluetoothDevice>>
    fun pairedDevices(): Flow<List<BluetoothDevice>>
    fun startDiscovery()
    fun stopDiscovery()
}