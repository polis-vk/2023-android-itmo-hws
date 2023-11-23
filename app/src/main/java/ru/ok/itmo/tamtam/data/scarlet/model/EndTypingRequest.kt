package ru.ok.itmo.tamtam.data.scarlet.model

import com.google.gson.annotations.SerializedName

data class EndTypingRequest(
    @SerializedName("EndTyping") val empty: Map<String, String>
)

