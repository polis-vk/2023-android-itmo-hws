package ru.ok.itmo.tamtam.data.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import ru.ok.itmo.tamtam.data.AccountStorage
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val accountStorage: AccountStorage
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = chain.request()
        .let { it.addTokenHeader() ?: it }
        .let { chain.proceed(it) }

    private fun Request.addTokenHeader(): Request? {
        val tokenHeaderName = "X-Auth-Token"
        val token = accountStorage.token ?: return null
        return newBuilder()
            .apply {
                header(tokenHeaderName, token)
            }
            .build()
    }
}