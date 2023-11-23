package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.data.repository.AuthRepository
import ru.ok.itmo.tamtam.utils.NotificationType
import ru.ok.itmo.tamtam.utils.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _credentials: MutableStateFlow<Credentials> =
        MutableStateFlow(Credentials("", "", false))
    val credentials: StateFlow<Credentials> get() = _credentials
    val notifications get() = authRepository.notifications

    suspend fun setLogin(login: String) {
        withContext(Dispatchers.IO) {
            _credentials.emit(credentials.value.copy(login = login, isBad = false))
        }
    }

    suspend fun setPassword(password: String) {
        withContext(Dispatchers.IO) {
            _credentials.emit(credentials.value.copy(password = password, isBad = false))
        }
    }

    fun login(body: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                authRepository.login(credentials.value.login, credentials.value.password)) {
                is Resource.Failure -> {
                    val isUnauthorized = result.throwable is NotificationType.Unauthorized
                    if (isUnauthorized) {
                        _credentials.emit(credentials.value.copy(isBad = true))
                    }
                }

                else -> withContext(Dispatchers.Main) { body.invoke() }
            }
        }
    }

    data class Credentials(
        val login: String,
        val password: String,
        val isBad: Boolean
    ) {
        val isValid get() = login.isNotEmpty() && password.isNotEmpty()
    }
}