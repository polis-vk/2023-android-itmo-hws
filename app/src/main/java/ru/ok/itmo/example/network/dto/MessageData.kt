package ru.ok.itmo.example.network.dto

import com.google.gson.annotations.SerializedName

data class MessageData(
    @SerializedName("Text") val text: Text?,
    @SerializedName("Image") val image: Image?
)
