package ru.ok.itmo.tuttut.messenger.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.messenger.domain.MessengerRepository
import javax.inject.Inject

@HiltViewModel
class MessengerViewModel @Inject constructor(
    private val repository: MessengerRepository
) : ViewModel() {
    fun inbox(name: String) {
        viewModelScope.launch {
            Log.d("INBOX", repository.getInbox().toString())
        }
    }
}
