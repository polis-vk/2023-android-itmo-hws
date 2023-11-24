package ru.ok.itmo.tamtam.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ru.ok.itmo.tamtam.data.retrofit.AuthService
import ru.ok.itmo.tamtam.data.retrofit.model.UserRequest
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import ru.ok.itmo.tamtam.utils.Resource
import ru.ok.itmo.tamtam.utils.handleResult
import javax.inject.Inject

@AppComponentScope
class AuthRepository @Inject constructor(
    private val accountStorage: AccountStorage,
    private val authService: AuthService
) {
    private val _isAuth = MutableStateFlow(accountStorage.token != null)
    val isAuth: StateFlow<Boolean> get() = _isAuth

    suspend fun login(login: String, password: String): Resource<Unit> {
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

            is Resource.Failure -> Resource.Failure(resource.throwable)
        }
    }

    suspend fun logout(): Resource<Unit> {
        runCatching {
            authService.logout()
        }
        accountStorage.clear()
        _isAuth.emit(false)
        return Resource.Success(Unit)
    }
}