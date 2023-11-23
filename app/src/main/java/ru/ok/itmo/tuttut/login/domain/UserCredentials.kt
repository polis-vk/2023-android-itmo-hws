package ru.ok.itmo.tuttut.login.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserCredentials(
    val name: String,
    @SerialName("pwd")
    val password: String
)

typealias UserXAuthToken = String
