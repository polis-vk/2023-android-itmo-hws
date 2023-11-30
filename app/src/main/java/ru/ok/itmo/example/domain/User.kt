package ru.ok.itmo.example.domain

data class LoginForm(
    private val name: String,
    private val pwd: String,
)

typealias AuthToken = String
