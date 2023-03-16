package dev.atick.safety.repository.content

import dev.atick.bluetooth.data.BtDataSource
import dev.atick.bluetooth.utils.BtUtils
import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.common.asFallIncident
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.data.contacts.asContact
import dev.atick.safety.data.devices.SafetyDevice
import dev.atick.safety.data.devices.asSafetyDevice
import dev.atick.sms.data.SmsDataSource
import dev.atick.sms.utils.SmsHelper
import dev.atick.storage.room.data.SafetyDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val safetyDao: SafetyDao,
    private val btUtils: BtUtils,
    private val btDataSource: BtDataSource,
    private val smsDataSource: SmsDataSource,
    private val smsHelper: SmsHelper
) : ContentRepository {
    override suspend fun insertContact(contact: Contact): Result<Unit> {
        return try {
            safetyDao.insertContact(contact.asRoomContact())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun updateContact(contact: Contact): Result<Unit> {
        return try {
            safetyDao.updateContact(contact.asRoomContact())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun deleteContact(contact: Contact): Result<Unit> {
        return try {
            safetyDao.deleteContact(contact.asRoomContact())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun getContacts(): Flow<List<Contact>> {
        return safetyDao.getContacts().map { contacts ->
            contacts.map { it.asContact() }
        }
    }

    override suspend fun insertFallIncident(fallIncident: FallIncident): Result<Unit> {
        return try {
            safetyDao.insertFallIncident(fallIncident.asRoomFallIncident())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun updateFallIncident(fallIncident: FallIncident): Result<Unit> {
        return try {
            safetyDao.updateFallIncident(fallIncident.asRoomFallIncident())
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun getRecentFallIncident(): Flow<FallIncident?> {
        return safetyDao.getRecentFallIncident().map { it?.asFallIncident() }
    }

    override fun getUnreadFallIncidents(): Flow<List<FallIncident>> {
        return safetyDao.getUnreadFallIncidents().map { fallIncidents ->
            fallIncidents.map { it.asFallIncident() }
        }
    }

    override fun getReadFallIncidents(): Flow<List<FallIncident>> {
        return safetyDao.getReadFallIncidents().map { fallIncidents ->
            fallIncidents.map { it.asFallIncident() }
        }
    }

    override fun getPairedDevices(): Flow<List<SafetyDevice>> {
        return btUtils.getPairedDevices().map { devices ->
            devices.map { it.asSafetyDevice() }
        }
    }

    override fun getScannedDevices(): Flow<List<SafetyDevice>> {
        return btUtils.getScannedDevices().map { devices ->
            devices.map { it.asSafetyDevice() }
        }
    }

    override fun getConnectedDevice(): Flow<SafetyDevice> {
        return btDataSource.getDeviceState().map {
            it.asSafetyDevice()
        }
    }

    override fun startDiscovery(): Result<Unit> {
        return try {
            btUtils.startDiscovery()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun stopDiscovery(): Result<Unit> {
        return try {
            btUtils.stopDiscovery()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override fun closeConnection(): Result<Unit> {
        return try {
            btDataSource.close()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun syncEmergencyMessages(): Result<Unit> {
        return try {
            smsDataSource.syncEmergencyMessages()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    override suspend fun sendEmergencySmsToSelectedContacts(): Result<Unit> {
        return try {
            smsHelper.sendEmergencySmsToSelectedContacts()
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}