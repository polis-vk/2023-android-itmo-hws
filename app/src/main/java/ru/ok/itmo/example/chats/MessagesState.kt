package ru.ok.itmo.example.chats

import ru.ok.itmo.example.chats.retrofit.models.Message

sealed interface MessagesState {
    class Success(val messages: List<Message>) : MessagesState
    class Failure(val error: Throwable) : MessagesState
    object Loading : MessagesState
}