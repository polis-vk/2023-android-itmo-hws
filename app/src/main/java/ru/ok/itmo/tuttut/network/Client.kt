package ru.ok.itmo.tuttut.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import java.net.UnknownHostException
import kotlin.reflect.KClass

object Client {
    private const val BASE_URL = "https://faerytea.name:8008"

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    fun <T> create(cls: Class<T>): T = retrofit.create(cls)

    private suspend inline fun <R> (suspend () -> R).multiCatch(
        vararg exceptions: KClass<out Throwable>,
    ): Result<R> {
        return try {
            Result.success(this.invoke())
        } catch (e: Exception) {
            if (e::class in exceptions) Result.failure(e) else throw e
        }
    }

    suspend fun <R> safeRequest(request: suspend () -> R): Result<R> {
        return request.multiCatch(HttpException::class, UnknownHostException::class)
    }
}