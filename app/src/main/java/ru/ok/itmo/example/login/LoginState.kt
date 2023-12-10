package ru.ok.itmo.example.login

import ru.ok.itmo.example.data.AuthTokenData

sealed interface LoginState {
    object Started: LoginState
    class Success(val authTokenData: AuthTokenData) : LoginState
    class Error(val error: Throwable) : LoginState
}