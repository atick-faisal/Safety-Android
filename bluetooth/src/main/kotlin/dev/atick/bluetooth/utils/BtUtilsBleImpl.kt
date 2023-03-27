package dev.atick.bluetooth.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.IntentFilter
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.receiver.BtStateReceiver
import dev.atick.bluetooth.receiver.FoundDeviceReceiver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@SuppressLint("MissingPermission")
class BtUtilsBleImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BtUtils {

    private val foundDevices = mutableListOf<BluetoothDevice>()
    private lateinit var scanCallback: ScanCallback

    override val isBluetoothEnabled: Flow<Boolean>
        get() = callbackFlow {
            trySend(bluetoothAdapter?.isEnabled ?: false)
            val btStateReceiver = BtStateReceiver { btState ->
                trySend(btState)
            }
            context.registerReceiver(
                btStateReceiver,
                IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            )
            awaitClose { context.unregisterReceiver(btStateReceiver) }
        }

    override fun getPairedDevices(): Flow<List<BluetoothDevice>> {
        return callbackFlow {
            trySend(bluetoothAdapter?.bondedDevices?.toList() ?: emptyList())
            val btStateReceiver = BtStateReceiver { btState ->
                if (btState) {
                    trySend(bluetoothAdapter?.bondedDevices?.toList() ?: emptyList())
                }
            }
            context.registerReceiver(
                btStateReceiver,
                IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
            )
            awaitClose { context.unregisterReceiver(btStateReceiver) }
        }
    }

    override fun getScannedDevices(): Flow<List<BluetoothDevice>> {
        return callbackFlow {
            trySend(emptyList()) // ... Required to use flow.combine()
            val foundDeviceReceiver = FoundDeviceReceiver { device ->
                if (device !in foundDevices) {
                    foundDevices.add(device)
                }
                trySend(foundDevices.toList())
            }
            context.registerReceiver(
                foundDeviceReceiver,
                IntentFilter(BluetoothDevice.ACTION_FOUND)
            )

            scanCallback = object : ScanCallback() {
                override fun onScanResult(callbackType: Int, result: ScanResult?) {
                    super.onScanResult(callbackType, result)
                    result?.device?.let { device ->
                        if (device !in foundDevices) {
                            Logger.d("$device")
                            foundDevices.add(device)
                        }
                        trySend(foundDevices.toList())
                    }
                }
            }

            awaitClose {
                stopDiscovery()
            }
        }
    }

    override fun startDiscovery() {
        foundDevices.clear()
        bluetoothAdapter?.bluetoothLeScanner?.startScan(scanCallback)
    }

    override fun stopDiscovery() {
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }
}