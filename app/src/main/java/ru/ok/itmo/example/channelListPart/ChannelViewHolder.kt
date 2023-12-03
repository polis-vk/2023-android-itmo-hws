package ru.ok.itmo.example.channelListPart

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import ru.ok.itmo.example.R

class ChannelViewHolder(channelView: View) : RecyclerView.ViewHolder(channelView) {
    private val channelNameView: TextView = channelView.findViewById(R.id.channelTitle)
    private val channelLastMsg: TextView = channelView.findViewById(R.id.channelMessage)
    private val channelMsgTime: TextView = channelView.findViewById(R.id.channelMsgTime)
    private val channelAvatar: CircleImageView = channelView.findViewById(R.id.channelAvatar)

    fun bind(channel: ChannelDataClass){
        channelNameView.text = channel.title
        channelLastMsg.text = channel.last_message
        channelMsgTime.text = channel.time.toString() //check???
        channelAvatar.setImageResource(R.drawable.bear_avatar)
    }
}
