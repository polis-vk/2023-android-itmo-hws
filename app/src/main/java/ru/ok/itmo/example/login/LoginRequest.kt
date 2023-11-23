package ru.ok.itmo.example.login

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import ru.ok.itmo.example.login.Login

class LoginRequest(val loginApi: LoginApi, val loginText: String, val passwordText: String) {
    var errorCode = 200
    var token = ""
    fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            val log = loginApi.login(
                Login(
                    loginText,
                    passwordText
                )
            )
            log.enqueue(object: Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if(response.isSuccessful) {
                        CoroutineScope(Dispatchers.Main).launch {
                            token = response.body().toString()
                            errorCode = 200
                        }
                    } else {
                        errorCode = response.code()
                    }
                }
                override fun onFailure(call: Call<String>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }
    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            loginApi.logout(token)
        }
    }
}