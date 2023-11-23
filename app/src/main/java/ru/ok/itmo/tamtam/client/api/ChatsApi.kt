package ru.ok.itmo.tamtam.client.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import ru.ok.itmo.tamtam.dto.ChannelName
import ru.ok.itmo.tamtam.dto.Message


interface ChatsApi {
    @GET("/channels")
    suspend fun getAllChannels(): Response<List<ChannelName>>

    @GET("/channel/{channelName}")
    suspend fun getChannelMessages(@Path("channelName") name: ChannelName): Response<List<Message>>

    companion object {
        fun provideChatsApi(retrofit: Retrofit): ChatsApi {
            return retrofit.create(ChatsApi::class.java)
        }
    }
}
