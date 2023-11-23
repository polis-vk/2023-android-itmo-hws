package ru.ok.itmo.tuttut.login.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.login.data.LoginRepositoryImpl
import ru.ok.itmo.tuttut.login.domain.LoginState
import ru.ok.itmo.tuttut.login.domain.UserCredentials

class LoginViewModel : ViewModel() {
    private val loginRepository = LoginRepositoryImpl()

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Unknown)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()
    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.emit(LoginState.Loading)
            loginRepository.login(UserCredentials(login, password)).onSuccess {
                _loginState.emit(LoginState.Success(it))
            }.onFailure {
                _loginState.emit(LoginState.Failure(it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.emit(LoginState.Unknown)
        }
    }
}