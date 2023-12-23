package ru.ok.itmo.example.chats.repository

import android.graphics.Bitmap
import ru.ok.itmo.example.chats.retrofit.models.Message

sealed interface MessagesState {
    class Success(val messages: List<Message>) : MessagesState
    class Failure(val error: Throwable) : MessagesState
    object Loading : MessagesState
}
sealed interface ImageState {
    class Success(val bitmap: Bitmap) : ImageState
    class Failure(val error: Throwable) : ImageState
    object Loading : ImageState
    object Started : ImageState
}