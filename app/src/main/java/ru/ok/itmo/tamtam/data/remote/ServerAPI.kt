package ru.ok.itmo.tamtam.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ok.itmo.tamtam.data.remote.dto.LoginRequest
import ru.ok.itmo.tamtam.data.remote.dto.Message

interface ServerAPI {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest,
    ): String

    @POST("logout")
    suspend fun logout(@Header("X-Auth-Token") token: String)

    @GET("channels")
    suspend fun getChats(): List<String>

    @GET("channel/{channelName}@channel")
    suspend fun getMessagesFromChat(
        @Path("channelName") chat: String,
        @Query("lastKnownId") lastKnownId: Long,
        @Query("limit") limit: Long = 20,
    ): List<Message>

    @POST("messages")
    @Headers("Content-Type: application/json;charset=UTF-8")
    suspend fun createChat(
        @Header("X-Auth-Token") token: String,
        @Body request: String,
    )
}