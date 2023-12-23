package ru.ok.itmo.example.chats

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.retrofit.models.Message
import ru.ok.itmo.example.chats.retrofit.models.getChat
import ru.ok.itmo.example.chats.retrofit.models.getShortText
import ru.ok.itmo.example.custom_view.AvatarCustomView

class ChatsRecyclerAdapter(
    private val lastMessages: List<Message>,
    private val userLogin: String,
    private val backgroundColors: List<Int>,
    private val chatAvatar: Bitmap?,
    private val onClick: (message: Message) -> Unit
) : RecyclerView.Adapter<ChatsRecyclerAdapter.MessageHolder>() {

    private var lastColor = 0

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageHolder {
        val holder = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recycler_view_container, viewGroup, false)

        return MessageHolder(holder)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(lastMessages[position], userLogin, getNewColor(), chatAvatar, onClick)
    }

    override fun getItemCount(): Int {
        return lastMessages.size
    }

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val nameField = view.findViewById<TextView>(R.id.name)
        private val messageField = view.findViewById<TextView>(R.id.message)
        private val avatar = view.findViewById<AvatarCustomView>(R.id.avatar)

        private val root = view

        fun bind(
            message: Message,
            userLogin: String,
            backgroundColor: Int,
            chatAvatar: Bitmap?,
            onClick: (message: Message) -> Unit
        ) {
            val chatName = message.getChat(userLogin)
            nameField.text = chatName
            messageField.text = message.getShortText()

            avatar.setCvBackgroundColor(backgroundColor)
            if (chatAvatar != null) {
                avatar.setImage(chatAvatar)
            } else {
                avatar.setText(chatName)
            }

            root.setOnClickListener { onClick(message) }
        }
    }

    private fun getNewColor(): Int {
        if (lastColor == backgroundColors.size) lastColor = 0
        return backgroundColors[lastColor++]
    }
}