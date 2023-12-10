package ru.ok.itmo.tamTam

object AuthInfo {
    var token: String = ""

    fun isAuthorized() = token.isNotEmpty()

    fun reset() {
        token = ""
    }
}
