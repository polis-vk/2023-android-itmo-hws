package ru.ok.itmo.example.network.dto

import com.google.gson.annotations.SerializedName

class LoginRequest(@SerializedName("name") val login: String,
                   @SerializedName("pwd") val password: String)