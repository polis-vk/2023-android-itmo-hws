package ru.ok.itmo.tamtam.data.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ok.itmo.tamtam.data.retrofit.model.UserRequest

interface AuthService {
    @POST("login")
    suspend fun login(@Body userRequest: UserRequest): Response<String>

    @POST("logout")
    suspend fun logout(): Response<String>
}