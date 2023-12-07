package ru.ok.itmo.example.domain

sealed class LoginState {

    object typeError {
        const val OK = 200
        const val UNAUTHORIZED = 401
        const val TIMED_OUT = 408
        const val SERVER_ERROR = 500
        const val No_Internet = 502
        const val Unknown_Error = 520
    }

    data class Success(val code : Int, val token: String) : LoginState()
    data class Error(val error: Int) : LoginState()

    companion object {
        val errorMap: Map<Int, String> = mapOf(
            typeError.OK to "OK",
            typeError.UNAUTHORIZED to "Unauthorized",
            typeError.TIMED_OUT to "Timed Out",
            typeError.SERVER_ERROR to "Server Error",
            typeError.No_Internet to "No Internet",
            typeError.Unknown_Error to "Unknown Error"
        )
    }
}