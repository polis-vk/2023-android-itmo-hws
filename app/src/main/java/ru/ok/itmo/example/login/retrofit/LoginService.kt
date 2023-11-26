package ru.ok.itmo.example.login.retrofit

import ru.ok.itmo.example.retrofit_client.RetrofitClient

object LoginService {
    val loginService: LoginAPI = RetrofitClient.getRetrofit().create(LoginAPI::class.java)
}