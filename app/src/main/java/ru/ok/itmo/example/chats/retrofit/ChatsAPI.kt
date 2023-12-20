package ru.ok.itmo.example.chats.retrofit

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url
import ru.ok.itmo.example.chats.retrofit.models.Message

interface ChatsAPI {
    @GET("/1ch")
    suspend fun get1chMessages(/*@Path("limit") limit: String = "20"*/): Response<List<Message>>

    @GET
    suspend fun getImage(@Url url: String): ResponseBody
}