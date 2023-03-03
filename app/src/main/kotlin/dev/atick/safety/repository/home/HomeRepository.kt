package dev.atick.safety.repository.home

import dev.atick.safety.data.home.Item

interface HomeRepository {
    suspend fun getItem(id: Int): Result<Item>
    suspend fun saveItem(item: Item)
    suspend fun getUserId(): Result<String>
}