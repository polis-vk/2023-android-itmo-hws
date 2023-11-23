package ru.ok.itmo.tamtam.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class MessageDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("time")
    val time: Long,
    @SerializedName("data")
    val data: DataDto
)

data class DataDto(
    @SerializedName("Text")
    val text: TextDto?,
    @SerializedName("Image")
    val image: ImageDto?,
)

data class TextDto(
    val text: String
)

data class ImageDto(
    val link: String
)