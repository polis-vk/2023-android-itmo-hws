package ru.ok.itmo.example.login.retrofit

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.ok.itmo.example.login.retrofit.models.UserCredentials
import ru.ok.itmo.example.login.repository.UserXAuthToken

interface LoginAPI {
    @POST("/login")
    suspend fun login(@Body userCredentials: UserCredentials): ResponseBody

    @POST("/logout")
    suspend fun logout(@Header("X-Auth-Token") token: UserXAuthToken): ResponseBody
}