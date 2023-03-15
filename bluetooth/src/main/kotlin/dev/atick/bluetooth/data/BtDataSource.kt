package dev.atick.bluetooth.data

import dev.atick.bluetooth.data.models.BtMessage
import dev.atick.bluetooth.data.models.DeviceState
import kotlinx.coroutines.flow.Flow

interface BtDataSource {
    fun getDeviceState(): Flow<DeviceState>
    fun listenForIncomingMessages(address: String): Flow<Result<BtMessage>>
    fun close()
}