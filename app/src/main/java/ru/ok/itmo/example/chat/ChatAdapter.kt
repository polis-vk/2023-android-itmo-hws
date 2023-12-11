package ru.ok.itmo.example.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.data.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val messages: List<Message>,
    private val username: String
) : RecyclerView.Adapter<ChatAdapter.ChatHolder>() {

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    class ChatHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val messageText = view.findViewById<TextView>(R.id.textMessage)
        private val time = view.findViewById<TextView>(R.id.time)

        @SuppressLint("SimpleDateFormat")
        fun bind(message: Message) {
            messageText.text = message.data.Text?.text ?: "img"
            time.text = SimpleDateFormat("hh:mm").format(Date(message.time!!))
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ChatHolder {
        val holder = when (viewType) {
            VIEW_TYPE_SENT  -> LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_container_send_message, viewGroup, false)
            else  -> LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_container_received_message, viewGroup, false)
        }

        return ChatHolder(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].to == username) {
            VIEW_TYPE_RECEIVED
        } else {
            VIEW_TYPE_SENT
        }
    }
    override fun onBindViewHolder(holder: ChatHolder, position: Int) = holder.bind(messages[position])

    override fun getItemCount(): Int = messages.size
}