package ru.ok.itmo.tamtam.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.common.BaseViewModel
import ru.ok.itmo.tamtam.data.repository.ChatRepository
import ru.ok.itmo.tamtam.domain.Chat
import ru.ok.itmo.tamtam.presentation.chatscreen.ChatListState

class ChatsScreenViewModel : BaseViewModel<ChatListState>(ChatListState.Loading) {

    private val repository = ChatRepository()

    init {
        viewModelScope.launch {
            setState(repository.getMessagesFrom1ch())
        }
    }

    fun fetchMessages(callBack: () -> Unit) {
        viewModelScope.launch {
            try {
                val st = (state.value as ChatListState.Success)
                val lastKnownId = st.chatInfo.messages.last().id
                val resultList = when (val result = repository.getMessagesFrom1ch(lastKnownId!!)) {
                    is ChatListState.Success -> st.chatInfo.messages + result.chatInfo.messages
                    else -> st.chatInfo.messages
                }
                setState(ChatListState.Success(Chat(st.chatInfo.title, resultList)))
            } catch (_: Throwable) {
            }
            callBack.invoke()
        }
    }

    fun logout(token: String) {
        viewModelScope.launch {
            repository.logout(token)
        }
    }

}