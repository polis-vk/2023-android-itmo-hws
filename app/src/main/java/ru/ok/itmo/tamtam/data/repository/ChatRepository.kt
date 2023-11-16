package ru.ok.itmo.tamtam.data.repository

import android.util.Log
import ru.ok.itmo.tamtam.common.Constants.getServerAPI
import ru.ok.itmo.tamtam.common.Resource
import ru.ok.itmo.tamtam.domain.Chat
import ru.ok.itmo.tamtam.presentation.chatscreen.ChatListState

class ChatRepository : BaseRepository() {

    private val api = getServerAPI()

    suspend fun getMessagesFrom1ch(lastKnownId: Long = 0): ChatListState =
        when (val response = apiCall { api.getMessagesFrom1ch(lastKnownId) }) {
            is Resource.Error -> ChatListState.Error("Что-то пошло не так в процессе загрузки сообщений. Попробуйте позже")
            is Resource.Loading -> ChatListState.Loading
            is Resource.Success -> {
                Log.d("Help_me", response.data!!.toString())
                ChatListState.Success(Chat("1ch", response.data))
            }
        }

    suspend fun logout(token: String) {
        api.logout(token)
    }
}