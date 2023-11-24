package ru.ok.itmo.tamtam.presentation.rv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ok.itmo.tamtam.databinding.ForeignMessageItemRvBinding
import ru.ok.itmo.tamtam.databinding.MyMessageItemRvBinding
import ru.ok.itmo.tamtam.domain.model.Message
import ru.ok.itmo.tamtam.presentation.rv.callback.MessageDiffCallback
import ru.ok.itmo.tamtam.utils.convertTimestampForMessage

class MessageAdapter(
    private val login: String
) : PagingDataAdapter<Message, RecyclerView.ViewHolder>(MessageDiffCallback()) {
    inner class MyMessageViewHolder(val binding: MyMessageItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ForeignMessageViewHolder(val binding: ForeignMessageItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    var onLoadImageByGlide: ((ImageView, String) -> Unit)? = null

    override fun getItemViewType(position: Int): Int =
        if (getItem(position)?.from == login) MY_MESSAGE_VIEW_TYPE_ITEM
        else FOREIGN_MESSAGE_VIEW_TYPE_ITEM

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is MyMessageViewHolder -> {
                val binding = holder.binding
                val item = getItem(position)!!
                binding.messageTW.text = item.messageText
                binding.messageTimeTW.text = convertTimestampForMessage(item.time)

                item.imageLink?.let {
                    onLoadImageByGlide?.invoke(
                        binding.imageIW,
                        "https://faerytea.name:8008/img/$it"
                    )
                }
            }

            is ForeignMessageViewHolder -> {
                val binding = holder.binding
                val item = getItem(position)!!
                binding.messageTW.text = item.messageText
                binding.messageTimeTW.text = convertTimestampForMessage(item.time)
                item.imageLink?.let {
                    onLoadImageByGlide?.invoke(
                        binding.imageIW,
                        "https://faerytea.name:8008/img/$it"
                    )
                }
                binding.nameTW.text = item.from
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            MY_MESSAGE_VIEW_TYPE_ITEM -> {
                val binding = MyMessageItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MyMessageViewHolder(binding)
            }

            FOREIGN_MESSAGE_VIEW_TYPE_ITEM -> {
                val binding = ForeignMessageItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ForeignMessageViewHolder(binding)
            }

            else -> throw RuntimeException("Bad view type")
        }

    companion object {
        const val MY_MESSAGE_VIEW_TYPE_ITEM = 0
        const val FOREIGN_MESSAGE_VIEW_TYPE_ITEM = 1
    }
}