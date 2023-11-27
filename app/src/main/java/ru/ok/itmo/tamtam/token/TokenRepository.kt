package ru.ok.itmo.tamtam.token

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import ru.ok.itmo.tamtam.helper.Helper
import ru.ok.itmo.tamtam.server.ServerException
import ru.ok.itmo.tamtam.server.ServerWorker

sealed class AuthException : Exception() {
    data object Missing : AuthException()
}

class TokenRepository : TokenModel(), KoinComponent {
    companion object {
        private const val TOKEN_KEY = "token"
        private const val DEFAULT_STRING = ""
    }

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        this.context = context

        sharedPreferences = EncryptedSharedPreferences.create(
            "secret_shared_prefs",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(TOKEN_KEY, token)
        editor.apply()
        this.token = token
    }

    private fun clearToken() {
        saveToken(DEFAULT_STRING)
    }

    fun logout(): Flow<Unit> = flow {
        clearToken()
        ServerWorker.logout().collect {
            Log.d(Helper.DEBUG_TAG, "logout: $it")
        }
        emit(Unit)
    }

    fun checkAuthAndGetChannels(): Flow<String> = flow {
        val token = sharedPreferences.getString(TOKEN_KEY, DEFAULT_STRING)

        if (token != null && token != DEFAULT_STRING) {
            this@TokenRepository.token = token

            ServerWorker.getChannels().collect {
                emit(it)
            }
        } else
            throw AuthException.Missing
    }.catch { e ->
        when (e) {
            AuthException.Missing -> throw e
            ServerException.Unauthorized -> throw AuthException.Missing
        }

        Log.d(Helper.DEBUG_TAG, "checkAuthAndGetChannels: $e")
        throw e
    }
}