package ru.ok.itmo.example.login

import ru.ok.itmo.example.data.AuthTokenData
import ru.ok.itmo.example.data.LoginData
import ru.ok.itmo.example.repository.LoginRepository
import ru.ok.itmo.example.retrofit.Retrofit

class LoginManager {
    private val loginRepository: LoginRepository = Retrofit.retrofit().create(LoginRepository::class.java)


    suspend fun login(loginData: LoginData): Result<AuthTokenData> {
        return try {
            val token: String = loginRepository.login(loginData).string()
            Result.success(token)
        } catch(e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun logout(authToken: AuthTokenData) {
        try {
            loginRepository.logout(authToken)
        } catch (_: Throwable) {

        }
    }
}