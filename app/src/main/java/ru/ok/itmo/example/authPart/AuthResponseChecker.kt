package ru.ok.itmo.example.authPart

import ru.ok.itmo.example.api_logic.AuthAPI
import ru.ok.itmo.example.api_logic.api_models.LoginJSONModel

object AuthResponseChecker {

    private val authAPI = AuthAPI.provideAuthAPI

    suspend fun safeLogIn(loginJSONModel: LoginJSONModel) : LoginState {
        try {
            val response = authAPI.logIn(loginJSONModel)
            if(response.isSuccessful && response.body() != null){
                return LoginState.Success(response.body()!!)
            }else{
                return LoginState.Failed("Не существует такого пользователя")
            }
        } catch (e: Exception){
            return LoginState.Failed("Упс. Что-то пошло не так")
        }
    }

    suspend fun safeLogOut(){
        authAPI.logOut()
    }

}