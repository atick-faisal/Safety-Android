package dev.atick.bluetooth.data

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.IntentFilter
import com.orhanobut.logger.Logger
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.atick.bluetooth.data.models.BtMessage
import dev.atick.bluetooth.data.models.DeviceState
import dev.atick.bluetooth.receiver.ConnectionStateReceiver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*
import javax.inject.Inject

class BtDataSourceImpl @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter?,
    @ApplicationContext private val context: Context
) : BtDataSource {

    companion object {
        const val BT_UUID = "00001101-0000-1000-8000-00805F9B34FB"
    }

    private var bluetoothSocket: BluetoothSocket? = null

    override fun getDeviceState(): Flow<DeviceState> {
        return callbackFlow {
            trySend(DeviceState())
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
        }
    }

    @SuppressLint("MissingPermission")
    override fun listenForIncomingMessages(address: String): Flow<Result<BtMessage>> {
        return flow {
            bluetoothSocket?.close()
            bluetoothSocket = bluetoothAdapter?.getRemoteDevice(address)
                ?.createInsecureRfcommSocketToServiceRecord(UUID.fromString(BT_UUID))
            try {
                bluetoothSocket?.let { socket ->
                    socket.connect()
                    val inputStream = socket.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    while (socket.isConnected) {
                        if (bufferedReader.ready()) {
                            val message = bufferedReader.readLine()

                            emit(Result.success(BtMessage(message = message)))
                        }
                    }
                }
            } catch (exception: Exception) {
                bluetoothSocket?.close()
                emit(Result.failure<BtMessage>(exception))
            }
        }.onCompletion {
            bluetoothSocket?.close()
            bluetoothSocket = null
        }.flowOn(Dispatchers.IO)
    }

    override fun close() {
        Logger.d("CLOSING ... ")
        bluetoothSocket?.close()
        bluetoothSocket = null
    }
}