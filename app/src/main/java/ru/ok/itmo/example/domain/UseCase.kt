package ru.ok.itmo.example.domain

import androidx.lifecycle.MutableLiveData
import ru.ok.itmo.example.domain.LoginState
import ru.ok.itmo.example.retrofit.dto.Request
import ru.ok.itmo.example.retrofit.regApi

class UseCase(private val api: regApi) {

    suspend fun getLogInfo(name: String, psw: String): LoginState {
        return try {
            val response = api.login(Request(name, psw))
            val responseString = response.string()
            LoginState.Success(LoginState.typeError.OK, responseString)
        } catch (e: Exception) {
            e.printStackTrace()
            LoginState.Error(LoginState.typeError.Unknown_Error)
        }
    }

    fun logout(token: String){
        api.logout(token)
    }

}
