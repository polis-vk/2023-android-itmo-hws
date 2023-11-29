package ru.ok.itmo.tamtam.data.scarlet.model

import com.google.gson.annotations.SerializedName

data class NewMessageRequest(
    @SerializedName("NewMessageText") val textMessageGPart: TextMessageGPart
)

data class TextMessageGPart(
    @SerializedName("to") val to: String,
    @SerializedName("text") val text: String,
)