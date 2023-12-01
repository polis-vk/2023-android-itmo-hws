package ru.ok.itmo.tuttut.login.domain

sealed interface LoginState {
    data class Success(val token: UserXAuthToken) : LoginState
    data class Failure(val throwable: Throwable) : LoginState
    data object Unauthorized : LoginState
    data object Loading : LoginState
}
