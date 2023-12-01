package ru.ok.itmo.example.api_logic

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.ok.itmo.example.api_logic.api_models.messageJSON.MessageJSONModel

interface ChanChatAPI {
    @GET("/channels")
    suspend fun getChannels(): Response<List<String>>

    @GET("/channel/{channelName}")
    fun getChannelMessages(
        @Path("channelName") channelName: String,
        @QueryMap queryMap: Map<String, String> = mapOf(
            "limit" to "20",
            "lastKnownId" to "0",
            "reverse" to "false"
        )
    ): Response<List<MessageJSONModel>>


    companion object{
        val provideChanChatAPI: ChanChatAPI by lazy {
            RetrofitInstance.retrofit.create(ChanChatAPI::class.java)
        }
    }
}