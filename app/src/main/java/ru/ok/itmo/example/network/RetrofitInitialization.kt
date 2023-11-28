package ru.ok.itmo.example.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitInitialization {
    private lateinit var retrofit: Retrofit

    fun getClient(authToken: String? = null): Retrofit {
        val interceptor = HttpLoggingInterceptor();
        interceptor.level = HttpLoggingInterceptor.Level.BODY;


        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().also {
                    it.addHeader("X-Auth-Token", "$authToken")
                }.build())
            }
            .connectTimeout(5, TimeUnit.SECONDS)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://faerytea.name:8008").client(client)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
        return retrofit
    }

}