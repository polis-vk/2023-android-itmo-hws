package ru.ok.itmo.example.chatsPackage

import java.lang.Integer.min


class ChatUseCase(private val api: chatApi) {

    suspend fun myGetAllChannels(): ChatState {
        try {
            val outChannels = mutableListOf<Channel>()
            val response = api.getAllChannels()
            if (response.isSuccessful) {
                val channels = response.body()
                channels?.forEach {
                    val message = myGetChannelMessage(it)
                    if (message != null) {
                        if (message.data.Text == null){
                            outChannels.add(Channel(it, null, message.data.Image))
                        } else {
                            outChannels.add(Channel(it, message.data.Text, null))
                        }

                    } else {
                        return ChatState.Error(response.code())
                    }
                }
                return ChatState.Success(outChannels)
            } else {
                return ChatState.Error(response.code())
            }
        } catch (e: Exception) {
            throw Error(e)
        }
    }


    suspend fun getChannelsByPage(page: Int, pageSize: Int): List<Channel> {
        val allChannels = when(val result = myGetAllChannels()) {
            is ChatState.Success -> result.chats
            is ChatState.Error -> throw Exception("Ошибка API")
            else -> throw Exception("Неизвестная ошибка")
        }

        val fromIndex = (page - 1)
        val toIndex = min(fromIndex + pageSize, allChannels.size)

        return if (fromIndex < allChannels.size) allChannels.subList(fromIndex, toIndex) else emptyList()
    }

    suspend fun myGetChannelMessage(channelId: String): Message? {
        try {
            val response = api.getChannelMessages(channelId)
            if (response.isSuccessful) {
                val messages = response.body()
                if (messages.isNullOrEmpty()) {
                    throw NoSuchElementException("Еще не было ни одного сообщения")
                } else {
                    return messages.last()
                }
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }


    suspend fun myGetChannelAllMessages(channelId: String): List<Message>? {
        try {
            val response = api.getChannelMessages(channelId)
            if (response.isSuccessful) {
                val messages = response.body()
                if (messages.isNullOrEmpty()) {
                    throw NoSuchElementException("Еще не было ни одного сообщения")
                } else {
                    return messages
                }
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

}
