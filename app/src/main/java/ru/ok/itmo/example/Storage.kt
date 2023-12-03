package ru.ok.itmo.example

import ru.ok.itmo.example.authPart.AuthResponseChecker
import ru.ok.itmo.example.api_logic.api_models.LoginJSONModel
import ru.ok.itmo.example.authPart.LoginState

object Storage {
    private var token : String? = null

    suspend fun logIn(loginJSONModel: LoginJSONModel): LoginState {
        val loginState = AuthResponseChecker.safeLogIn(loginJSONModel)
        if(loginState is LoginState.Success)
            token = loginState.token
        return loginState
    }

    suspend fun logOut(){
        AuthResponseChecker.safeLogOut()
        token = null
    }
}