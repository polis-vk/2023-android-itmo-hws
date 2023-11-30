package ru.ok.itmo.tamtam.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.presentation.AvatarView

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val chatName = view.findViewById<TextView>(R.id.chat_name)
    private val chatAvatar: AvatarView = view.findViewById<AvatarView>(R.id.chat_avatar)

    fun bind(chatName: String, image: String?, onClick: () -> Unit) {
        this.chatName.text = chatName


        val split = chatName.split(" ")
        if (split.size > 1) {
            this.chatAvatar.setInitials(
                split[0][0].toString().uppercase() + split[1][0].toString().uppercase()
            )
        } else {
            this.chatAvatar.setInitials(split[0][0].toString().uppercase())
        }

        this.chatAvatar.setImage(image)

        itemView.setOnClickListener {
            onClick()
        }
    }
}