package dev.atick.safety.repository.intro

interface IntroRepository {
    suspend fun getUserId(): Result<String?>
}