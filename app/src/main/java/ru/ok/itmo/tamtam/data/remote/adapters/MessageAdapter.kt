package ru.ok.itmo.tamtam.data.remote.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.domain.Message
import ru.ok.itmo.tamtam.presentation.viewholders.ImageMessageViewHolder
import ru.ok.itmo.tamtam.presentation.viewholders.TextMessageViewHolder

class MessageAdapter(private val context: Context, private val messages: List<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TextMessage -> TextMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.text_message_item, parent, false)
            )

            else -> ImageMessageViewHolder(
                LayoutInflater.from(context).inflate(R.layout.image_message_item, parent, false)
            )
        }
    }

    override fun getItemCount(): Int = messages.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TextMessage -> {
                (holder as TextMessageViewHolder).bind(messages[position])
            }

            ImageMessage -> {
                (holder as ImageMessageViewHolder).bind(messages[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int =
        if (messages[position].data.Image == null) {
            TextMessage
        } else {
            ImageMessage
        }

    private companion object {
        const val TextMessage = 1
        const val ImageMessage = 2
    }
}