package ru.ok.itmo.tamtam.domain

import ru.ok.itmo.tamtam.client.AuthorizationProvider
import ru.ok.itmo.tamtam.dto.UserAuthorization

object AuthorizationStorage {
    private val authorizationProvider = AuthorizationProvider()
    private var token: String? = null

    suspend fun login(userAuthorization: UserAuthorization): Result<String?> {
        val result = authorizationProvider.login(userAuthorization)
        token = result.getOrNull()
        return result
    }

    suspend fun logout() {
        authorizationProvider.logout(token)
    }
}
