package ru.ok.itmo.example.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.models.Channel

class ChannelRecyclerViewAdapter(private val channels: List<Channel>) :
    RecyclerView.Adapter<ChannelRecyclerViewAdapter.MessageViewHolder>() {

    var onItemClick: ((Channel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_channel, parent, false)
        return MessageViewHolder(view)
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val channel = channels[position]
        holder.channelName.text = channel.name!!.removeSuffix("@channel")
        holder.lastMessage.text = channel.lastMessage!!.data!!.Text!!.text
        //holder.lastMessageWhen.text = SimpleDateFormat("HH:MM").format(Instant.ofEpochMilli(channel.lastMessage.time))

    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val channelName: TextView = itemView.findViewById(R.id.channel_name)
        val lastMessage: TextView = itemView.findViewById(R.id.last_message_text)
        // val lastMessageSender: ImageView = itemView.findViewById(R.id.last_man_messaged)
        val lastMessageWhen: TextView = itemView.findViewById(R.id.last_message_when)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(channels[adapterPosition])
            }
        }



//        holder.sendedTime.text =
//        SimpleDateFormat("HH:MM").format(Date(Timestamp(message.time!! * 1000L).time))
//        .toString()
    }
}