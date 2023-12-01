package ru.ok.itmo.tuttut.messenger.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ok.itmo.tuttut.messenger.data.MessengerRepositoryImpl
import ru.ok.itmo.tuttut.messenger.domain.MessengerRepository

@Module
@InstallIn(SingletonComponent::class)
interface MessengerModule {
    @Binds
    fun bindMessengerRepository(messengerRepositoryImpl: MessengerRepositoryImpl): MessengerRepository
}