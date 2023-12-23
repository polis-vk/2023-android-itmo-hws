package ru.ok.itmo.example.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.example.login.repository.LoginRepository
import ru.ok.itmo.example.login.repository.UserDataRepository
import ru.ok.itmo.example.login.retrofit.models.UserCredentials
import ru.ok.itmo.example.login.repository.UserXAuthToken
import java.lang.IllegalStateException
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ViewModel() {
    private val repository = LoginRepository();
    private var xAuthToken: UserXAuthToken? = null

    private val _state = MutableStateFlow<LoginState>(LoginState.Started)
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<LoginEvents>()
    val effect = _effect.asSharedFlow()


    fun login(login: String, password: String) {
        Log.d(LoginFragment.TAG, "login: $login, password: $password")
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(LoginState.Loading)
            val credentials = UserCredentials(login, password)
            repository.login(credentials).onSuccess {
                xAuthToken = it
                Log.i(LoginFragment.TAG, "AccessToken: $it")
                userDataRepository.login(it, credentials)
                Log.d(LoginFragment.TAG, userDataRepository.getLogin())
                _state.emit(LoginState.Success(it))
                _effect.emit(LoginEvents.Navigate)
            }.onFailure {
                Log.d(LoginFragment.TAG, "Error: $it")
                _state.emit(LoginState.Failure(it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _state.emit(LoginState.Started)
            if (xAuthToken == null) {
                throw IllegalStateException("X-Auth-Token is null")
            }
            repository.logout(xAuthToken!!).onSuccess {
                Log.i(LoginFragment.TAG, "Logout with token: $xAuthToken")
                userDataRepository.logout()
            }.onFailure {
                Log.d(LoginFragment.TAG, it.message.toString())
                throw it
            }
        }
    }

    fun isLogin(): Boolean {
        return userDataRepository.isLoggedIn()
    }

    fun getAuthToken(): UserXAuthToken? {
        return xAuthToken
    }
}

sealed interface LoginState {
    class Success(val userXAuthToken: UserXAuthToken) : LoginState
    class Failure(val error: Throwable) : LoginState
    object Loading : LoginState
    object Started : LoginState
}

sealed interface LoginEvents {
    object Navigate : LoginEvents
}