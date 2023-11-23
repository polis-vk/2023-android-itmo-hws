package ru.ok.itmo.tamtam.domain.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.domain.AuthorizationStorage
import ru.ok.itmo.tamtam.domain.state.LoginState
import ru.ok.itmo.tamtam.dto.UserAuthorization

class LoginViewModel : ViewModel() {
    private val _loginState = MutableLiveData<LoginState>(LoginState.Unknown)
    val loginState: LiveData<LoginState>
        get() = _loginState

    fun login(login: String, password: String) {
        viewModelScope.launch {
            AuthorizationStorage.login(UserAuthorization(login, password)).onSuccess {
                _loginState.value = LoginState.Success(it)
            }.onFailure {
                _loginState.value = LoginState.Failure(it)
            }
        }
    }
}
