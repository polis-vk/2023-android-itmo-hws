package ru.ok.itmo.example.list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.ok.itmo.example.repositories.Repository

class ListViewModel : ViewModel() {
    private val repository = Repository()

    fun logout(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val r = repository.logout(token)
            Log.i("Logout", r.toString())
        }
    }

}