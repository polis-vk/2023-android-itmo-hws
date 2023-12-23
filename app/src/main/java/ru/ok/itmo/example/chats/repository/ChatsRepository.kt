package ru.ok.itmo.example.chats.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import ru.ok.itmo.example.chats.retrofit.models.ChannelId
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.login.repository.UserXAuthToken
import javax.inject.Inject

class ChatsRepository @Inject constructor(
    private val localDataSource: MessagesLocalDataSource,
    private val remoteDataSource: MessageRemoteDataSource
) {
    @WorkerThread
    fun getMessages(channelId: ChannelId): Flow<List<Message>> {
        val messages = localDataSource.getMessages(channelId)
        return remoteDataSource.getMessages(channelId)
            .onStart { emit(messages) }
            .onEach {
                if (it != messages) {
                    localDataSource.cacheMessages(it)
                }
            }
            .flowOn(Dispatchers.IO)
    }

    @WorkerThread
    fun getImage(url: String): Flow<Bitmap> {
        return remoteDataSource.getImage(url)
            .map { res -> BitmapFactory.decodeStream(res.byteStream()) }
    }

    @WorkerThread
    fun getUserMessages(userXAuthToken: UserXAuthToken, user: String): Flow<List<Message>> {
        val messages = localDataSource.getUserMessages(user)
        return remoteDataSource.getUserMessages(userXAuthToken, user)
            .onStart { emit(messages) }
            .onEach {
                if (it != messages) {
                    localDataSource.cacheMessages(it)
                }
            }
            .flowOn(Dispatchers.IO)

    }

    @WorkerThread
    fun sendMessage(userXAuthToken: UserXAuthToken, message: Message): Flow<Int> {
        return remoteDataSource.sendMessage(userXAuthToken, message).flowOn(Dispatchers.IO)
    }
}