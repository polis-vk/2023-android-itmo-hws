package ru.ok.itmo.tamtam

object Constants {
    const val CHANNEL_SUFFIX = "@channel"
    const val API_BASE_URL = "https://faerytea.name:8008/"
    const val CONNECT_TIMEOUT = 20L
    const val READ_TIMEOUT = 40L
    const val WRITE_TIMEOUT = 40L
    const val RECONNECT_TIME = 5000L
    const val WS_BASE_URL = "wss://faerytea.name:8008/ws/%s?token=%s"
    const val API_AVATAR_URL = "https://faerytea.name:8008/avatar/%s.png"
}