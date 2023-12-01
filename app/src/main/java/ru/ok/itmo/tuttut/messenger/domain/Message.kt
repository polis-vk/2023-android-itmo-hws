package ru.ok.itmo.tuttut.messenger.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val from: String,
    val to: String,
    val data: MessageData,
    val time: Long
) {
    @Serializable
    data class MessageData(
        @SerialName("Text")
        val text: InnerText?,
        @SerialName("Image")
        val image: InnerImage?
    ) {
        @Serializable

        data class InnerText(val text: String)

        @Serializable
        data class InnerImage(val image: String)
    }
}
