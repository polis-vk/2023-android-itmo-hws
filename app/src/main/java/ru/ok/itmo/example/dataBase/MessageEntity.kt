package ru.ok.itmo.example.dataBase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "messageEntity")
data class MessageEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "chat_name")
    val chatName: String,
    @ColumnInfo(name = "from")
    val from: String,
    @ColumnInfo(name = "to")
    val to: String,
    @ColumnInfo(name = "time")
     val time: Long,
    @ColumnInfo(name = "message_text")
     val messageText: String?
) 