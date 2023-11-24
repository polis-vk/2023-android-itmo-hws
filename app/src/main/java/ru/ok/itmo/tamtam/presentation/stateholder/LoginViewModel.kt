package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.data.AuthRepository
import ru.ok.itmo.tamtam.utils.NotificationType
import ru.ok.itmo.tamtam.utils.Resource
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _credentials: MutableStateFlow<Credentials> = MutableStateFlow(Credentials("", ""))
    val credentials: StateFlow<Credentials> get() = _credentials
    val notifications: Channel<NotificationType> = Channel()

    suspend fun setLogin(login: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _credentials.emit(credentials.value.copy(login = login))
        }
    }

    suspend fun setPassword(password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _credentials.emit(credentials.value.copy(password = password))
        }
    }

    suspend fun login(body: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result =
                authRepository.login(credentials.value.login, credentials.value.password)) {
                is Resource.Failure -> {
                    val notificationType = result.throwable as? NotificationType
                    notificationType?.let { notifications.trySend(it) }
                }

                else -> withContext(Dispatchers.Main) { body.invoke() }
            }
        }
    }

    data class Credentials(
        val login: String,
        val password: String
    ) {
        val isValid get() = login.isNotEmpty() && password.isNotEmpty()
    }
}