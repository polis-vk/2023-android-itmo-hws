package ru.ok.itmo.tamtam.presentation.rv.callback

import androidx.recyclerview.widget.DiffUtil
import ru.ok.itmo.tamtam.domain.model.Message

class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }
}