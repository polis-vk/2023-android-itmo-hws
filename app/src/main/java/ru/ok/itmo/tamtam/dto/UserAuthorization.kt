package ru.ok.itmo.tamtam.dto;

typealias AuthToken = String

data class UserAuthorization(
    val name: String,
    val pwd: String,
)
