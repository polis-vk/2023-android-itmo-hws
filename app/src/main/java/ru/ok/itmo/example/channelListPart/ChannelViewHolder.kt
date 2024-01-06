package ru.ok.itmo.example.channelListPart

import android.graphics.Bitmap
import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.custom.AvatarImageView

class ChannelViewHolder(channelView: View, private val bitmaps: HashMap<String, Bitmap>) :
    RecyclerView.ViewHolder(channelView) {
    private val channelNameView: TextView = channelView.findViewById(R.id.channelTitle)
    private val channelLastMsg: TextView = channelView.findViewById(R.id.channelMessage)
    private val channelMsgTime: TextView = channelView.findViewById(R.id.channelMsgTime)
    private val channelAvatar: AvatarImageView =
        channelView.findViewById(R.id.channelAvatar)//changed

    fun bind(channel: ChannelDataClass) {
        channelNameView.text = channel.title
        channelLastMsg.text = channel.last_message
        channelMsgTime.text = channel.time.toString() //check???
        //channelAvatar.setImageResource(R.drawable.bear_avatar)
        if (channel.image_link.isBlank()) {
            channelAvatar.setText(channelNameView.text as String)
        } else {
            bitmaps[channel.title]?.let { channelAvatar.setImage(it) }
        }

    }
}
