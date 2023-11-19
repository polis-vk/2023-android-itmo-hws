package ru.ok.itmo.example.retrofit.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val name: String,
    @SerialName("pwd")
    val pwd: String
)
