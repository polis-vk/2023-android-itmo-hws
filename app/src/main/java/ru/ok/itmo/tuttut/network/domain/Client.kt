package ru.ok.itmo.tuttut.network.domain

interface Client {
    fun <T> create(cls: Class<T>): T

    suspend fun <R> safeRequest(request: suspend () -> R): Result<R>
}