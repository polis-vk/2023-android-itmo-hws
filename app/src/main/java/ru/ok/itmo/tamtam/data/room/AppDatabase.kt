package ru.ok.itmo.tamtam.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ok.itmo.tamtam.data.room.dao.MessageDao
import ru.ok.itmo.tamtam.domain.model.Chat
import ru.ok.itmo.tamtam.domain.model.Message


@Database(
    entities = [Message::class, Chat::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        const val DATABASE_NAME = "tamtam-database"
    }
}