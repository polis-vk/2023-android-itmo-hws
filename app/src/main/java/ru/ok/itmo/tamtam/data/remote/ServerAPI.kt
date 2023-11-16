package ru.ok.itmo.tamtam.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import ru.ok.itmo.tamtam.data.remote.dto.LoginRequest
import ru.ok.itmo.tamtam.domain.Message

interface ServerAPI {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest,
    ): String

    @GET("1ch")
    suspend fun getMessagesFrom1ch(
        @Query("lastKnownId") lastKnownId: Long = 0,
        @Query("limit") limit: Long = 20,
    ): List<Message>

    @POST("logout")
    suspend fun logout(@Header("X-Auth-Token") token: String)
}