package ru.ok.itmo.example.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentChatBinding
import ru.ok.itmo.example.databinding.FragmentEnterBinding
import ru.ok.itmo.example.domain.UseCase
import ru.ok.itmo.example.enter.LoginViewModel
import ru.ok.itmo.example.retrofit.RetrofitProvider
import ru.ok.itmo.example.retrofit.regApi

class ChatFragment : Fragment(R.layout.fragment_chat)  {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnClickListener {
            try {
                MainScope().launch {
                    viewModel.logout(viewModel.token.toString())
                }
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}

