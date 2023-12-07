package ru.ok.itmo.example.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.dataBase.MessageEntity
import ru.ok.itmo.example.databinding.MessageCardBinding

class messageAdapter() : ListAdapter<MessageEntity, messageAdapter.Holder>(MyDiffUtils()) {

    class MyDiffUtils : DiffUtil.ItemCallback<MessageEntity>(){

        override fun areItemsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem.messageText == newItem.messageText
        }

        override fun areContentsTheSame(oldItem: MessageEntity, newItem: MessageEntity): Boolean {
            return oldItem == newItem
        }

    }

    class Holder(view: View) : RecyclerView.ViewHolder(view){
        var binding = MessageCardBinding.bind(view)
        fun bind(message: MessageEntity) = with(binding){
            binding.messageFrom.text = message.from
            binding.messageTime.text = message.time.toString()
            binding.messageText.text = message.messageText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_card, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

}
