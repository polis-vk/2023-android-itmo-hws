package ru.ok.itmo.example.config

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.ok.itmo.example.domain.MessagePreview
import ru.ok.itmo.example.repository.MessageDao

@Database(entities = [MessagePreview::class], version = 1)
abstract class MessagesDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessageDao
}
