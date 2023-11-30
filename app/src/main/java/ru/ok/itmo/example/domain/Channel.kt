package ru.ok.itmo.example.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
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
    val time: Long,
)

@Entity(tableName = "message")
data class MessagePreview(
    @PrimaryKey val id: Long,
    val from: String,
    val text: String?,
    val imageBase64: String?,
    val time: Long,
)
