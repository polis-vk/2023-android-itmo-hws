package ru.ok.itmo.example.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chatsPackage.Channel
import ru.ok.itmo.example.chatsPackage.ChatPagingSource
import ru.ok.itmo.example.chatsPackage.ChatUseCase
import ru.ok.itmo.example.chatsPackage.PagingAdapter
import ru.ok.itmo.example.chatsPackage.chatApi
import ru.ok.itmo.example.dataBase.MainDb
import ru.ok.itmo.example.databinding.FragmentChatBinding
import ru.ok.itmo.example.messages.ChannelFragment
import ru.ok.itmo.example.messages.OnChatClickListener
import ru.ok.itmo.example.retrofit.RetrofitProvider


class ChatFragment : Fragment(R.layout.fragment_chat), OnChatClickListener {
    private var _bindingFragment: FragmentChatBinding? = null
    private val binding get() = _bindingFragment!!
   // private lateinit var adapterFragment: Adapter
    private lateinit var database: MainDb
    private lateinit var pagingAdapter: PagingAdapter

    private val chatUseCase by lazy {
        val retrofit = RetrofitProvider.retrofit
        val chatApiInter = chatApi.provideRequestApi(retrofit)
        ChatUseCase(chatApiInter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindingFragment = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = MainDb.getDatabase(requireContext())
      //  adapterFragment = Adapter(this)
        binding.rcView.layoutManager = LinearLayoutManager(context)
       // binding.rcView.adapter = adapterFragment

/*
так я пытался загружать чаты до попытки сделать подгрузку через пагинацию
CoroutineScope(Dispatchers.IO).launch {
            val chatState = chatUseCase.myGetAllChannels()
            withContext(Dispatchers.Main) {
                when (chatState) {
                    is ChatState.Success -> {
                        chatState.chats.forEach {
                            val list = chatUseCase.myGetChannelAllMessages(it.name)
                            val messageEntityList = if (!list.isNullOrEmpty()) {
                                list.map { message ->
                                    MessageEntity(
                                        id = message.id.toInt(),
                                        chatName = it.name,
                                        from = message.from,
                                        to = message.to,
                                        time = message.time,
                                        messageText = message.data.Text?.text
                                    )
                                }
                            } else {
                                // здесь можно прописать картинку с анимациями
                                emptyList()
                            }

                            CoroutineScope(Dispatchers.IO).launch {
                                database.dao().insertListMessages(messageEntityList)
                            }
                        }
                        view.findViewById<ProgressBar>(R.id.LoadingChat).isVisible = false
                        adapterFragment.submitList(chatState.chats)
                    }
                    is ChatState.Error -> {
                        val a = 5
                    }
                    else -> {}
                }
            }
        }*/

        pagingAdapter = PagingAdapter(this)

        binding.rcView.layoutManager = LinearLayoutManager(context)
        binding.rcView.adapter = pagingAdapter


        val pager = Pager(
            config = PagingConfig(pageSize = 6, enablePlaceholders = true),
            pagingSourceFactory = { ChatPagingSource(chatUseCase) }
        ).flow
            .cachedIn(lifecycleScope)

        lifecycleScope.launch {
            pager.collectLatest { pagingData ->
                pagingAdapter.submitData(pagingData)
            }
        }

        pagingAdapter.addLoadStateListener { loadState ->
            binding.loadingChat.isVisible = loadState.refresh is LoadState.Loading
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingFragment = null
    }

    override fun onChatClicked(chat: Channel) {
        replaceFragmentWithNewLayout(chat)
    }

    private fun replaceFragmentWithNewLayout(chat: Channel) {
        val fragmentManager = requireActivity().supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val newFragment = ChannelFragment(chat)
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.commit()
    }
}

