package ru.ok.itmo.example.chats.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.http.Url
import ru.ok.itmo.example.chats.retrofit.ChatsAPI
import ru.ok.itmo.example.chats.retrofit.models.ChannelId
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.login.repository.UserXAuthToken
import javax.inject.Inject

class MessageRemoteDataSource @Inject constructor(
    private val chatsAPI: ChatsAPI
) {

    fun getMessages(channelId: ChannelId): Flow<List<Message>> = flow {
        emit(chatsAPI.getMessages(channelId))
    }

    fun getUserMessages(userXAuthToken: UserXAuthToken, user: String): Flow<List<Message>> = flow {
        emit(chatsAPI.getUserMessages(userXAuthToken, user))
    }

    fun getImage(@Url url: String): Flow<ResponseBody> = flow {
        emit(chatsAPI.getImage(url))
    }

    fun sendMessage(userXAuthToken: UserXAuthToken, message: Message): Flow<Int> = flow {
        emit(chatsAPI.sendMessage(userXAuthToken, message))
    }

}