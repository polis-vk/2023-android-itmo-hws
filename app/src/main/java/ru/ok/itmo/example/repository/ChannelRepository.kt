package ru.ok.itmo.example.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.ok.itmo.example.domain.ChannelName
import ru.ok.itmo.example.domain.Message

interface ChannelRepository {
    @GET("/channels")
    fun getAllChannels(): Call<List<ChannelName>>

    @GET("/channel/{channelName}")
    fun getChannelMessages(@Path("channelName") name: ChannelName): Call<List<Message>>
}