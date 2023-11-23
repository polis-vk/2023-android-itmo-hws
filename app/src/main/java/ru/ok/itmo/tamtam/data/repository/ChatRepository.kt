package ru.ok.itmo.tamtam.data.repository

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.ok.itmo.tamtam.common.Constants
import ru.ok.itmo.tamtam.common.Constants.getServerAPI
import ru.ok.itmo.tamtam.common.Resource
import ru.ok.itmo.tamtam.data.database.dao.ChatDAO
import ru.ok.itmo.tamtam.data.database.toChatEntity
import ru.ok.itmo.tamtam.domain.Chat
import ru.ok.itmo.tamtam.presentation.chatscreen.ChatListState
import ru.ok.itmo.tamtam.presentation.chatsscreen.ChatsScreenState
import ru.ok.itmo.tamtam.data.remote.dto.Message
import ru.ok.itmo.tamtam.data.remote.dto.Data
import ru.ok.itmo.tamtam.data.remote.dto.Text
import java.util.Date

class ChatRepository(private val database: ChatDAO? = null) : BaseRepository() {

    private val api = getServerAPI()

    suspend fun logout(token: String) {
        api.logout(token)
    }

    suspend fun getMessagesFromChat(chat: String, lastKnownId: Long = 0): ChatListState =
        when (val response = apiCall { api.getMessagesFromChat(chat, lastKnownId) }) {
            is Resource.Error -> ChatListState.Error("Что-то пошло не так в процессе загрузки сообщений. Попробуйте позже")
            is Resource.Loading -> ChatListState.Loading
            is Resource.Success -> {
                Log.d("Help_me", response.data!!.toString())
                ChatListState.Success(Chat(chat, response.data))
            }
        }

    suspend fun getChats(): ChatsScreenState =
        when (val response = apiCall { api.getChats() }) {
            is Resource.Error -> ChatsScreenState.Error("Что-то пошло не так в процессе загрузки чатов. Попробуйте позже")
            is Resource.Loading -> ChatsScreenState.Loading
            is Resource.Success -> {
                withContext(Dispatchers.IO) {
                    response.data?.map { it.removeSuffix("@channel") }?.forEach {
                        database?.insertChat(it.toChatEntity())
                    }
                }
                ChatsScreenState.Success(response.data?.map { it.removeSuffix("@channel") }!!)
            }
        }

    suspend fun createChat(chatName: String, context: Context, callBack: () -> Unit) {
        val message = Message(
            user = Constants.USER_LOGIN,
            targetChat = "$chatName@channel",
            data = Data(Text = Text(" ")),
            time = Date().time.toString()
        )
        kotlin.runCatching {
            Constants.getMapper().writeValueAsString(message).replaceFirst("text", "Text")
        }.onSuccess {
            val req = it.replace(",\"id\":null", "").replace("\"image\":null,", "")
            Log.d("JsonSender", req)
            val token = context.getSharedPreferences(Constants.SHARED, Context.MODE_PRIVATE)
                .getString(Constants.TOKEN, "") ?: ""
            try {
                api.createChat(token, req)
                callBack.invoke()
            } catch (e: Throwable) {
                Log.d("JsonSender", e.message!!)
            }
        }
    }

    suspend fun getMessagesFromDataBase(): List<String>? {
        return withContext(Dispatchers.IO) {
            database?.getAllChats()?.map { it.title }
        }
    }

}