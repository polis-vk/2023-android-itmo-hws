package ru.ok.itmo.example.models

data class LoginResponse(
    val token: String?,
    val code: Int?
)
