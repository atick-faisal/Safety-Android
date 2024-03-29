package dev.atick.safety.repository.content

import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.data.devices.SafetyDevice
import kotlinx.coroutines.flow.Flow

interface ContentRepository {
    suspend fun insertContact(contact: Contact): Result<Unit>
    suspend fun updateContact(contact: Contact): Result<Unit>
    suspend fun deleteContact(contact: Contact): Result<Unit>
    fun getContacts(): Flow<List<Contact>>
    suspend fun insertFallIncident(fallIncident: FallIncident): Result<Unit>
    suspend fun updateFallIncident(fallIncident: FallIncident): Result<Unit>
    fun getRecentFallIncident(): Flow<FallIncident?>
    fun getUnreadFallIncidents(): Flow<List<FallIncident>>
    fun getReadFallIncidents(): Flow<List<FallIncident>>
    fun getPairedDevices(): Flow<List<SafetyDevice>>
    fun getScannedDevices(): Flow<List<SafetyDevice>>
    fun getConnectedDevice(): Flow<SafetyDevice>
    fun startDiscovery(): Result<Unit>
    fun stopDiscovery(): Result<Unit>
    fun closeConnection(): Result<Unit>
    suspend fun syncEmergencyMessages(): Result<Unit>
    suspend fun sendEmergencySmsToSelectedContacts(): Result<Unit>
}