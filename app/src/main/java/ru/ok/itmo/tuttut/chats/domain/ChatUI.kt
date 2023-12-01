package ru.ok.itmo.tuttut.chats.domain

import android.graphics.Bitmap

data class ChatUI(
    val name: String,
    val lastTime: String?,
    val lastMessage: String?,
    val avatarBitmap: Bitmap?
)
