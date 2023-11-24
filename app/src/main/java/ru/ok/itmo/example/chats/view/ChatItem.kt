package ru.ok.itmo.example.chats.view

data class ChatItem(
    val time: String,
    val lastMessageData: MessageData,
    val chatName: String,
    val chatType: ChatType
)