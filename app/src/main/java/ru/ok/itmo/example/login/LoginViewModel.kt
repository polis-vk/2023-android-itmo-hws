package ru.ok.itmo.example.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.example.login.repository.LoginState
import ru.ok.itmo.example.login.repository.RealLoginRepository
import ru.ok.itmo.example.login.repository.UserCredentials
import ru.ok.itmo.example.login.repository.UserXAuthToken
import java.lang.IllegalStateException

class LoginViewModel : ViewModel() {
    private val loginRepository = RealLoginRepository();
    private var xAuthToken: UserXAuthToken? = null
    private var isLogin: Boolean = false

    private val _status = MutableStateFlow<LoginState>(LoginState.Started)
    val status = _status.asStateFlow()


    fun login(login: String, password: String) {
        Log.d(LoginFragment.TAG, "login: $login, password: $password")
        viewModelScope.launch {
            _status.emit(LoginState.Loading)
            loginRepository.login(UserCredentials(login, password)).onSuccess {
                isLogin = true
                xAuthToken = it
                Log.i(LoginFragment.TAG, "AccessToken: $it")
                _status.emit(LoginState.Success(it))
            }.onFailure {
                Log.d(LoginFragment.TAG, "Error: $it")
                _status.emit(LoginState.Failure(it))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _status.emit(LoginState.Started)

            if (xAuthToken == null) {
                throw IllegalStateException("X-Auth-Token is null")
            }
            loginRepository.logout(xAuthToken!!).onSuccess {
                Log.i(LoginFragment.TAG, "Logout with token: $xAuthToken")
                isLogin = false
            }.onFailure {
                Log.d(LoginFragment.TAG, it.message.toString())
                throw it
            }
        }
    }

    fun isLogin(): Boolean {
        return isLogin
    }
}