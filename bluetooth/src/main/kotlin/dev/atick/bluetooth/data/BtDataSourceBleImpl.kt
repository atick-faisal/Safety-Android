package dev.atick.bluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.content.IntentFilter
import android.os.Build
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.data.models.BtMessage
import dev.atick.bluetooth.data.models.DeviceState
import dev.atick.bluetooth.receiver.ConnectionStateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.nio.charset.Charset
import java.util.*
import javax.inject.Inject

@Suppress("DEPRECATION")
@SuppressLint("MissingPermission")
class BtDataSourceBleImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BtDataSource {

    companion object {
        const val CCCD_UUID = "00002902-0000-1000-8000-00805f9b34fb"
        const val SAFETY_SERVICE_UUID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
        const val SAFETY_CHARACTERISTIC_UUID = "beb5483e-36e1-4688-b7f5-ea07361b26a8"
    }

    private var bluetoothGatt: BluetoothGatt? = null

    private var bluetoothSocket: BluetoothSocket? = null

    override fun getDeviceState(): Flow<DeviceState> {
        return callbackFlow {
            trySend(DeviceState(isConnected = bluetoothSocket?.isConnected ?: false))
            val connectionStateReceiver = ConnectionStateReceiver { deviceState ->
                Logger.d("DEVICE STATE: ${deviceState.isConnected}")
                trySend(deviceState)
            }
            context.registerReceiver(
                connectionStateReceiver,
                IntentFilter().apply {
                    addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
                    addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
                }
            )
            awaitClose {
                context.unregisterReceiver(connectionStateReceiver)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun listenForIncomingMessages(address: String): Flow<Result<BtMessage>> {
        return callbackFlow {
            val gattCallback = object : BluetoothGattCallback() {
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt?,
                    status: Int,
                    newState: Int
                ) {
                    if (status == BluetoothGatt.GATT_SUCCESS &&
                        newState == BluetoothProfile.STATE_CONNECTED
                    ) {
                        bluetoothGatt = gatt
                        bluetoothGatt?.discoverServices()
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                    super.onServicesDiscovered(gatt, status)
                    enableNotification(SAFETY_SERVICE_UUID, SAFETY_CHARACTERISTIC_UUID)
                }

                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic,
                    value: ByteArray
                ) {
                    super.onCharacteristicChanged(gatt, characteristic, value)
                    trySend(
                        Result.success(
                            BtMessage(
                                message = String(
                                    value,
                                    Charset.defaultCharset()
                                )
                            )
                        )
                    )
                }
            }

            val device = bluetoothAdapter?.getRemoteDevice(address)
            device?.connectGatt(context, false, gattCallback)

            awaitClose { close() }

        }.flowOn(Dispatchers.IO)
    }

    @Suppress("SameParameterValue")
    private fun enableNotification(serviceUuid: String, charUuid: String) {
        val cccdUuid = UUID.fromString(CCCD_UUID)
        bluetoothGatt?.let { gatt ->
            val characteristic = gatt
                .getService(UUID.fromString(serviceUuid))
                ?.getCharacteristic(UUID.fromString(charUuid))
            characteristic?.let { char ->
                val payload = when {
                    char.isNotifiable() ->
                        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    char.isIndicatable() ->
                        BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                    else -> {
                        Logger.e("Can't Enable Notification!")
                        return
                    }
                }

                Logger.w("Enabling Notification ... ")
                char.getDescriptor(cccdUuid)?.let { cccDescriptor ->
                    if (bluetoothGatt?.setCharacteristicNotification(
                            characteristic,
                            true
                        ) == false
                    ) {
                        Logger.e("Enabling Notification Failed!")
                        return
                    }
                    writeDescriptor(cccDescriptor, payload)
                } ?: Logger.e("${char.uuid}: CCCD Not Found!")
            }
        } ?: Logger.e("Not connected to a BLE device!")
    }

    private fun writeDescriptor(
        descriptor: BluetoothGattDescriptor,
        payload: ByteArray
    ) {
        bluetoothGatt?.let { gatt ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                gatt.writeDescriptor(descriptor, payload)
            } else {
                descriptor.value = payload
                gatt.writeDescriptor(descriptor)
            }
        }
    }

    private fun BluetoothGattCharacteristic.isIndicatable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_INDICATE)

    private fun BluetoothGattCharacteristic.isNotifiable(): Boolean =
        containsProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY)

    private fun BluetoothGattCharacteristic.containsProperty(property: Int): Boolean {
        return properties and property != 0
    }


    override fun close() {
        Logger.d("CLOSING ... ")
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}