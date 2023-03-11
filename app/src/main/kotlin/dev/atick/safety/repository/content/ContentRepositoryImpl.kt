package dev.atick.safety.repository.content

import dev.atick.safety.data.common.FallIncident
import dev.atick.safety.data.contacts.Contact
import dev.atick.safety.data.contacts.asContact
import dev.atick.storage.room.data.SafetyDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ContentRepositoryImpl @Inject constructor(
    private val safetyDao: SafetyDao
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
        TODO("Not yet implemented")
    }

    override suspend fun updateFallIncident(fallIncident: FallIncident): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun getRecentFallIncident(): Flow<FallIncident> {
        TODO("Not yet implemented")
    }

    override fun getUnreadFallIncidents(): Flow<List<FallIncident>> {
        TODO("Not yet implemented")
    }

    override fun getReadFallIncidents(): Flow<List<FallIncident>> {
        TODO("Not yet implemented")
    }
}