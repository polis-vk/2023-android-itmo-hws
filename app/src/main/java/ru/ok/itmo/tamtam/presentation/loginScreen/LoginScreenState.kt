package ru.ok.itmo.tamtam.presentation.loginScreen

import com.bluelinelabs.conductor.Controller
import ru.ok.itmo.tamtam.common.BaseState

sealed class LoginScreenState : BaseState() {
    data class Error(val message: String) : LoginScreenState()
    data class Success(
        val login: String = "",
        val pwd: String = "",
        val continueButtonState: Boolean = false
    ) : LoginScreenState()

    data class Navigate(
        val route: Controller,
        val token: String
    ) : LoginScreenState()
}