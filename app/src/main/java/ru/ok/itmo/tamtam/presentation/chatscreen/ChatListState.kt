package ru.ok.itmo.tamtam.presentation.chatscreen

import ru.ok.itmo.tamtam.common.BaseState
import ru.ok.itmo.tamtam.domain.Chat

sealed class ChatListState : BaseState() {

    data class Error(val message: String?) : ChatListState()

    object Loading : ChatListState()

    data class Success(
        val chatInfo: Chat
    ) : ChatListState()
}