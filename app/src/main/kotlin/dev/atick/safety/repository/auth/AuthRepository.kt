package dev.atick.safety.repository.auth

interface AuthRepository {
    suspend fun saveUserId(userId: String): Result<Unit>
}