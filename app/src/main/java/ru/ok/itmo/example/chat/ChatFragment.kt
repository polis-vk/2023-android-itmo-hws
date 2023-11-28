package ru.ok.itmo.example.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ru.ok.itmo.example.DaApplication
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.FragmentChatBinding
import ru.ok.itmo.example.factories.ChatFactory
import ru.ok.itmo.example.recyclerview.MessageRecyclerViewAdapter
import ru.ok.itmo.example.recyclerview.PaginationScrollListener
import ru.ok.itmo.example.repositories.InvalidDataException

class ChatFragment : Fragment() {
    private var number: String? = "1"
    private lateinit var binding: FragmentChatBinding
    private var isLoading = false
    private var isLastPage = false
    private val viewModel: ChatViewModel by viewModels {
        ChatFactory((requireActivity().application as DaApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            number = it.getString("id")
        }
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "${number}"
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.action_chatFragment_to_listFragment)
        }
        val linearLayoutManager = LinearLayoutManager(this@ChatFragment.context)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.reverseLayout = true

        getChannel(linearLayoutManager)

        binding.recyclerView.addOnScrollListener(object :
            PaginationScrollListener(linearLayoutManager) {

            override fun loadMoreItems() {
                viewModel.isLast(number.toString())
                viewModel.isLast.observe(viewLifecycleOwner) {
                    isLoading = true
                    isLastPage = it
                    getChannel(linearLayoutManager)
                }
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

        })
        binding.sendButton.setOnClickListener {
            viewModel.sendMessage(binding.messageTextEdit.text.toString(), number.toString())
            binding.messageTextEdit.setText("")
            getChannel(linearLayoutManager)
        }

    }

    fun getChannel(linearLayoutManager: LinearLayoutManager) {
        viewModel.getChannel(number.toString())

        viewModel.messages.observe(viewLifecycleOwner) {
            try {
                if (it.isEmpty()) {
                    binding.shimmerView.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.noMessagesLayout.visibility = View.VISIBLE
                } else {
                    binding.shimmerView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.noMessagesLayout.visibility = View.GONE
                    binding.recyclerView.apply {
                        layoutManager = linearLayoutManager
                        adapter = MessageRecyclerViewAdapter(it)
                    }
                    isLoading = false
                }
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