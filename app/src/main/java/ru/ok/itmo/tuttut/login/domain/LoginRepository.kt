package ru.ok.itmo.tuttut.login.domain

interface LoginRepository {
    suspend fun login(userCredentials: UserCredentials): Result<UserXAuthToken>
    suspend fun logout(token: UserXAuthToken)
}