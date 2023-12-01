package ru.ok.itmo.example.authPart

sealed class LoginState{
    data class Success(val token: String) : LoginState()
    data class Failed(val error_messenge: String) : LoginState()
    data object Unknown : LoginState()
}
