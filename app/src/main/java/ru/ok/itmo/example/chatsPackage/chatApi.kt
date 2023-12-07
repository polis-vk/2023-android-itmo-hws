package ru.ok.itmo.example.chatsPackage

import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path


interface chatApi {
    @GET("/channels")
    suspend fun getAllChannels(): Response<List<String>>

    @GET("/channel/{channelName}")
    suspend fun getChannelMessages(@Path("channelName") name: String): Response<List<Message>>

    companion object {
        fun provideRequestApi(retrofit: Retrofit): chatApi {
            return retrofit.create(chatApi::class.java)
        }

    }

}