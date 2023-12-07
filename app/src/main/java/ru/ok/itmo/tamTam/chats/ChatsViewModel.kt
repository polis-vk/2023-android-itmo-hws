package ru.ok.itmo.tamTam.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.tamTam.chats.collector.Collector
import ru.ok.itmo.tamTam.chats.models.ChatPreview

class ChatsViewModel : ViewModel() {
    private var _chats = MutableLiveData<Result<MutableList<ChatPreview>>>()
    val chats: LiveData<Result<MutableList<ChatPreview>>> get() = _chats

    fun messages() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = Repository.messages()
            if (result.isFailure) {
                _chats.postValue(Result.failure(result.exceptionOrNull()!!))
            } else {
                _chats.postValue(Result.success(Collector.toDialogue(result.getOrNull()!!)))
            }
        }
    }
}