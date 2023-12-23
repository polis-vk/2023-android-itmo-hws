package ru.ok.itmo.example.chats.retrofit

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.login.repository.UserXAuthToken

interface ChatsAPI {
    @GET("/channel/{channel_id}")
    suspend fun getMessages(@Path("channel_id") channelId: String): List<Message>

    @GET("/inbox/{user}")
    suspend fun getUserMessages(
        @Header("X-Auth-Token") userXAuthToken: UserXAuthToken,
        @Path("user") user: String
    ): List<Message>

    @GET
    suspend fun getImage(@Url url: String): ResponseBody

    @POST("/messages")
    suspend fun sendMessage(
        @Header("X-Auth-Token") userXAuthToken: UserXAuthToken,
        @Body message: Message
    ): Int
}