package ru.ok.itmo.example.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import retrofit2.Response
import ru.ok.itmo.example.chats.view.Chat
import ru.ok.itmo.example.chats.view.ChatItem
import ru.ok.itmo.example.chats.view.ChatType
import ru.ok.itmo.example.chats.view.ImageData
import ru.ok.itmo.example.chats.view.MessageData
import ru.ok.itmo.example.chats.view.TextData
import ru.ok.itmo.example.domain.AuthUseCase.Companion.TAG
import ru.ok.itmo.example.network.api.ChatsApi
import ru.ok.itmo.example.network.dto.Message
import ru.ok.itmo.example.network.result.ErrorType
import ru.ok.itmo.example.network.result.ListResult
import ru.ok.itmo.example.utils.TimeUtils
import java.util.concurrent.atomic.AtomicBoolean

class ChatsUseCase(private val chatsApi: ChatsApi) {
    suspend fun getChatItems(token: String): ListResult<ChatItem> {
        val chatList = getChatsList()
        if (chatList.isError()) {
            return ListResult.error(chatList.errorType)
        }
        val res = mutableListOf<ChatItem>()
        val isError = AtomicBoolean(false)
        val jobs = arrayListOf<Job>()
        for (chat in chatList.getListNotNull()) {
            jobs.add(CoroutineScope(Dispatchers.IO).launch {
                val result = when (chat.type) {
                    ChatType.TWO_USERS_CHAT -> getMessagesForUser(token, chat.chatName, 1)
                    ChatType.CHANNEL -> getMessagesFromChannel(chat.chatName, 1)
                }
                if (result.isError()) {
                    isError.set(true)
                } else if (result.getListNotNull().isNotEmpty()) {
                    synchronized(res) {
                        res.add(getChatItem(result.getListNotNull(), chat))
                    }
                }
            })
        }
        jobs.joinAll()
        if (isError.get() || chatList.errorType != ErrorType.NO_ERROR) {
            return ListResult.success(res, ErrorType.LIST_ITEM_ERROR)
        }
        return ListResult.success(res)
    }

    private fun getMessagesFromChannel(
        chatName: String,
        limit: Int = 20,
        lastKnowId: Int = 0,
        reverse: Boolean = false
    ): ListResult<Message> {
        val response: Response<List<Message>>?
        try {
            response = chatsApi.getMessagesFromChannel(
                chatName,
                mapOf(
                    "limit" to "$limit",
                    "lastKnowId" to "$lastKnowId",
                    "reverse" to "$reverse"
                )
            ).execute()
        } catch (e: Exception) {
            Log.d(TAG, "failed to get messages: ${e.message}")
            return ListResult.error(ErrorType.UNKNOWN_ERROR)
        }
        if (response.isSuccessful) {
            return ListResult.success(response.body()!!)
        }
        return ListResult.error(ErrorType.UNKNOWN_ERROR)
    }

    private fun getChatItem(messages: List<Message>, chat: Chat): ChatItem {
        val lastMessage = messages[0]
        val messageData: MessageData =
            if (lastMessage.data.text != null)
                TextData(lastMessage.data.text.text)
            else
                ImageData(lastMessage.data.image!!.link)
        return ChatItem(
            time = TimeUtils.getDayTime(lastMessage.time),
            chatName = chat.chatName,
            lastMessageData = messageData,
            chatType = chat.type
        )
    }

    private suspend fun getMessagesForUser(
        token: String,
        userName: String,
        limit: Int = 20,
        lastKnowId: Int = 0,
        reverse: Boolean = false
    ): ListResult<Message> {
        val response: Response<List<Message>>?
        try {
            response = chatsApi.getMessagesForUser(
                token, userName,
                mapOf(
                    "limit" to "$limit",
                    "lastKnowId" to "$lastKnowId",
                    "reverse" to "$reverse"
                )
            ).execute()
        } catch (e: Exception) {
            Log.d(TAG, "failed to get messages: ${e.message}")
            return ListResult.error(ErrorType.UNKNOWN_ERROR)
        }
        if (response.isSuccessful) {
            return ListResult.success(response.body()!!)
        }
        return ListResult.error(ErrorType.UNKNOWN_ERROR)
    }

    private suspend fun getUsers(): ListResult<String> {
        val response: Response<List<String>>?
        try {
            response = chatsApi.getUsers().execute()
        } catch (e: Exception) {
            Log.d(TAG, "failed to get users: ${e.message}")
            return ListResult.error(ErrorType.UNKNOWN_ERROR)
        }
        if (response.isSuccessful) {
            return ListResult.success(response.body()!!)
        }
        return ListResult.error(ErrorType.UNKNOWN_ERROR)
    }

    private suspend fun getChannels(): ListResult<String> {
        val response: Response<List<String>>?
        try {
            response = chatsApi.getChannels().execute()
        } catch (e: Exception) {
            Log.d(TAG, "failed to get channels: ${e.message}")
            return ListResult.error(ErrorType.UNKNOWN_ERROR)
        }
        if (response.isSuccessful) {
            return ListResult.success(response.body()!!)
        }
        return ListResult.error(ErrorType.UNKNOWN_ERROR)
    }

    private suspend fun getChatsList(): ListResult<Chat> {
        val users = getUsers()
        val channels = getChannels()
        if (users.isError() && channels.isError()) {
            return ListResult.error(users.errorType)
        }
        if (users.isError()) {
            return ListResult.success(
                channels.getListNotNull().map { Chat(it, ChatType.CHANNEL) },
                ErrorType.CHILD_LIST_NOT_LOADED
            )
        }
        if (channels.isError()) {
            return ListResult.success(
                channels.getListNotNull().map { Chat(it, ChatType.TWO_USERS_CHAT) },
                ErrorType.CHILD_LIST_NOT_LOADED
            )
        }

        val chats: MutableList<Chat> =
            users.getListNotNull()
                .map { Chat(it, ChatType.TWO_USERS_CHAT) }
                .toMutableList()
        chats.addAll(
            channels.getListNotNull()
                .map { Chat(it, ChatType.CHANNEL) }
        )
        return ListResult.success(chats)
    }
}