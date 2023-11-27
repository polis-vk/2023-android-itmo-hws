package ru.ok.itmo.tamtam.login.login_fragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var readyForAuthorization: MutableLiveData<Boolean> = MutableLiveData(false)

    private var model: LoginModel = LoginModel()
    private var isLoginReady: Boolean = false
    private var isPasswordReady: Boolean = false
    private var login: String = ""
    private var password: String = ""

    sealed class State {
        data class Error(val e: Exception) : State()
        data object Success : State()
        data object Wait : State()
    }

    var state: MutableLiveData<State> = MutableLiveData()

    fun authorization() {
        state.value = State.Wait

        viewModelScope.launch {
            try {
                model.authorization(login, password) {
                    viewModelScope.launch(Dispatchers.Main) {
                        state.value = State.Success
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    state.value = State.Error(e)
                }
            }
        }
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