package ru.ok.itmo.tamtam.presentation.rv.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import ru.ok.itmo.tamtam.databinding.ContactItemRvBinding
import ru.ok.itmo.tamtam.domain.model.Contact
import ru.ok.itmo.tamtam.presentation.rv.callback.ContactDiffCallback

class ContactAdapter : ListAdapter<Contact, ViewHolder>(ContactDiffCallback()) {
    inner class ContactViewHolder(val binding: ContactItemRvBinding) :
        ViewHolder(binding.root)

    var onLoadImageByGlide: ((ImageView, String) -> Unit)? = null
    var onClick: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = ContactItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ContactViewHolder(binding)
            }

            else -> throw RuntimeException("Bad view type")
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is ContactViewHolder -> {
                val contact = getItem(position)
                val binding = holder.binding
                binding.nameTW.text = contact.name
                onLoadImageByGlide?.invoke(binding.avatarIW, contact.name)

                binding.chatCL.setOnClickListener {
                    onClick?.invoke(contact.name)
                }
            }
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
    }
}