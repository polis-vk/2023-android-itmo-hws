package ru.ok.itmo.example.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

data class sendMessage(
    @JsonProperty("from") val from: String? = "LetItBeRickAstley",
    @JsonProperty("to") val to: String? = "1@chanel",
    @JsonProperty("data") val data: sendData?
)

data class sendData(
    @JvmField val Text: Text? = null
)
