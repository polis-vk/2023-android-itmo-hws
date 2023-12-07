package ru.ok.itmo.tamTam.chats.models

data class Message(
    val id: Int,
    val from: String,
    val to: String,
    val data: Data,
    val time: Long
)

data class Data(
    val Text: Text
)

data class Text(
    val text: String
)