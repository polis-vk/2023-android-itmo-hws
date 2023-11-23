package ru.ok.itmo.example.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.ok.itmo.example.MainViewModel
import ru.ok.itmo.example.R
import ru.ok.itmo.example.model.Message

class ChatsListAdapter(val viewModel: MainViewModel) :
    RecyclerView.Adapter<ChatsListAdapter.ViewHolder>() {

    var data: Array<Chat> = emptyArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView
        val messagePreview: TextView

        init {
            textView = view.findViewById(R.id.textViewName)
            messagePreview = view.findViewById(R.id.textViewPreview)
        }

        fun updatePreview(messages: Array<Message>?) {
            val context = itemView.context
            messagePreview.text = if (messages == null) {
                context.getString(R.string.error_occured)
            } else if (messages.size == 0){
                context.getString(R.string.empty_channel)
            } else {
                messages[0].data.text ?: context.getString(R.string.image)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chat_block, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val channelName = data[position].name
        viewHolder.textView.text = channelName
        viewHolder.itemView.setOnClickListener {
            viewModel.openChannel(channelName)
        }
        viewModel.getChannelMessages(
            channelName,
            { messages -> viewHolder.updatePreview(messages)},
            limit = 1,
            reverse = true
        )
    }

    override fun getItemCount() = data.size
}
