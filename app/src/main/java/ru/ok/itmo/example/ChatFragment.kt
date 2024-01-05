package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.example.chat.ChatAdapter
import ru.ok.itmo.example.data.Data
import ru.ok.itmo.example.data.Message
import ru.ok.itmo.example.data.Text
import ru.ok.itmo.example.messages.MessagesState
import ru.ok.itmo.example.messages.MessagesViewModel
import ru.ok.itmo.example.utils.CustomAvatarView

class ChatFragment : Fragment(R.layout.chat_fragment) {

    private val chatViewModel by viewModels<MessagesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.messages)
        val textName = view.findViewById<TextView>(R.id.textName)
        val textInfo = view.findViewById<TextView>(R.id.textInfo)
        val inputMessage = view.findViewById<TextView>(R.id.inputMessage)
        val profileImage = view.findViewById<CustomAvatarView>(R.id.imageProfile)
        val me: String = AppManager.username!!
        val companion: String = AppManager.chatOpen!!
        profileImage.setText(companion)

        textName.text = AppManager.chatOpen
        progressBar.isVisible = true

        view.findViewById<AppCompatImageView>(R.id.sendMessage).setOnClickListener {
            if (inputMessage.text != "") {
                chatViewModel.postMessage(AppManager.authTokenData!!, Message(-1, me, companion, Data(
                    Text(inputMessage.text.toString()),
                    null
                ), null))
                inputMessage.text = ""

            }
        }

        view.findViewById<AppCompatImageView>(R.id.imageBack).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }


        chatViewModel.run(AppManager.username!!, AppManager.authTokenData!!)
        lifecycleScope.launch {
            chatViewModel.state.onEach {
                when (it) {
                    is MessagesState.Success -> {
                        if (it.messages.isEmpty()) {
                            recyclerView.isVisible = false
                            progressBar.isVisible = false
                            textInfo.isVisible = true
                            textInfo.text = resources.getString(R.string.no_message)
                        } else {
                            recyclerView.smoothScrollToPosition(0)
                            recyclerView.isVisible = true
                            progressBar.isVisible = false
                            textInfo.isVisible = false

                            val result = mutableListOf<Message>()
                            for (message in it.messages) {
                                if (message.to == companion || message.from == companion) {
                                    result.add(message)
                                }
                            }

                            recyclerView.adapter = ChatAdapter(result, me)
                        }
                    }
                    is MessagesState.Error -> {
                        Toast.makeText(requireActivity(), it.error.toString(), Toast.LENGTH_SHORT).show()
                        progressBar.isVisible = false
                        recyclerView.isVisible = false
                        textInfo.isVisible = true
                        textInfo.text = resources.getString(R.string.chat_error)
                    }
                    else -> {}
                }
            }.stateIn(this)
        }
    }
}