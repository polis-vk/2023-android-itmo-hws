package ru.ok.itmo.example.models

import com.fasterxml.jackson.annotation.JsonProperty

data class Message(
    @JsonProperty("id") val id: Long?,
    @JsonProperty("from") val from: String? = "LetItBeRickAstley",
    @JsonProperty("to") val to: String? = "1@chanel",
    @JsonProperty("data") val data: Data?,
    @JsonProperty("time") val time: Long?
)

