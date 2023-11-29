package ru.ok.itmo.tamtam.data.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import ru.ok.itmo.tamtam.domain.model.Chat
import ru.ok.itmo.tamtam.domain.model.IMessage
import ru.ok.itmo.tamtam.domain.model.Message
import ru.ok.itmo.tamtam.utils.getChatName
import ru.ok.itmo.tamtam.Constants
import ru.ok.itmo.tamtam.Constants.CHANNEL_SUFFIX

@Dao
interface MessageDao {
    @Query(
        "" +
                "SELECT Message.*, Chat.* " +
                "FROM Chat " +
                "JOIN Message ON Chat.last_message_id = Message.id " +
                "ORDER BY Message.time DESC" +
                ""
    )
    fun getChatsAsFlow(): Flow<List<Chat>>

    @Query("SELECT MAX(last_message_id) FROM Chat WHERE is_channel = 0")
    fun getMaxLastMessageIdForNotChannel(): Int?


    @Query("SELECT * FROM Chat WHERE name = :name")
    fun getChatByName(name: String): Chat?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplace(chat: Chat)

    @Transaction
    fun updateLastMessageIdForChat(name: String, lastMessageId: Int) {
        val chat =
            getChatByName(name) ?: Chat(
                name = name,
                isAttach = false,
                lastViewedMessageId = lastMessageId,
                lastMessageId = lastMessageId,
                isChannel = name.endsWith(CHANNEL_SUFFIX)
            )
        insertOrReplace(chat.copy(lastMessageId = lastMessageId))
    }

    @Transaction
    fun updateLastViewedForChat(chatName: String, lastViewedMessageId: Int) {
        val chat = getChatByName(chatName) ?: return
        insertOrReplace(
            chat.copy(
                lastViewedMessageId = maxOf(
                    lastViewedMessageId,
                    chat.lastViewedMessageId
                )
            )
        )
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrReplaceMessages(vararg list: Message)

    @Transaction
    fun addMessages(iMessages: List<IMessage>, login: String, isSent: Boolean = true) {
        val messages = iMessages.groupBy {
            getChatName(login, it.from, it.to)
        }.map { (chatName, messages) ->
            val maxId = messages.maxBy { it.id }.id
            updateLastMessageIdForChat(chatName, maxId)
            messages.map {
                Message(
                    id = it.id,
                    chatName = chatName,
                    from = it.from,
                    to = it.to,
                    time = it.time,
                    messageText = it.messageText,
                    imageLink = it.imageLink,
                    isSent = isSent
                )
            }
        }.flatten()
        insertOrReplaceMessages(*messages.toTypedArray())
    }

    @Query("SELECT * FROM Message WHERE id = :id")
    fun getMessageById(id: Int): Message

    @Query("SELECT COUNT(*) FROM Message WHERE chat_name = :chatName AND id BETWEEN :startId AND :endId")
    fun getCountMessageBetweenIdsForChat(chatName: String, startId: Int, endId: Int): Int

    @Query(
        "SELECT * FROM Message WHERE chat_name = :chatName " +
                "AND (id > :lastKnownId) " +
                "ORDER BY id ASC " +
                "LIMIT :count"
    )
    fun getMessagesAfter(
        chatName: String,
        lastKnownId: Int,
        count: Int,
    ): List<Message>

    @Query(
        "SELECT * FROM Message WHERE chat_name = :chatName " +
                "AND (id <= :lastKnownId) " +
                "ORDER BY id DESC " +
                "LIMIT :count"
    )
    fun getMessagesBefore(
        chatName: String,
        lastKnownId: Int,
        count: Int,
    ): List<Message>

    @Query("DELETE FROM Chat")
    fun clearChats()

    @Query("DELETE FROM Chat")
    fun clearMessages()

    @Transaction
    fun clear() {
        clearChats()
        clearMessages()
    }
}
