package ru.ok.itmo.example.api_logic

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import ru.ok.itmo.example.api_logic.api_models.LoginJSONModel

interface AuthAPI {

    @POST("login")
    suspend fun logIn(@Body login: LoginJSONModel): Response<String>

    @POST("logout")
    suspend fun logOut()

    companion object{
        val provideAuthAPI: AuthAPI by lazy {
            RetrofitInstance.retrofit.create(AuthAPI::class.java)
        }
    }
}