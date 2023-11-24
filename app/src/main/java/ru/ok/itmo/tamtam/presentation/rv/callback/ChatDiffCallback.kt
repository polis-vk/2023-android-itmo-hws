package ru.ok.itmo.tamtam.presentation.rv.callback

import androidx.recyclerview.widget.DiffUtil
import ru.ok.itmo.tamtam.domain.model.Chat

class ChatDiffCallback : DiffUtil.ItemCallback<Chat>() {
    override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
        return oldItem == newItem
    }
}