package ru.ok.itmo.tamtam.domain

import ru.ok.itmo.tamtam.data.remote.dto.Message

data class Chat(val title: String, val messages: List<Message>)