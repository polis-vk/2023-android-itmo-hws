package ru.ok.itmo.example.room


import androidx.room.Database
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ok.itmo.example.room.entities.TokenEntity

@Database(
    version = 2,
    entities = [
        TokenEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase(){


    abstract fun getDao(): TokenDao;
    companion object{

        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "adb.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}