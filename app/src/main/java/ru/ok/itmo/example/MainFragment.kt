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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.ok.itmo.example.messages.MessagesState
import ru.ok.itmo.example.messages.MessagesViewModel
import ru.ok.itmo.example.messages.MessagesAdapter
import ru.ok.itmo.example.data.Message
import ru.ok.itmo.example.login.LoginViewModel

class MainFragment : Fragment(R.layout.main_fragment) {

    private lateinit var loginViewModel: LoginViewModel
    private val messageViewModel by viewModels<MessagesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
        val recyclerView = view.findViewById<RecyclerView>(R.id.chats)
        val textInfo = view.findViewById<TextView>(R.id.textInfo)
        progressBar.isVisible = true

        loginViewModel = ViewModelProvider(requireActivity())[LoginViewModel::class.java]
        view.findViewById<Button>(R.id.signOut).setOnClickListener {
            loginViewModel.logout()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, StartedFragment())
                .commit()
        }

        messageViewModel.run(AppManager.username!!, AppManager.authTokenData!!)
        lifecycleScope.launch {
            messageViewModel.state.onEach {
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
                            recyclerView.adapter = MessagesAdapter(result,
                                {x -> onMessageListener(x)}, AppManager.username!!)
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

    fun onMessageListener(message: Message) {
        if (message.from == AppManager.username) {
            AppManager.chatOpen = message.to
        } else {
            AppManager.chatOpen = message.from
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ChatFragment())
            .commit()
    }
}