package ru.ok.itmo.example.chatsPackage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.CustomView.AvatarView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.databinding.ChatCardBinding
import ru.ok.itmo.example.messages.OnChatClickListener

class PagingAdapter(val chatClickListener: OnChatClickListener) : PagingDataAdapter<Channel, PagingAdapter.Holder>(MyDiffUtils()) {

    class MyDiffUtils : DiffUtil.ItemCallback<Channel>() {
        override fun areItemsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Channel, newItem: Channel): Boolean {
            return oldItem == newItem
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view){
        private val channelCardPreview: AvatarView = view.findViewById(R.id.channel_avatar)
        var binding = ChatCardBinding.bind(view)
        fun bind(chat: Channel) = with(binding){
            if (chat.lastMessageText == null){
                channelCardPreview.has_image = true
            }
            channelCardPreview.setInitials(chat.name)
            binding.channelName.text = chat.name
            binding.channelPreview.text = chat.lastMessageText.toString()
            itemView.setOnClickListener {
                chatClickListener.onChatClicked(chat)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_card, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}
