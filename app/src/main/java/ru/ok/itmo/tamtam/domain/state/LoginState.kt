package ru.ok.itmo.tamtam.domain.state

sealed interface LoginState {
    data class Success(val token: String?) : LoginState
    data class Failure(val throwable: Throwable) : LoginState
    data object Unknown : LoginState
}
