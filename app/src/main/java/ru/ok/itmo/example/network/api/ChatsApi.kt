package ru.ok.itmo.example.network.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.ok.itmo.example.network.dto.Message

interface ChatsApi {
    @GET("/users")
    fun getUsers(): Call<List<String>>

    @GET("/channels")
    fun getChannels(): Call<List<String>>

    @GET("/inbox/{userName}")
    fun getMessagesForUser(
        @Header("X-Auth-Token") token: String,
        @Path("userName") username: String,
        @QueryMap queryMap: Map<String, String> = mapOf(
            "limit" to "20",
            "lastKnownId" to "0",
            "reverse" to "false"
        )
    ): Call<List<Message>>

    @GET("/channel/{channelName}")
    fun getMessagesFromChannel(
        @Path("channelName") channelName: String,
        @QueryMap queryMap: Map<String, String> = mapOf(
            "limit" to "20",
            "lastKnownId" to "0",
            "reverse" to "false"
        )
    ): Call<List<Message>>

    companion object {
        fun provideChatsApi(retrofit: Retrofit): ChatsApi {
            return retrofit.create(ChatsApi::class.java)
        }
    }
}