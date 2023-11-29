package ru.ok.itmo.tamtam.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class TextMessageDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("from")
    val from: String,
    @SerializedName("to")
    val to: String,
    @SerializedName("time")
    val time: Long,
    @SerializedName("data")
    val data: TextDataDto
)

data class TextDataDto(
    @SerializedName("Text")
    val text: TextDto?
)