package ru.ok.itmo.example.chats.repository

import kotlinx.coroutines.flow.callbackFlow
import ru.ok.itmo.example.chats.retrofit.models.ChannelId
import ru.ok.itmo.example.chats.retrofit.models.Message

class MessagesLocalDataSource {
    private var messages: List<Message> = ArrayList()

    fun getMessages(channelId: ChannelId): List<Message> {
        return messages.filter { message -> message.to == channelId }
    }

    fun getUserMessages(user: String): List<Message> {
        return messages.filter { message -> message.from == user }
    }

    fun cacheMessages(newMessages: List<Message>) {
        messages.plus(newMessages.filter { message -> !messages.contains(message) })
    }
}