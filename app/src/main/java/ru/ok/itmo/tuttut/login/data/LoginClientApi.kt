package ru.ok.itmo.tuttut.login.data

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.ok.itmo.tuttut.login.domain.UserCredentials
import ru.ok.itmo.tuttut.login.domain.UserXAuthToken

interface LoginClientApi {
    @POST("/login")
    suspend fun login(@Body userCredentials: UserCredentials): ResponseBody

    @POST("/logout")
    suspend fun logout(@Header("X-Auth-Token") token: UserXAuthToken)
}
