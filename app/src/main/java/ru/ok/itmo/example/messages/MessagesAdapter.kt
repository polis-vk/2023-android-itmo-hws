package ru.ok.itmo.example.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.data.Message
import ru.ok.itmo.example.utils.CustomAvatarView

class MessagesAdapter(
    private val messages: List<Message>,
    private val onclick: (message: Message) -> Unit,
    val username: String
) : RecyclerView.Adapter<MessagesAdapter.MessageHolder>() {

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name = view.findViewById<TextView>(R.id.textName)
        private val messageText = view.findViewById<TextView>(R.id.textMessage)
        private val image = view.findViewById<CustomAvatarView>(R.id.imageChat)

        private val root = view

        fun bind(message: Message, onclick: (message: Message) -> Unit, username: String) {
            name.text = message.to
            var text: String = message.data.Text?.text ?: "img"
            if (text.length > 20) {
                text = text.substring(0, 17) + "..."
            }
            if (username == message.to) {
                image.setText(message.from)
            } else {
                image.setText(message.to)
            }

            messageText.text = text
            root.setOnClickListener{
                onclick(message)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageHolder {
        val holder = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_container_recent_conversation, viewGroup, false)

        return MessageHolder(holder)
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) = holder.bind(messages[position], onclick, username)

    override fun getItemCount(): Int = messages.size
}