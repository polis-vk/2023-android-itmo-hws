package ru.ok.itmo.tamTam.chats

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.ok.itmo.tamTam.AuthInfo
import ru.ok.itmo.tamTam.chats.models.Message
import ru.ok.itmo.tamTam.serverService

object Repository {
    suspend fun messages(): Result<MutableList<Message>>{
        return try {
            val response = serverService.messages(AuthInfo.login, AuthInfo.token)

            val listType = object : TypeToken<List<Message>>() {}.type
            val messageList: MutableList<Message> = Gson().fromJson(response.string(), listType)

            Result.success(messageList.toMutableList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
