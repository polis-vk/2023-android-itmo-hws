package ru.ok.itmo.example.main

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.R
import ru.ok.itmo.example.domain.MessagePreview
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min

class MessageAdapter(
    private val messages: List<MessagePreview>,
    private val visibleListSize: Int
) : RecyclerView.Adapter<MessageAdapter.MessageHolder>() {
    class MessageHolder(root: View) : RecyclerView.ViewHolder(root) {
        private val from: TextView = root.findViewById(R.id.messageFrom)
        private val time: TextView = root.findViewById(R.id.messageTime)
        private val image: ImageView = root.findViewById(R.id.messageImage)
        private val text: TextView = root.findViewById(R.id.messageText)

        fun bind(message: MessagePreview) {
            from.text = message.from
            text.text = message.text ?: ""
            message.imageBase64?.let {
                val decodedString: ByteArray = Base64.decode(it, Base64.DEFAULT)
                image.setImageBitmap(BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size))
            }
            time.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(message.time))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
        return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_card_view, parent, false))
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int) = holder.bind(messages[position])

    override fun getItemCount(): Int = min(visibleListSize, messages.size)
}
