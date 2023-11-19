package ru.ok.itmo.example.retrofit

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST


interface regApi {
    @POST("/login")
    suspend fun login(@Body credentials: ru.ok.itmo.example.retrofit.dto.Request): ResponseBody

    @POST("/logout")
    fun logout(@Header("X-Auth-Token") token: String): Call<Unit>

    companion object {
        fun provideRequestApi(retrofit: Retrofit): regApi {
            return retrofit.create(regApi::class.java)
        }

    }
}