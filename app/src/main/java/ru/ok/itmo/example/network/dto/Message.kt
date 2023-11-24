package ru.ok.itmo.example.network.dto

data class Message(
    val id: Int,
    val from: String,
    val to: String,
    val time: Long,
    val data: MessageData
)