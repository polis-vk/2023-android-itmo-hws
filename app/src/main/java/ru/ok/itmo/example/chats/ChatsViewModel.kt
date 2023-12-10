package ru.ok.itmo.example.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.example.data.AuthTokenData


class ChatsViewModel: ViewModel() {
    private val userManager = UserManager()
    private val _state = MutableStateFlow<ChatsState>(ChatsState.Loading)
    val state = _state.asStateFlow()


    fun getChats(username: String, authTokenData: AuthTokenData) {
        viewModelScope.launch {
            _state.emit(ChatsState.Loading)
            userManager.getChats(username, authTokenData).onSuccess {
                _state.emit(ChatsState.Success(it))
            }.onFailure {
                _state.emit(ChatsState.Error(it))
            }
        }
    }

}