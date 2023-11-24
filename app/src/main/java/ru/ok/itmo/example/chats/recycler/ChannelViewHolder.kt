package ru.ok.itmo.example.chats.recycler

import android.view.View
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.view.ChatItem

class ChannelViewHolder(view: View) : ChatViewHolder(view) {
    private val lastMessageAvatar = view.findViewById<AvatarView>(R.id.last_message_avatar)
    override fun bind(item: ChatItem) {
        setText(item)
        avatar.loadImage(R.drawable.channel_avatar_placeholder)
        lastMessageAvatar.loadImage(R.drawable.avatar_placeholder)
    }
}