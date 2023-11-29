package ru.ok.itmo.tamtam.utils

import retrofit2.Response

fun <T> Result<Response<T>>.handleResult(): Resource<T> {
    val exception = this.exceptionOrNull()
    if (exception != null) return Resource.Failure(NotificationType.Connection(exception))
    val response = this.getOrNull()
        ?: return Resource.Failure(NotificationType.Unknown(RuntimeException("No response")))

    return when (response.code()) {
        200 -> {
            val body = response.body()
                ?: return Resource.Failure(NotificationType.Unknown(RuntimeException("No body")))
            Resource.Success(body)
        }

        400, in 402..499 -> Resource.Failure(NotificationType.Client(RuntimeException("Client exception")))
        401 -> Resource.Failure(NotificationType.Unauthorized(RuntimeException("Unauthorized")))
        in 500..599 -> Resource.Failure(NotificationType.Server(RuntimeException("Server exception")))
        else -> Resource.Failure(NotificationType.Unknown(RuntimeException("Unknown exception")))
    }
}