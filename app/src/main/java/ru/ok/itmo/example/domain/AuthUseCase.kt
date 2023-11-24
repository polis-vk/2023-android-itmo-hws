package ru.ok.itmo.example.domain

import android.util.Log
import retrofit2.Response
import ru.ok.itmo.example.network.result.LogInResult
import ru.ok.itmo.example.network.ResponseCode
import ru.ok.itmo.example.network.api.AuthApi
import ru.ok.itmo.example.network.dto.LoginRequest

class AuthUseCase(private val authApi: AuthApi) {
    companion object {
        const val TAG = "AuthUseCase"
    }

    suspend fun logIn(login: String, password: String): LogInResult {
        val response: Response<String>?
        try {
            response = authApi.logIn(LoginRequest(login, password)).execute()
        } catch (e: Exception) {
            Log.d(TAG, "error: ${e.message}")
            return LogInResult.failure(LogInResult.ErrorType.UNKNOWN_ERROR)
        }
        if (response.isSuccessful) {
            return LogInResult.success(response.body()!!)
        }
        if (response.code() == ResponseCode.UNAUTHORIZED) {
            return LogInResult.failure(LogInResult.ErrorType.INVALID_LOGIN_OR_PASSWORD)
        }
        return when (response.code()) {
            ResponseCode.UNAUTHORIZED -> LogInResult.failure(LogInResult.ErrorType.INVALID_LOGIN_OR_PASSWORD)
            ResponseCode.TIMED_OUT -> LogInResult.failure(LogInResult.ErrorType.TIMED_OUT)
            else -> LogInResult.failure(LogInResult.ErrorType.UNKNOWN_ERROR)
        }
    }

    suspend fun logout(token: String) {
        try {
            authApi.logout(token).execute()
        } catch (e: Exception) {
            Log.d(TAG, "failed ot logout: ${e.message}")
        }
    }
}