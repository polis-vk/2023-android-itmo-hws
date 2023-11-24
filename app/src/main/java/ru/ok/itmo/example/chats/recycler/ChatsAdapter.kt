package ru.ok.itmo.example.chats.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.view.ChatItem
import ru.ok.itmo.example.chats.view.ChatType

class ChatsAdapter(private val list: List<ChatItem>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = when (viewType) {
            ChatType.TWO_USERS_CHAT.ordinal -> R.layout.item_chat
            else -> R.layout.item_chat_channel
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return when (viewType) {
            ChatType.TWO_USERS_CHAT.ordinal -> ChatViewHolder(view)
            else -> ChannelViewHolder(view)
        }
    }

    override fun getItemCount(): Int = list.size
    override fun getItemViewType(position: Int): Int = list[position].chatType.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ChannelViewHolder) {
            holder.bind(list[position])
        } else if (holder is ChatViewHolder) {
            holder.bind(list[position])
        }
    }
}
