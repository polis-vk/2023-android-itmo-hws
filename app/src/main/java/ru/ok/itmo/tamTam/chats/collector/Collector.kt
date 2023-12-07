package ru.ok.itmo.tamTam.chats.collector

import android.icu.text.SimpleDateFormat
import ru.ok.itmo.tamTam.AuthInfo
import ru.ok.itmo.tamTam.chats.models.ChatPreview
import ru.ok.itmo.tamTam.chats.models.Message
import java.util.Date
import java.util.Locale

object Collector {
    fun toDialogue(messages: MutableList<Message>): MutableList<ChatPreview> {
        val chatMap = mutableMapOf<String, MutableList<Message>>()

        for (message in messages) {
            val key = if (message.from < message.to) message.from + message.to else message.to + message.from
            if (!chatMap.containsKey(key)) {
                chatMap[key] = mutableListOf()
            }
            chatMap[key]?.add(message)
        }

        val chatList = mutableListOf<ChatPreview>()
        for (entry in chatMap.entries) {
            val chatMessages = entry.value
            val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val lastMessage = chatMessages.maxByOrNull { it.time }
            val senderName = if (lastMessage?.from == AuthInfo.login) lastMessage.to else lastMessage?.from ?: ""
            val formattedTime = lastMessage?.time?.let { dateFormat.format(Date(it)) } ?: ""
            val chatPreview = ChatPreview(
                isChat = false,
                title = senderName,
                textMessage = lastMessage?.data?.Text?.text ?: "",
                senderName = senderName,
                time = formattedTime
            )

            chatList.add(chatPreview)
        }

        return chatList
    }

}