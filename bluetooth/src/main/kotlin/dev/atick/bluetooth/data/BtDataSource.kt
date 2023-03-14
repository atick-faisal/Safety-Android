package dev.atick.bluetooth.data

import dev.atick.bluetooth.data.models.BtMessage
import dev.atick.bluetooth.data.models.DeviceState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BtDataSource {
    fun getDeviceState(): StateFlow<DeviceState>
    fun listenForIncomingMessages(address: String): Flow<BtMessage>
    suspend fun close()
}