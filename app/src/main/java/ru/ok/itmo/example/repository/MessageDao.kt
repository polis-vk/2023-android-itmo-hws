package ru.ok.itmo.example.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import ru.ok.itmo.example.domain.MessagePreview

@Dao
interface MessageDao {
    @Query(value = "select * from message")
    suspend fun getAll(): List<MessagePreview>

    @Insert
    suspend fun saveAll(vararg messages: MessagePreview)
}
