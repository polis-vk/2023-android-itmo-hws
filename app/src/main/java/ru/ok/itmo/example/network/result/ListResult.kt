package ru.ok.itmo.example.network.result

class ListResult<T>(
    private val list: List<T>? = null,
    private val isError: Boolean,
    val errorType: ErrorType
) {
    fun isError(): Boolean {
        return isError
    }

    fun getListNotNull(): List<T> {
        return list!!
    }

    fun contains(): Boolean {
        return list != null
    }

    companion object {
        fun <T> success(list: List<T>, errorType: ErrorType = ErrorType.NO_ERROR): ListResult<T> {
            return ListResult(list, false, errorType)
        }

        fun <T> error(errorType: ErrorType): ListResult<T> {
            return ListResult(null, true, errorType)
        }
    }
}