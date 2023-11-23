package ru.ok.itmo.tamtam.data.scarlet.model

import com.google.gson.annotations.SerializedName

data class NewMessageResponse(@SerializedName("NewMessage") val newMessageGPart: NewMessageGPart?)
data class NewMessageGPart(@SerializedName("msg") val messageGPart: MessageGPart)
data class MessageGPart(
    @SerializedName("id") val id: Int,
    @SerializedName("from") val from: String,
    @SerializedName("to") val to: String,
    @SerializedName("time") val time: Long,
    @SerializedName("data") val dataGPart: DataGPart
)

data class DataGPart(
    @SerializedName("Text") val textGPart: TextGPart?,
    @SerializedName("Image") val imageGPart: ImageGPart?
)

data class TextGPart(@SerializedName("text") val text: String)
data class ImageGPart(@SerializedName("link") val link: String)
