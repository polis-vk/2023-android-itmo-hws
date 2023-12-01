package ru.ok.itmo.tuttut.login.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.LOGIN
import ru.ok.itmo.tuttut.PASSWORD
import ru.ok.itmo.tuttut.TOKEN
import ru.ok.itmo.tuttut.login.domain.LoginRepository
import ru.ok.itmo.tuttut.login.domain.LoginState
import ru.ok.itmo.tuttut.login.domain.UserCredentials
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Unauthorized)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(login: String, password: String) {
        viewModelScope.launch {
            _loginState.emit(LoginState.Loading)
            loginRepository.login(UserCredentials(login, password)).onSuccess {
                dataStore.edit { prefs ->
                    prefs[LOGIN] = login
                    prefs[PASSWORD] = password
                    prefs[TOKEN] = it
                }
                _loginState.emit(LoginState.Success(it))
            }.onFailure {
                _loginState.emit(LoginState.Failure(it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _loginState.emit(LoginState.Unauthorized)
            dataStore.edit {
                it[TOKEN]?.let { token ->
                    it[TOKEN] = ""
                    loginRepository.logout(token)
                }
            }
        }
    }
}