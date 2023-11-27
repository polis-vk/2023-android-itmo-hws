package ru.ok.itmo.tamtam.chat

import ru.ok.itmo.tamtam.server.ServerWorker

class ChatModel {
    suspend fun getMessage(
        onDataReadyCallback: OnDataReadyCallbackString
    ) {
        ServerWorker.getMessagesFrom1ch().collect { response ->
            onDataReadyCallback.onDataReady(response)
        }
    }

}

interface OnDataReadyCallbackString {
    fun onDataReady(data: String)
}