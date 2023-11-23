package ru.ok.itmo.tamtam.presentation.viewmodels

import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.viewModelScope
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.common.BaseViewModel
import ru.ok.itmo.tamtam.data.database.ChatsDatabase
import ru.ok.itmo.tamtam.data.repository.ChatRepository
import ru.ok.itmo.tamtam.presentation.chatscreen.ChatListController
import ru.ok.itmo.tamtam.presentation.chatsscreen.ChatsScreenState

class ChatsViewModel(private val router: Router) :
    BaseViewModel<ChatsScreenState>(ChatsScreenState.Loading) {

    private val chatRepository =
        ChatRepository(ChatsDatabase.getDatabase(router.activity?.baseContext!!).chatsDAO())

    init {
        viewModelScope.launch {
            setState(chatRepository.getChats())
        }
    }

    fun logout(token: String) {
        viewModelScope.launch {
            chatRepository.logout(token)
        }
    }

    fun onChatClick(chat: String) {
        router.pushController(RouterTransaction.with(ChatListController.newInstance(chat)))
    }

    fun createChat() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(router.activity!!)
        builder.setTitle(R.string.create_chat)

        val input = EditText(router.activity?.baseContext)
        builder.setView(input)

        builder.setPositiveButton(R.string.create) { _, _ ->
            viewModelScope.launch {
                chatRepository.createChat(input.text.toString(), router.activity?.baseContext!!) {
                    viewModelScope.launch {
                        setState(chatRepository.getChats())
                    }
                }
            }
        }
        builder.setNegativeButton(R.string.cancel) { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    fun checkDataBase() {
        viewModelScope.launch {
            val saved = chatRepository.getMessagesFromDataBase()
            if (saved?.isEmpty()!!) {
                val check = state.value
                if (check is ChatsScreenState.Error) {
                    setState(check.copy(fromDb = true))
                }
            } else {
                setState(ChatsScreenState.Success(saved))
            }
        }
    }
}