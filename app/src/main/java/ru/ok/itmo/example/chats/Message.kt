package ru.ok.itmo.example.chats


data class Message(
    val id: Int,
    val from: String,
    val to: String,
    val data: Data,
    val time: ULong
)
