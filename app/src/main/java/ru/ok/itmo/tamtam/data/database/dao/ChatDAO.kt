package ru.ok.itmo.tamtam.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.ok.itmo.tamtam.data.database.ChatEntity

@Dao
interface ChatDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChat(chat: ChatEntity)

    @Query("select * from chats")
    fun getAllChats(): List<ChatEntity>
}