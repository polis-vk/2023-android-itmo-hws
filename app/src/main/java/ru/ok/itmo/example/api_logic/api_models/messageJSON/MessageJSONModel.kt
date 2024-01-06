package ru.ok.itmo.example.api_logic.api_models.messageJSON

data class MessageJSONModel(
    val `data`: Data,
    val from: String,
    val id: String,
    val time: String,
    val to: String
)