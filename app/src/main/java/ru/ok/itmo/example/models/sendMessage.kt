package ru.ok.itmo.example.models

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.JsonProperty

data class sendMessage(
    @JsonProperty("from") val from: String? = "LetItBeRickAstley",
    @JsonProperty("to") val to: String? = "1@chanel",
    @JsonProperty("data") val data: Data?
)

@JsonInclude(Include.NON_NULL)
data class Data(
    @JsonProperty("Text") val Text: Text? = null,
    @JsonProperty("Image") val Image: Image? = null
)

data class Text(
    @JsonProperty("text") val text: String? = ""
)

data class Image(
    @JsonProperty("link") val link: String? = ""
)