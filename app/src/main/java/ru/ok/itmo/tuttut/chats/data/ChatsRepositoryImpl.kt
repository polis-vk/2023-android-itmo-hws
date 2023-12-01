package ru.ok.itmo.tuttut.chats.data

import ru.ok.itmo.tuttut.chats.domain.ChatsRepository
import ru.ok.itmo.tuttut.messenger.domain.Chat
import ru.ok.itmo.tuttut.network.domain.Client
import javax.inject.Inject

class ChatsRepositoryImpl @Inject constructor(
    private val client: Client
) : ChatsRepository {
    private val api = client.create(ChatsAPI::class.java)
    override suspend fun getChats(): Result<List<Chat>> {
        return client.safeRequest { api.users() }.map { it.map { Chat(it, listOf()) } }
    }
}