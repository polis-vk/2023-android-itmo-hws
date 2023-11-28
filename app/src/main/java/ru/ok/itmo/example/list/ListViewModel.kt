package ru.ok.itmo.example.list

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.example.models.Channel
import ru.ok.itmo.example.repositories.InvalidDataException
import ru.ok.itmo.example.repositories.Repository

class ListViewModel(private val repository: Repository) : ViewModel() {

    val channels = MutableLiveData<List<Channel>>()
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.initRetrofit()
            val r = repository.logout()
            Log.i("Logout", r.toString())
        }
    }

    fun getInbox() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.initRetrofit()
                val channelsResponse: MutableList<Channel> = repository.getInbox()
                channelsResponse.sortBy { it.name!!.removeSuffix("@channel")}
                channels.postValue(channelsResponse)
            } catch (e: InvalidDataException) {
                throw e
            }
        }
    }

    fun postTo(destination: String) {
        val text = "new chat"
        viewModelScope.launch(Dispatchers.IO) {
            repository.initRetrofit()
            repository.postMessage(text, destination)
        }
    }

}