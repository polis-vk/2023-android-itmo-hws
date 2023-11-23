package ru.ok.itmo.tamtam.presentation.rv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ok.itmo.tamtam.databinding.ErrorChatItemRvBinding
import ru.ok.itmo.tamtam.databinding.LoadingChatItemRvBinding

class HeaderFooterAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ViewHolder>() {
    inner class ErrorViewHolder(val binding: ErrorChatItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class LoadingViewHolder(val binding: LoadingChatItemRvBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        when (holder) {
            is ErrorViewHolder -> {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {

        when (loadState) {
            is LoadState.Error -> {
                val binding = ErrorChatItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return ErrorViewHolder(binding)
            }

            LoadState.Loading -> {
                val binding = LoadingChatItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return LoadingViewHolder(binding)
            }

            is LoadState.NotLoading -> {
                val binding = ErrorChatItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return ErrorViewHolder(binding)
            }
        }

    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Loading || loadState is LoadState.Error
    }
}