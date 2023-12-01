package ru.ok.itmo.tuttut.chats.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ok.itmo.tuttut.chats.data.ChatsRepositoryImpl
import ru.ok.itmo.tuttut.chats.domain.ChatsRepository


@Module
@InstallIn(SingletonComponent::class)
interface ChatsModule {
    @Binds
    fun bindChatsRepository(chatsRepositoryImpl: ChatsRepositoryImpl): ChatsRepository
}