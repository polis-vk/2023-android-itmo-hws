package ru.ok.itmo.example.chats.recycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.view.ChatItem
import ru.ok.itmo.example.chats.view.ImageData
import ru.ok.itmo.example.chats.view.TextData

open class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val time: TextView = view.findViewById(R.id.time)
    private val userName: TextView = view.findViewById(R.id.chat_name)
    private val lastMessage: TextView = view.findViewById(R.id.last_message)
    protected val avatar: AvatarView = view.findViewById(R.id.avatar)

    open fun bind(item: ChatItem) {
        setText(item)
        avatar.loadImage(R.drawable.avatar_placeholder)
    }

    protected fun setText(item: ChatItem) {
        time.text = item.time
        userName.text = item.chatName
        lastMessage.text = when (item.lastMessageData) {
            is TextData -> item.lastMessageData.text
            is ImageData -> itemView.resources.getString(R.string.photo)
            else -> itemView.resources.getString(R.string.three_dots)
        }
    }
}