package ru.ok.itmo.tuttut

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.ok.itmo.tuttut.messenger.domain.ChatsDAO

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideDataStore(app: Application): DataStore<Preferences> = app.dataStore

    @Provides
    fun provideChatsDAO(database: AppDatabase): ChatsDAO = database.chatsDAO()

    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "passports").build()

    @Provides
    fun provideGlideBuilder(
        @ApplicationContext context: Context
    ): RequestManager = Glide.with(context)
}