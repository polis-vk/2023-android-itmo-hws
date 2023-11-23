package ru.ok.itmo.tamtam.client.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Header
import ru.ok.itmo.tamtam.dto.AuthToken
import ru.ok.itmo.tamtam.dto.UserAuthorization

interface LoginApi {
    @POST("login")
    suspend fun login(@Body userRequest: UserAuthorization): Response<AuthToken>

    @POST("logout")
    suspend fun logout(@Header("X-Auth-Token") token: AuthToken)

    companion object {
        fun provideLoginApi(retrofit: Retrofit): LoginApi {
            return retrofit.create(LoginApi::class.java)
        }
    }
}
