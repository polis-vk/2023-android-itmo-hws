package ru.ok.itmo.example.chats.retrofit.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") var id: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("data") var data: Data? = null,
    @SerializedName("time") var time: String? = null
)

fun Message.getChat(userLogin: String): String {
    return if (from == userLogin) to!! else from!!
}

fun Message.getShortText(): String {
    val result = if (data!!.text != null) data!!.text!!.text else "Изображение"
    return if (result.length > 20) result.substring(0..16) + "..." else result
}


data class Data(
    @SerializedName("Text") var text: Text?,
    @SerializedName("Image") var image: Image?
)

data class Text(
    @SerializedName("text") var text: String
)

data class Image(
    @SerializedName("link") var link: String
)

typealias ChannelId = String
