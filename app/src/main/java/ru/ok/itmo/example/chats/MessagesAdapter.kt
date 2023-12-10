package ru.ok.itmo.example.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.data.Message

class MessagesAdapter(
    private val messages: List<Message>
) : RecyclerView.Adapter<MessagesAdapter.MessageHolder>() {

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.textName)
        private val messageText = view.findViewById<TextView>(R.id.textMessage)

        fun bind(message: Message) {
            name.text = message.to
            var text: String = message.data.Text?.text ?: "img"
            if (text.length > 20) {
                text = text.substring(0, 17) + "..."
            }
            messageText.text = text
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageHolder {
        val holder = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_container_recent_conversation, viewGroup, false)

        return MessageHolder(holder)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) = holder.bind(messages[position])

    override fun getItemCount(): Int = messages.size
}