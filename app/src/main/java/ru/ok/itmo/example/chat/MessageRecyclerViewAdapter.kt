package ru.ok.itmo.example.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.models.Message
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class MessageRecyclerViewAdapter(private val messages: List<Message>) :
    RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        if (message.from.equals("LetItBeRickAstley")) {
            holder.recievedLayout.visibility = View.GONE
            holder.sendedLayout.visibility = View.VISIBLE
            holder.sendedText.text = message.data?.Text?.text.toString()
            holder.sendedTime.text =
                SimpleDateFormat("HH:MM").format(Date(Timestamp(message.time!! * 1000L).time))
                    .toString()
        } else {
            holder.recievedLayout.visibility = View.VISIBLE
            holder.sendedLayout.visibility = View.GONE
            holder.recievedAuthor.text = message.from.toString()
            when (message.data?.Text.toString()) {
                "null" -> {
                    holder.recievedText.text = message.data?.Image!!.link.toString()
                }

                else -> holder.recievedText.text = message.data?.Text!!.text.toString()
            }
            holder.recievedTime.text =
                SimpleDateFormat("HH:MM").format(Date(Timestamp(message.time!! * 1000L).time))
                    .toString()
        }
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recievedLayout: LinearLayout = itemView.findViewById(R.id.recieved_message)
        val recievedAuthor: TextView = itemView.findViewById(R.id.recieved_message_author)
        val recievedText: TextView = itemView.findViewById(R.id.recieved_message_text)
        val recievedTime: TextView = itemView.findViewById(R.id.recieved_message_when)


        val sendedLayout: LinearLayout = itemView.findViewById(R.id.sended_message)
        val sendedText: TextView = itemView.findViewById(R.id.sended_message_text)
        val sendedTime: TextView = itemView.findViewById(R.id.sended_message_when)
    }
}