package ru.ok.itmo.example.enter.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.domain.AuthUseCase
import ru.ok.itmo.example.network.RetrofitProvider
import ru.ok.itmo.example.network.api.AuthApi
import ru.ok.itmo.example.network.result.LogInResult

class EnterViewModel : ViewModel() {
    companion object {
        const val TAG = "EnterViewModel"
    }

    private var login: String = ""
    private var password: String = ""

    private val _isCorrectData = MutableLiveData(false)
    val isCorrectData: LiveData<Boolean> get() = _isCorrectData

    private val authUseCase by lazy {
        val retrofit = RetrofitProvider.retrofit
        val authApi = AuthApi.provideAuthApi(retrofit)
        AuthUseCase(authApi)
    }

    fun loginChange(login: String) {
        this.login = login
        Log.d(TAG, "new login: '$login'")
        validate()
    }

    fun passwordChange(password: String) {
        this.password = password
        Log.d(TAG, "new password: '$password'")
        validate()
    }

    private fun validate() {
        _isCorrectData.value = isValidLogin() && isValidPassword()
    }

    private fun isValidLogin(): Boolean {
        return login.isNotEmpty()
    }

    private fun isValidPassword(): Boolean {
        return password.isNotEmpty()
    }

    fun getLogin(): String = login
    fun getPassword(): String = password
    fun logIn(isOnline: Boolean, resultHandler: (LogInResult) -> Unit) {
        if (isCorrectData.value == false) return
        if (!isOnline) {
            resultHandler.invoke(
                LogInResult.failure(LogInResult.ErrorType.NO_INTERNET_CONNECTION)
            )
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val result = authUseCase.logIn(login, password)
            withContext(Dispatchers.Main) {
                resultHandler.invoke(result)
            }
        }
    }
}