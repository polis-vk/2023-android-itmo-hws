package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.data.AccountStorage
import ru.ok.itmo.tamtam.data.AvatarGenerator
import ru.ok.itmo.tamtam.databinding.FragmentChatBinding
import ru.ok.itmo.tamtam.presentation.rv.adapter.FooterAdapter
import ru.ok.itmo.tamtam.presentation.rv.adapter.MessageAdapter
import ru.ok.itmo.tamtam.presentation.stateholder.ChatState
import ru.ok.itmo.tamtam.presentation.stateholder.ChatViewModel
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import java.io.File
import javax.inject.Inject

class ChatFragment : FragmentWithBinding<FragmentChatBinding>(FragmentChatBinding::inflate) {
    @Inject
    lateinit var avatarGenerator: AvatarGenerator

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var accountStorage: AccountStorage

    lateinit var messageAdapter: MessageAdapter

    private val args: ChatFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
        messageAdapter = MessageAdapter(accountStorage.login!!)
        chatViewModel =
            ViewModelProvider(this, viewModelFactory)[ChatViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseParams()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupRecyclerView()
        observePaging()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launch {
            chatViewModel.chatState.collect {
                when (val state = it) {
                    is ChatState.Idle -> {
                        binding.toolbar.title = state.chat.name
                        setupAvatar(state.chat.name)
                    }

                    ChatState.Init -> {}
                }
            }
        }
    }

    private fun observePaging() {
        lifecycleScope.launch {
            if (chatViewModel.chatState.value !is ChatState.Init) {
                chatViewModel.getPagingMessages().collect {
                    messageAdapter.submitData(it)
                }
            }
        }
    }

    private fun parseParams() {
        lifecycleScope.launch {
            chatViewModel.init(args.chatId)
        }
    }

    private fun setupRecyclerView() {
        val footerAdapter = FooterAdapter({ messageAdapter.retry() })
        binding.messagesRV.adapter =
            messageAdapter.withLoadStateHeaderAndFooter(footerAdapter, footerAdapter)
        messageAdapter.onLoadImageByGlide = { imageView, path ->
            Glide.with(this.requireActivity())
                .load(path.toUri())
                .override(600, Target.SIZE_ORIGINAL)
                .centerInside()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView)
        }

        binding.messagesRV.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            if (messageAdapter.itemCount == 0) {
                binding.messagesRV.visibility = View.INVISIBLE
            } else {
                binding.messagesRV.visibility = View.VISIBLE
            }
        }

    }

    private fun setupAvatar(name: String) {
        Glide.with(this.requireActivity())
            .load(
                File(
                    this.requireContext().cacheDir,
                    avatarGenerator.getPathToAvatarForName(name)
                )
            )
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    binding.toolbar.menu.findItem(R.id.face).icon = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.messageET.addTextChangedListener {
            if (it != null) {
                if (it.isNotEmpty()) {
                    binding.sendBtn.visibility = View.VISIBLE
                    binding.attachBtn.visibility = View.GONE
                    binding.voiceBtn.visibility = View.GONE
                    chatViewModel.sendStartTyping()
                } else {
                    binding.sendBtn.visibility = View.GONE
                    binding.attachBtn.visibility = View.VISIBLE
                    binding.voiceBtn.visibility = View.VISIBLE
                }
            }
        }
        binding.sendBtn.setOnClickListener {
            lifecycleScope.launch {
                val textValue = binding.messageET.text.toString()
                binding.messageET.text.clear()
                chatViewModel.sendMessage(textValue)
                delay(200)
                messageAdapter.refresh()
                delay(100)
                binding.messagesRV.smoothScrollToPosition(messageAdapter.itemCount)
            }

        }
    }
}