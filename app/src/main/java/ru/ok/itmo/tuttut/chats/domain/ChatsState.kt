package ru.ok.itmo.tuttut.chats.domain

sealed interface ChatsState {
    data object Loading : ChatsState
    data class FromCache(val chats: List<ChatUI>) : ChatsState
    data class Success(val chats: List<ChatUI>) : ChatsState
    data class Failure(val errorMessage: String) : ChatsState
}