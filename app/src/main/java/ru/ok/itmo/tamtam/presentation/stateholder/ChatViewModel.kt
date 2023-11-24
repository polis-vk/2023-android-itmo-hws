package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.data.MessageRepository
import ru.ok.itmo.tamtam.domain.model.Chat
import ru.ok.itmo.tamtam.domain.model.Message
import ru.ok.itmo.tamtam.presentation.paging.MessagePagingSource
import ru.ok.itmo.tamtam.utils.Resource
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState.Init)
    val chatState: StateFlow<ChatState> get() = _chatState
    fun init(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val chatResource = messageRepository.getChat(chatId)) {
                is Resource.Failure -> throw RuntimeException(chatResource.throwable)
                is Resource.Success -> {
                    _chatState.emit(ChatState.Idle(chat = chatResource.data))
                }
            }
        }
    }

    fun getPagingMessages(): Flow<PagingData<Message>> {
        val chatState = _chatState.value as? ChatState.Idle ?: throw RuntimeException("bad state")

        return Pager(
            PagingConfig(
                pageSize = TOTAL_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true,
                initialLoadSize = TOTAL_PER_PAGE,
                maxSize = MAXIMUM_LIMIT_OF_CACHE_ITEMS
            )
        ) {
            MessagePagingSource(
                messageRepository = messageRepository,
                chatId = chatState.chat.id,
                initialKey = chatState.chat.lastViewedMessageId,
                pageSize = TOTAL_PER_PAGE,
                isLocal = !chatState.chat.isChannel
            )
        }.flow.cachedIn(viewModelScope)
    }

    fun sendMessage(message: String) {
        viewModelScope.launch {
            when (val state = chatState.value) {
                is ChatState.Idle -> {
                    messageRepository.sendMessage(message, state.chat)
                }

                ChatState.Init -> {}
            }
        }
    }

    fun sendStartTyping() {
        viewModelScope.launch {
            when (val state = chatState.value) {
                is ChatState.Idle -> {
                    messageRepository.sendStartTyping(state.chat.name)
                }

                ChatState.Init -> {}
            }
        }
    }

    fun sendEndTyping() {
        viewModelScope.launch {
            when (val state = chatState.value) {
                is ChatState.Idle -> {
                    messageRepository.sendEndTyping()
                }

                ChatState.Init -> {}
            }
        }
    }

    companion object {
        const val TOTAL_PER_PAGE = 16
        const val MAXIMUM_LIMIT_OF_CACHE_ITEMS = 200
    }
}

sealed class ChatState {
    object Init : ChatState()
    data class Idle(val chat: Chat) : ChatState()
}