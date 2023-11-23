package ru.ok.itmo.tamtam.utils

fun getChatName(login: String, from: String, to: String): String {
    when {
        from.endsWith("@channel") -> return from
        to.endsWith("@channel") -> return to
        from == login -> return to
        to == login -> return from
    }
    throw RuntimeException("can't take chatName")
}