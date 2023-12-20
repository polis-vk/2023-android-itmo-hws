package ru.ok.itmo.example.chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.example.chats.repository.MessagesState
import ru.ok.itmo.example.chats.repository.ChatsRepository
import ru.ok.itmo.example.chats.repository.ImageState

class ChatsViewModel : ViewModel() {
    private val repository = ChatsRepository()
    private val _messages = MutableStateFlow<MessagesState>(MessagesState.Started)
    val messages = _messages.asStateFlow()

    private val _images = MutableStateFlow<ImageState>(ImageState.Started)
    val images = _images.asStateFlow()

    fun getLastMessage() {
        viewModelScope.launch(Dispatchers.IO) {
            _messages.emit(MessagesState.Loading)
            repository.get1chMessages(1).onSuccess {
                Log.d(ChatsFragment.TAG, "Emit ${it[0]}")
                _messages.emit(MessagesState.Success(it[0]))
            }.onFailure {
                Log.d(ChatsFragment.TAG, it.message!!)
                _messages.emit(MessagesState.Failure(it))
            }
        }
    }

    fun getImage(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _images.emit(ImageState.Loading)
            repository.getImage("/img/$url").onSuccess {
                Log.d(ChatsFragment.TAG, "Emit image")
                _images.emit(ImageState.Success(it))
            }.onFailure {
                Log.d(ChatsFragment.TAG, "ImageState = failure")
                _images.emit(ImageState.Failure(it))
            }
        }
    }
}