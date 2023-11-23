package ru.ok.itmo.tuttut.login.data

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ok.itmo.tuttut.login.domain.UserCredentials

interface LoginClientApi {
    @POST("/login")
    suspend fun login(@Body userCredentials: UserCredentials): ResponseBody
}
