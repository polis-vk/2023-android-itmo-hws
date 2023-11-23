package ru.ok.itmo.tamtam.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.presentation.viewholders.ChatViewHolder
import ru.ok.itmo.tamtam.presentation.viewmodels.ChatsViewModel

class ChatAdapter(
    private val context: Context,
    private val chats: List<String>,
    private val viewModel: ChatsViewModel
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        ChatViewHolder(LayoutInflater.from(context).inflate(R.layout.chat_list_item, parent, false))

    override fun getItemCount(): Int = chats.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ChatViewHolder).bind(chats[position]) {
            viewModel.onChatClick(chats[position])
        }
    }
}