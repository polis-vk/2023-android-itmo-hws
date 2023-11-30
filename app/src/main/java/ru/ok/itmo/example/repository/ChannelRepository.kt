package ru.ok.itmo.example.repository

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.ok.itmo.example.domain.ChannelName
import ru.ok.itmo.example.domain.Message

interface ChannelRepository {
    @GET("/channels")
    fun getAllChannels(): Call<List<ChannelName>>

    @GET("/channel/{channelName}")
    fun getChannelMessages(@Path("channelName") name: ChannelName): Call<List<Message>>

    @POST("/channel/{channelName}")
    fun createChannel(@Path("channelName") name: ChannelName, @Body message: Message): Call<Long>

    @GET("/img/{path}")
    fun getPicture(@Path("path") path: String): Call<String>
}
