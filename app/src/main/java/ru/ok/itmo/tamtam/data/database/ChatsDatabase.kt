package ru.ok.itmo.tamtam.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ok.itmo.tamtam.data.database.dao.ChatDAO

@Database(entities = [ChatEntity::class], version = 1)
abstract class ChatsDatabase : RoomDatabase() {
    abstract fun chatsDAO(): ChatDAO

    companion object {
        @Volatile
        private var INSTANCE: ChatsDatabase? = null

        fun getDatabase(context: Context): ChatsDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildDatabase(context: Context): ChatsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ChatsDatabase::class.java,
                "chats"
            ).build()
        }
    }
}