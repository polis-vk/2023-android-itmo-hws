package ru.ok.itmo.tuttut.login.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.ok.itmo.tuttut.login.data.LoginRepositoryImpl
import ru.ok.itmo.tuttut.login.domain.LoginRepository

@Module
@InstallIn(SingletonComponent::class)
interface LoginModule {
    @Binds
    fun bindLoginRepository(loginRepositoryImpl: LoginRepositoryImpl): LoginRepository
}