package ru.ok.itmo.example.channelListPart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.example.Storage

class ChannelListViewModel: ViewModel() {
    private val getChannelListStateMutable = MutableLiveData<GetChannelListState>(GetChannelListState.Empty)
    val getChannelListState : LiveData<GetChannelListState>
        get() = getChannelListStateMutable

    fun logOut(){
        viewModelScope.launch {
            Storage.logOut()
        }
    }

    fun getChannels(){
        viewModelScope.launch {
            getChannelListStateMutable.value = ChannelListModel.getChannels()
        }
    }
}