package ru.ok.itmo.example.login

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginApi {
    @POST("/login")
    fun login(@Body login: Login): Call<String>

    @POST("/logout")
    fun logout(@Header("X-Auth-Token") token: String): Call<Unit>

    companion object {
        fun create(retrofit: Retrofit): LoginApi {
            return retrofit.create(LoginApi::class.java)
        }
    }
}