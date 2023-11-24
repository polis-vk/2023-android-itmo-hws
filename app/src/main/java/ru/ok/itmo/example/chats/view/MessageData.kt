package ru.ok.itmo.example.chats.view

abstract class MessageData

data class TextData(val text: String) : MessageData()

data class ImageData(val link: String) : MessageData()