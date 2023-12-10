package ru.ok.itmo.example

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.example.chats.ChatsState
import ru.ok.itmo.example.chats.ChatsViewModel
import ru.ok.itmo.example.chats.MessagesAdapter
import ru.ok.itmo.example.data.Message
import ru.ok.itmo.example.login.LoginViewModel

class MainFragment : Fragment(R.layout.main_fragment) {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val chatViewModel by viewModels<ChatsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.chats)
        val textInfo = view.findViewById<TextView>(R.id.textInfo)
        progressBar.isVisible = true

        view.findViewById<Button>(R.id.signOut).setOnClickListener {
            loginViewModel.logout()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }

        chatViewModel.getChats(AppManager.username!!, AppManager.authTokenData!!)
        lifecycleScope.launch {
            chatViewModel.state.onEach {
                when (it) {
                    is ChatsState.Success -> {
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
                            val messageSet = mutableSetOf<String>()
                            val result = mutableListOf<Message>()
                            for (message in it.messages) {
                                var first = message.from
                                var second = message.to
                                if (first < second) {
                                    first = second
                                    second = message.from
                                }
                                val res = first + second
                                if (!messageSet.contains(res)) {
                                    messageSet.add(res)
                                    result.add(message)
                                }
                            }
                            recyclerView.adapter = MessagesAdapter(result)
                        }
                    }
                    is ChatsState.Error -> {
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