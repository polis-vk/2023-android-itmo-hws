package ru.ok.itmo.example

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatApi {
    @POST("/login")
    suspend fun login(@Body login: Login): Response<String>
    companion object {
        fun create(retrofit: Retrofit): ChatApi {
            return retrofit.create(ChatApi::class.java)
        }
    }
}