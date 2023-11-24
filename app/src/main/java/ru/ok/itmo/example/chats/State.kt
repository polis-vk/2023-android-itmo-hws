package ru.ok.itmo.example.chats

enum class State {
    INIT,
    LOADING_CHATS,
    ERROR,
    CHATS_LOADED,
    CHATS_LOADED_WITH_ERRORS
}