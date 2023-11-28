package ru.ok.itmo.example.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.example.models.Message
import ru.ok.itmo.example.repositories.InvalidDataException
import ru.ok.itmo.example.repositories.Repository

class ChatViewModel(private val repository: Repository) : ViewModel() {
    private val mutableMessages: MutableList<Message> = mutableListOf()
    val messages = MutableLiveData<List<Message>>()
    val isLast = MutableLiveData(false)

    fun getChannel(number: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.initRetrofit()
                for(i in repository.getChannel(number)!!){
                    mutableMessages.add(i)
                }
                messages.postValue(mutableMessages)
            } catch (e: InvalidDataException) {
                throw e
            }

        }
    }

    fun isLast(key: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.initRetrofit()
            isLast.postValue(repository.isLast(key))
        }
    }

    fun sendMessage(text: String, where: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initRetrofit()
            repository.postMessage(text, where)
        }
    }
}