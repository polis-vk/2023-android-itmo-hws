package ru.ok.itmo.example.login

data class LoginData(private val name: String, private val pwd: String)

typealias AuthTokenData = String
