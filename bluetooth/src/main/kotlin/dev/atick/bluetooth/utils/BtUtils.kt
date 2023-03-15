package dev.atick.bluetooth.utils

import android.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BtUtils {
    val isBluetoothEnabled: Flow<Boolean>
    fun getScannedDevices(): Flow<List<BluetoothDevice>>
    fun getPairedDevices(): Flow<List<BluetoothDevice>>
    fun startDiscovery()
    fun stopDiscovery()
}