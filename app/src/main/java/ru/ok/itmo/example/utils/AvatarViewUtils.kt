package ru.ok.itmo.example.utils

class AvatarViewUtils {

    fun getTitle(text: String) : String {
        return text.split(" ".toRegex())
            .take(2)
            .joinToString("") { it[0].uppercase() }
    }
}