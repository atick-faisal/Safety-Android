package dev.atick.bluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.data.models.BtMessage
import dev.atick.bluetooth.data.models.DeviceState
import dev.atick.bluetooth.receiver.ConnectionStateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

class BtDataSourceImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BtDataSource {

    companion object {
        const val BT_UUID = "27b7d1da-08c7-4505-a6d1-2459987e5e2d"
    }

    private var bluetoothSocket: BluetoothSocket? = null

    private val connectionStateReceiver = ConnectionStateReceiver {
        _deviceState.update { it }
    }

    private val _deviceState = MutableStateFlow(DeviceState())
    override fun getDeviceState(): StateFlow<DeviceState> {
        return _deviceState.asStateFlow()
    }

    init {
        context.registerReceiver(connectionStateReceiver, IntentFilter().apply {
            addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
            addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        })
    }

    @SuppressLint("MissingPermission")
    override fun listenForIncomingMessages(address: String): Flow<BtMessage> {
        return flow {
            val buffer = ByteArray(1024)
            bluetoothSocket?.close()
            bluetoothSocket = bluetoothAdapter?.getRemoteDevice(address)
                ?.createRfcommSocketToServiceRecord(UUID.fromString(BT_UUID))
            bluetoothSocket?.connect()
            try {
                bluetoothSocket?.let { socket ->
                    socket.connect()
                    while (socket.isConnected) {
                        val byteCount = socket.inputStream.read(buffer)
                        val message = buffer.decodeToString(endIndex = byteCount)
                        emit(BtMessage(message = message))
                    }

                }
            } catch (exception: Exception) {
                bluetoothSocket?.close()
                exception.printStackTrace()
                throw exception
            }
        }.onCompletion { close() }.flowOn(Dispatchers.IO)
    }

    override suspend fun close() {
        context.unregisterReceiver(connectionStateReceiver)
        bluetoothSocket?.close()
        bluetoothSocket = null
    }
}