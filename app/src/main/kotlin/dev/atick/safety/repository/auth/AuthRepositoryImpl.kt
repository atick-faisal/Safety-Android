package dev.atick.safety.repository.auth

import dev.atick.storage.preferences.data.PreferencesDatastore
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val preferencesDatastore: PreferencesDatastore
) : AuthRepository {
    override suspend fun saveUserId(userId: String): Result<Unit> {
        return try {
            preferencesDatastore.saveUserId(userId)
            Result.success(Unit)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}