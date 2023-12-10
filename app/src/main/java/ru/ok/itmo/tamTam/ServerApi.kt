package ru.ok.itmo.tamTam

import okhttp3.ResponseBody
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.http.Header
import ru.ok.itmo.tamTam.login.LoginRequest


val serverService: ServerApi = Retrofit.Builder()
    .baseUrl("https://faerytea.name:8008/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(ServerApi::class.java)

interface ServerApi {

    @POST("/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body loginRequest: LoginRequest): ResponseBody

    @POST("logout")
    suspend fun logout(@Header("X-Auth-Token") token: String)
}
