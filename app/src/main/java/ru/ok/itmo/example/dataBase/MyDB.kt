package ru.ok.itmo.example.dataBase

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        MessageEntity::class
    ],
    version = 1
)
abstract class MainDb : RoomDatabase() {
    abstract fun dao(): MyDao

    companion object{
        fun getDatabase(context: Context): MainDb{
            return Room.databaseBuilder(
                context,
                MainDb::class.java,
                "messanges.db"
            ).build()
        }
    }
}