package dev.atick.storage.room.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.atick.storage.room.data.SafetyDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val ROOM_DATABASE_NAME = "dev.atick.safety.room"

    @Singleton
    @Provides
    fun provideRoomDatabase(
        @ApplicationContext appContext: Context
    ): SafetyDatabase {
        return Room.databaseBuilder(
            appContext,
            SafetyDatabase::class.java,
            ROOM_DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}