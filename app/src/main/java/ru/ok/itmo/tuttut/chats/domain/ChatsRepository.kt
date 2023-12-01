package ru.ok.itmo.tuttut.chats.domain

import ru.ok.itmo.tuttut.messenger.domain.Chat

interface ChatsRepository {
    suspend fun getChats(): Result<List<Chat>>
}