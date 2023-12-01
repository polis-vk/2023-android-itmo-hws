package ru.ok.itmo.tuttut.messenger.data

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.ok.itmo.tuttut.login.domain.UserXAuthToken

interface MessengerAPI {
    @GET("/inbox/{name}")
    suspend fun inbox(
        @Header("X-Auth-Token") authToken: UserXAuthToken,
        @Path("name") name: String
    ): ResponseBody
}