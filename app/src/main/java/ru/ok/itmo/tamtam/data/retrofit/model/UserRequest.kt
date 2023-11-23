package ru.ok.itmo.tamtam.data.retrofit.model

import com.google.gson.annotations.SerializedName

data class UserRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("pwd")
    val pwd: String,
)