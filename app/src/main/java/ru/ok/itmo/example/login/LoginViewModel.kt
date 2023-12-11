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
            } catch (e: Throwable) {
                _state.emit(LoginState.Error(e))
            }
        }
    }

}