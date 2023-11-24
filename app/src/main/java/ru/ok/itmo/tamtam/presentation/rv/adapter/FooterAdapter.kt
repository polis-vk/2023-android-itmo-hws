package ru.ok.itmo.tamtam.presentation.rv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ok.itmo.tamtam.R
import ru.ok.itmo.tamtam.databinding.ChatItemRvBinding
import ru.ok.itmo.tamtam.databinding.ErrorChatItemRvBinding
import ru.ok.itmo.tamtam.databinding.LoadingChatItemRvBinding

class FooterAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<RecyclerView.ViewHolder>() {
    inner class LoadingChatViewHolder(val binding: LoadingChatItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ErrorChatViewHolder(val binding: ErrorChatItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        when (holder) {
            is LoadingChatViewHolder -> {}
            is ErrorChatViewHolder -> {
                holder.binding.retryBtn.setOnClickListener {
                    retry.invoke()
                }
            }

        }
    }

    override fun getStateViewType(loadState: LoadState): Int = when (loadState) {
        is LoadState.Loading -> R.layout.loading_chat_item_rv
        is LoadState.Error -> R.layout.error_chat_item_rv
        is LoadState.NotLoading -> R.layout.loading_chat_item_rv
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val layout = getStateViewType(loadState)
        when(layout){
            R.layout.loading_chat_item_rv ->{
                val binding = LoadingChatItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return LoadingChatViewHolder(binding)

            }
            R.layout.error_chat_item_rv ->{
                val binding = ErrorChatItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return ErrorChatViewHolder(binding)
            }
        }
        throw RuntimeException("Unknown layout")
    }
}