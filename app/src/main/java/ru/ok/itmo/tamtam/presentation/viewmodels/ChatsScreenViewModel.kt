package ru.ok.itmo.tamtam.presentation.viewmodels

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.common.BaseViewModel
import ru.ok.itmo.tamtam.data.repository.ChatRepository
import ru.ok.itmo.tamtam.domain.Chat
import ru.ok.itmo.tamtam.presentation.chatscreen.ChatListState

class ChatsScreenViewModel(private val chatId: String) :
    BaseViewModel<ChatListState>(ChatListState.Loading) {

    private val repository = ChatRepository()

    init {
        viewModelScope.launch {
            setState(repository.getMessagesFromChat(chatId))
        }
    }

    private fun fetchMessages(callBack: () -> Unit) {
        viewModelScope.launch {
            try {
                val st = (state.value as ChatListState.Success)
                val lastKnownId = st.chatInfo.messages.lastOrNull()?.id
                val resultList =
                    when (val result = repository.getMessagesFromChat(chatId, lastKnownId ?: 0)) {
                        is ChatListState.Success -> st.chatInfo.messages + result.chatInfo.messages
                        else -> st.chatInfo.messages
                    }
                setState(ChatListState.Success(Chat(st.chatInfo.title, resultList)))
            } catch (_: Throwable) {
            }
            callBack.invoke()
        }
    }

    fun onScrolled(lastPosition: Int, numberOfElements: Int, callBack: () -> Unit) {
        if (lastPosition >= numberOfElements - MESSAGE_PIVOT) {
            viewModelScope.launch {
                fetchMessages(callBack)
            }
        }
    }

    private companion object {
        const val MESSAGE_PIVOT = 10
    }

}