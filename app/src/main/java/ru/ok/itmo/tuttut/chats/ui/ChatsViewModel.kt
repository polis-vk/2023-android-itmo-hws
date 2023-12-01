package ru.ok.itmo.tuttut.chats.ui

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.chats.domain.ChatUI
import ru.ok.itmo.tuttut.chats.domain.ChatsRepository
import ru.ok.itmo.tuttut.chats.domain.ChatsState
import ru.ok.itmo.tuttut.messenger.domain.Chat
import ru.ok.itmo.tuttut.messenger.domain.ChatsDAO
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatsViewModel @Inject constructor(
    private val chatsDAO: ChatsDAO,
    private val chatsRepository: ChatsRepository,
    private val glideManager: RequestManager,
) : ViewModel() {

    private val _chatsState = MutableStateFlow<ChatsState>(ChatsState.Loading)
    val chatsState: StateFlow<ChatsState> = _chatsState.asStateFlow()

    fun getChats() {
        viewModelScope.launch {
            _chatsState.emit(
                ChatsState.FromCache(
                    chatsDAO.getAll().map(::chatToUI)
                )
            )
            chatsRepository.getChats().onSuccess { chats ->
                chatsDAO.insert(chats)
                val chatUIs = chats.map {
                    chatToUI(it) to listOf(
                        null,
                        "https://i.pravatar.cc/100?u=${
                            UUID.randomUUID()
                        }"
                    ).random()?.let {
                        viewModelScope.async(Dispatchers.IO) {
                            glideManager
                                .asBitmap()
                                .load(it)
                                .apply(RequestOptions.circleCropTransform())
                                .submit()
                                .get()
                        }
                    }
                }.map {
                    joinChatUI(it)
                }
                _chatsState.emit(
                    ChatsState.Success(chatUIs)
                )
            }.onFailure {
                Log.e("Chats", it.toString())
            }
        }
    }

    private fun chatToUI(chat: Chat): ChatUI {
        val (message, time) = chat.messages.lastOrNull()?.let {
            it.data.text?.text to
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(
                        Instant.ofEpochMilli(it.time)
                    )
        } ?: (null to null)

        return ChatUI(
            chat.name,
            message,
            time,
            null
        )
    }

    private suspend fun joinChatUI(chatUI: Pair<ChatUI, Deferred<Bitmap>?>) =
        chatUI.first.copy(
            avatarBitmap = chatUI.second?.await()
        )
}