package ru.ok.itmo.tuttut.chats.data

import retrofit2.http.GET
import retrofit2.http.Headers

interface ChatsAPI {
    @GET("/users")
    @Headers("Content-Type: application/json")
    suspend fun users(): List<String>
}