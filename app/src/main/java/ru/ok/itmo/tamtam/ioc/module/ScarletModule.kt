package ru.ok.itmo.tamtam.ioc.module

import com.google.gson.Gson
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.retry.LinearBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.ok.itmo.tamtam.data.AccountStorage
import ru.ok.itmo.tamtam.data.scarlet.adapter.FlowStreamAdapter
import ru.ok.itmo.tamtam.data.scarlet.api.MessageApi
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import ru.ok.itmo.tamtam.Constants.API_BASE_URL
import ru.ok.itmo.tamtam.Constants.CONNECT_TIMEOUT
import ru.ok.itmo.tamtam.Constants.RECONNECT_TIME
import ru.ok.itmo.tamtam.Constants.WRITE_TIMEOUT
import ru.ok.itmo.tamtam.Constants.READ_TIMEOUT
import ru.ok.itmo.tamtam.Constants.WS_BASE_URL


@Module
interface ScarletModule {
    companion object {
        @ScarletQualifier
        @Provides
        fun provideOkHttpClient(
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .build()
        }

        @Provides
        fun provideMessageApi(
            @ScarletQualifier okHttpClient: OkHttpClient,
            accountStorage: AccountStorage,
        ): MessageApi {
            val scarlet = Scarlet.Builder()
                .backoffStrategy(LinearBackoffStrategy(RECONNECT_TIME))
                .webSocketFactory(
                    okHttpClient.newWebSocketFactory(
                        String.format(
                            WS_BASE_URL,
                            accountStorage.login,
                            accountStorage.token
                        )
                    )
                )
                .addStreamAdapterFactory(FlowStreamAdapter.Factory)
                .addMessageAdapterFactory(GsonMessageAdapter.Factory(Gson()))
                .build()
            return scarlet.create()
        }
    }
}

@Qualifier
annotation class ScarletQualifier