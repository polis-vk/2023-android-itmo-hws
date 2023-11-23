package ru.ok.itmo.tamtam.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var readyForAuthorization: MutableLiveData<Boolean> = MutableLiveData(false)

    private var model: LoginModel = LoginModel()
    private var isLoginReady: Boolean = false
    private var isPasswordReady: Boolean = false
    private var login: String = ""
    private var password: String = ""

    var token: MutableLiveData<String> = MutableLiveData()

    suspend fun authorization() {
        model.authorization(login, password, object : OnDataReadyCallback {
            override fun onDataReady(data: String) {
                token.postValue(data)
            }
        })
    }

    fun changeLogin(newLogin: String) {
        login = newLogin
        isLoginReady = newLogin.isNotEmpty()
        updateReadyForAuthorization()
    }

    fun changePassword(newPassword: String) {
        password = newPassword
        isPasswordReady = newPassword.isNotEmpty()
        updateReadyForAuthorization()
    }

    private fun updateReadyForAuthorization() {
        readyForAuthorization.value = isLoginReady && isPasswordReady
    }
}