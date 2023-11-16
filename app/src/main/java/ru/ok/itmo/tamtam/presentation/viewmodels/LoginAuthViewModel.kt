package ru.ok.itmo.tamtam.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.common.BaseViewModel
import ru.ok.itmo.tamtam.data.repository.AuthRepository
import ru.ok.itmo.tamtam.presentation.loginScreen.LoginScreenState

class LoginAuthViewModel : BaseViewModel<LoginScreenState>(LoginScreenState.Success()) {

    private val repository = AuthRepository()
    fun tryLogin(
        login: String,
        password: String,
    ) = viewModelScope.launch {
        setState(repository.login(login, password))
    }

    fun updateLogin(newLogin: String) {
        viewModelScope.launch {
            val check = state.value
            if (check is LoginScreenState.Success) {
                setState(
                    check.copy(
                        login = newLogin,
                        continueButtonState = check.pwd.isNotEmpty() && newLogin.isNotEmpty()
                    )
                )
            } else {
                setState(LoginScreenState.Success(login = newLogin))
            }
        }
    }

    fun updatePassword(newPassword: String) {
        viewModelScope.launch {
            val check = state.value
            if (check is LoginScreenState.Success) {
                setState(
                    check.copy(
                        pwd = newPassword,
                        continueButtonState = newPassword.isNotEmpty() && check.login.isNotEmpty()
                    )
                )
            } else {
                setState(LoginScreenState.Success(pwd = newPassword))
            }
        }
    }


}