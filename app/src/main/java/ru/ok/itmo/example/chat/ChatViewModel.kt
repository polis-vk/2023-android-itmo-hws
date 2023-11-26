package ru.ok.itmo.example.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.example.models.Message
import ru.ok.itmo.example.repositories.InvalidDataException
import ru.ok.itmo.example.repositories.Repository

class ChatViewModel : ViewModel() {
    val repository = Repository()
    val messages = MutableLiveData<List<Message>>()

    fun get1ch() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                messages.postValue(repository.get1ch())
            } catch (e: InvalidDataException) {
                throw e
            }

        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.postMessage(text)
        }
    }
}