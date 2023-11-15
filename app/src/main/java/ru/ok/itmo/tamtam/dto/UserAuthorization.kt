package ru.ok.itmo.tamtam.dto;

import com.google.gson.annotations.SerializedName

data class UserAuthorization(
    @SerializedName("name")
    val name: String,
    @SerializedName("pwd")
    val pwd: String,
)
