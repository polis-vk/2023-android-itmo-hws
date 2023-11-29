package ru.ok.itmo.tamtam.presentation.rv.callback

import androidx.recyclerview.widget.DiffUtil
import ru.ok.itmo.tamtam.domain.model.Contact

class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}