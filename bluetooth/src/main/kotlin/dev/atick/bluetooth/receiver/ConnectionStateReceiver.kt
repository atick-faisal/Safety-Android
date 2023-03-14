@file:Suppress("DEPRECATION")

package dev.atick.bluetooth.receiver

import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import dev.atick.bluetooth.data.models.DeviceState

class ConnectionStateReceiver(
    private val onConnectionStateChange: (DeviceState) -> Unit
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val device = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent?.getParcelableExtra(
                BluetoothDevice.EXTRA_DEVICE,
                BluetoothDevice::class.java
            )
        } else {
            intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
        }
        when (intent?.action) {
            BluetoothDevice.ACTION_ACL_CONNECTED -> {
                onConnectionStateChange(
                    DeviceState(device ?: return, true)
                )
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> {
                onConnectionStateChange(
                    DeviceState(device ?: return, false)
                )
            }
        }
    }
}