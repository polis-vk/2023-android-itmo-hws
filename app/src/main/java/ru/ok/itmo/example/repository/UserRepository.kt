package ru.ok.itmo.example.repository

import retrofit2.http.*
import ru.ok.itmo.example.data.Message

interface UserRepository {
    @GET("/inbox/{user}")
    suspend fun inbox(@Header("X-Auth-Token") token: String, @Path("user") user: String): List<Message>

    @GET("/users")
    suspend fun users() : List<String>

    @POST("/messages")
    suspend fun message(@Header("X-Auth-Token") token: String, @Body message: Message): Int
}