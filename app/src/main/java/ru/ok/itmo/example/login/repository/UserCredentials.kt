package ru.ok.itmo.example.login.repository

import com.google.gson.annotations.SerializedName

data class UserCredentials(
    @SerializedName("name")
    val name: String,
    @SerializedName("pwd")
    val password: String
)
// login: kok
// password: lOrEmaZaZaeAtIFMO