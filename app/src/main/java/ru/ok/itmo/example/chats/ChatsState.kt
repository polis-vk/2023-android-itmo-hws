package ru.ok.itmo.example.chats

import ru.ok.itmo.example.data.Message

sealed interface ChatsState {
    object Loading: ChatsState
    class Success(val messages: List<Message>) : ChatsState
    class Error(val error: Throwable) : ChatsState
}