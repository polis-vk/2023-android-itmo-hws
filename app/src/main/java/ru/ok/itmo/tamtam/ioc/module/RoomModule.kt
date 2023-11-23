package ru.ok.itmo.tamtam.ioc.module

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.ok.itmo.tamtam.data.room.AppDatabase
import ru.ok.itmo.tamtam.data.room.dao.MessageDao
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.utils.ApplicationContext

@Module
interface RoomModule {
    companion object {
        @AppComponentScope
        @Provides
        fun provideAppDataBase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                AppDatabase.DATABASE_NAME
            ).build()
        }

        @AppComponentScope
        @Provides
        fun provideMessageDao(appDatabase: AppDatabase): MessageDao {
            return appDatabase.messageDao()
        }

    }
}