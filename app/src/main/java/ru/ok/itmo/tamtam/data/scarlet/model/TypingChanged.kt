package ru.ok.itmo.tamtam.data.scarlet.model

import com.google.gson.annotations.SerializedName

data class TypingChanged(
    @SerializedName("TypingChanged")
    val typingChanged: Map<String, List<String>>
)

