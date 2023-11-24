package ru.ok.itmo.example.network.result

data class LogInResult(
    val isAuthorized: Boolean = false,
    val errorType: ErrorType = ErrorType.NO_ERROR,
    val token: String? = null
) {
    enum class ErrorType {
        NO_ERROR,
        INVALID_LOGIN_OR_PASSWORD,
        NO_INTERNET_CONNECTION,
        TIMED_OUT,
        UNKNOWN_ERROR
    }

    companion object {
        fun success(token: String): LogInResult {
            return LogInResult(true, ErrorType.NO_ERROR, token)
        }

        fun failure(error: ErrorType): LogInResult {
            return LogInResult(false, error)
        }
    }
}