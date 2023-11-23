package ru.ok.itmo.tamtam.server

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

sealed class ServerException : Throwable() {
    object Unauthorized : ServerException()
}

class ServerWorker {
    companion object {
        fun login(login: String, password: String): Flow<String> = flow {
            val loginRequest = LoginRequest(name = login, pwd = password)
            val responseBody = RetrofitClient.apiService.login(loginRequest)
            val token = responseBody.string()
            emit(token)
        }.flowOn(Dispatchers.IO).catch { e ->
            if (e is HttpException && e.code() == 401) throw ServerException.Unauthorized
            Log.d("ServerWorker", "$e")
            throw e
        }
    }
}