package ru.ok.itmo.example.data

data class Message(
    val id: Int,
    val from: String,
    val to: String,
    val data: Data,
    val time: Long?
)

data class Data(
    val Text: Text?,
    val Image: Image?
)

data class Text(val text: String)

data class Image(val link: String)