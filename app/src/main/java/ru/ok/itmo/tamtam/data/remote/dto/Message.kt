package ru.ok.itmo.tamtam.data.remote.dto

import android.graphics.Bitmap
import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    @JsonProperty("from")
    val user: String,
    @JsonProperty("to")
    val targetChat: String,
    val data: Data,
    val time: String,
    val id: Long? = null,
)

data class Data(
    @JsonProperty("Image")
    val Image: Image? = null,
    @JsonProperty("Text")
    val Text: Text? = null,
)

data class Image(
    val link: String = "",
    var bitmap: Bitmap? = null,
)

data class Text(
    val text: String = "",
)