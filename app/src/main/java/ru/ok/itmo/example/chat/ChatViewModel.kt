package ru.ok.itmo.example.chat


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.example.domain.UseCase
import ru.ok.itmo.example.retrofit.RetrofitProvider
import ru.ok.itmo.example.retrofit.regApi

class ChatViewModel : ViewModel()  {
    val token: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    fun setToken(token: String) {
        this.token.value = token
    }

    private val loginUseCase by lazy {
        val retrofit = RetrofitProvider.retrofit
        val weatherApi = regApi.provideRequestApi(retrofit)
        UseCase(weatherApi)
    }

    suspend fun logout(token: String) {
        try {
            loginUseCase.logout(token)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}