package ru.ok.itmo.tamtam.presentation.chatsscreen

import ru.ok.itmo.tamtam.common.BaseState

sealed class ChatsScreenState : BaseState() {
    data class Error(val message: String?, val fromDb: Boolean = false) : ChatsScreenState()

    object Loading : ChatsScreenState()

    data class Success(
        val chats: List<String>
    ) : ChatsScreenState()
}