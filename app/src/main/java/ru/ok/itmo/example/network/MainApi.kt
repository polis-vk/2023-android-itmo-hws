package ru.ok.itmo.example.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ok.itmo.example.models.LoginRequest
import ru.ok.itmo.example.models.Message
import ru.ok.itmo.example.models.sendMessage

interface MainApi {
    @POST("/login")
    fun logIn(@Body loginRequest: LoginRequest): Call<String>

    @POST("/logout")
    fun logout(@Header("X-Auth-Token") token: String): Call<Void>

    @GET("/channel/{channel_name}@channel?reverse=true")
    fun getChannel(@Path("channel_name") chanel: String?, @Query("lastKnownId") id:Long?): Call<List<Message>>


    @POST("/messages")
    fun postMessage(@Header("X-Auth-Token") token: String, @Body message: sendMessage): Call<Void>

    @GET("/inbox/{user}")
    fun getInbox(@Header("X-Auth-Token") token: String, @Path("user") user: String = "LetItBeRickAstley"): Call<List<Message>>

    @GET("/channel/{channel_name}?limit=1&lastKnownId=1000000&reverse=true")
    fun getLastMessage(@Header("X-Auth-Token") token: String, @Path("channel_name") chanel: String?): Call<List<Message>>

    @GET("/channel/{channel_name}@channel?limit=1")
    fun getFirstMessage(@Header("X-Auth-Token") token: String, @Path("channel_name") chanel: String?): Call<List<Message>>

}