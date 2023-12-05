package ru.ok.itmo.tamtam.data.repository

import android.util.Log
import ru.ok.itmo.tamtam.common.Constants.getServerAPI
import ru.ok.itmo.tamtam.common.Resource
import ru.ok.itmo.tamtam.data.remote.dto.LoginRequest
import ru.ok.itmo.tamtam.presentation.chatscreen.ChatListController
import ru.ok.itmo.tamtam.presentation.loginScreen.LoginScreenState

class AuthRepository : BaseRepository() {

    suspend fun login(login: String, pwd: String): LoginScreenState =
        when (val response = apiCall { api.login(LoginRequest(login, pwd)) }) {
            is Resource.Error -> {
                Log.d("HELP_ME", response.data.toString())
                LoginScreenState.Error(response.message!!)
            }

            is Resource.Success -> {
                LoginScreenState.Navigate(
                    ChatListController(),
                    response.data!!
                )
            }

            is Resource.Loading -> LoginScreenState.Success()
        }


    private companion object {
        val api = getServerAPI()
    }
}