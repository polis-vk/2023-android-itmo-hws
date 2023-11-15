package ru.ok.itmo.tamtam.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ChatsViewModel : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            AuthorizationStorage.logout()
        }
    }

}
