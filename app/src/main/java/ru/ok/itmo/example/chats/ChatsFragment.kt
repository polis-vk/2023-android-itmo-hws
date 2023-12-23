package ru.ok.itmo.example.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.chats.retrofit.models.getChat

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    companion object {
        const val TAG = "CHATS_FRAGMENT"
        val colors = arrayListOf(
            R.color.teal_700,
            R.color.active_field,
            R.color.green,
            R.color.black,
            R.color.purple_500,
            R.color.purple_200,
            R.color.teal_200,
        )
    }

    private val chatsViewModel by viewModels<ChatsViewModel>()
    private val chats = mutableSetOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chats_fragment, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        chats.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ImageView>(R.id.back_button).setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val textView = view.findViewById<TextView>(R.id.text)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        chatsViewModel.messages.onEach {
            when (it) {
                is MessagesState.Success -> {
                    if (it.messages.isEmpty()) {
                        recyclerView.isVisible = false
                        progressBar.isVisible = false
                        textView.isVisible = true
                        textView.text = resources.getString(R.string.empty_chats_list_message)
                    } else {
                        progressBar.isVisible = false
                        textView.isVisible = false
                        recyclerView.isVisible = true
                        recyclerView.smoothScrollToPosition(0)

                        val lastMessages = getLastChatsMessages(it.messages)
                        lastMessages.forEach { message ->
                            chats.add(message.getChat(chatsViewModel.getCurrentUser()))
                        }

                        recyclerView.adapter = ChatsRecyclerAdapter(
                            lastMessages,
                            chatsViewModel.getCurrentUser(),
                            colors,
                            null
                        ) { message ->
                            chatsViewModel.openChat(message.getChat(chatsViewModel.getCurrentUser()))
                            findNavController().navigate(R.id.action_chatsFragment_to_chatFragment)
                        }
                    }
                }

                is MessagesState.Failure -> {
                    recyclerView.isVisible = false
                    progressBar.isVisible = false
                    textView.isVisible = true
                    textView.text = resources.getString(R.string.error_loading_chats_message)
                    Toast.makeText(requireActivity(), it.error.message, Toast.LENGTH_SHORT)
                }

                is MessagesState.Loading -> {
                    recyclerView.isVisible = false
                    textView.isVisible = false
                    progressBar.isVisible = true
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        chatsViewModel.getUserMessages()

        chatsViewModel.effects.onEach {
            when (it) {
                is ChatListEffect.ReloadChatList -> chatsViewModel.getUserMessages()
                else -> {
                    throw UnsupportedOperationException("Unsupported chats effect")
                }
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        view.findViewById<ImageView>(R.id.avatar).setOnClickListener {
            val chatName = "Kok's chat"
            if (chats.contains(chatName)) {
                Toast.makeText(requireActivity(), R.string.chat_already_exist_error, Toast.LENGTH_SHORT)
            } else {
                chatsViewModel.createNewChat(chatName)
            }
        }
    }

    private fun getLastChatsMessages(messages: List<Message>): List<Message> {
        return messages
            .groupingBy { it.getChat(chatsViewModel.getCurrentUser()) }
            .reduce { _, acc, m ->
                maxOf(acc, m, compareBy { it.id })
            }.values.toList()
    }

    private fun getInitials(name: String): String {
        if (name.isBlank()) return ""

        val cur = name.split(" ")
        if (cur.size == 1) {
            return if (name[0] != '@') name[0].toString() else ""
        }
        return "${cur[0][0]}${cur[0][1]}"
    }
}