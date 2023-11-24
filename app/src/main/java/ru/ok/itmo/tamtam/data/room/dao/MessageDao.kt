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
import java.util.UUID

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
    fun updateChat(name: String, lastMessageId: Int): String {
        val chatId = UUID.randomUUID().toString()
        val chat =
            getChatByName(name) ?: Chat(
                id = chatId,
                name = name,
                isAttach = false,
                lastViewedMessageId = lastMessageId,
                lastMessageId = lastMessageId,
                isChannel = name.endsWith("@channel")
            )
        insertOrReplace(chat.copy(lastMessageId = lastMessageId))
        return chat.id
    }

    @Transaction
    fun updateLastViewedForChat(chatId: String, lastViewedMessageId: Int) {
        val chat = getChatById(chatId)
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
            val chatId = updateChat(chatName, maxId)
            messages.map {
                Message(
                    id = it.id,
                    chatId = chatId,
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

    @Query("SELECT COUNT(*) FROM Message WHERE chat_id = :chatId AND id BETWEEN :startId AND :endId")
    fun getCountMessageBetweenIdsForChat(chatId: String, startId: Int, endId: Int): Int

    @Query(
        "SELECT * FROM Message WHERE chat_id = :chatId " +
                "AND (id > :lastKnownId) " +
                "ORDER BY id ASC " +
                "LIMIT :count"
    )
    fun getMessagesAfter(
        chatId: String,
        lastKnownId: Int,
        count: Int,
    ): List<Message>

    @Query(
        "SELECT * FROM Message WHERE chat_id = :chatId " +
                "AND (id <= :lastKnownId) " +
                "ORDER BY id DESC " +
                "LIMIT :count"
    )
    fun getMessagesBefore(
        chatId: String,
        lastKnownId: Int,
        count: Int,
    ): List<Message>

    @Query("SELECT * FROM Chat WHERE id = :chatId")
    fun getChatById(chatId: String): Chat
}
