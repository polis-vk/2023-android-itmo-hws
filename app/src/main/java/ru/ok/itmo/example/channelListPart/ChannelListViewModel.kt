package ru.ok.itmo.example.channelListPart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.example.Storage
import ru.ok.itmo.example.channelListPart.states.GetChannelListState

class ChannelListViewModel(private val model: ChannelListModel): ViewModel() {
    private val getChannelListStateMutable = MutableLiveData<GetChannelListState>(
        GetChannelListState.Empty)
    val getChannelListState : LiveData<GetChannelListState>
        get() = getChannelListStateMutable

    fun logOut(){
        viewModelScope.launch {
            Storage.logOut()
        }
    }

    fun getChannelList(){
        viewModelScope.launch {
            getChannelListStateMutable.value = GetChannelListState.Loading
            val result = model.getChannelList()
            getChannelListStateMutable.value = result
        }
    }
}