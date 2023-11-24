package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.data.AuthRepository
import ru.ok.itmo.tamtam.data.AvatarGenerator
import ru.ok.itmo.tamtam.databinding.FragmentChatsBinding
import ru.ok.itmo.tamtam.presentation.rv.adapter.ChatAdapter
import ru.ok.itmo.tamtam.presentation.stateholder.ChatsState
import ru.ok.itmo.tamtam.presentation.stateholder.ChatsViewModel
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import ru.ok.itmo.tamtam.utils.OnBackPressed
import java.io.File
import javax.inject.Inject

class ChatsFragment : FragmentWithBinding<FragmentChatsBinding>(FragmentChatsBinding::inflate) {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var chatsViewModel: ChatsViewModel
    private var chatAdapter: ChatAdapter = ChatAdapter()

    @Inject
    lateinit var avatarGenerator: AvatarGenerator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        chatsViewModel =
            ViewModelProvider(this, viewModelFactory)[ChatsViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            chatsViewModel.startLoading()
        }
        setupOnBackPressedCallback()
        setupRecyclerView()
        observeChats()
        observeChatsState()
        observeNotifications()
        lifecycleScope.launch {
            delay(100)
            startPostponedEnterTransition()
        }
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            for (notification in chatsViewModel.notifications) {
                Toast.makeText(this@ChatsFragment.requireContext(), "Ошибка соединения",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeChatsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatsViewModel.chatsState.collect {
                when (it) {
                    ChatsState.Idle -> {
                        binding.chatsRV.visibility = View.VISIBLE
                        binding.loadingPB.visibility = View.INVISIBLE
                    }

                    ChatsState.Init -> {}
                    ChatsState.Loading -> {
                        binding.chatsRV.visibility = View.INVISIBLE
                        binding.loadingPB.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun observeChats() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatsViewModel.chats.collect {
                chatAdapter.submitList(it)
                if (it.isNotEmpty()) {
                    chatsViewModel.setIdle()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.chatsRV.adapter = chatAdapter
        chatAdapter.onLoadImageByGlide = { imageView, name ->
            Glide.with(this.requireActivity())
                .load(
                    File(
                        this.requireContext().cacheDir,
                        avatarGenerator.getPathToAvatarForName(name)
                    )
                )
                .into(imageView)
        }
        chatAdapter.onClick = { chatId ->
            findNavController().navigate(
                ChatsFragmentDirections.actionChatsFragmentToChatFragment(
                    chatId
                )
            )
        }

    }

    private fun setupOnBackPressedCallback() {
        (requireActivity() as? OnBackPressed)?.addCustomOnBackPressed(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                authRepository.logout()
            }
        }
    }
}