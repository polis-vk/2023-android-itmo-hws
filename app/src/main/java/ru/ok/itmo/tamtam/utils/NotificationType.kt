package ru.ok.itmo.tamtam.utils

sealed class NotificationType(val throwable: Throwable) : Exception() {
    class Unknown(throwable: Throwable) : NotificationType(throwable)
    class Unauthorized(throwable: Throwable) : NotificationType(throwable)
    class Client(throwable: Throwable) : NotificationType(throwable)
    class Server(throwable: Throwable) : NotificationType(throwable)
    class Connection(throwable: Throwable) : NotificationType(throwable)

    override val message: String?
        get() = throwable.message
}