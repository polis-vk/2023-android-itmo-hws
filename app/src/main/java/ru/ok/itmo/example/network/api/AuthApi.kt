package ru.ok.itmo.example.network.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.ok.itmo.example.network.dto.LoginRequest

interface AuthApi {
    @POST("/login")
    fun logIn(@Body loginRequest: LoginRequest): Call<String>

    @POST("/logout")
    fun logout(@Header("X-Auth-Token") token: String): Call<Unit>

    companion object {
        fun provideAuthApi(retrofit: Retrofit): AuthApi {
            return retrofit.create(AuthApi::class.java)
        }
    }
}