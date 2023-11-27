package ru.ok.itmo.tamtam.server

import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ok.itmo.tamtam.token.TokenModel
import java.io.IOException

class AuthInterceptor : Interceptor, KoinComponent {
    private val modelInstance: TokenModel by inject()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newRequest = originalRequest.newBuilder()
            .header("X-Auth-Token", modelInstance.token)
            .build()
        return chain.proceed(newRequest)
    }
}