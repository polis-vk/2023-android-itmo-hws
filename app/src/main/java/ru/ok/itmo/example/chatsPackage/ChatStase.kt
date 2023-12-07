package ru.ok.itmo.example.chatsPackage

import ru.ok.itmo.example.domain.LoginState

sealed class ChatState {

    data class Success(val chats: List<Channel>) : ChatState()
    data class Error(val error: Int) : ChatState()
    data object Unknown : ChatState()

}


