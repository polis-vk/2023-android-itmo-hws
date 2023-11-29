package ru.ok.itmo.tamtam.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.ok.itmo.tamtam.App
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.data.AccountStorage
import ru.ok.itmo.tamtam.databinding.FragmentChatBinding
import ru.ok.itmo.tamtam.presentation.rv.adapter.HeaderFooterAdapter
import ru.ok.itmo.tamtam.presentation.rv.adapter.MessageAdapter
import ru.ok.itmo.tamtam.presentation.stateholder.ChatState
import ru.ok.itmo.tamtam.presentation.stateholder.ChatViewModel
import ru.ok.itmo.tamtam.presentation.view.AvatarImageView
import ru.ok.itmo.tamtam.utils.FragmentWithBinding
import ru.ok.itmo.tamtam.utils.getThemeColor
import ru.ok.itmo.tamtam.utils.observeNotifications
import ru.ok.itmo.tamtam.utils.setStatusBarTextDark
import javax.inject.Inject
import ru.ok.itmo.tamtam.Constants.API_AVATAR_URL

class ChatFragment : FragmentWithBinding<FragmentChatBinding>(FragmentChatBinding::inflate) {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChatViewModel::class.java]
    }

    @Inject
    lateinit var accountStorage: AccountStorage

    private val messageAdapter: MessageAdapter by lazy { MessageAdapter(accountStorage.login!!) }

    private val args: ChatFragmentArgs by navArgs()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as App).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            parseParams()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusBar()
        setupListeners()
        setupRecyclerView()
        observeState()
        this.requireContext()
            .observeNotifications(viewLifecycleOwner.lifecycleScope, chatViewModel.notifications)
    }

    private fun parseParams() {
        chatViewModel.init(args.chatName)
    }


    private val pagingMutex = Mutex()
    private fun observePaging() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (pagingMutex.isLocked) return@launch
            pagingMutex.withLock {
                chatViewModel.pagingData?.collect {
                    messageAdapter.submitData(it)
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewModel.chatState.collect {
                when (val state = it) {
                    is ChatState.Idle -> {
                        binding.toolbar.title = state.chat.name
                        setupAvatar(state.chat.name)
                        binding.loadingPB.visibility = View.INVISIBLE
                        binding.messageInputLL.visibility = View.VISIBLE
                        if (state.chat.lastMessageId == 0) {
                            binding.messagesRV.visibility = View.INVISIBLE
                            binding.noMessageI.noMessageLL.visibility = View.VISIBLE

                        } else {
                            binding.noMessageI.noMessageLL.visibility = View.INVISIBLE
                            binding.messagesRV.visibility = View.VISIBLE
                            observePaging()
                            messageAdapter.refresh()
                        }
                    }

                    ChatState.Loading -> {
                        binding.loadingPB.visibility = View.VISIBLE
                        binding.messageInputLL.visibility = View.INVISIBLE
                        binding.messagesRV.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    private fun setupAvatar(name: String) {
        val linearLayout = binding.toolbar.menu.findItem(R.id.face).actionView as LinearLayout
        val avatarImageView = linearLayout.findViewById<AvatarImageView>(R.id.avatarIW)
        avatarImageView.setText(name)
        Glide.with(this.requireActivity())
            .load(String.format(API_AVATAR_URL, name))
            .placeholder(avatarImageView.drawable)
            .centerCrop()
            .into(avatarImageView)

        binding.toolbar.menu.findItem(R.id.face).isVisible = true
    }

    private fun setupRecyclerView() {
        binding.messagesRV.adapter = messageAdapter.withLoadStateHeaderAndFooter(
            HeaderFooterAdapter({ messageAdapter.retry() }),
            HeaderFooterAdapter({ messageAdapter.retry() })
        )
        messageAdapter.onLoadImageByGlide = { imageView, path ->
            Glide.with(this.requireActivity())
                .load(path.toUri())
                .apply(RequestOptions().placeholder(R.drawable.placeholder))
                .transition(DrawableTransitionOptions.withCrossFade())
                .override(600, Target.SIZE_ORIGINAL)
                .centerInside()
                .into(imageView)
        }
        messageAdapter.addLoadStateListener {
        }
        binding.messagesRV.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            val layoutManager = binding.messagesRV.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

            if (lastVisibleItem > totalItemCount - 3) {
                binding.messagesRV.smoothScrollToPosition(binding.messagesRV.getBottom());
            }
        }
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
                binding.messagesRV.smoothScrollToPosition(messageAdapter.itemCount)
            }
        }
    }

    private fun setupStatusBar() {
        requireActivity().window.statusBarColor =
            requireContext().getThemeColor(androidx.appcompat.R.attr.colorPrimary)
        requireActivity().setStatusBarTextDark(true)
    }
}