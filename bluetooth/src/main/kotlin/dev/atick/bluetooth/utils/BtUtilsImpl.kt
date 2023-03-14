package dev.atick.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.receiver.FoundDeviceReceiver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BtUtilsImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BtUtils {

    private val foundDevices = mutableListOf<BluetoothDevice>()

    override val isEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled ?: false

    override fun pairedDevices(): Flow<List<BluetoothDevice>> {
        return flowOf(bluetoothAdapter?.bondedDevices?.toList() ?: emptyList())
    }

    override fun scannedDevices(): Flow<List<BluetoothDevice>> {
        return callbackFlow {
            trySend(emptyList()) // ... Required to use flow.combine()
            val foundDeviceReceiver = FoundDeviceReceiver { device ->
                if (device !in foundDevices) {
                    foundDevices.add(device)
                }
                trySend(foundDevices.toList())
            }
            context.registerReceiver(
                foundDeviceReceiver, IntentFilter(BluetoothDevice.ACTION_FOUND)
            )
            awaitClose {
                context.unregisterReceiver(foundDeviceReceiver)
            }
        }
    }

    override fun startDiscovery() {
        foundDevices.clear()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }
}