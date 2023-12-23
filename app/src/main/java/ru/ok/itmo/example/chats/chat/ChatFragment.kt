package ru.ok.itmo.example.chats.chat

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.ChatEffect
import ru.ok.itmo.example.chats.ChatRecyclerAdapter
import ru.ok.itmo.example.chats.ChatViewModel
import ru.ok.itmo.example.chats.MessagesState
import ru.ok.itmo.example.chats.retrofit.models.Data
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.chats.retrofit.models.Text
import ru.ok.itmo.example.custom_view.AvatarCustomView

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.chat_fragment) {

    companion object {
        const val TAG = "CHAT_FRAGMENT"
    }

    private val chatViewModel by viewModels<ChatViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        view.findViewById<TextView>(R.id.chat_fragment_title).text = chatViewModel.getCurrentChat()

        val avatar = view.findViewById<AvatarCustomView>(R.id.avatar)
        avatar.setText(chatViewModel.getCurrentChat())
        avatar.setCvBackgroundColor(R.color.teal_700)
        chatViewModel.getChatImage()?.let {
            avatar.setImage(it)
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val textInfoField = view.findViewById<TextView>(R.id.text)

        chatViewModel.messages.onEach {
            when (it) {
                is MessagesState.Success -> {
                    if (it.messages.isEmpty()) {
                        recyclerView.isVisible = false
                        textInfoField.isVisible = true
                        textInfoField.text = resources.getString(R.string.empty_message_list)
                    } else {
                        textInfoField.isVisible = false
                        recyclerView.isVisible = true

                        recyclerView.adapter =
                            ChatRecyclerAdapter(
                                it.messages.sortedBy { message -> message.id!!.toInt() },
                                chatViewModel.getCurrentUser()
                            )
                    }
                }

                is MessagesState.Failure -> {
                    recyclerView.isVisible = false
                    textInfoField.isVisible = true
                    textInfoField.text = resources.getString(R.string.message_loading_failure)
                }

                else -> {}
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        chatViewModel.getMessages()

        chatViewModel.effects.onEach {
            when (it) {
                is ChatEffect.ReloadChatList -> {
                    chatViewModel.getMessages()
                }
                else -> {}
            }
        }

        val messageInput = view.findViewById<TextInputLayout>(R.id.message_input)
        view.findViewById<ImageView>(R.id.send_message).setOnClickListener {
            if (!messageInput.editText!!.text.isNullOrEmpty()) {
                chatViewModel.sendMessage(Message(
                    "-1",
                    chatViewModel.getCurrentUser(),
                    chatViewModel.getCurrentChat(),
                    Data(Text(messageInput.editText!!.text.toString()), null),
                    null
                ))
                messageInput.editText!!.clearComposingText()
                messageInput.editText!!.clearFocus()
            }
        }
    }

}