package dev.atick.safety.repository.intro

import dev.atick.storage.preferences.data.PreferencesDatastore
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
    private val preferencesDatastore: PreferencesDatastore
) : IntroRepository {
    override suspend fun getUserId(): Result<String?> {
        return try {
            val userId = preferencesDatastore.getUserId()
            Result.success(userId)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}