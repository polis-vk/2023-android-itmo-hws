package ru.ok.itmo.tamtam.ioc.module

import android.util.Log
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import ru.ok.itmo.tamtam.data.retrofit.AuthService
import ru.ok.itmo.tamtam.data.retrofit.MessageService
import ru.ok.itmo.tamtam.data.retrofit.TokenInterceptor
import ru.ok.itmo.tamtam.ioc.scope.AppComponentScope
import java.util.concurrent.TimeUnit
import ru.ok.itmo.tamtam.Constants.API_BASE_URL
import ru.ok.itmo.tamtam.Constants.CONNECT_TIMEOUT
import ru.ok.itmo.tamtam.Constants.READ_TIMEOUT
import ru.ok.itmo.tamtam.Constants.WRITE_TIMEOUT


@Module
interface RetrofitModule {
    companion object {

        @AppComponentScope
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor {
                Log.d("HttpLoggingInterceptor", it)
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        @AppComponentScope
        @Provides
        fun provideRetrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
        }

        @AppComponentScope
        @Provides
        fun provideOkHttpClient(
            tokenInterceptor: TokenInterceptor,
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(tokenInterceptor)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }

        @AppComponentScope
        @Provides
        fun provideAuthService(
            retrofitBuilder: Retrofit.Builder,
            okHttpClient: OkHttpClient
        ): AuthService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(AuthService::class.java)
        }

        @AppComponentScope
        @Provides
        fun provideMessageService(
            retrofitBuilder: Retrofit.Builder,
            okHttpClient: OkHttpClient
        ): MessageService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(MessageService::class.java)
        }
    }
}