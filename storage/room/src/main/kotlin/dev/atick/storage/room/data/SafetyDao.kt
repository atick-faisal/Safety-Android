package dev.atick.storage.room.data

import androidx.room.*
import dev.atick.storage.room.data.models.Contact
import dev.atick.storage.room.data.models.FallIncident
import kotlinx.coroutines.flow.Flow

@Dao
interface SafetyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("SELECT * FROM contacts")
    fun getContacts(): Flow<List<Contact>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFallIncident(fallIncident: FallIncident)

    @Update
    suspend fun updateFallIncident(fallIncident: FallIncident)

    @Query("SELECT * FROM falls LIMIT 1")
    fun getRecentFallIncident(): Flow<FallIncident?>

    @Query("SELECT * FROM falls WHERE read_by_user = false")
    fun getUnreadFallIncidents(): Flow<List<FallIncident>>

    @Query("SELECT * FROM falls WHERE read_by_user = true")
    fun getReadFallIncidents(): Flow<List<FallIncident>>
}