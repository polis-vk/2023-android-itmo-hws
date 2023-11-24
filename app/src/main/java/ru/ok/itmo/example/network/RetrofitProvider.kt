package ru.ok.itmo.example.network

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    val retrofit: Retrofit by lazy<Retrofit> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(provideAuthInterceptor())
            .addInterceptor(loggingInterceptor)
            .connectTimeout(NetworkConfig.connectTimeout, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder()
            .setLenient()
            .setPrettyPrinting()
            .create()
        val gsonConverterFactory = GsonConverterFactory.create(gson)

        Retrofit.Builder()
            .baseUrl(NetworkConfig.API_URL)
            .client(okHttpClient)
            .addConverterFactory(ToStringConverterFactory())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    private fun provideAuthInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original: Request = chain.request()
            val requestBuilder: Request.Builder = original.newBuilder()
                .url(original.url)
            val request: Request = requestBuilder.build()
            chain.proceed(request)
        }
    }
}