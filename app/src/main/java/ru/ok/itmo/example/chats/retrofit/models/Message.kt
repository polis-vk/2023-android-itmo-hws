package ru.ok.itmo.example.chats.retrofit.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") var id: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("data") var data: Data? = null,
    @SerializedName("time") var time: String? = null
)


data class Data(
    @SerializedName("Text") var Text: Text?,
    @SerializedName("Image") var Image: Image?
)

data class Text(
    @SerializedName("text") var text: String
)

data class Image(
    @SerializedName("link") var link: String
)
