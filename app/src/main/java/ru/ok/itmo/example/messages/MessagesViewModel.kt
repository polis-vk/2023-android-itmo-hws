package ru.ok.itmo.example.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.data.AuthTokenData
import ru.ok.itmo.example.data.Message


class MessagesViewModel: ViewModel() {
    private val userManager = UserManager()
    private val _state = MutableStateFlow<MessagesState>(MessagesState.Loading)
    val state = _state.asStateFlow()



    fun getChats(username: String, authTokenData: AuthTokenData) {
        viewModelScope.launch {
            _state.emit(MessagesState.Loading)
            userManager.getChats(username, authTokenData).onSuccess {
                _state.emit(MessagesState.Success(it))
            }.onFailure {
                _state.emit(MessagesState.Error(it))
            }
        }
    }

    fun postMessage(authTokenData: AuthTokenData, message: Message) {
        viewModelScope.launch {
            userManager.message(authTokenData, message).onSuccess {
                println(it)
            }.onFailure {
                println(it)
            }
        }
    }

    fun run(username: String, authTokenData: AuthTokenData) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    while (true) {
                        getChats(username, authTokenData)
                        delay(1000)
                    }
                }
            } catch (error: Throwable) {
                println("Error $error")
            }
        }
    }

}