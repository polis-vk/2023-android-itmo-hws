package ru.ok.itmo.example.domain

import ru.ok.itmo.example.network.api.AuthApi
import ru.ok.itmo.example.network.api.ChatsApi
import ru.ok.itmo.example.network.RetrofitProvider

class UseCaseProvider {
    companion object {
        fun getAuthUseCase(): AuthUseCase {
            return AuthUseCase(
                AuthApi.provideAuthApi(
                    RetrofitProvider.retrofit
                )
            )
        }

        fun getChatsUseCase(): ChatsUseCase {
            return ChatsUseCase(
                ChatsApi.provideChatsApi(
                    RetrofitProvider.retrofit
                )
            )
        }
    }
}