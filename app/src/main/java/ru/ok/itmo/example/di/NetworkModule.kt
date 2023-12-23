package ru.ok.itmo.example.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ok.itmo.example.chats.repository.MessagesLocalDataSource
import ru.ok.itmo.example.chats.retrofit.ChatsAPI
import ru.ok.itmo.example.login.repository.UserDataRepository
import ru.ok.itmo.example.retrofit_client.RetrofitClient


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun providesChatsApi(): ChatsAPI {
        return RetrofitClient.getRetrofit().create(ChatsAPI::class.java)
    }

    @Provides
    fun providesMessagesLocalDataSource(): MessagesLocalDataSource {
        return MessagesLocalDataSource()
    }

    @Provides
    fun providesUserData(): UserDataRepository {
        return UserDataRepository()
    }
}