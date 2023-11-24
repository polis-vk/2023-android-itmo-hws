package ru.ok.itmo.tamtam.utils

sealed class Resource<out T> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Failure<T>(val throwable: Throwable) : Resource<T>()

    suspend fun <U> mapIfSuccess(transform: suspend (T) -> U): Resource<U> {
        return when (this) {
            is Failure -> Failure(this.throwable)
            is Success -> Success(transform.invoke(this@Resource.data))
        }
    }

    fun getOrNull(): T? {
        return when (this) {
            is Failure -> null
            is Success -> this.data
        }
    }
}

fun <I> Resource<List<I>>.merge(that: Resource<List<I>>): Resource<List<I>> {
    return if (this is Resource.Failure || that is Resource.Failure) this
    else Resource.Success(this.getOrNull()!! + that.getOrNull()!!)
}