package ru.ok.itmo.example.logins

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import ru.ok.itmo.example.chats.Message

interface ChatApi {
    @GET("/channel/{chatName}")
    fun getChat(@Path("chatName") chatName: String,
                @Path("limit") limit: String = "20",
                @Path("lastKnownId") lastKnownId: String = "0",
                @Path("reverse") reverse: Boolean = false): Call<List<Message>>
    @GET("/inbox/{userName}")
    fun getInbox(@Path("userName") userName: String,
                 @Path("limit") limit: String = "20",
                 @Path("lastKnownId") lastKnownId: String = "0",
                 @Path("reverse") reverse: Boolean = false): Call<List<Message>>
    companion object {
        fun create(retrofit: Retrofit): ChatApi {
            return retrofit.create(ChatApi::class.java)
        }
    }
}