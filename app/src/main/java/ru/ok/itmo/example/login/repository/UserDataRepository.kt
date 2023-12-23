package ru.ok.itmo.example.login.repository

import android.graphics.Bitmap
import ru.ok.itmo.example.login.retrofit.models.UserCredentials

class UserDataRepository {
    companion object {
        private var userXAuthToken: UserXAuthToken = ""
        private var userCredentials: UserCredentials? = null
        private var currentChat: String = ""
        private val chatAvatarImage: Bitmap? = null
    }

    fun login(userXAuthToken: UserXAuthToken, userCredentials: UserCredentials) {
        UserDataRepository.userXAuthToken = userXAuthToken
        UserDataRepository.userCredentials = userCredentials
    }

    fun logout() {
        currentChat = ""
        userXAuthToken = ""
        userCredentials = null
    }

    fun getToken(): UserXAuthToken {
        return userXAuthToken
    }

    fun getLogin(): String {
        return userCredentials?.name ?: throw RuntimeException("User not found")
    }

    fun openChat(chatName: String, avatar: Bitmap? = null) {
        currentChat = chatName
    }

    fun closeChat() {
        currentChat = ""
    }

    fun getCurrentChat(): String {
        return currentChat
    }

    fun getCurrentChatImage(): Bitmap? {
        return chatAvatarImage
    }

    fun getPassword(): String {
        return userCredentials?.password ?: throw RuntimeException("User not found")
    }

    fun isLoggedIn(): Boolean {
        return userXAuthToken.isNotEmpty()
    }
}