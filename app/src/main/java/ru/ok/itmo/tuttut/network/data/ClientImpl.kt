package ru.ok.itmo.tuttut.network.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import ru.ok.itmo.tuttut.network.domain.Client
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ClientImpl @Inject constructor() : Client {
    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()

    override fun <T> create(cls: Class<T>): T = retrofit.create(cls)

    private suspend inline fun <R> (suspend () -> R).multiCatch(
        vararg exceptions: KClass<out Throwable>,
    ): Result<R> {
        return try {
            Result.success(this.invoke())
        } catch (e: Exception) {
            if (e::class in exceptions) Result.failure(e) else throw e
        }
    }

    override suspend fun <R> safeRequest(request: suspend () -> R): Result<R> {
        return request.multiCatch(HttpException::class, UnknownHostException::class)
    }

    companion object {
        private const val BASE_URL = "https://faerytea.name:8008"
    }
}