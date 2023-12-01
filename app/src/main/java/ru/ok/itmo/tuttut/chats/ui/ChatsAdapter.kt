package ru.ok.itmo.tuttut.chats.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tuttut.R
import ru.ok.itmo.tuttut.chats.domain.ChatUI
import ru.ok.itmo.tuttut.messenger.ui.AvatarView

class ChatsAdapter(
    private val onClick: (ChatUI) -> Unit
) : ListAdapter<ChatUI, ChatsAdapter.ChatViewHolder>(
    object : DiffUtil.ItemCallback<ChatUI>() {
        override fun areItemsTheSame(oldItem: ChatUI, newItem: ChatUI) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: ChatUI, newItem: ChatUI) = oldItem == newItem
    }) {

    class ChatViewHolder(itemView: View, val onClick: (ChatUI) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val chatAvatar: AvatarView = itemView.findViewById(R.id.avatar)
        private val chatName: TextView = itemView.findViewById(R.id.chat_name)
        private val chatMessage: TextView = itemView.findViewById(R.id.chat_message)
        private val chatTime: TextView = itemView.findViewById(R.id.chat_time)
        private var currentChat: ChatUI? = null

        init {
            itemView.setOnClickListener {
                currentChat?.let {
                    onClick(it)
                }
            }
        }

        fun bind(chatUI: ChatUI) {
            currentChat = chatUI

            chatName.text = chatUI.name
            chatMessage.text = chatUI.lastMessage
            chatTime.text = chatUI.lastTime

            chatAvatar.clear()
            fun String.initials() = first().toString() + (getOrNull(1) ?: "")
            chatAvatar.setInitials(chatUI.name.initials())
            chatUI.avatarBitmap?.let(chatAvatar::setImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatUI = getItem(position)
        holder.bind(chatUI)
    }
}