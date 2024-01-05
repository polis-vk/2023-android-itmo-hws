package ru.ok.itmo.example.repository

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.ok.itmo.example.login.LoginData

interface LoginRepository {
    @POST("/login")
    suspend fun login(@Body form: LoginData): ResponseBody

    @POST("/logout")
    suspend fun logout(@Header("X-Auth-Token") token: String)
}