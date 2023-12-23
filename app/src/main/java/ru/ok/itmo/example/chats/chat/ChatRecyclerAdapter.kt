package ru.ok.itmo.example.chats

import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.chats.retrofit.models.Message
import java.util.Date
import java.util.Locale

class ChatRecyclerAdapter(
    private val messages: List<Message>,
    private val userLogin: String,
) : RecyclerView.Adapter<ChatRecyclerAdapter.MessageHolder>() {

    companion object ViewType {
        const val SENT = 1
        const val RECEIVED = 2
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MessageHolder {
        val holder = if (viewType == ViewType.SENT) {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_view_sent_message, viewGroup, false)
        } else {
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.recycler_view_received_message, viewGroup, false)
        }

        return MessageHolder(holder)
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].from == userLogin) ViewType.SENT else ViewType.RECEIVED
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            private val DATE_FORMAT = SimpleDateFormat("HH:MM", Locale.ENGLISH)
        }

        private val textField = view.findViewById<TextView>(R.id.text)
        private val timeField = view.findViewById<TextView>(R.id.time)

        fun bind(message: Message) {
            textField.text = message.data!!.text!!.text
            if (message.time == null) {
                timeField.text = "--:--"
            } else {
                timeField.text = DATE_FORMAT.format(Date(message.time!!.toLong()))
            }
        }
    }
}