package ru.ok.itmo.example.login.repository

interface LoginRepository {
    suspend fun login(userCredentials: UserCredentials): Result<UserXAuthToken>
    suspend fun logout(userXAuthToken: UserXAuthToken): Result<UserXAuthToken>
}

typealias UserXAuthToken = String
