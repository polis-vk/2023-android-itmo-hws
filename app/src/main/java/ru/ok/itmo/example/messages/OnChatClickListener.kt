package ru.ok.itmo.example.messages

import ru.ok.itmo.example.chatsPackage.Channel

interface OnChatClickListener {
    fun onChatClicked(chat: Channel)
}
