package ru.ok.itmo.tamTam.chats

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import ru.ok.itmo.tamTam.R
import ru.ok.itmo.tamTam.chats.recycle.ChatAdapter

class ChatsFragment : Fragment(R.layout.chats_fragment) {

    private lateinit var dataList: RecyclerView
    private lateinit var toolbar: MaterialToolbar
    private val chatsViewModel: ChatsViewModel by viewModels()

    override fun onStart() {
        super.onStart()
        chatsViewModel.messages()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)

        toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        dataList = view.findViewById(R.id.recyclerView)
        dataList.layoutManager = LinearLayoutManager(
            requireContext(), LinearLayoutManager.VERTICAL,
            false
        )
        chatsViewModel.chats.observe(viewLifecycleOwner) { result ->
            val chats = result.getOrNull()
            if (chats != null) {
                dataList.adapter = ChatAdapter(chats)
            }

        }
    }
}
