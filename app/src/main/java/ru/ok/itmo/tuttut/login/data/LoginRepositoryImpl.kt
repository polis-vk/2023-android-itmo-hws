package ru.ok.itmo.tuttut.login.data

import ru.ok.itmo.tuttut.login.domain.LoginRepository
import ru.ok.itmo.tuttut.login.domain.UserCredentials
import ru.ok.itmo.tuttut.login.domain.UserXAuthToken
import ru.ok.itmo.tuttut.network.domain.Client
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val client: Client
) : LoginRepository {

    private val api = client.create(LoginClientApi::class.java)
    override suspend fun login(userCredentials: UserCredentials): Result<UserXAuthToken> {
        return client.safeRequest { api.login(userCredentials).string() }
    }

    override suspend fun logout(token: UserXAuthToken) {
        client.safeRequest { api.logout(token) }
    }
}