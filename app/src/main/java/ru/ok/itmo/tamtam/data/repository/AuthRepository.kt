package ru.ok.itmo.tamtam.data.repository

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import ru.ok.itmo.tamtam.data.AccountStorage
import ru.ok.itmo.tamtam.data.retrofit.AuthService
import ru.ok.itmo.tamtam.data.retrofit.model.UserRequest
import ru.ok.itmo.tamtam.data.room.dao.MessageDao
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.utils.NotificationType
import ru.ok.itmo.tamtam.utils.Resource
import ru.ok.itmo.tamtam.utils.handleResult
import javax.inject.Inject

@AppComponentScope
class AuthRepository @Inject constructor(
    private val accountStorage: AccountStorage,
    private val authService: AuthService,
    private val messageDao: MessageDao
) {

    val notifications: Channel<NotificationType> = Channel()

    private val _isAuth = MutableStateFlow(accountStorage.token != null)
    val isAuthAsFlow: Flow<Boolean> get() = _isAuth
    val isAuth get() = _isAuth.value

    suspend fun login(login: String, password: String): Resource<Unit> {
        if (_isAuth.value) return Resource.Success(Unit)
        val resource = runCatching {
            authService.login(
                userRequest = UserRequest(
                    name = login,
                    pwd = password
                )
            )
        }.handleResult()
        return when (resource) {
            is Resource.Success -> {
                accountStorage.token = resource.data
                accountStorage.login = login
                _isAuth.emit(true)
                Resource.Success(Unit)
            }

            is Resource.Failure -> Resource.Failure<Unit>(resource.throwable)
                .also {
                    (resource.throwable as? NotificationType)?.let {
                        notifications.trySend(it)
                    }
                }
        }
    }

    suspend fun logout(): Resource<Unit> {
        runCatching {
            authService.logout()
        }
        accountStorage.clear()
        messageDao.clear()
        _isAuth.emit(false)
        return Resource.Success(Unit)
    }
}