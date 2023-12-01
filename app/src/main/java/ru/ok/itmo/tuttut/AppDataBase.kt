package ru.ok.itmo.tuttut

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.ok.itmo.tuttut.messenger.data.MessagesConverter
import ru.ok.itmo.tuttut.messenger.domain.Chat
import ru.ok.itmo.tuttut.messenger.domain.ChatsDAO

@Database(entities = [Chat::class], version = 1)
@TypeConverters(MessagesConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatsDAO(): ChatsDAO
}