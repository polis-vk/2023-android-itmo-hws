package ru.ok.itmo.tamtam.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Message(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override val id: Int,
    @ColumnInfo(name = "chat_name")
    val chatName: String,
    @ColumnInfo(name = "from")
    override val from: String,
    @ColumnInfo(name = "to")
    override val to: String,
    @ColumnInfo(name = "time")
    override val time: Long,
    @ColumnInfo(name = "message_text")
    override val messageText: String?,
    @ColumnInfo(name = "image_link")
    override val imageLink: String?,
    @ColumnInfo(name = "is_sent")
    override val isSent: Boolean
) : IMessage {
    companion object {
        val UNDEFINED_ID = -1
    }
}

interface IMessage {
    val id: Int
    val from: String
    val to: String
    val time: Long
    val messageText: String?
    val imageLink: String?
    val isSent: Boolean
}