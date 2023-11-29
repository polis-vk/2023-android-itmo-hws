package ru.ok.itmo.tamtam.utils

import ru.ok.itmo.tamtam.Constants.CHANNEL_SUFFIX

fun getChatName(login: String, from: String, to: String): String {
    when {
        from.endsWith(CHANNEL_SUFFIX) -> return from
        to.endsWith(CHANNEL_SUFFIX) -> return to
        from == login -> return to
        to == login -> return from
    }
    throw RuntimeException("can't take chatName")
}