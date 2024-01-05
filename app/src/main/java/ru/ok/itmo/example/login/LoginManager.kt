package ru.ok.itmo.example.login

import ru.ok.itmo.example.repository.LoginRepository
import ru.ok.itmo.example.retrofit.Retrofit

class LoginManager {
    private val loginRepository: LoginRepository = Retrofit.retrofit().create(LoginRepository::class.java)


    suspend fun login(loginData: LoginData): Result<AuthTokenData> {
        return try {
            Result.success(loginRepository.login(loginData).toString())
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