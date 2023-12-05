package ru.ok.itmo.tamtam.data.repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.common.Resource

open class BaseRepository {

    suspend fun <T> apiCall(
        callback: suspend () -> T
    ): Resource<T> {
        return withContext(Dispatchers.IO) {
            try {
                Resource.Success(callback())
            } catch (e: Throwable) {
                Log.d("Help_me", e.message!!)
                Resource.Error(e.message!!)
            }
        }
    }
}