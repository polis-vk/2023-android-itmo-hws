package ru.ok.itmo.example.messages

import ru.ok.itmo.example.data.Message

sealed interface MessagesState {
    object Loading: MessagesState
    class Success(val messages: List<Message>) : MessagesState
    class Error(val error: Throwable) : MessagesState
}