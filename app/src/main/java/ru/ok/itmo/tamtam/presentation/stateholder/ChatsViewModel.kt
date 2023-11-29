package ru.ok.itmo.tamtam.presentation.stateholder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.data.repository.MessageRepository
import ru.ok.itmo.tamtam.domain.model.Chat
import javax.inject.Inject

@OptIn(FlowPreview::class)
class ChatsViewModel @Inject constructor(
    private val messageRepository: MessageRepository
) : ViewModel() {
    private val _chatsState: MutableStateFlow<ChatsState> = MutableStateFlow(ChatsState.Loading)
    val chatsState: Flow<ChatsState> get() = _chatsState
    val notifications get() = messageRepository.notifications

    init {
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.startTracking()
        }
        viewModelScope.launch(Dispatchers.IO) {
            messageRepository.chats.drop(1)
                .debounce(300)
                .collect {
                    _chatsState.emit(
                        ChatsState.Idle(chats = it)
                    )
                }
        }
    }

    override fun onCleared() {
        super.onCleared()
        messageRepository.stopTracking()
    }
}

sealed class ChatsState {
    object Loading : ChatsState()
    data class Idle(
        val chats: List<Chat>
    ) : ChatsState()
}