package ru.ok.itmo.tuttut.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ok.itmo.tuttut.network.data.ClientImpl
import ru.ok.itmo.tuttut.network.domain.Client

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {
    @Binds
    fun bindClient(clientImpl: ClientImpl): Client
}