package ru.ok.itmo.tamTam.chats.recycle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamTam.R
import ru.ok.itmo.tamTam.chats.models.ChatPreview

class ChatAdapter( private val items: MutableList<ChatPreview>) :
    RecyclerView.Adapter<ChatViewHolder>() {
    override fun onCreateViewHolder (parent: ViewGroup, viewType: Int): ChatViewHolder
    {
        val view = LayoutInflater.from(parent. context).inflate(
            R.layout.chat_preview,
            parent,
            false
        )
        return ChatViewHolder(view)
    }
    override fun onBindViewHolder (holder: ChatViewHolder, position: Int) {
        holder.bind( items[position])
    }
    override fun getItemCount () = items.size

}