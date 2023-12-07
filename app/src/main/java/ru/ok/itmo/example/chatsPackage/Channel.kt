package ru.ok.itmo.example.chatsPackage

data class Channel(
    val name: String,
    val lastMessageText: MessageDataText?,
    val image_link : MessageDataImage?
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
    val time: Long,
)