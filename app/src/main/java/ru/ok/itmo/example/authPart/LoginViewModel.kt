package ru.ok.itmo.example.authPart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.example.Storage
import ru.ok.itmo.example.api_logic.api_models.LoginJSONModel

class LoginViewModel: ViewModel() {
    private val loginStateMutable = MutableLiveData<LoginState>(LoginState.Unknown)
    val loginState : LiveData<LoginState>
        get() = loginStateMutable

    fun logIn(name: String, password: String) {
        viewModelScope.launch {
            loginStateMutable.value = Storage.logIn(LoginJSONModel(name, password))
        }
    }

}