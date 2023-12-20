package ru.ok.itmo.example.chats.repository

import android.graphics.Bitmap

sealed interface ImageState {
    class Success(val bitmap: Bitmap) : ImageState
    class Failure(val error: Throwable) : ImageState
    object Loading : ImageState
    object Started : ImageState
}