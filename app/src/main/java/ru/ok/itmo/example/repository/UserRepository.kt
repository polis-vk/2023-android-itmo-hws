package ru.ok.itmo.example.repository

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import ru.ok.itmo.example.data.Message

interface UserRepository {
    @GET("/inbox/{user}")
    suspend fun inbox(@Header("X-Auth-Token") token: String, @Path("user") user: String): List<Message>

    @POST("/users")
    suspend fun users() : List<String>
}