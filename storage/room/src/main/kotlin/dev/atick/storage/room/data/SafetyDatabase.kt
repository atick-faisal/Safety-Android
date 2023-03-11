package dev.atick.storage.room.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.atick.storage.room.data.models.Contact
import dev.atick.storage.room.data.models.FallIncident

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        Contact::class,
        FallIncident::class
    ]
)
abstract class SafetyDatabase : RoomDatabase() {
    abstract fun getSafetyDao(): SafetyDao
}