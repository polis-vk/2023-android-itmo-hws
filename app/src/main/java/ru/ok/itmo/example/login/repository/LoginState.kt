package ru.ok.itmo.example.login.repository

sealed interface LoginState {
    class Success(val userXAuthToken: UserXAuthToken) : LoginState
    class Failure(val error: Throwable) : LoginState
    object Loading : LoginState
    object Started : LoginState
}