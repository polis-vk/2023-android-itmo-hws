package ru.ok.itmo.tamtam.domain.state

import ru.ok.itmo.tamtam.domain.model.ChatsViewModel

sealed interface ChatsState {
    data class Success(val chatInfoList: List<ChatsViewModel.ChatInfo>) : ChatsState
    data class Failure(val throwable: Throwable) : ChatsState
    data object Unknown : ChatsState
    data object LoadingChatsInfo : ChatsState
}
