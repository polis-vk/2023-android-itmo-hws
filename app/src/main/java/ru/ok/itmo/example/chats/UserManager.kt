package ru.ok.itmo.example.chats

import ru.ok.itmo.example.data.AuthTokenData
import ru.ok.itmo.example.data.Message
import ru.ok.itmo.example.repository.UserRepository
import ru.ok.itmo.example.retrofit.Retrofit

class UserManager {
    private val userRepository: UserRepository = Retrofit.retrofit().create(UserRepository::class.java)


    suspend fun getChats(username: String, authTokenData: AuthTokenData): Result<List<Message>> {
        return try {
            Result.success(userRepository.inbox(authTokenData, username))
        } catch(e: Throwable) {
            Result.failure(e)
        }
    }
}