package ru.ok.itmo.tamtam.data.scarlet.model

import com.google.gson.annotations.SerializedName

data class TypingChangedResponse(@SerializedName("TypingChanged") val typingChangedGPart: TypingChangedGPart?)
data class TypingChangedGPart(@SerializedName("newTyping") val newTypingGPart: Map<String, List<String>>)
