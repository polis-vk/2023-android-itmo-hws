package ru.ok.itmo.tamTam.login

import ru.ok.itmo.tamTam.serverService

object LoginRepository {
    suspend fun login(user: User): Result<String> {
        return try {
            val loginRequest = LoginRequest(user.username, user.password)
            val response = serverService.login(loginRequest)
            Result.success(response.string())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(token : String) {
        serverService.logout(token)
    }
}
