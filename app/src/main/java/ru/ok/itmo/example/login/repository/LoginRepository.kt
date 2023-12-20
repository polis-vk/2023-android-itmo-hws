package ru.ok.itmo.example.login.repository

import ru.ok.itmo.example.login.retrofit.LoginService
import ru.ok.itmo.example.login.retrofit.models.UserCredentials

class LoginRepository {
    suspend fun login(userCredentials: UserCredentials): Result<UserXAuthToken> {
        return try {
            Result.success(LoginService.loginService.login(userCredentials).string())
        } catch(e: Throwable) {
            Result.failure(e)
        }
    }

    suspend fun logout(userXAuthToken: UserXAuthToken): Result<UserXAuthToken> {
        return try {
            LoginService.loginService.logout(userXAuthToken)
            Result.success(userXAuthToken)
        } catch (e: Throwable) {
            Result.failure(e)
        }
    }
}

typealias UserXAuthToken = String
