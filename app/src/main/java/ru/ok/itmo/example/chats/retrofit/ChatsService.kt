package ru.ok.itmo.example.chats.retrofit

import ru.ok.itmo.example.retrofit_client.RetrofitClient

object ChatsService {
    val chatsService: ChatsAPI = RetrofitClient.getRetrofit().create(ChatsAPI::class.java)
}