package ru.ok.itmo.example.enter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    var name = ""
    var password = ""
    lateinit var token: MutableLiveData<String>
    val isCorrect: MutableLiveData<Boolean> = MutableLiveData(false)

    fun validateCredentials(name: String, psw: String) {
        isCorrect.value = name.isNotEmpty() && psw.isNotEmpty()
    }
}
