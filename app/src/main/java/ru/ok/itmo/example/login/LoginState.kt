package ru.ok.itmo.example.login

sealed interface LoginState {
    object Started: LoginState
    class Success(val authTokenData: AuthTokenData) : LoginState
    object Incorrect : LoginState
    class Error(val error: Throwable) : LoginState
}