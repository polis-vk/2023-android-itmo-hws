package ru.ok.itmo.example.repositories

import android.util.Log
import ru.ok.itmo.example.models.Data
import ru.ok.itmo.example.models.LoginRequest
import ru.ok.itmo.example.models.LoginResponse
import ru.ok.itmo.example.models.Message
import ru.ok.itmo.example.models.Text
import ru.ok.itmo.example.models.sendMessage
import ru.ok.itmo.example.network.MainApi
import ru.ok.itmo.example.network.RetrofitInitialization

class Repository() {

    private var mainApi = RetrofitInitialization().getClient().create(MainApi::class.java)

    fun logIn(
        loginRequest: LoginRequest
    ): LoginResponse {
        val logInResponse = mainApi.logIn(loginRequest).execute()
        Log.i("returnCode", logInResponse.code().toString())
        when (logInResponse.code()) {
            200 -> {
                return LoginResponse(logInResponse.body(), logInResponse.code())
            }

            else -> {
                return LoginResponse("", logInResponse.code())
            }
        }
    }

    fun logout(token: String) {
        mainApi.logout(token).execute()
    }

    fun get1ch(): List<Message>? {
        val request = mainApi.get1ch().execute()
        when (request.code()) {
            200 -> return request.body()
            else -> throw InvalidDataException("Invalid Data Recieved")
        }
    }

    fun postMessage(text: String) {
        mainApi.postMessage(sendMessage("LetItBeRickAstley", "1@channel", Data(Text(text))))
            .execute()
    }

    fun getChannel(name: String) {
        mainApi.getChannel(name)
    }
}

class InvalidDataException(s: String) : Throwable(s)
