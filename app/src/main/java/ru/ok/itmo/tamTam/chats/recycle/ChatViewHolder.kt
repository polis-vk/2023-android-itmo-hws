package ru.ok.itmo.tamTam.chats.recycle

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamTam.R
import ru.ok.itmo.tamTam.chats.models.ChatPreview

class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(chat: ChatPreview) {
        itemView.findViewById<TextView>(R.id.messageTextView).text = chat.textMessage
        itemView.findViewById<TextView>(R.id.senderNameTextView).text = chat.title
        itemView.findViewById<TextView>(R.id.time).text = chat.time
    }
}