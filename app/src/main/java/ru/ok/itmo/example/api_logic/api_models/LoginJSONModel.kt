package ru.ok.itmo.example.api_logic.api_models

import com.google.gson.annotations.SerializedName

data class LoginJSONModel(
    @SerializedName("name")
    val name: String,
    @SerializedName("pwd")
    val pwd: String,
)