package ru.ok.itmo.example.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.config.App
import ru.ok.itmo.example.domain.Message
import ru.ok.itmo.example.domain.MessagePreview

object MessageService {
    suspend fun getAllMessagesFromDb() = withContext(Dispatchers.IO) {
        App.instance.messagesDatabase.messagesDao().getAll()
    }

    suspend fun saveAllMessagesToDb(messages: List<MessagePreview>) = withContext(Dispatchers.IO) {
        App.instance.messagesDatabase.messagesDao().saveAll(*messages.toTypedArray())
    }
}
