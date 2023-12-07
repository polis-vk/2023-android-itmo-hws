package ru.ok.itmo.example.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.ok.itmo.example.chatsPackage.Message

@Dao
interface MyDao {

    @Query("SELECT * FROM MessageEntity WHERE id = :id")
    fun getMessageById(id: Int): MessageEntity

    @Query("SELECT * FROM messageEntity")
    fun getAllMessages(): Flow<List<MessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertListMessages(messages: List<MessageEntity>)

    @Query("SELECT * FROM messageEntity WHERE chat_name = :chatName")
    fun getMessagesByChat(chatName: String): Flow<List<MessageEntity>>

    @Query("DELETE FROM messageEntity WHERE id = :id")
    fun deleteMessageById(id: Int)
}
