package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.data.AvatarGenerator
import ru.ok.itmo.tamtam.data.repository.AuthRepository
import ru.ok.itmo.tamtam.databinding.FragmentChatsBinding
import ru.ok.itmo.tamtam.presentation.rv.adapter.ChatAdapter
import ru.ok.itmo.tamtam.presentation.stateholder.ChatsState
import ru.ok.itmo.tamtam.presentation.stateholder.ChatsViewModel
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import ru.ok.itmo.tamtam.utils.OnBackPressed
import ru.ok.itmo.tamtam.utils.getThemeColor
import ru.ok.itmo.tamtam.utils.observeNotifications
import ru.ok.itmo.tamtam.utils.setStatusBarTextDark
import java.io.File
import javax.inject.Inject

class ChatsFragment : FragmentWithBinding<FragmentChatsBinding>(FragmentChatsBinding::inflate) {
    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val chatsViewModel: ChatsViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatsViewModel::class.java]
    }
    private val chatAdapter: ChatAdapter by lazy { ChatAdapter() }

    @Inject
    lateinit var avatarGenerator: AvatarGenerator

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        postponeEnterTransition()
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        setupOnBackPressedCallback()
        setupRecyclerView()
        observeChatsState()
        this.requireContext()
            .observeNotifications(viewLifecycleOwner.lifecycleScope, chatsViewModel.notifications)
        lifecycleScope.launch {
            delay(100)
            startPostponedEnterTransition()
        }
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(
                ChatsFragmentDirections.actionChatsFragmentToContactsFragment()
            )
        }
    }

    private fun observeChatsState() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatsViewModel.chatsState.collect {
                when (it) {
                    is ChatsState.Idle -> {
                        chatAdapter.submitList(it.chats)
                        binding.chatsRV.visibility = View.VISIBLE
                        binding.loadingPB.visibility = View.INVISIBLE
                        binding.noContactTV.visibility =
                            if (it.chats.isEmpty()) View.VISIBLE else View.INVISIBLE
                    }

                    ChatsState.Loading -> {
                        binding.chatsRV.visibility = View.INVISIBLE
                        binding.loadingPB.visibility = View.VISIBLE
                    }
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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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

    private fun setupStatusBar() {
        requireActivity().window.statusBarColor =
            requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary)
        requireActivity().setStatusBarTextDark(true)
    }

    private fun setupOnBackPressedCallback() {
        (requireActivity() as? OnBackPressed)?.addCustomOnBackPressed(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                authRepository.logout()
            }
        }
    }
}