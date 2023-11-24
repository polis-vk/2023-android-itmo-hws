package ru.ok.itmo.tamtam.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity
data class Chat(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "is_attach")
    val isAttach: Boolean,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "last_message_id")
    val lastMessageId: Int,
    @ColumnInfo(name = "last_viewed_message_id")
    val lastViewedMessageId: Int,
    @ColumnInfo(name = "is_channel")
    val isChannel: Boolean,
    @Ignore
    val typingUsers: List<String>,
    @Ignore
    val lastMessage: Message?,
    @Ignore
    val countNewMessage: Int
) {
    constructor(
        id: String,
        isAttach: Boolean,
        name: String,
        lastMessageId: Int,
        lastViewedMessageId: Int,
        isChannel: Boolean
    ) : this(id, isAttach, name, lastMessageId, lastViewedMessageId, isChannel, listOf(), null, 0)
}