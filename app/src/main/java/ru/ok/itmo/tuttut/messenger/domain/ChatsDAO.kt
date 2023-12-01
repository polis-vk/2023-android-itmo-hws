package ru.ok.itmo.tuttut.messenger.domain

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ChatsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passport: Chat)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(passports: List<Chat>)

    @Delete
    suspend fun delete(passport: Chat)

    @Query("SELECT * FROM chats")
    suspend fun getAll(): List<Chat>

    @Query("DELETE FROM chats")
    suspend fun clear()
}
