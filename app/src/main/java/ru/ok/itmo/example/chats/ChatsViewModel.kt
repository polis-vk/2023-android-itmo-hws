package ru.ok.itmo.example.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.chats.view.ChatItem
import ru.ok.itmo.example.domain.UseCaseProvider
import ru.ok.itmo.example.network.result.ErrorType
import ru.ok.itmo.example.network.result.ListResult

class ChatsViewModel : ViewModel() {

    private val _state = MutableLiveData(State.INIT)
    val state: LiveData<State> get() = _state

    private var chatItems: ListResult<ChatItem>? = null

    private val chatsUseCase by lazy {
        UseCaseProvider.getChatsUseCase()
    }

    private val authUseCase by lazy {
        UseCaseProvider.getAuthUseCase()
    }

    fun logout(token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            authUseCase.logout(token)
        }
    }

    fun loadChatItems(token: String) {
        _state.value = State.LOADING_CHATS
        viewModelScope.launch(Dispatchers.IO) {
            val result = chatsUseCase.getChatItems(token)
            withContext(Dispatchers.Main) {
                if (!result.isError()) {
                    chatItems = result
                    if (result.errorType != ErrorType.NO_ERROR) {
                        _state.value = State.CHATS_LOADED_WITH_ERRORS
                    } else {
                        _state.value = State.CHATS_LOADED
                    }
                } else {
                    _state.value = State.ERROR
                }
            }
        }
    }

    fun getResultNotNull(): ListResult<ChatItem> {
        return chatItems!!
    }

}