package ru.ok.itmo.example.chats.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import ru.ok.itmo.example.chats.ChatsFragment
import ru.ok.itmo.example.chats.retrofit.ChatsService
import ru.ok.itmo.example.chats.retrofit.models.Message
import kotlin.RuntimeException

class ChatsRepository {
    suspend fun get1chMessages(limit: Int = 1): Result<List<Message>> {
        val response = ChatsService.chatsService.get1chMessages()
        return if (response.isSuccessful && !response.body().isNullOrEmpty()) {
            Result.success(response.body()!!)
        } else {
            Result.failure(RuntimeException("Get messages error"))
        }
    }

    suspend fun getImage(url: String): Result<Bitmap> {
        return try {
            Result.success(BitmapFactory.decodeStream(ChatsService.chatsService.getImage(url).byteStream()))
        } catch (e: Throwable) {
            Log.d(ChatsFragment.TAG, e.message.toString())
            Result.failure(RuntimeException("Get image error"))
        }
    }
}