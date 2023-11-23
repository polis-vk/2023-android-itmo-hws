package ru.ok.itmo.tuttut.login.data

import ru.ok.itmo.tuttut.login.domain.LoginRepository
import ru.ok.itmo.tuttut.login.domain.UserCredentials
import ru.ok.itmo.tuttut.login.domain.UserXAuthToken
import ru.ok.itmo.tuttut.network.Client

class LoginRepositoryImpl : LoginRepository {

    private val client = Client.create(LoginClientApi::class.java)
    override suspend fun login(userCredentials: UserCredentials): Result<UserXAuthToken> {
        return Client.safeRequest { client.login(userCredentials).string() }
    }
}