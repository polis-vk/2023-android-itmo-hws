package ru.ok.itmo.example.messages

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chat.ChatFragment
import ru.ok.itmo.example.chatsPackage.Channel
import ru.ok.itmo.example.dataBase.MainDb
import ru.ok.itmo.example.databinding.ChannelFragmentBinding

class ChannelFragment(val chat : Channel) : Fragment(R.layout.channel_fragment) {
    private var _binding: ChannelFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: MainDb
    private lateinit var adapterFragment: messageAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChannelFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<ProgressBar>(R.id.LoadingChannel).isVisible = true
        database = MainDb.getDatabase(requireContext())
        adapterFragment = messageAdapter()
        binding.rcViewChannel.layoutManager = LinearLayoutManager(context)
        binding.rcViewChannel.adapter = adapterFragment


        view.findViewById<ProgressBar>(R.id.LoadingChannel).isVisible = false
        CoroutineScope(Dispatchers.IO).launch {
            val messages = database.dao().getMessagesByChat(chat.name)
                withContext(Dispatchers.Main) {
                messages.collect { messageList ->
                    adapterFragment.submitList(messageList)
                }
            }
        }

        view.findViewById<MaterialToolbar>(R.id.toolbarChannel).setOnClickListener{
            val fragmentManager = requireActivity().supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            val newFragment = ChatFragment()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.commit()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
