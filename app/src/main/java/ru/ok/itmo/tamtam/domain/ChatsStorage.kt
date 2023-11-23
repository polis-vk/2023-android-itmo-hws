package ru.ok.itmo.tamtam.domain

import ru.ok.itmo.tamtam.client.provider.ChatsProvider
import ru.ok.itmo.tamtam.dto.ChannelName
import ru.ok.itmo.tamtam.dto.Message

object ChatsStorage {
    private val chatsProvider = ChatsProvider()

    suspend fun getAllChannels(): Result<List<ChannelName>> {
        return chatsProvider.getAllChannels()
    }

    suspend fun getChannelMessages(name: ChannelName): Result<List<Message>> {
        return chatsProvider.getChannelMessages(name)
    }

}