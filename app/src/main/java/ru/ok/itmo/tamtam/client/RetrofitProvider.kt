package ru.ok.itmo.tamtam.client

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitProvider {
    private const val BASE_URL = "https://faerytea.name:8008"

    private fun interceptor(token: String) = Interceptor { chain ->
        val request: Request =
            chain.request()
                .newBuilder()
                .addHeader("X-Auth-Token", token)
                .build()
        chain.proceed(request)
    }

    private fun client(token: String) = OkHttpClient.Builder()
        .addInterceptor(interceptor(token))
        .build()

    private val gson = GsonBuilder()
        .setLenient()
        .create()

    fun retrofit(token: String): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client(token))
        .build()
}
