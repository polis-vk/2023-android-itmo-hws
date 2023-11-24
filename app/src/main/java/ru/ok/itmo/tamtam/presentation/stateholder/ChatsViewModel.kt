package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.data.MessageRepository
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    val chats
        get() = messageRepository.chats
            .flowOn(Dispatchers.IO)
    val notifications = messageRepository.notifications

    private val _chatsState: MutableStateFlow<ChatsState> = MutableStateFlow(ChatsState.Init)
    val chatsState: StateFlow<ChatsState> get() = _chatsState

    fun startLoading() {
        viewModelScope.launch {
            _chatsState.emit(ChatsState.Loading)
        }
    }

    fun setIdle() {
        viewModelScope.launch {
            _chatsState.emit(ChatsState.Idle)
        }
    }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                messageRepository.runTracking()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messageRepository.stopTracking()
    }
}

sealed class ChatsState {
    object Init : ChatsState()
    object Loading : ChatsState()
    object Idle : ChatsState()
}