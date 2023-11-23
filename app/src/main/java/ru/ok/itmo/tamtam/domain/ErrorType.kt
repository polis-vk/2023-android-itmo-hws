package ru.ok.itmo.tamtam.domain

sealed class ErrorType : Exception() {
    class Unknown : ErrorType()
    class Unauthorized : ErrorType()
    class InternetConnection : ErrorType()
    class IncorrectToken : ErrorType()
}
