package ru.ok.itmo.example.chat

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentChatBinding
import ru.ok.itmo.example.repositories.InvalidDataException

class ChatFragment : Fragment() {

    private lateinit var binding: FragmentChatBinding
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        binding.toolbar.title = "1"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_chatFragment_to_listFragment)
        }

        get1ch()

        binding.sendButton.setOnClickListener {
            viewModel.sendMessage(binding.messageTextEdit.text.toString())
            get1ch()
        }

    }

    fun get1ch() {
        viewModel.get1ch()
        viewModel.messages.observe(viewLifecycleOwner) {
            try {
                if (it.isEmpty()) {
                    binding.shimmerView.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.noMessagesLayout.visibility = View.VISIBLE
                    Log.i(
                        "Empty",
                        "1"
                    )
                } else {
                    binding.shimmerView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noMessagesLayout.visibility = View.GONE
                    binding.recyclerView.apply {
                        layoutManager = LinearLayoutManager(this@ChatFragment.context)
                        adapter = MessageRecyclerViewAdapter(it)
                    }
                }
                Log.i(
                    "token",
                    activity?.getSharedPreferences("token_pref", MODE_PRIVATE)!!
                        .getString("token", "").toString()
                )
                Log.i(
                    "itIsEmpty",
                    it.isEmpty().toString()
                )
            } catch (e: InvalidDataException) {
                binding.shimmerView.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                binding.noMessagesLayout.visibility = View.VISIBLE
                binding.noMessagesText.text = "Ошибка при получении данных"
                binding.noMessagesDescription.text =
                    "Получены неправильные данные, повторите попытку позже"
            }
        }
    }

}