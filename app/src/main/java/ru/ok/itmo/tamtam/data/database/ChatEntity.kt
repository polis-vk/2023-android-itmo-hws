package ru.ok.itmo.tamtam.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chats")
data class ChatEntity(
    @PrimaryKey val title: String
)

fun String.toChatEntity(): ChatEntity = ChatEntity(this)