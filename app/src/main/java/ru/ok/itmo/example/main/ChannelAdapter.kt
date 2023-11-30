package ru.ok.itmo.example.main

import ru.ok.itmo.example.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.domain.ChannelPreview
import ru.ok.itmo.example.view.AvatarView
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.min

class ChannelAdapter(
    private val channels: List<ChannelPreview>,
    private val visibleListSize: Int,
    private val onClick: (ChannelPreview) -> Unit,
) : RecyclerView.Adapter<ChannelAdapter.ChannelHolder>() {

    class ChannelHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val channelCardPreview: AvatarView = root.findViewById(R.id.channelCardPreview)
        private val name: TextView = root.findViewById(R.id.channelName)
        private val preview: TextView = root.findViewById(R.id.channelPreview)
        private val time: TextView = root.findViewById(R.id.channelTime)
        val card: ConstraintLayout = root.findViewById(R.id.channelCard)

        fun bind(contact: ChannelPreview) {
            channelCardPreview.setText(contact.name)
            name.text = contact.name
            preview.text = contact.preview.run {
                if (length > 15) substring(0, 12) + "..." else this
            }
            time.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(contact.time)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelHolder {
        val holder = ChannelHolder(LayoutInflater.from(parent.context).inflate(R.layout.channel_card_view, parent, false))

        holder.card.setOnClickListener {
            onClick(channels[holder.adapterPosition]) // absoluteAdapterPosition
        }

        return holder
    }

    override fun onBindViewHolder(holder: ChannelHolder, position: Int) = holder.bind(channels[position])

    override fun getItemCount(): Int = min(visibleListSize, channels.size)
}
