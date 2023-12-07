package ru.ok.itmo.example.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.example.chatsPackage.Channel
import ru.ok.itmo.example.chatsPackage.ChatState
import ru.ok.itmo.example.chatsPackage.Message
import ru.ok.itmo.example.domain.UseCase
import ru.ok.itmo.example.retrofit.RetrofitProvider
import ru.ok.itmo.example.retrofit.regApi

class ChatViewModel : ViewModel()  {
    val token: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    private val chats = mutableListOf<Channel>()
    private val _chatsState = MutableLiveData<ChatState>(ChatState.Unknown)
    val chatsState: LiveData<ChatState>
        get() = _chatsState


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