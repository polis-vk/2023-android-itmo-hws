package ru.ok.itmo.example.login

import android.app.Activity
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.ok.itmo.example.models.LoginRequest
import ru.ok.itmo.example.models.LoginResponse
import ru.ok.itmo.example.repositories.Repository
import java.net.SocketTimeoutException

class LoginViewModel(private val repository: Repository): ViewModel()
{

    var isEnabledLive = MutableLiveData(false)
    private var login = ""
    private var pwd = ""
    var navigationLive = MutableLiveData<LoginResponse>()
    val isLoggedIn = MutableLiveData(false)


    fun editLogin(string: String) {
        this.login = string
        isEnabled()
    }


    fun editPwd(string: String) {
        this.pwd = string
        isEnabled()
    }

    fun  isLoggedIn(){
        viewModelScope.launch(Dispatchers.IO) {
            isLoggedIn.postValue(repository.isLoggedIn())
        }
    }
    fun isEnabled() {
        isEnabledLive.value = login.isNotEmpty() && pwd.isNotEmpty()
    }


    fun auth() {
        val lp = LoginRequest(this.login, this.pwd)
        viewModelScope.launch(Dispatchers.IO) {
            repository.initRetrofit()
            try {
                navigationLive.postValue(repository.logIn(lp))
            } catch (e: SocketTimeoutException) {
                navigationLive.postValue(LoginResponse("", 403))
            } catch (e: Exception) {
                Log.i("Error", "${e.message}")
                navigationLive.postValue(LoginResponse("", -1))
            }
        }

    }

}