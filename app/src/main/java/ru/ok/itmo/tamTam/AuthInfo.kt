package ru.ok.itmo.tamTam

object AuthInfo {
    var token: String = ""
    var login: String = ""
    fun isAuthorized() = token.isNotEmpty()

    fun reset() {
        token = ""
    }
}
