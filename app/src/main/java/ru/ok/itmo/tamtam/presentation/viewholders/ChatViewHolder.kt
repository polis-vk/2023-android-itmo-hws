package ru.ok.itmo.tamtam.presentation.viewholders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamtam.R

class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val chatName = view.findViewById<TextView>(R.id.chat_name)

    fun bind(chatName: String, onClick: () -> Unit) {
        this.chatName.text = chatName
        itemView.setOnClickListener {
            onClick()
        }
    }
}