package ru.ok.itmo.example.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.ok.itmo.example.models.LoginRequest
import ru.ok.itmo.example.models.Message
import ru.ok.itmo.example.models.sendMessage

interface MainApi {
    @POST("/login")
    fun logIn(@Body loginRequest: LoginRequest): Call<String>

    @POST("/logout")
    fun logout(@Header("X-Auth-Token") token: String): Call<Void>

    @GET("/1ch?limit=1000&lastKnownId=5041")
    fun get1ch(): Call<List<Message>>

    @GET("/channel/{channel_name}")
    fun getChannel(@Path("channel_name") chanel: String? = "1@channel"): Call<List<Message>>

    @POST("/1ch")
    fun postMessage(@Body message: sendMessage): Call<Void>


}