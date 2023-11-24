package ru.ok.itmo.tamtam.data.scarlet.model

import com.google.gson.annotations.SerializedName

data class StartTypingRequest(
    @SerializedName("StartTyping") val chatNameGPart: ChatNameGPart
)

data class ChatNameGPart(
    @SerializedName("chat") val chat: String,
)