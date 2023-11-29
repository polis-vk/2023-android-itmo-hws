package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.data.repository.MessageRepository
import ru.ok.itmo.tamtam.domain.model.Chat
import ru.ok.itmo.tamtam.domain.model.Message
import ru.ok.itmo.tamtam.presentation.paging.MessagePagingSource
import ru.ok.itmo.tamtam.utils.Resource
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _chatState: MutableStateFlow<ChatState> = MutableStateFlow(ChatState.Loading)
    val chatState: Flow<ChatState> get() = _chatState
    val notifications get() = messageRepository.notifications

    var pagingData: Flow<PagingData<Message>>? = null
    fun init(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val chatResource = messageRepository.getChatByName(chatId)) {
                is Resource.Failure -> throw RuntimeException(chatResource.throwable)
                is Resource.Success -> {
                    _chatState.emit(
                        ChatState.Idle(
                            chat = chatResource.data
                        )
                    )
                    pagingData = Pager(
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
                            chatName = chatResource.data.name,
                            initialKey = chatResource.data.lastViewedMessageId,
                            pageSize = TOTAL_PER_PAGE,
                            isLocal = !chatResource.data.isChannel
                        )
                    }.flow.cachedIn(viewModelScope)
                    observeChat(chatResource.data)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeChat(chat: Chat) {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.chats.drop(1)
                .mapNotNull { it.find { it.name == chat.name } }
                .distinctUntilChanged()
                .debounce(300)
                .collect {
                    val oldState = _chatState.value
                    if (oldState is ChatState.Idle) {
                        _chatState.emit(
                            oldState.copy(
                                chat = it,
                            )
                        )
                    }
                }
        }
    }


    fun sendMessage(message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = _chatState.value
            if (oldState is ChatState.Idle) {
                messageRepository.sendMessage(message, oldState.chat)
            }
        }
    }

    fun sendStartTyping() {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = _chatState.value
            if (oldState is ChatState.Idle) {
                messageRepository.sendStartTyping(oldState.chat.name)
            }
        }
    }

    fun sendEndTyping() {
        viewModelScope.launch(Dispatchers.IO) {
            val oldState = _chatState.value
            if (oldState is ChatState.Idle) {
                messageRepository.sendEndTyping()
            }
        }
    }

    companion object {
        const val TOTAL_PER_PAGE = 10
        const val MAXIMUM_LIMIT_OF_CACHE_ITEMS = 100
    }
}

sealed class ChatState {
    object Loading : ChatState()
    data class Idle(
        val chat: Chat
    ) : ChatState()
}