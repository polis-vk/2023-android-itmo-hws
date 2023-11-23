package ru.ok.itmo.tamtam.presentation.rv.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ok.itmo.tamtam.databinding.ChatItemRvBinding
import ru.ok.itmo.tamtam.domain.model.Chat
import ru.ok.itmo.tamtam.presentation.rv.callback.ChatDiffCallback
import ru.ok.itmo.tamtam.utils.convertTimestampForChat

class ChatAdapter : ListAdapter<Chat, ViewHolder>(ChatDiffCallback()) {
    inner class ChatViewHolder(val binding: ChatItemRvBinding) :
        ViewHolder(binding.root)

    var onLoadImageByGlide: ((ImageView, String) -> Unit)? = null
    var onClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = ChatItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ChatViewHolder(binding)
            }

            else -> throw RuntimeException("Bad view type")
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ChatViewHolder -> {
                val chat = getItem(position)
                val binding = holder.binding
                binding.nameTW.text = chat.name
                binding.timeLastMessageTW.text = convertTimestampForChat(chat.lastMessage!!.time)
                binding.avatarLastMessageIW.visibility = View.GONE

                if (chat.countNewMessage == 0) {
                    binding.countBadgeTW.visibility = View.GONE
                } else {
                    binding.countBadgeTW.visibility = View.VISIBLE
                    binding.countBadgeTW.text = chat.countNewMessage.toString()
                }

                if (chat.typingUsers.isEmpty()) {
                    binding.lastMessageTW.text = chat.lastMessage.messageText
                    onLoadImageByGlide?.invoke(binding.avatarLastMessageIW, chat.lastMessage.from)
                    if (chat.isChannel) {
                        binding.avatarLastMessageIW.visibility = View.VISIBLE
                    }
                } else {
                    binding.lastMessageTW.text = "${chat.typingUsers[0]} печатает.."
                }

                onLoadImageByGlide?.invoke(binding.avatarIW, chat.name)

                binding.chatCL.setOnClickListener {
                    onClick?.invoke(chat.name)
                }
                binding.chatCL.setOnLongClickListener {
                    true
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
    }
}