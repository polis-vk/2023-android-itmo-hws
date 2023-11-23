package ru.ok.itmo.tamtam.domain.model

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.domain.AuthorizationStorage
import ru.ok.itmo.tamtam.domain.state.ChatsState
import ru.ok.itmo.tamtam.domain.ChatsStorage
import ru.ok.itmo.tamtam.dto.ChannelName
import ru.ok.itmo.tamtam.util.TextPresentObjects
import java.text.SimpleDateFormat
import java.util.Date

class ChatsViewModel : ViewModel() {
    data class ChatInfo(val author: String?, val lastMessage: String?, val time: String?)

    private val chatInfoList = mutableListOf<ChatInfo>()

    private val _chatsState = MutableLiveData<ChatsState>(ChatsState.Unknown)
    val chatsState: LiveData<ChatsState>
        get() = _chatsState

    fun getAllChannels() {
        viewModelScope.launch {
            ChatsStorage.getAllChannels().onSuccess { list ->
                _chatsState.value = ChatsState.LoadingChatsInfo
                list.forEach {
                    getChannelInfo(it)
                }
                _chatsState.value = ChatsState.Success(chatInfoList)

            }.onFailure {
                _chatsState.value = ChatsState.Failure(it)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            AuthorizationStorage.logout()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private val dateFormatter = SimpleDateFormat("hh:mm")
    private suspend fun getChannelInfo(channelName: ChannelName) {
        ChatsStorage.getChannelMessages(channelName).onSuccess {
            val message = it.last()
            val textMes = message.data.Text?.text ?: TextPresentObjects.image
            val time = if (message.time != null) Date(message.time) else null
            chatInfoList.add(ChatInfo(
                message.from,
                textMes,
                time?.let { it1 -> dateFormatter.format(it1) }
            ))
        }
    }
}
