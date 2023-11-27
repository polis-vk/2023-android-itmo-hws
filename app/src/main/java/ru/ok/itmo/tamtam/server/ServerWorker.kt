package ru.ok.itmo.tamtam.server

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import ru.ok.itmo.tamtam.helper.Helper

sealed class ServerException : Exception() {
    data object Unauthorized : ServerException()
}

class ServerWorker {
    companion object {
        fun login(login: String, password: String): Flow<String> = flow {
            val loginRequest = LoginRequest(login = login, password = password)
            val responseBody = RetrofitClient.apiService.login(loginRequest)
            val token = responseBody.string()
            emit(token)
        }.flowOn(Dispatchers.IO).catch { e ->
            if (e is HttpException && e.code() == 401) throw ServerException.Unauthorized
            Log.d(Helper.DEBUG_TAG, "login: $e")
            throw e
        }

        fun getMessagesFrom1ch(): Flow<String> = flow {
            val responseBody = RetrofitClientWithInterceptor.apiService.getMessagesFrom1ch()
            val result = responseBody.string()
            emit(result)
        }.flowOn(Dispatchers.IO).catch { e ->
            if (e is HttpException && e.code() == 401) throw ServerException.Unauthorized
            Log.d(Helper.DEBUG_TAG, "getMessages: $e")
            throw e
        }

        fun getChannels(): Flow<String> = flow {
            val responseBody = RetrofitClientWithInterceptor.apiService.getChannels()
            val result = responseBody.string()
            emit(result)
        }.flowOn(Dispatchers.IO).catch { e ->
            if (e is HttpException && e.code() == 401) throw ServerException.Unauthorized
            Log.d(Helper.DEBUG_TAG, "getChannels: $e")
            throw e
        }

        fun logout(): Flow<String> = flow {
            val responseBody = RetrofitClientWithInterceptor.apiService.logout()
            val result = responseBody.string()
            emit(result)
        }.flowOn(Dispatchers.IO).catch { e ->
            if (e is HttpException && e.code() == 401) throw ServerException.Unauthorized
            Log.d(Helper.DEBUG_TAG, "getChannels: $e")
            throw e
        }
    }
}