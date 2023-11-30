package ru.ok.itmo.example.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.ok.itmo.example.domain.MessagePreview

class ChannelMessagesViewModel : ViewModel() {
    private val messagesHolder = MutableLiveData<MutableList<MessagePreview>>(mutableListOf())
    val messages: LiveData<out List<MessagePreview>> = messagesHolder

    fun messageArrived(message: MessagePreview) {
        messagesHolder.value?.add(message)
    }

    fun isEmpty() = messages.value.isNullOrEmpty()
}