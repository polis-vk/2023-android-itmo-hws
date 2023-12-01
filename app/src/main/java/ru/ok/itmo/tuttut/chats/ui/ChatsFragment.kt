package ru.ok.itmo.tuttut.chats.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.ok.itmo.tuttut.MainActivity
import ru.ok.itmo.tuttut.R
import ru.ok.itmo.tuttut.chats.domain.ChatsState
import ru.ok.itmo.tuttut.databinding.FragmentChatsBinding

@AndroidEntryPoint
class ChatsFragment : Fragment() {
    private val viewModel: ChatsViewModel by viewModels()

    private lateinit var binding: FragmentChatsBinding

    private fun actionBar() = (activity as MainActivity?)?.supportActionBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ChatsAdapter {
            findNavController().navigate(
                ChatsFragmentDirections.actionChatsFragmentToMessengerFragment(it.name)
            )
        }
        val recyclerView = binding.chatsRecycler
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        viewModel.getChats()
        Log.d("CHATS", "CREATED")
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chatsState.collect {
                Log.d("CHATS", it.toString())
                when (it) {
                    is ChatsState.Failure -> {
                        actionBar()?.setTitle(R.string.chats)
                        Toast.makeText(
                            context,
                            it.errorMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    ChatsState.Loading -> actionBar()?.setTitle(R.string.loading)

                    is ChatsState.Success -> {
                        actionBar()?.setTitle(R.string.chats)
                        adapter.submitList(it.chats)
                    }

                    is ChatsState.FromCache -> {
                        actionBar()?.setTitle(R.string.loading)
                        adapter.submitList(it.chats)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }
}