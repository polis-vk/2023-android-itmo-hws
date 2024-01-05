package ru.ok.itmo.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.example.AppManager
import ru.ok.itmo.example.data.LoginData


class LoginViewModel: ViewModel() {
    private val loginManager = LoginManager()

    private val _state = MutableStateFlow<LoginState>(LoginState.Started)
    val state = _state.asStateFlow()


    fun loginCheck(loginText: String, passwordText: String) {
        if (correctCheck(loginText, passwordText)) {
            login(LoginData(loginText, passwordText))
        } else {
            viewModelScope.launch {
                _state.emit(LoginState.Incorrect)
            }
        }
    }

    fun login(loginData: LoginData) {
        viewModelScope.launch {
            _state.emit(LoginState.Started)
            loginManager.login(loginData).onSuccess {
                AppManager.authTokenData = it
                AppManager.username = loginData.name
                _state.emit(LoginState.Success(it))
            }.onFailure {
                _state.emit(LoginState.Error(it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                loginManager.logout(AppManager.authTokenData!!)
                _state.emit(LoginState.Started)
            } catch (e: Throwable) {
                _state.emit(LoginState.Error(e))
            }
        }
    }

    private fun correctCheck(login: String, password: String): Boolean {
        if (login.isEmpty()) {
            return false
        } else if (password.isEmpty()) {
            return false
        } else if (!loginIsCorrect(login)) {
            return false
        } else return passwordIsCorrect(password)
    }

    private fun loginIsCorrect(login: String): Boolean {
        return login.length >= 3
    }

    private fun passwordIsCorrect(password: String): Boolean {
        return password.length > 6 && password.lowercase() != password
                && password.uppercase() != password
    }

}