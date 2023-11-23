package ru.ok.itmo.tamtam.ui.chats

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.domain.model.ChatsViewModel

class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val authorNameView: TextView = itemView.findViewById(R.id.channel_author)
    private val lastMessageView: TextView = itemView.findViewById(R.id.channel_last_message)
    private val timeLastMessageView: TextView = itemView.findViewById(R.id.channel_message_time)

    fun bind(item: ChatsViewModel.ChatInfo) {
        authorNameView.text = item.author
        lastMessageView.text = item.lastMessage
        timeLastMessageView.text = item.time
    }
}
