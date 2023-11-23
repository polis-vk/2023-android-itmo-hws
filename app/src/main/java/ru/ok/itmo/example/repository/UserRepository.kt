package ru.ok.itmo.example.repository

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import ru.ok.itmo.example.domain.AuthToken
import ru.ok.itmo.example.domain.LoginForm

interface UserRepository {
    @POST("/login")
    fun login(@Body form: LoginForm): Call<AuthToken>

    @POST("/logout")
    fun logout(@Header("X-Auth-Token") token: String)
}