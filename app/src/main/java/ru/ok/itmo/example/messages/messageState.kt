package ru.ok.itmo.example.messages

import ru.ok.itmo.example.dataBase.MessageEntity

sealed class messageState {

    data class Success(val messages: List<MessageEntity>) : messageState()
    data class Error(val error: Int) : messageState()
    data object Loading : messageState()
    data object Unknown : messageState()
}