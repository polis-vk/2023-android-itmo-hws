package ru.ok.itmo.tamtam.data.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap
import ru.ok.itmo.tamtam.data.retrofit.model.MessageDto
import ru.ok.itmo.tamtam.data.retrofit.model.TextMessageDto

interface MessageService {
    @GET("channel/{channelName}")
    suspend fun listOfMessagesFromChannel(
        @Path("channelName") channelName: String,
        @QueryMap queryMap: Map<String, String> = mapOf(
            "limit" to "20",
            "lastKnownId" to "0",
            "reverse" to "false"
        )
    ): Response<List<MessageDto>>

    @GET("inbox/{userName}")
    suspend fun listOfMessagesForUser(
        @Path("userName") userName: String,
        @QueryMap queryMap: Map<String, String> = mapOf(
            "limit" to "20",
            "lastKnownId" to "0",
            "reverse" to "false"
        )
    ): Response<List<MessageDto>>

    @GET("channels")
    suspend fun channels(): Response<List<String>>

    @GET("users")
    suspend fun users(): Response<List<String>>

    @GET("typing/{name}")
    suspend fun typingForChat(@Path("name") name: String): Response<List<String>>

    @POST("messages")
    suspend fun sendTextMessage(@Body textMessageDto: TextMessageDto): Response<String>
}