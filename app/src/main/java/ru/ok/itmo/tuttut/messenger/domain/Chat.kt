package ru.ok.itmo.tuttut.messenger.domain

import androidx.room.Entity

@Entity(tableName = "chats", primaryKeys = ["name"])
data class Chat(
    val name: String,
    val messages: List<Message>
)