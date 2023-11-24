package ru.ok.itmo.example.domain

import java.util.Date

typealias ChannelName = String

data class ChannelPreview(
    val name: ChannelName,
    val preview: String,
    val time: Date,
)

data class MessageDataText(
    val text: String
)

data class MessageDataImage(
    val link: String
)

data class MessageData(
    val Text: MessageDataText? = null,
    val Image: MessageDataImage? = null,
)

data class Message(
    val id: Long,
    val from: String,
    val to: String,
    val data: MessageData,
    private val time: Long,
) {
    val dateTime = Date(time)
}
