package ru.ok.itmo.tamtam.server

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class LoginRequest(val name: String, val pwd: String)

interface ServerApi {
    @POST("/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body requestBody: LoginRequest): ResponseBody
}

object RetrofitClient {
    private const val URL = "https://faerytea.name:8008/"

    val apiService: ServerApi by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerApi::class.java)
    }
}