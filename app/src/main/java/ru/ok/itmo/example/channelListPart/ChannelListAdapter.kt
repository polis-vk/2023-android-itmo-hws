package ru.ok.itmo.example.channelListPart

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R

class ChannelListAdapter(
    private val channels: List<ChannelDataClass>,
    private val bitmaps: HashMap<String, Bitmap>
) :
    RecyclerView.Adapter<ChannelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.channel_card,
            parent,
            false
        )
        return ChannelViewHolder(view, bitmaps)
    }

    override fun getItemCount() = channels.size

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(channels[position])
    }
}