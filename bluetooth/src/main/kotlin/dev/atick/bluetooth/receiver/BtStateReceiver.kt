package dev.atick.bluetooth.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BtStateReceiver(
    private val onBtStateChange: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)) {
            BluetoothAdapter.STATE_ON -> onBtStateChange(true)
            BluetoothAdapter.STATE_OFF -> onBtStateChange(false)
        }
    }
}