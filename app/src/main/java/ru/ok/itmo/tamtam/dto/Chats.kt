package ru.ok.itmo.tamtam.dto

typealias ChannelName = String

data class TextData(
    val text: String
)

data class ImageData(
    val link: String?
)

data class MessageData(
    val Text: TextData? = null,
    val Image: ImageData? = null,
)

data class Message(
    val id: Long,
    val from: String,
    val to: String?,
    val data: MessageData,
    val time: Long?,
)
